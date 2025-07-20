package GUI;

import controller.Controller;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

/**
 * The type Voli user.
 */
public class VoliUser {
    private JFrame FrameVoli;
    private Controller controller;
    private JPanel panel1;
    private JButton btnHomepage;
    private JPanel topPanel;
    private JLabel listaVoli;
    private JPanel midPanel;
    private JScrollPane scrollPane;
    private JTable tabellaVoli;
    private JPanel bottomPanel;
    private JLabel spacerBypass;
    private JButton btnBookFlight;

    private DefaultTableModel tableModel;           // Nel codice impostiamo anche la tabella di visualizzazione dei voli, commentata di seguito!

    /**
     * Instantiates a new Voli user.
     *
     * @param frameDash the frame dash
     */
    public VoliUser(JFrame frameDash) {
        controller = Controller.getInstance();
        FrameVoli = new JFrame();

        FrameVoli.setTitle("Voli Esistenti");
        FrameVoli.setContentPane(panel1);
        /* Non vogliamo che alla chiusura venga terminata l'esecuzione del programma! (exit_on_cose)
         * Facendo 'dispose_on_close' abbiamo il controllo sulla chiusura della finestra...
         * Aggiungeremo un Listener (WindowsListener) per gestire la chiusura della finestra e tornare alla Dashboard Admin
         */
        FrameVoli.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        FrameVoli.pack();
        FrameVoli.setLocationRelativeTo(null);
        FrameVoli.setVisible(true);
        FrameVoli.setResizable(false);

        setupButtons();
        setupTable();
        loadVoli();

        // Aggiungiamo il listener per il pulsante Homepage
        btnHomepage.addActionListener(e -> {
            FrameVoli.dispose();                            // Facciamo il dispose della finestra VoliAdmin, non è previsto un riutilizzo sicuro!
            frameDash.setVisible(true);                     // Mostriamo nuovamente l'interfaccia AdminDashboard, che non avevamo mai cancellato ma solo nascosto!
        });


        /*
         * Listener PRENOTA VOLO
         * Attenzione! Come fatto (e ben commentato) in "AreaPrivata", andremo a passare insieme al frame anche
         * le "informazioni" del volo che selezioniamo. Non servirà inserire dati, ma semplicemente cliccare sul volo dalla lista!
         */
        btnBookFlight.addActionListener(e -> prenotaVoloSelezionato());

        /* Window Listener
         * Questa volta necessitiamo di un WindowListener
         * Il metodo windowClosing() viene chiamato quando si preme il pulsante X
         * Così torniamo alla dashboard admin quando questa finestra viene chiusa
         */
        FrameVoli.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameDash.setVisible(true);
            }
        });

    }

    private void setupButtons() {
        // Impostiamo i pulsanti con lo stesso stile utilizzato nelle altre interfacce!
        Color sfondoLeggermenteScuro = new Color(214, 214, 214);

        // Pulsante BOOK FLIGHT
        btnBookFlight.setBackground(sfondoLeggermenteScuro);
        btnBookFlight.setForeground(new Color(78, 78, 78));
        btnBookFlight.setFocusPainted(false);
        btnBookFlight.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnBookFlight.setOpaque(true);

        // Pulsante HOMEPAGE
        btnHomepage.setBackground(sfondoLeggermenteScuro);
        btnHomepage.setForeground(new Color(78, 78, 78));
        btnHomepage.setFocusPainted(false);
        btnHomepage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnHomepage.setOpaque(true);

    }
    private void setupTable() {
        // Configuriamo il modello della tabella!
        String[] columnNames = {"Numero Volo", "Compagnia", "Partenza", "Destinazione", "Data", "Orario", "Stato", "Tipo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabella non modificabile
            }
        };
        tabellaVoli.setModel(tableModel);
        tabellaVoli.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);      // Si può selezionare una sola riga per volta! (necessario per le funzionalità successive)

        // Applichiamo il custom renderer a tutte le colonne, per evidenziare particolari dettagli! Commentato sotto...
        VoliTableCellRenderer renderer = new VoliTableCellRenderer();
        for (int i = 0; i < tabellaVoli.getColumnCount(); i++) {
            tabellaVoli.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

    }
    private void loadVoli() {
        tableModel.setRowCount(0);      // È sempre "buona maniera" usarlo, così per pulire la tabella da eventuali righe "sporche"

        var voli = controller.getTuttiIVoli();              // Richiamiamo il metodo esistente, sviluppato nel Controller

        if (voli == null || voli.isEmpty()) {               // Verifichiamo se ci sono voli
            // Aggiungi una riga vuota per indicare che non ci sono voli
            tableModel.addRow(new Object[]{"N/A", "Nessun volo", "disponibile", "al momento", "", "", "", ""});
            btnBookFlight.setEnabled(false);             // In tal caso, non permettiamo all'utente di cliccare il pulsante "Prenota"
        } else {
            for (var volo : voli) {
                // Formatto l'orario con eventuale ritardo
                String orarioCompleto = volo.getOrarioPrevisto();
                if (volo.getRitardo() > 0) {
                    orarioCompleto += " (+" + volo.getRitardo() + " min)";
                }

                Object[] row = {
                        volo.getNumeroVolo(),
                        volo.getCompagniaAerea(),
                        volo.getPartenza(),
                        volo.getDestinazione(),
                        volo.getData(),
                        orarioCompleto,
                        volo.getStato(),
                        controller.getTipoVolo(volo)
                };
                tableModel.addRow(row);
            }
            btnBookFlight.setEnabled(true);
        }
    }

    private void prenotaVoloSelezionato() {
        int selectedRow = tabellaVoli.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(FrameVoli,
                    "Seleziona un volo da prenotare.",
                    "Nessun volo selezionato",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ottengo il numero del volo selezionato
        String numeroVolo = (String) tableModel.getValueAt(selectedRow, 0);

        if ("N/A".equals(numeroVolo)) {
            JOptionPane.showMessageDialog(FrameVoli,
                    "Non ci sono voli disponibili al momento.",
                    "Nessun volo disponibile",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Ottengo il volo completo dal controller
        var voloSelezionato = controller.getVoloPerNumero(numeroVolo);

        if (voloSelezionato == null) {
            JOptionPane.showMessageDialog(FrameVoli,
                    "Errore nel recupero dei dati del volo.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Controllo se il volo è disponibile alla prenotazione
        if (!controller.isVoloPrenotabile(numeroVolo)) {
            JOptionPane.showMessageDialog(FrameVoli,
                    "Questo volo non è prenotabile.\n" +
                            "Potrebbe essere cancellato, già decollato o nel passato.",
                    "Volo non prenotabile",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mostriamo le informazioni dettagliate del volo
        String dettagliVolo = String.format(
                "Dettagli del volo selezionato:\n\n" +
                        "Numero Volo: %s\n" +
                        "Compagnia: %s\n" +
                        "Partenza: %s\n" +
                        "Destinazione: %s\n" +
                        "Data: %s\n" +
                        "Orario: %s\n" +
                        "Stato: %s\n" +
                        "Tipo: %s",
                voloSelezionato.getNumeroVolo(),
                voloSelezionato.getCompagniaAerea(),
                voloSelezionato.getPartenza(),
                voloSelezionato.getDestinazione(),
                voloSelezionato.getData(),
                voloSelezionato.getOrarioPrevisto(),
                voloSelezionato.getStato(),
                controller.getTipoVolo(voloSelezionato)
        );

        // Chiediamo all'utente la conferma della prenotazione!
        int confirm = JOptionPane.showConfirmDialog(FrameVoli,
                dettagliVolo + "\n\nVuoi procedere con la prenotazione?",
                "Conferma Prenotazione",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        // Per ora mostriamo un messaggio di successo, ma qui andremo a implementare i dialog per creare i tickets
        if (confirm == JOptionPane.YES_OPTION) {
            // Procediamo con la prenotazione effettiva
            effettuaPrenotazione(numeroVolo);
        }

    }

    /**
     * Effettua la prenotazione completa del volo
     * @param numeroVolo Il numero del volo da prenotare
     */
    private void effettuaPrenotazione(String numeroVolo) {
        // Step 1: Chiediamo il numero di passeggeri
        int numeroPasseggeri = chiediNumeroPasseggeri();
        if (numeroPasseggeri == -1) {
            return; // Operazione annullata
        }

        // Step 2: Raccogliamo i dati per ogni passeggero
        String[][] datiPasseggeri = new String[numeroPasseggeri][4];
        for (int i = 0; i < numeroPasseggeri; i++) {
            String[] datiPasseggero = raccogliDatiPasseggero(i + 1, numeroPasseggeri);
            if (datiPasseggero == null) {
                return; // Operazione annullata
            }
            datiPasseggeri[i] = datiPasseggero;
        }

        // Step 3: Completiamo la prenotazione tramite controller
        String codicePrenotazione = controller.completaPrenotazione(numeroVolo, numeroPasseggeri, datiPasseggeri);
        if (codicePrenotazione != null) {
            // Step 4: Mostriamo la conferma finale
            mostraConfermaPrenotazione(codicePrenotazione);
        } else {
            JOptionPane.showMessageDialog(FrameVoli,
                    "Errore durante la creazione della prenotazione.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Chiede all'utente il numero di passeggeri
     * @return Numero di passeggeri o -1 se annullato
     */
    private int chiediNumeroPasseggeri() {
        String[] opzioni = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

        String scelta = (String) JOptionPane.showInputDialog(
                FrameVoli,
                "Seleziona il numero di passeggeri per questa prenotazione:",
                "Numero Passeggeri",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opzioni,
                "1"
        );

        if (scelta == null) {
            return -1; // Operazione annullata
        }

        return Integer.parseInt(scelta);
    }

    /**
     * Raccoglie i dati di un singolo passeggero utilizzando il dialog personalizzato
     * @param numeroPasseggero Il numero del passeggero
     * @param totalePasseggeri Il totale dei passeggeri
     * @return Array con i dati del passeggero [nome, cognome, documento, dataNascita] o null se annullato
     */
    private String[] raccogliDatiPasseggero(int numeroPasseggero, int totalePasseggeri) {
        DialogDatiPasseggero dialog = new DialogDatiPasseggero(FrameVoli, numeroPasseggero, totalePasseggeri);
        dialog.setVisible(true);

        // Verifica esplicita (vediamo se l'utente ha confermato la prenotazione oppure ha deciso di annullarla!)
        if (dialog.isConfermato()) {
            return dialog.getDatiPasseggero();
        } else {
            return null;        // Operazione annullata
        }
    }

    /**
     * Mostra la conferma finale della prenotazione
     * @param codicePrenotazione Il codice della prenotazione creata
     */
    private void mostraConfermaPrenotazione(String codicePrenotazione) {
        String[] dettagliPrenotazione = controller.getDettagliPrenotazione(codicePrenotazione);
        String[][] tickets = controller.getTicketsPrenotazione(codicePrenotazione);

        if (dettagliPrenotazione == null || tickets == null) {
            JOptionPane.showMessageDialog(FrameVoli,
                    "Errore nel recupero dei dettagli della prenotazione.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder conferma = new StringBuilder();
        conferma.append("✅ PRENOTAZIONE COMPLETATA CON SUCCESSO!\n\n");
        conferma.append("Codice Prenotazione: ").append(dettagliPrenotazione[0]).append("\n");
        conferma.append("Volo: ").append(dettagliPrenotazione[1]).append("\n");
        conferma.append("Data: ").append(dettagliPrenotazione[2]).append("\n");
        conferma.append("Tratta: ").append(dettagliPrenotazione[3]).append("\n");
        conferma.append("Stato: ").append(dettagliPrenotazione[4]).append("\n\n");
        conferma.append("Passeggeri: ").append(dettagliPrenotazione[5]).append("\n");

        conferma.append("TICKETS CREATI:\n");
        for (String[] ticket : tickets) {
            conferma.append("• ").append(ticket[0]).append(" ").append(ticket[1]);
            conferma.append(" - Posto: ").append(ticket[4]).append("\n");
        }

        JOptionPane.showMessageDialog(FrameVoli,
                conferma.toString(),
                "Prenotazione Completata",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Questo metodo (in realtà classe a tutti gli effetti)
     * è stato richiamato dalla repository di un utente su reddit, e adattato al nostro progetto.
     * Ciò ci permette di evidenziare un volo "IN_RITARDO" oppure "CANCELLATO"
     * Così che sia più visibile a primo impatto all'utente visualizzatore
     */
    private class VoliTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            Component cell = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            // Ottieni lo stato del volo per questa riga (colonna 6 = stato)
            Object statoObj = table.getValueAt(row, 6);
            String stato = statoObj != null ? statoObj.toString() : "";

            if ("CANCELLATO".equals(stato)) {
                // Rosso chiaro per voli cancellati
                if (!isSelected) {
                    cell.setBackground(new Color(255, 200, 200));
                    cell.setForeground(Color.RED.darker());
                }
                setFont(getFont().deriveFont(Font.BOLD));

            } else if ("IN_RITARDO".equals(stato)) {
                // Arancione chiaro per voli in ritardo
                if (!isSelected) {
                    cell.setBackground(new Color(255, 235, 200));
                    cell.setForeground(new Color(204, 102, 0));
                }
                setFont(getFont().deriveFont(Font.BOLD));

            } else {
                // Colori normali per altri stati
                if (!isSelected) {
                    cell.setBackground(Color.WHITE);
                    cell.setForeground(Color.BLACK);
                }
                setFont(getFont().deriveFont(Font.PLAIN));
            }

            return cell;
        }
    }






        {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.setEnabled(true);
        panel1.setMaximumSize(new Dimension(990, 700));
        panel1.setMinimumSize(new Dimension(990, 700));
        panel1.setPreferredSize(new Dimension(990, 700));
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 75));
        topPanel.setMaximumSize(new Dimension(900, 120));
        topPanel.setMinimumSize(new Dimension(900, 120));
        topPanel.setPreferredSize(new Dimension(900, 120));
        panel1.add(topPanel);
        listaVoli = new JLabel();
        Font listaVoliFont = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.BOLD, 32, listaVoli.getFont());
        if (listaVoliFont != null) listaVoli.setFont(listaVoliFont);
        listaVoli.setHorizontalAlignment(10);
        listaVoli.setHorizontalTextPosition(10);
        listaVoli.setMaximumSize(new Dimension(820, 40));
        listaVoli.setMinimumSize(new Dimension(820, 40));
        listaVoli.setPreferredSize(new Dimension(820, 40));
        listaVoli.setText("Lista Voli");
        listaVoli.setVerticalAlignment(3);
        listaVoli.setVerticalTextPosition(3);
        topPanel.add(listaVoli);
        btnHomepage = new JButton();
        Font btnHomepageFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, btnHomepage.getFont());
        if (btnHomepageFont != null) btnHomepage.setFont(btnHomepageFont);
        btnHomepage.setHorizontalAlignment(0);
        btnHomepage.setHorizontalTextPosition(0);
        btnHomepage.setMaximumSize(new Dimension(60, 40));
        btnHomepage.setMinimumSize(new Dimension(60, 40));
        btnHomepage.setPreferredSize(new Dimension(60, 40));
        btnHomepage.setText("Home");
        btnHomepage.setToolTipText("Torna alla Dashboard");
        btnHomepage.setVerticalAlignment(0);
        topPanel.add(btnHomepage);
        midPanel = new JPanel();
        midPanel.setLayout(new BorderLayout(0, 0));
        midPanel.setMaximumSize(new Dimension(900, 420));
        midPanel.setMinimumSize(new Dimension(900, 420));
        midPanel.setPreferredSize(new Dimension(900, 420));
        panel1.add(midPanel);
        scrollPane = new JScrollPane();
        Font scrollPaneFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, scrollPane.getFont());
        if (scrollPaneFont != null) scrollPane.setFont(scrollPaneFont);
        scrollPane.setMaximumSize(new Dimension(890, 410));
        scrollPane.setMinimumSize(new Dimension(890, 410));
        scrollPane.setPreferredSize(new Dimension(890, 410));
        midPanel.add(scrollPane, BorderLayout.CENTER);
        tabellaVoli = new JTable();
        tabellaVoli.setAutoCreateRowSorter(true);
        tabellaVoli.setAutoResizeMode(2);
        tabellaVoli.setFillsViewportHeight(true);
        Font tabellaVoliFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, tabellaVoli.getFont());
        if (tabellaVoliFont != null) tabellaVoli.setFont(tabellaVoliFont);
        tabellaVoli.setPreferredSize(new Dimension(71, 40));
        scrollPane.setViewportView(tabellaVoli);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        bottomPanel.setMaximumSize(new Dimension(900, 100));
        bottomPanel.setMinimumSize(new Dimension(900, 100));
        bottomPanel.setPreferredSize(new Dimension(900, 100));
        panel1.add(bottomPanel);
        spacerBypass = new JLabel();
        spacerBypass.setHorizontalAlignment(2);
        spacerBypass.setMaximumSize(new Dimension(760, 40));
        spacerBypass.setMinimumSize(new Dimension(760, 40));
        spacerBypass.setPreferredSize(new Dimension(760, 40));
        spacerBypass.setText("");
        bottomPanel.add(spacerBypass);
        btnBookFlight = new JButton();
        Font btnBookFlightFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, btnBookFlight.getFont());
        if (btnBookFlightFont != null) btnBookFlight.setFont(btnBookFlightFont);
        btnBookFlight.setHorizontalAlignment(0);
        btnBookFlight.setHorizontalTextPosition(0);
        btnBookFlight.setMaximumSize(new Dimension(130, 40));
        btnBookFlight.setMinimumSize(new Dimension(130, 40));
        btnBookFlight.setPreferredSize(new Dimension(130, 40));
        btnBookFlight.setText("Prenota Volo");
        btnBookFlight.setVerticalAlignment(0);
        btnBookFlight.setVerticalTextPosition(0);
        bottomPanel.add(btnBookFlight);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * $$$ get root component $$$ j component.
     *
     * @return the j component
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
