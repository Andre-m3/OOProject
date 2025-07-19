package GUI;

import controller.Controller;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

public class VoliAdmin {
    private JFrame FrameFlightlist;
    private Controller controller;
    private JPanel panel1;
    private JPanel topPanel;
    private JLabel listaVoli;
    private JPanel midPanel;
    private JButton btnHomepage;
    private JTable tabellaVoli;
    private JScrollPane scrollPane;
    private JPanel bottomPanel;
    private JButton btnUpdateFlight;
    private JLabel spacerBypass;
    private JButton btnUpdateGate;
    private JButton btnDeleteFlight;

    private DefaultTableModel tableModel;       // Nel codice impostiamo anche la tabella di visualizzazione dei voli, commentata di seguito!

    public VoliAdmin(JFrame frameDash) {
        controller = Controller.getInstance();
        FrameFlightlist = new JFrame();

        FrameFlightlist.setTitle("Voli Esistenti");
        FrameFlightlist.setContentPane(panel1);
        /* Non vogliamo che alla chiusura venga terminata l'esecuzione del programma! (exit_on_cose)
         * Facendo 'dispose_on_close' abbiamo il controllo sulla chiusura della finestra...
         * Aggiungeremo un Listener (WindowsListener) per gestire la chiusura della finestra e tornare alla Dashboard Admin
         */
        FrameFlightlist.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        FrameFlightlist.pack();
        FrameFlightlist.setLocationRelativeTo(null);
        FrameFlightlist.setVisible(true);
        FrameFlightlist.setResizable(false);

        setupButtons();
        setupTable();
        loadVoliAdmin();


        // Aggiungiamo il listener per il pulsante Homepage
        btnHomepage.addActionListener(e -> {
            FrameFlightlist.dispose();                  // Facciamo il dispose della finestra VoliAdmin, non è previsto un riutilizzo sicuro!
            frameDash.setVisible(true);                     // Mostriamo nuovamente l'interfaccia AdminDashboard, che non avevamo mai cancellato ma solo nascosto!
        });

        /* Questa volta necessitiamo di un WindowListener
         * Il metodo windowClosing() viene chiamato quando si preme il pulsante X
         * Così torniamo alla dashboard admin quando questa finestra viene chiusa
         */
        FrameFlightlist.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameDash.setVisible(true);
            }
        });

        // Listener per il pulsante "Modifica Volo"
        btnUpdateFlight.addActionListener(e -> {
            int selectedRow = tabellaVoli.getSelectedRow();
            if (selectedRow >= 0) {
                String numeroVolo = (String) tableModel.getValueAt(selectedRow, 0);
                if (!"N/A".equals(numeroVolo)) {
                    // Dialog creato e presente nel package GUI (DialogModificaVolo)
                    new DialogModificaVolo(FrameFlightlist, numeroVolo, this::loadVoliAdmin);       // Nota: "this::loadVoliAdmin" è il Runnable! (onSaveCallback), commentato nei dialog
                    // Nello specifico "this::loadVoliAdmin" è una method reference.
                    // È un modo alternativo (ma più efficiente) per dire: "quando il dialog finisce, esegui questo metodo" (in questo caso loadVoliAdmin)
                }
            } else {
                JOptionPane.showMessageDialog(FrameFlightlist,
                        "Seleziona un volo dalla tabella per modificarlo.",
                        "Nessun volo selezionato",
                        JOptionPane.WARNING_MESSAGE);
            }
        });


        // Listener per il pulsante "Aggiorna Gate"
        btnUpdateGate.addActionListener(e -> {
            int selectedRow = tabellaVoli.getSelectedRow();
            if (selectedRow >= 0) {
                String numeroVolo = (String) tableModel.getValueAt(selectedRow, 0);
                if (!"N/A".equals(numeroVolo)) {
                    // Apriamo il Dialog creato, equivalente al commento effettuato per il primo Listener (UpdateFlight)
                    new DialogAggiornaGate(FrameFlightlist, numeroVolo, this::loadVoliAdmin);       // Nota: vedi commento sopra, ha funzionalità equivalente (UpdateFlight)
                }
            } else {
                JOptionPane.showMessageDialog(FrameFlightlist,
                        "Seleziona un volo dalla tabella per aggiornare il gate.",
                        "Nessun volo selezionato",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        // Listener per il pulsante "Elimina Volo" - aggiungi dopo gli altri listener
        btnDeleteFlight.addActionListener(e -> {
            int selectedRow = tabellaVoli.getSelectedRow();
            if (selectedRow >= 0) {
                String numeroVolo = (String) tableModel.getValueAt(selectedRow, 0);
                if (!"N/A".equals(numeroVolo)) {
                    eliminaVoloSelezionato(numeroVolo);
                }
            } else {
                JOptionPane.showMessageDialog(FrameFlightlist,
                        "Seleziona un volo dalla tabella per eliminarlo.",
                        "Nessun volo selezionato",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

    }

    // Metodo per la modifica (in questo caso estetica) dei vari pulsanti
    private void setupButtons() {
        // Impostiamo i pulsanti con lo stesso stile utilizzato nelle altre interfacce!
        Color sfondoLeggermenteScuro = new Color(214, 214, 214);

        // Pulsante "Aggiorna Volo"
        btnUpdateFlight.setBackground(sfondoLeggermenteScuro);
        btnUpdateFlight.setForeground(new Color(78, 78, 78));
        btnUpdateFlight.setFocusPainted(false);
        btnUpdateFlight.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnUpdateFlight.setOpaque(true);

        // Pulsante "Aggiorna Gate"
        btnUpdateGate.setBackground(sfondoLeggermenteScuro);
        btnUpdateGate.setForeground(new Color(78, 78, 78));
        btnUpdateGate.setFocusPainted(false);
        btnUpdateGate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnUpdateGate.setOpaque(true);

        // Pulsante "Homepage"
        btnHomepage.setBackground(sfondoLeggermenteScuro);
        btnHomepage.setForeground(new Color(78, 78, 78));
        btnHomepage.setFocusPainted(false);
        btnHomepage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnHomepage.setOpaque(true);

        // Pulsante "Elimina Volo"
        btnDeleteFlight.setBackground(sfondoLeggermenteScuro);
        btnDeleteFlight.setForeground(new Color(78, 78, 78));
        btnDeleteFlight.setFocusPainted(false);
        btnDeleteFlight.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnDeleteFlight.setOpaque(true);

    }

    // Metodo per la configurazione della tabella di visualizzazione dei voli (per un admin)
    private void setupTable() {
        String[] columnNames = {"Numero Volo", "Compagnia", "Partenza", "Destinazione", "Data", "Orario", "Stato", "Tipo", "Gate"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;       // Tabella non editabile
            }
        };

        tabellaVoli.setModel(tableModel);

        /* IMPORTANTE! Non dobbiamo dare all'utente la possibilità di selezionare più di un volo
         * Più selezioni contemporanee creeranno problemi difficilmente gestibili dopo il click sul btn "Prenota".
         */
        tabellaVoli.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // Metodo per richiamare e gestire il risultato restituito dal metodo del Controller
    private void loadVoliAdmin() {
        tableModel.setRowCount(0); // È sempre buono usarlo, così per pulire la tabella da eventuali righe "sporche"

        var voli = controller.getTuttiIVoli();              // Richiamiamo il metodo esistente, sviluppato nel Controller

        if (voli == null || voli.isEmpty()) {               // Verifichiamo se ci sono voli
            tableModel.addRow(new Object[]{"N/A", "Nessun volo", "presente", "nel sistema", "", "", "", "", ""});
            btnUpdateFlight.setEnabled(false);      // In tal caso, non permettiamo all'amministratore di cliccare il pulsante "Modifica Volo"
            btnUpdateGate.setEnabled(false);        // Stesso discorso per il pulsante "Aggiorna Gate"
            btnDeleteFlight.setEnabled(false);      // Stesso discorso per DeleteFlight
        } else {
            for (var volo : voli) {                 // L'utilizzo di var ci permette di non avere problemi, siccome che non importiamo la classe volo, essendo classe del package model!
                String orarioCompleto = volo.getOrarioPrevisto();
                if (volo.getRitardo() > 0) {
                    orarioCompleto += " (+" + volo.getRitardo() + " min)";
                }

                // Otteniamo il gate (se è un volo in partenza)
                String gateInfo = "";
                if (controller.isVoloInPartenza(volo)) {
                    Short gate = controller.getGateImbarco(volo);
                    gateInfo = gate != null ? "Gate " + gate : "Non assegnato";
                } else {
                    gateInfo = "N/A";
                }

                Object[] row = {
                        volo.getNumeroVolo(),
                        volo.getCompagniaAerea(),
                        volo.getPartenza(),
                        volo.getDestinazione(),
                        volo.getData(),
                        orarioCompleto,
                        volo.getStato(),
                        controller.getTipoVolo(volo),
                        gateInfo

                };
                tableModel.addRow(row);
            }
            btnUpdateFlight.setEnabled(true);
            btnUpdateGate.setEnabled(true);
            btnDeleteFlight.setEnabled(true);
        }
    }

    /**
     * Versione semplificata per l'eliminazione del volo
     *
     * @param numeroVolo Il numero del volo da eliminare
     */
    private void eliminaVoloSelezionato(String numeroVolo) {
        // Verifica se il volo può essere eliminato
        if (!controller.puoEliminareVolo(numeroVolo)) {
            JOptionPane.showMessageDialog(FrameFlightlist,
                    "Impossibile eliminare il volo " + numeroVolo + ":\n" +
                            "Potrebbero esserci prenotazioni attive per questo volo.",
                    "Eliminazione non consentita",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Dialog di conferma semplificato
        String messaggioConferma = String.format(
                "⚠️ ATTENZIONE ⚠️\n\n" +
                        "Stai per eliminare definitivamente il volo:\n\n" +
                        "- %s\n\n" +
                        "Questa operazione NON può essere annullata!\n\n" +
                        "Sei sicuro di voler procedere?",
                numeroVolo
        );

        int conferma = JOptionPane.showConfirmDialog(
                FrameFlightlist,
                messaggioConferma,
                "Conferma eliminazione volo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (conferma == JOptionPane.YES_OPTION) {
            // Procedi con l'eliminazione
            boolean successo = controller.eliminaVolo(numeroVolo);

            if (successo) {
                JOptionPane.showMessageDialog(FrameFlightlist,
                        "✅ Volo " + numeroVolo + " eliminato con successo!",
                        "Eliminazione completata",
                        JOptionPane.INFORMATION_MESSAGE);
                loadVoliAdmin();
            } else {
                JOptionPane.showMessageDialog(FrameFlightlist,
                        "❌ Errore durante l'eliminazione del volo " + numeroVolo,
                        "Errore eliminazione",
                        JOptionPane.ERROR_MESSAGE);
            }
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
        spacerBypass.setMaximumSize(new Dimension(485, 40));
        spacerBypass.setMinimumSize(new Dimension(485, 40));
        spacerBypass.setPreferredSize(new Dimension(485, 40));
        spacerBypass.setText("");
        bottomPanel.add(spacerBypass);
        btnUpdateGate = new JButton();
        Font btnUpdateGateFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, btnUpdateGate.getFont());
        if (btnUpdateGateFont != null) btnUpdateGate.setFont(btnUpdateGateFont);
        btnUpdateGate.setHorizontalAlignment(0);
        btnUpdateGate.setHorizontalTextPosition(0);
        btnUpdateGate.setMaximumSize(new Dimension(130, 40));
        btnUpdateGate.setMinimumSize(new Dimension(130, 40));
        btnUpdateGate.setPreferredSize(new Dimension(130, 40));
        btnUpdateGate.setText("Aggiorna Gate");
        btnUpdateGate.setVerticalAlignment(0);
        btnUpdateGate.setVerticalTextPosition(0);
        bottomPanel.add(btnUpdateGate);
        btnUpdateFlight = new JButton();
        Font btnUpdateFlightFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, btnUpdateFlight.getFont());
        if (btnUpdateFlightFont != null) btnUpdateFlight.setFont(btnUpdateFlightFont);
        btnUpdateFlight.setHorizontalAlignment(0);
        btnUpdateFlight.setHorizontalTextPosition(0);
        btnUpdateFlight.setMaximumSize(new Dimension(130, 40));
        btnUpdateFlight.setMinimumSize(new Dimension(130, 40));
        btnUpdateFlight.setPreferredSize(new Dimension(130, 40));
        btnUpdateFlight.setText("Modifica Volo");
        btnUpdateFlight.setVerticalAlignment(0);
        btnUpdateFlight.setVerticalTextPosition(0);
        bottomPanel.add(btnUpdateFlight);
        btnDeleteFlight = new JButton();
        Font btnDeleteFlightFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, btnDeleteFlight.getFont());
        if (btnDeleteFlightFont != null) btnDeleteFlight.setFont(btnDeleteFlightFont);
        btnDeleteFlight.setMaximumSize(new Dimension(130, 40));
        btnDeleteFlight.setMinimumSize(new Dimension(130, 40));
        btnDeleteFlight.setPreferredSize(new Dimension(130, 40));
        btnDeleteFlight.setText("Elimina Volo");
        bottomPanel.add(btnDeleteFlight);
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
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
