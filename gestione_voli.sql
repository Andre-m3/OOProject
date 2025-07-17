--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-07-17 20:38:42

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 879 (class 1247 OID 24625)
-- Name: stato_prenotazione; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.stato_prenotazione AS ENUM (
    'CONFERMATA',
    'IN_ATTESA',
    'CANCELLATA'
);


ALTER TYPE public.stato_prenotazione OWNER TO postgres;

--
-- TOC entry 858 (class 1247 OID 16468)
-- Name: stato_volo; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.stato_volo AS ENUM (
    'PROGRAMMATO',
    'DECOLLATO',
    'IN_RITARDO',
    'ATTERRATO',
    'CANCELLATO'
);


ALTER TYPE public.stato_volo OWNER TO postgres;

--
-- TOC entry 226 (class 1255 OID 16551)
-- Name: conta_posti_occupati(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.conta_posti_occupati(num_volo character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    posti_occupati INTEGER;
BEGIN
    SELECT COALESCE(SUM(p.numero_passeggeri), 0) INTO posti_occupati
    FROM prenotazioni p
    WHERE p.numero_volo = num_volo 
    AND p.stato_prenotazione IN ('CONFERMATA', 'IN_ATTESA');
    
    RETURN posti_occupati;
END;
$$;


ALTER FUNCTION public.conta_posti_occupati(num_volo character varying) OWNER TO postgres;

--
-- TOC entry 229 (class 1255 OID 24647)
-- Name: get_prenotazioni_utente(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_prenotazioni_utente(user_email character varying) RETURNS TABLE(codice_prenotazione character varying, numero_volo character varying, compagnia_aerea character varying, partenza character varying, destinazione character varying, data_volo date, orario_previsto time without time zone, stato_prenotazione public.stato_prenotazione, numero_passeggeri integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT p.codice_prenotazione, v.numero_volo, v.compagnia_aerea, 
           v.partenza, v.destinazione, v.data_volo, v.orario_previsto,
           p.stato_prenotazione, p.numero_passeggeri
    FROM prenotazioni p
    JOIN voli v ON p.numero_volo = v.numero_volo
    WHERE p.email = user_email
    ORDER BY p.data_prenotazione DESC;
END;
$$;


ALTER FUNCTION public.get_prenotazioni_utente(user_email character varying) OWNER TO postgres;

--
-- TOC entry 225 (class 1255 OID 16550)
-- Name: get_tickets_prenotazione(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_tickets_prenotazione(cod_prenotazione character varying) RETURNS TABLE(nome character varying, cognome character varying, numero_documento character varying, data_nascita character varying, posto_assegnato character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT t.nome, t.cognome, t.numero_documento, t.data_nascita, t.posto_assegnato
    FROM tickets t
    WHERE t.codice_prenotazione = cod_prenotazione
    ORDER BY t.posto_assegnato;
END;
$$;


ALTER FUNCTION public.get_tickets_prenotazione(cod_prenotazione character varying) OWNER TO postgres;

--
-- TOC entry 224 (class 1255 OID 16548)
-- Name: get_voli_disponibili(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_voli_disponibili() RETURNS TABLE(numero_volo character varying, compagnia_aerea character varying, partenza character varying, destinazione character varying, data character varying, orario_previsto character varying, stato public.stato_volo, ritardo integer, tipo_volo character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT v.numero_volo, v.compagnia_aerea, v.partenza, v.destinazione, 
           v.data, v.orario_previsto, v.stato, v.ritardo, v.tipo_volo
    FROM voli v
    WHERE v.stato NOT IN ('CANCELLATO', 'ATTERRATO')
    ORDER BY v.data, v.orario_previsto;
END;
$$;


ALTER FUNCTION public.get_voli_disponibili() OWNER TO postgres;

--
-- TOC entry 228 (class 1255 OID 16553)
-- Name: login_utente(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.login_utente(user_email character varying, user_password character varying) RETURNS TABLE(email character varying, username character varying, tipo_utente character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT u.email, u.username, u.tipo_utente
    FROM utenti u
    WHERE u.email = user_email AND u.password = user_password;
END;
$$;


ALTER FUNCTION public.login_utente(user_email character varying, user_password character varying) OWNER TO postgres;

--
-- TOC entry 227 (class 1255 OID 16552)
-- Name: verifica_disponibilita_posto(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.verifica_disponibilita_posto(num_volo character varying, posto character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    posto_libero BOOLEAN;
BEGIN
    SELECT NOT EXISTS(
        SELECT 1 
        FROM tickets t
        JOIN prenotazioni p ON t.codice_prenotazione = p.codice_prenotazione
        WHERE p.numero_volo = num_volo 
        AND t.posto_assegnato = posto
        AND p.stato_prenotazione IN ('CONFERMATA', 'IN_ATTESA')
    ) INTO posto_libero;
    
    RETURN posto_libero;
END;
$$;


ALTER FUNCTION public.verifica_disponibilita_posto(num_volo character varying, posto character varying) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 219 (class 1259 OID 16504)
-- Name: prenotazioni; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prenotazioni (
    codice_prenotazione character varying(50) NOT NULL,
    email character varying(255) NOT NULL,
    numero_volo character varying(50) NOT NULL,
    stato_prenotazione public.stato_prenotazione DEFAULT 'IN_ATTESA'::public.stato_prenotazione,
    numero_passeggeri integer DEFAULT 1 NOT NULL,
    data_prenotazione timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_numero_passeggeri CHECK (((numero_passeggeri > 0) AND (numero_passeggeri <= 9)))
);


ALTER TABLE public.prenotazioni OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16523)
-- Name: tickets; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tickets (
    id_ticket integer NOT NULL,
    nome character varying(100) NOT NULL,
    cognome character varying(100) NOT NULL,
    numero_documento character varying(100) NOT NULL,
    data_nascita character varying(50) NOT NULL,
    posto_assegnato character varying(10) NOT NULL,
    codice_prenotazione character varying(50) NOT NULL
);


ALTER TABLE public.tickets OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16522)
-- Name: tickets_id_ticket_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tickets_id_ticket_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tickets_id_ticket_seq OWNER TO postgres;

--
-- TOC entry 4954 (class 0 OID 0)
-- Dependencies: 220
-- Name: tickets_id_ticket_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tickets_id_ticket_seq OWNED BY public.tickets.id_ticket;


--
-- TOC entry 217 (class 1259 OID 16489)
-- Name: utenti; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utenti (
    email character varying(255) NOT NULL,
    username character varying(25) NOT NULL,
    password character varying(255) NOT NULL,
    is_admin boolean NOT NULL
);


ALTER TABLE public.utenti OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16498)
-- Name: voli; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.voli (
    numero_volo character varying(50) NOT NULL,
    compagnia_aerea character varying(100) NOT NULL,
    partenza character varying(100) NOT NULL,
    destinazione character varying(100) NOT NULL,
    data_volo date NOT NULL,
    orario_previsto time without time zone NOT NULL,
    stato public.stato_volo NOT NULL,
    ritardo integer DEFAULT 0,
    tipo_volo character varying(20) NOT NULL,
    gate_imbarco smallint,
    CONSTRAINT chk_ritardo CHECK ((ritardo >= 0)),
    CONSTRAINT chk_tipo_volo CHECK (((tipo_volo)::text = ANY ((ARRAY['PARTENZA'::character varying, 'ARRIVO'::character varying])::text[])))
);


ALTER TABLE public.voli OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 24640)
-- Name: vw_prenotazioni_complete; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vw_prenotazioni_complete AS
 SELECT p.codice_prenotazione,
    p.email,
    p.numero_volo,
    p.stato_prenotazione,
    p.numero_passeggeri,
    p.data_prenotazione,
    u.username,
    u.is_admin AS tipo_utente,
    v.compagnia_aerea,
    v.partenza,
    v.destinazione,
    v.data_volo AS data,
    v.orario_previsto,
    v.stato AS stato_volo,
    v.ritardo,
    v.tipo_volo
   FROM ((public.prenotazioni p
     JOIN public.utenti u ON (((p.email)::text = (u.email)::text)))
     JOIN public.voli v ON (((p.numero_volo)::text = (v.numero_volo)::text)));


ALTER VIEW public.vw_prenotazioni_complete OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 24635)
-- Name: vw_tickets_complete; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vw_tickets_complete AS
 SELECT t.id_ticket,
    t.nome,
    t.cognome,
    t.numero_documento,
    t.data_nascita,
    t.posto_assegnato,
    t.codice_prenotazione,
    p.email,
    p.numero_volo,
    p.stato_prenotazione,
    v.compagnia_aerea,
    v.partenza,
    v.destinazione,
    v.data_volo AS data,
    v.orario_previsto,
    v.stato AS stato_volo
   FROM ((public.tickets t
     JOIN public.prenotazioni p ON (((t.codice_prenotazione)::text = (p.codice_prenotazione)::text)))
     JOIN public.voli v ON (((p.numero_volo)::text = (v.numero_volo)::text)));


ALTER VIEW public.vw_tickets_complete OWNER TO postgres;

--
-- TOC entry 4778 (class 2604 OID 16526)
-- Name: tickets id_ticket; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets ALTER COLUMN id_ticket SET DEFAULT nextval('public.tickets_id_ticket_seq'::regclass);


--
-- TOC entry 4946 (class 0 OID 16504)
-- Dependencies: 219
-- Data for Name: prenotazioni; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prenotazioni (codice_prenotazione, email, numero_volo, stato_prenotazione, numero_passeggeri, data_prenotazione) FROM stdin;
P20001	mario.rossi@email.it	AZ123	CONFERMATA	1	2024-01-10 09:30:00
P20002	giulia.bianchi@email.it	FR456	CONFERMATA	2	2024-01-11 14:15:00
P20003	luca.verdi@gmail.com	VY789	CONFERMATA	3	2024-01-12 16:45:00
P20004	anna.neri@yahoo.it	U2101	CONFERMATA	1	2024-01-13 11:20:00
P20005	pietro.blu@hotmail.com	W6202	CONFERMATA	2	2024-01-14 08:50:00
P20006	sofia.gialli@libero.it	LH789	CONFERMATA	1	2024-01-12 19:30:00
P20007	marco.viola@tiscali.it	BA101	CONFERMATA	4	2024-01-13 12:15:00
P20008	chiara.rosa@gmail.com	EK202	CONFERMATA	1	2024-01-14 15:40:00
P20009	andrea.grigi@email.it	TK303	CONFERMATA	2	2024-01-15 10:25:00
P20010	francesca.arancioni@yahoo.com	QR404	CONFERMATA	1	2024-01-16 13:10:00
P20011	davide.marroni@gmail.it	EN303	IN_ATTESA	1	2024-01-15 17:20:00
P20012	elena.celesti@hotmail.it	KL404	IN_ATTESA	2	2024-01-16 09:45:00
P20013	mario.rossi@email.it	AF505	IN_ATTESA	1	2024-01-17 11:30:00
P20014	giulia.bianchi@email.it	LX606	IN_ATTESA	3	2024-01-17 14:55:00
P20015	luca.verdi@gmail.com	SU505	IN_ATTESA	1	2024-01-18 16:20:00
P20016	anna.neri@yahoo.it	IB707	CANCELLATA	1	2024-01-13 20:10:00
P20017	pietro.blu@hotmail.com	OS606	CANCELLATA	2	2024-01-14 12:35:00
P20018	sofia.gialli@libero.it	LO707	CANCELLATA	1	2024-01-15 18:40:00
P20021	mario.rossi@email.it	TK303	CONFERMATA	2	2024-01-18 10:30:00
P20022	giulia.bianchi@email.it	QR404	CONFERMATA	1	2024-01-18 15:45:00
P20023	luca.verdi@gmail.com	BA101	IN_ATTESA	1	2024-01-19 09:20:00
P20024	anna.neri@yahoo.it	EK202	CONFERMATA	3	2024-01-19 13:50:00
P20025	pietro.blu@hotmail.com	LH789	CONFERMATA	1	2024-01-19 17:10:00
P20019	marco.viola@tiscali.it	SK808	CANCELLATA	2	2024-01-16 07:25:00
P20020	chiara.rosa@gmail.com	AY909	CANCELLATA	1	2024-01-17 21:15:00
85F07735	user@example.com	VY789	CONFERMATA	1	2025-07-17 16:00:53.187811
A9E84AC2	user@example.com	VY789	CONFERMATA	1	2025-07-17 16:04:40.217484
\.


--
-- TOC entry 4948 (class 0 OID 16523)
-- Dependencies: 221
-- Data for Name: tickets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tickets (id_ticket, nome, cognome, numero_documento, data_nascita, posto_assegnato, codice_prenotazione) FROM stdin;
1	Mario	Rossi	AB123456	15/03/1985	12A	P20001
2	Giulia	Bianchi	CD789012	22/07/1990	15B	P20002
3	Marco	Bianchi	EF345678	10/11/1988	15C	P20002
4	Luca	Verdi	GH901234	05/09/1992	8A	P20003
5	Maria	Verdi	IJ567890	18/12/1994	8B	P20003
6	Giuseppe	Verdi	KL234567	03/06/1965	8C	P20003
7	Anna	Neri	MN890123	28/01/1987	20F	P20004
8	Pietro	Blu	OP456789	14/05/1983	6A	P20005
9	Carla	Blu	QR012345	25/08/1986	6B	P20005
10	Sofia	Gialli	ST678901	11/02/1991	11D	P20006
11	Marco	Viola	UV234567	07/10/1989	3A	P20007
12	Laura	Viola	WX890123	19/04/1993	3B	P20007
13	Matteo	Viola	YZ456789	30/07/2010	3C	P20007
14	Alessia	Viola	AA012345	12/11/2012	3D	P20007
15	Chiara	Rosa	BB678901	26/06/1988	14E	P20008
16	Andrea	Grigi	CC234567	09/12/1984	18A	P20009
17	Paola	Grigi	DD890123	21/03/1987	18B	P20009
18	Francesca	Arancioni	EE456789	16/09/1995	9F	P20010
19	Davide	Marroni	FF012345	04/01/1982	13C	P20011
20	Elena	Celesti	GG678901	23/05/1990	5A	P20012
21	Roberto	Celesti	HH234567	08/08/1989	5B	P20012
22	Mario	Rossi	AB123456	15/03/1985	7D	P20013
23	Giulia	Bianchi	CD789012	22/07/1990	16A	P20014
24	Simone	Bianchi	II890123	17/10/1987	16B	P20014
25	Federica	Bianchi	JJ456789	02/02/1993	16C	P20014
26	Luca	Verdi	GH901234	05/09/1992	19E	P20015
27	Anna	Neri	MN890123	28/01/1987	10A	P20016
28	Pietro	Blu	OP456789	14/05/1983	4C	P20017
29	Silvia	Blu	KK012345	13/07/1985	4D	P20017
30	Sofia	Gialli	ST678901	11/02/1991	22F	P20018
31	Marco	Viola	UV234567	07/10/1989	17A	P20019
32	Cristina	Viola	LL678901	24/09/1991	17B	P20019
33	Chiara	Rosa	BB678901	26/06/1988	21C	P20020
34	Mario	Rossi	AB123456	15/03/1985	2A	P20021
35	Valentina	Rossi	MM234567	06/04/1987	2B	P20021
36	Giulia	Bianchi	CD789012	22/07/1990	1E	P20022
37	Luca	Verdi	GH901234	05/09/1992	23D	P20023
38	Anna	Neri	MN890123	28/01/1987	24A	P20024
39	Francesco	Neri	NN890123	15/11/1985	24B	P20024
40	Giovanna	Neri	OO456789	20/06/1960	24C	P20024
41	Pietro	Blu	OP456789	14/05/1983	25F	P20025
\.


--
-- TOC entry 4944 (class 0 OID 16489)
-- Dependencies: 217
-- Data for Name: utenti; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utenti (email, username, password, is_admin) FROM stdin;
admin@aeroporto.it	admin	admin123	t
supervisor@aeroporto.it	supervisor	super456	t
manager@aeroporto.it	manager	manager789	t
mario.rossi@email.it	mrossi	password123	f
giulia.bianchi@email.it	gbianchi	password456	f
luca.verdi@gmail.com	lverdi	luca2024	f
anna.neri@yahoo.it	aneri	anna789	f
pietro.blu@hotmail.com	pblu	pietro456	f
sofia.gialli@libero.it	sgialli	sofia123	f
marco.viola@tiscali.it	mviola	marco999	f
chiara.rosa@gmail.com	crosa	chiara555	f
andrea.grigi@email.it	agrigi	andrea777	f
francesca.arancioni@yahoo.com	farancioni	francy888	f
davide.marroni@gmail.it	dmarroni	davide321	f
elena.celesti@hotmail.it	ecelesti	elena654	f
user@example.com	user	password123	f
Andreamontella@studenti.unina.it	andreamont3	AndreaMontella3!	t
adrianomontella1@gmail.com	adrimon5	Adriano5!	t
\.


--
-- TOC entry 4945 (class 0 OID 16498)
-- Dependencies: 218
-- Data for Name: voli; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.voli (numero_volo, compagnia_aerea, partenza, destinazione, data_volo, orario_previsto, stato, ritardo, tipo_volo, gate_imbarco) FROM stdin;
AZ123	Alitalia	Roma Fiumicino	Milano Malpensa	2024-01-15	10:00:00	PROGRAMMATO	0	PARTENZA	\N
FR456	Ryanair	Milano Bergamo	Napoli	2024-01-16	14:30:00	PROGRAMMATO	15	PARTENZA	\N
VY789	Vueling	Roma Fiumicino	Barcellona	2024-01-17	08:45:00	PROGRAMMATO	0	PARTENZA	\N
EN303	Air Dolomiti	Verona	Monaco	2024-01-20	09:30:00	PROGRAMMATO	0	PARTENZA	\N
KL404	KLM	Milano Malpensa	Amsterdam	2024-01-21	11:45:00	DECOLLATO	0	PARTENZA	\N
AF505	Air France	Roma Fiumicino	Parigi CDG	2024-01-22	15:10:00	PROGRAMMATO	0	PARTENZA	\N
LX606	Swiss	Milano Malpensa	Zurigo	2024-01-23	13:25:00	PROGRAMMATO	0	PARTENZA	\N
IB707	Iberia	Roma Fiumicino	Madrid	2024-01-24	17:40:00	ATTERRATO	0	PARTENZA	\N
LH789	Lufthansa	Francoforte	Roma Fiumicino	2024-01-17	09:15:00	DECOLLATO	0	ARRIVO	\N
BA101	British Airways	Londra Heathrow	Milano Malpensa	2024-01-18	16:45:00	PROGRAMMATO	0	ARRIVO	\N
EK202	Emirates	Dubai	Roma Fiumicino	2024-01-19	22:30:00	CANCELLATO	45	ARRIVO	\N
TK303	Turkish Airlines	Istanbul	Milano Malpensa	2024-01-20	14:20:00	PROGRAMMATO	0	ARRIVO	\N
QR404	Qatar Airways	Doha	Roma Fiumicino	2024-01-21	06:50:00	PROGRAMMATO	0	ARRIVO	\N
SU505	Aeroflot	Mosca	Milano Malpensa	2024-01-22	19:15:00	PROGRAMMATO	0	ARRIVO	\N
OS606	Austrian Airlines	Vienna	Roma Fiumicino	2024-01-23	12:30:00	IN_RITARDO	0	ARRIVO	\N
LO707	LOT Polish Airlines	Varsavia	Milano Malpensa	2024-01-24	18:45:00	PROGRAMMATO	0	ARRIVO	\N
SK808	SAS	Stoccolma	Roma Fiumicino	2024-01-25	10:20:00	PROGRAMMATO	0	ARRIVO	\N
AY909	Finnair	Helsinki	Milano Malpensa	2024-01-26	21:10:00	PROGRAMMATO	0	ARRIVO	\N
EM12000A	Emirates	Dubai	Napoli	2025-07-29	10:30:00	PROGRAMMATO	0	ARRIVO	\N
U2101	EasyJet	Milano Malpensa	Londra Gatwick	2024-01-18	12:15:00	CANCELLATO	30	PARTENZA	3
W6202	Wizz Air	Napoli	Budapest	2024-01-20	15:20:00	IN_RITARDO	0	PARTENZA	\N
\.


--
-- TOC entry 4955 (class 0 OID 0)
-- Dependencies: 220
-- Name: tickets_id_ticket_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tickets_id_ticket_seq', 46, true);


--
-- TOC entry 4791 (class 2606 OID 24581)
-- Name: tickets posto_prenotazione_unici; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT posto_prenotazione_unici UNIQUE (posto_assegnato, codice_prenotazione);


--
-- TOC entry 4789 (class 2606 OID 16511)
-- Name: prenotazioni prenotazioni_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prenotazioni
    ADD CONSTRAINT prenotazioni_pkey PRIMARY KEY (codice_prenotazione);


--
-- TOC entry 4793 (class 2606 OID 16528)
-- Name: tickets tickets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_pkey PRIMARY KEY (id_ticket);


--
-- TOC entry 4783 (class 2606 OID 16495)
-- Name: utenti utenti_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utenti
    ADD CONSTRAINT utenti_pkey PRIMARY KEY (email);


--
-- TOC entry 4785 (class 2606 OID 16497)
-- Name: utenti utenti_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utenti
    ADD CONSTRAINT utenti_username_key UNIQUE (username);


--
-- TOC entry 4787 (class 2606 OID 16503)
-- Name: voli voli_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voli
    ADD CONSTRAINT voli_pkey PRIMARY KEY (numero_volo);


--
-- TOC entry 4794 (class 2606 OID 16512)
-- Name: prenotazioni prenotazioni_email_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prenotazioni
    ADD CONSTRAINT prenotazioni_email_fkey FOREIGN KEY (email) REFERENCES public.utenti(email) ON DELETE CASCADE;


--
-- TOC entry 4795 (class 2606 OID 16517)
-- Name: prenotazioni prenotazioni_numero_volo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prenotazioni
    ADD CONSTRAINT prenotazioni_numero_volo_fkey FOREIGN KEY (numero_volo) REFERENCES public.voli(numero_volo) ON DELETE CASCADE;


--
-- TOC entry 4796 (class 2606 OID 16529)
-- Name: tickets tickets_codice_prenotazione_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_codice_prenotazione_fkey FOREIGN KEY (codice_prenotazione) REFERENCES public.prenotazioni(codice_prenotazione) ON DELETE CASCADE;


-- Completed on 2025-07-17 20:38:42

--
-- PostgreSQL database dump complete
--

