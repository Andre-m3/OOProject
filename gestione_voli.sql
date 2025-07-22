--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-07-19 23:04:54

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

INSERT INTO public.prenotazioni VALUES 
('P20001', 'user@example.com', 'AZ456', 'CONFERMATA', 1, '2025-07-19 23:03:01.397822'),
('P20002', 'mario.rossi@email.it', 'FR789', 'CONFERMATA', 2, '2025-07-19 23:03:01.397822'),
('P20003', 'giulia.bianchi@email.it', 'LH234', 'CONFERMATA', 3, '2025-07-19 23:03:01.397822'),
('P20004', 'luca.verdi@gmail.com', 'BA567', 'CONFERMATA', 4, '2025-07-19 23:03:01.397822'),
('P20005', 'elena.celesti@hotmail.it', 'EK890', 'CONFERMATA', 1, '2025-07-19 23:03:01.397822'),
('P20006', 'marco.viola@tiscali.it', 'AF123', 'CONFERMATA', 2, '2025-07-19 23:03:01.397822'),
('P20007', 'francesca.arancioni@yahoo.com', 'VY345', 'CONFERMATA', 3, '2025-07-19 23:03:01.397822'),
('P20008', 'andrea.grigi@email.it', 'W6678', 'CONFERMATA', 2, '2025-07-19 23:03:01.397822');


--
-- TOC entry 4948 (class 0 OID 16523)
-- Dependencies: 221
-- Data for Name: tickets; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tickets VALUES 
(57, 'Alessandro', 'Romano', 'AB123456', '1990-03-15', 'C2', 'P20001'),
(58, 'Mario', 'Rossi', 'CD789123', '1985-07-20', 'A1', 'P20002'),
(59, 'Giulia', 'Rossi', 'EF456789', '1988-11-08', 'A2', 'P20002'),
(60, 'Giulia', 'Bianchi', 'GH123456', '1992-05-12', 'B3', 'P20003'),
(61, 'Francesco', 'Bianchi', 'IJ789123', '1990-09-25', 'B4', 'P20003'),
(62, 'Sofia', 'Bianchi', 'KL456789', '2018-12-03', 'B5', 'P20003'),
(63, 'Luca', 'Verdi', 'MN123456', '1975-01-18', 'D1', 'P20004'),
(64, 'Elena', 'Verdi', 'OP789123', '1978-04-22', 'D2', 'P20004'),
(65, 'Matteo', 'Verdi', 'QR456789', '2008-08-14', 'D3', 'P20004'),
(66, 'Chiara', 'Verdi', 'ST123456', '2012-10-30', 'D4', 'P20004'),
(67, 'Elena', 'Celesti', 'UV789123', '1987-06-09', 'E1', 'P20005'),
(68, 'Marco', 'Viola', 'WX456789', '1995-02-28', 'F2', 'P20006'),
(69, 'Anna', 'Viola', 'YZ123456', '1993-12-17', 'F3', 'P20006'),
(70, 'Francesca', 'Arancioni', 'AA789123', '1980-11-05', 'G1', 'P20007'),
(71, 'Davide', 'Arancioni', 'BB456789', '1983-03-19', 'G2', 'P20007'),
(72, 'Lorenzo', 'Arancioni', 'CC123456', '2015-07-11', 'G3', 'P20007'),
(73, 'Andrea', 'Grigi', 'DD789123', '1991-09-13', 'H1', 'P20008'),
(74, 'Chiara', 'Grigi', 'EE456789', '1994-01-26', 'H2', 'P20008');


--
-- TOC entry 4944 (class 0 OID 16489)
-- Dependencies: 217
-- Data for Name: utenti; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.utenti VALUES 
('admin@aeroporto.it', 'admin', 'admin123', true),
('supervisor@aeroporto.it', 'supervisor', 'super456', true),
('manager@aeroporto.it', 'manager', 'manager789', true),
('mario.rossi@email.it', 'mrossi', 'password123', false),
('giulia.bianchi@email.it', 'gbianchi', 'password456', false),
('luca.verdi@gmail.com', 'lverdi', 'luca2024', false),
('anna.neri@yahoo.it', 'aneri', 'anna789', false),
('pietro.blu@hotmail.com', 'pblu', 'pietro456', false),
('sofia.gialli@libero.it', 'sgialli', 'sofia123', false),
('marco.viola@tiscali.it', 'mviola', 'marco999', false),
('chiara.rosa@gmail.com', 'crosa', 'chiara555', false),
('andrea.grigi@email.it', 'agrigi', 'andrea777', false),
('francesca.arancioni@yahoo.com', 'farancioni', 'francy888', false),
('davide.marroni@gmail.com', 'dmarroni', 'davide321', false),
('elena.celesti@hotmail.it', 'ecelesti', 'elena654', false),
('user@example.com', 'user', 'password123', false),
('Andreamontella@studenti.unina.it', 'andreamont3', 'AndreaMontella3!', true),
('adrianomontella1@gmail.com', 'adrimon5', 'Adriano5!', true),
('catanzarofan1@gmail.com', 'catanzaro1', 'Catanzaro!', false),
('marioaugusto@gmail.com', 'marioaugusto', 'Mario3!', false);


--
-- TOC entry 4945 (class 0 OID 16498)
-- Dependencies: 218
-- Data for Name: voli; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.voli VALUES 
('W6202', 'Wizz Air', 'Napoli', 'Budapest', '2024-01-20', '15:20:00', 'IN_RITARDO', 0, 'PARTENZA', NULL),
('PO1235', 'Poland Airways', 'Varsavia', 'Napoli', '2025-10-08', '10:30:00', 'PROGRAMMATO', 0, 'ARRIVO', NULL),
('IT999G', 'Itairways', 'Dubai', 'Napoli', '2025-12-08', '20:15:00', 'PROGRAMMATO', 0, 'ARRIVO', NULL),
('RY827A', 'Ryanair', 'Napoli', 'Salerno', '2025-09-11', '09:12:00', 'IN_RITARDO', 5, 'PARTENZA', 4),
('KL404', 'KLM', 'Napoli', 'Amsterdam', '2024-01-21', '11:45:00', 'PROGRAMMATO', 0, 'PARTENZA', 7),
('AZ456', 'Alitalia', 'Napoli', 'Venezia', '2025-08-15', '08:30:00', 'PROGRAMMATO', 0, 'PARTENZA', 7),
('FR789', 'Ryanair', 'Napoli', 'Barcellona', '2025-08-16', '12:15:00', 'IN_RITARDO', 15, 'PARTENZA', 11),
('LH234', 'Lufthansa', 'Napoli', 'Francoforte', '2025-08-17', '15:45:00', 'PROGRAMMATO', 0, 'PARTENZA', 4),
('BA567', 'British Airways', 'Napoli', 'Manchester', '2025-08-18', '18:20:00', 'PROGRAMMATO', 0, 'PARTENZA', 9),
('EK890', 'Emirates', 'Napoli', 'Dubai', '2025-08-19', '22:10:00', 'PROGRAMMATO', 0, 'PARTENZA', 2),
('AF123', 'Air France', 'Napoli', 'Nizza', '2025-08-20', '06:45:00', 'IN_RITARDO', 25, 'PARTENZA', 13),
('VY345', 'Vueling', 'Madrid', 'Napoli', '2025-08-21', '10:30:00', 'PROGRAMMATO', 0, 'ARRIVO', NULL),
('W6678', 'Wizz Air', 'Budapest', 'Napoli', '2025-08-22', '14:15:00', 'PROGRAMMATO', 0, 'ARRIVO', NULL),
('U2901', 'easyJet', 'Berlino', 'Napoli', '2025-08-23', '16:50:00', 'IN_RITARDO', 10, 'ARRIVO', NULL),
('TK234', 'Turkish Airlines', 'Istanbul', 'Napoli', '2025-08-24', '19:25:00', 'PROGRAMMATO', 0, 'ARRIVO', NULL),
('LX567', 'Swiss Air', 'Zurigo', 'Napoli', '2025-08-25', '11:40:00', 'PROGRAMMATO', 0, 'ARRIVO', NULL),
('OS890', 'Austrian Airlines', 'Vienna', 'Napoli', '2025-08-26', '13:55:00', 'ATTERRATO', 0, 'ARRIVO', NULL);


--
-- TOC entry 4955 (class 0 OID 0)
-- Dependencies: 220
-- Name: tickets_id_ticket_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tickets_id_ticket_seq', 74, true);


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


-- Completed on 2025-07-19 23:04:54

--
-- PostgreSQL database dump complete
--

