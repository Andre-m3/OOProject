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
            FrameVoli.dispose();                  // Facciamo il dispose della finestra VoliAdmin, non è previsto un riutilizzo sicuro!
            frameDash.setVisible(true);                     // Mostriamo nuovamente l'interfaccia AdminDashboard, che non avevamo mai cancellato ma solo nascosto!
        });


        /* Listener PRENOTA VOLO
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
        String[] columnNames = {"Numero Volo", "Compagnia", "Data", "Orario", "Stato", "Tipo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabella non modificabile
            }
        };
        tabellaVoli.setModel(tableModel);
        tabellaVoli.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        // Impostiamo la larghezza di ciascuna colonna
        tabellaVoli.getColumnModel().getColumn(0).setPreferredWidth(100); // Numero Volo
        tabellaVoli.getColumnModel().getColumn(1).setPreferredWidth(120); // Compagnia
        tabellaVoli.getColumnModel().getColumn(2).setPreferredWidth(100); // Data
        tabellaVoli.getColumnModel().getColumn(3).setPreferredWidth(80);  // Orario
        tabellaVoli.getColumnModel().getColumn(4).setPreferredWidth(100); // Stato
        tabellaVoli.getColumnModel().getColumn(5).setPreferredWidth(100); // Tipo
        // Bisogna effettuare modifiche in seguito alla correzione del diagramma UML!

    }
    private void loadVoli() {
        tableModel.setRowCount(0);      // È sempre buono usarlo, così per pulire la tabella da eventuali righe "sporche"

        var voli = controller.getVoliDisponibili();         // Richiamiamo il metodo esistente, sviluppato nel Controller

        if (voli == null || voli.isEmpty()) {               // Verifichiamo se ci sono voli
            // Aggiungi una riga vuota per indicare che non ci sono voli
            tableModel.addRow(new Object[]{"N/A", "Nessun volo", "prenotabile", "al momento", "", ""});
            btnBookFlight.setEnabled(false);                // In tal caso, non permettiamo all'utente di cliccare il pulsante "Prenota"
        } else {
            for (var volo : voli) {
                // Formatta l'orario con eventuale ritardo
                String orarioCompleto = volo.getOrarioPrevisto();
                if (volo.getRitardo() > 0) {
                    orarioCompleto += " (+" + volo.getRitardo() + " min)";
                }

                Object[] row = {
                        volo.getNumeroVolo(),
                        volo.getCompagniaAerea(),
                        volo.getData(),
                        orarioCompleto,
                        volo.getStato(),
                        volo.getTipoVolo()
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


            // Mostriamo le informazioni dettagliate del volo
            String dettagliVolo = String.format(
                    "Dettagli del volo selezionato:\n\n" +
                            "Numero Volo: %s\n" +
                            "Compagnia: %s\n" +
                            "Data: %s\n" +
                            "Orario: %s\n" +
                            "Stato: %s\n" +
                            "Tipo: %s",
                    voloSelezionato.getNumeroVolo(),
                    voloSelezionato.getCompagniaAerea(),
                    voloSelezionato.getData(),
                    voloSelezionato.getOrarioPrevisto(),
                    voloSelezionato.getStato(),
                    voloSelezionato.getTipoVolo()
            );

            int confirm = JOptionPane.showConfirmDialog(FrameVoli,
                    dettagliVolo + "\n\nVuoi procedere con la prenotazione?",
                    "Conferma Prenotazione",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // Per ora mostriamo un messaggio di successo
                // In futuro qui andranno i dialog per inserire i passeggeri, ecc.
                JOptionPane.showMessageDialog(FrameVoli,
                        "Prenotazione in corso per il volo " + numeroVolo + "!\n\n" +
                                "Funzionalità completa di prenotazione in arrivo...",
                        "Prenotazione",
                        JOptionPane.INFORMATION_MESSAGE);
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
        panel1.setMaximumSize(new Dimension(800, 650));
        panel1.setMinimumSize(new Dimension(800, 650));
        panel1.setPreferredSize(new Dimension(800, 650));
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 75));
        topPanel.setMaximumSize(new Dimension(720, 120));
        topPanel.setMinimumSize(new Dimension(720, 120));
        topPanel.setPreferredSize(new Dimension(720, 120));
        panel1.add(topPanel);
        listaVoli = new JLabel();
        Font listaVoliFont = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.BOLD, 32, listaVoli.getFont());
        if (listaVoliFont != null) listaVoli.setFont(listaVoliFont);
        listaVoli.setHorizontalAlignment(10);
        listaVoli.setHorizontalTextPosition(10);
        listaVoli.setMaximumSize(new Dimension(630, 40));
        listaVoli.setMinimumSize(new Dimension(630, 40));
        listaVoli.setPreferredSize(new Dimension(630, 40));
        listaVoli.setText("Lista Voli Prenotabili");
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
        midPanel.setMaximumSize(new Dimension(720, 350));
        midPanel.setMinimumSize(new Dimension(720, 350));
        midPanel.setPreferredSize(new Dimension(720, 350));
        panel1.add(midPanel);
        scrollPane = new JScrollPane();
        Font scrollPaneFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, scrollPane.getFont());
        if (scrollPaneFont != null) scrollPane.setFont(scrollPaneFont);
        scrollPane.setMaximumSize(new Dimension(710, 340));
        scrollPane.setMinimumSize(new Dimension(710, 340));
        scrollPane.setPreferredSize(new Dimension(710, 340));
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
        bottomPanel.setMaximumSize(new Dimension(720, 100));
        bottomPanel.setMinimumSize(new Dimension(720, 100));
        bottomPanel.setPreferredSize(new Dimension(720, 100));
        panel1.add(bottomPanel);
        spacerBypass = new JLabel();
        spacerBypass.setHorizontalAlignment(2);
        spacerBypass.setMaximumSize(new Dimension(570, 40));
        spacerBypass.setMinimumSize(new Dimension(570, 40));
        spacerBypass.setPreferredSize(new Dimension(570, 40));
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
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
