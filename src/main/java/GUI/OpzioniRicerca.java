package GUI;

import controller.Controller;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

public class OpzioniRicerca {
    private JFrame FrameUser;
    private JPanel panel1;
    private JPanel topPanel;
    private JPanel mainPanel;
    private JPanel opt1Panel;
    private JPanel opt2Panel;
    private JTextField textFieldPartenza;
    private JTextField textFieldDestinazione;
    private JButton btnRicercaPartenza;
    private JButton btnRicercaDestinazione;
    private JPanel bottomPanel;
    private JTable tabellaRisultati;
    private JPanel homePanel;
    private JButton btnHomepage;

    private Controller controller;
    private DefaultTableModel tableModel;

    public OpzioniRicerca(JFrame frame) {

        controller = Controller.getInstance();
        FrameUser = new JFrame("Opzioni di Ricerca Voli");
        FrameUser.setContentPane(panel1);
        /* Non vogliamo che alla chiusura venga terminata l'esecuzione del programma! (exit_on_cose)
         * Facendo 'dispose_on_close' abbiamo il controllo sulla chiusura della finestra...
         * Aggiungeremo un Listener (WindowsListener) per gestire la chiusura della finestra e tornare alla Dashboard Admin
         */
        FrameUser.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        FrameUser.pack();
        FrameUser.setLocationRelativeTo(null);
        FrameUser.setVisible(true);
        FrameUser.setResizable(false);

        setupButtons();
        setupTable();

        // Listener per la ricerca per partenza
        btnRicercaPartenza.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cittaPartenza = textFieldPartenza.getText().trim();
                if (!cittaPartenza.isEmpty()) {
                    ricercaVoliPerPartenza(cittaPartenza);
                } else {
                    JOptionPane.showMessageDialog(panel1,
                            "Inserisci una città di partenza!",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Listener per la ricerca per destinazione
        btnRicercaDestinazione.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cittaDestinazione = textFieldDestinazione.getText().trim();
                if (!cittaDestinazione.isEmpty()) {
                    ricercaVoliPerDestinazione(cittaDestinazione);
                } else {
                    JOptionPane.showMessageDialog(panel1,
                            "Inserisci una città di destinazione!",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Listener per tornare alla Homepage (User Dashboard)
        btnHomepage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Rende nuovamente visibile la finestra precedente (UserDashboard)
                frame.setVisible(true);

                // Chiude la finestra OpzioniRicerca
                FrameUser.dispose();
            }
        });

        // Listener per dire cosa bisogna fare al click del pulsante "X" (chiusura) oltre al Dispose imposto sopra!
        FrameUser.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(true);
            }
        });

        // Listener per premere ENTER nei campi di testo (equivalente a cliccare sul pulsante "btnRicerca...", molto utile!)
        textFieldPartenza.addActionListener(e -> btnRicercaPartenza.doClick());
        textFieldDestinazione.addActionListener(e -> btnRicercaDestinazione.doClick());
    }

    private void setupButtons() {

        Color sfondoLeggermenteScuro = new Color(214, 214, 214);

        // Pulsante RICERCA_PARTENZA
        btnRicercaPartenza.setBackground(sfondoLeggermenteScuro);
        btnRicercaPartenza.setForeground(new Color(78, 78, 78));
        btnRicercaPartenza.setFocusPainted(false);
        btnRicercaPartenza.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnRicercaPartenza.setOpaque(true);

        // Pulsante RICERCA_DESTINAZIONE
        btnRicercaDestinazione.setBackground(sfondoLeggermenteScuro);
        btnRicercaDestinazione.setForeground(new Color(78, 78, 78));
        btnRicercaDestinazione.setFocusPainted(false);
        btnRicercaDestinazione.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnRicercaDestinazione.setOpaque(true);

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
        // Configurazione iniziale della tabella
        String[] colonneIniziali = {"Informazioni"};
        tableModel = new DefaultTableModel(colonneIniziali, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabella non modificabile!
            }
        };

        tabellaRisultati.setModel(tableModel);
        tabellaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);     // SELEZIONE SINGOLA. IMPORTANTE (in tutte le nostre tabelle!!)
        tabellaRisultati.getTableHeader().setFont(new Font("JetBrains Mono Medium", Font.BOLD, 12));        // "Intestazioni" della tabella in Grassetto!

        // Colori per migliorare l'aspetto...
        tabellaRisultati.setGridColor(new Color(200, 200, 200));
        tabellaRisultati.setSelectionBackground(new Color(184, 207, 229));
        tabellaRisultati.setSelectionForeground(Color.BLACK);

    }

    private void ricercaVoliPerPartenza(String cittaPartenza) {
        try {
            // Ottengo i dati tramite il controller!
            Object[][] voli = controller.getVoliInPartenza(cittaPartenza);
            String[] colonne = controller.getColonneVoliPartenza();
            int numeroVoli = controller.contaVoliInPartenza(cittaPartenza);

            // Ora aggiorniamo il modello della tabella
            tableModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            if (numeroVoli == 0) {
                // Nessun volo trovato
                tableModel.addRow(new Object[]{
                        "Nessun volo in partenza da: " + cittaPartenza,
                        "Controlla l'ortografia della città",
                        "Esempi: Napoli, Milano, Roma",
                        "", "", "", ""
                });
            } else {
                // Aggiungiamo tutti i voli trovati
                for (Object[] volo : voli) {
                    tableModel.addRow(volo);
                }
            }

            tabellaRisultati.setModel(tableModel);

            // Messaggio informativo... (è stato trovato almeno un volo?)
            String titolo = (numeroVoli == 0) ? "Nessun risultato" :
                    "Trovati " + numeroVoli + " voli in partenza da " + cittaPartenza;

            JOptionPane.showMessageDialog(panel1, titolo, "Risultati ricerca",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel1,
                    "Errore durante la ricerca: " + ex.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ricercaVoliPerDestinazione(String cittaDestinazione) {
        try {
            // Ottengo i dati tramite il controller...
            Object[][] voli = controller.getVoliInArrivo(cittaDestinazione);
            String[] colonne = controller.getColonneVoliArrivo();
            int numeroVoli = controller.contaVoliInArrivo(cittaDestinazione);

            tableModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            if (numeroVoli == 0) {
                // Nessun volo trovato
                tableModel.addRow(new Object[]{
                        "Nessun volo in arrivo verso: " + cittaDestinazione,
                        "Controlla l'ortografia della città",
                        "Esempi: Napoli, Milano, Roma",
                        "", "", ""
                });
            } else {
                // // Aggiungiamo tutti i voli trovati
                for (Object[] volo : voli) {
                    tableModel.addRow(volo);
                }
            }

            tabellaRisultati.setModel(tableModel);

            // Messaggio informativo
            String titolo = (numeroVoli == 0) ? "Nessun risultato" :
                    "Trovati " + numeroVoli + " voli in arrivo verso " + cittaDestinazione;

            JOptionPane.showMessageDialog(panel1, titolo, "Risultati ricerca",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel1,
                    "Errore durante la ricerca: " + ex.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
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
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setMaximumSize(new Dimension(800, 600));
        panel1.setMinimumSize(new Dimension(800, 600));
        panel1.setPreferredSize(new Dimension(800, 600));
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(580, 60));
        topPanel.setMinimumSize(new Dimension(580, 60));
        topPanel.setPreferredSize(new Dimension(580, 60));
        panel1.add(topPanel, BorderLayout.NORTH);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.BOLD, 24, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("Opzioni di Ricerca Voli");
        label1.setVerticalAlignment(3);
        label1.setVerticalTextPosition(3);
        topPanel.add(label1, BorderLayout.CENTER);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setMaximumSize(new Dimension(580, 250));
        mainPanel.setMinimumSize(new Dimension(580, 250));
        mainPanel.setPreferredSize(new Dimension(580, 250));
        panel1.add(mainPanel, BorderLayout.CENTER);
        opt1Panel = new JPanel();
        opt1Panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        opt1Panel.setMaximumSize(new Dimension(450, 90));
        opt1Panel.setMinimumSize(new Dimension(450, 90));
        opt1Panel.setPreferredSize(new Dimension(450, 90));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(opt1Panel, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Ricerca per Città di Partenza:");
        opt1Panel.add(label2);
        textFieldPartenza = new JTextField();
        textFieldPartenza.setColumns(35);
        Font textFieldPartenzaFont = this.$$$getFont$$$("JetBrains Mono Light", Font.PLAIN, 12, textFieldPartenza.getFont());
        if (textFieldPartenzaFont != null) textFieldPartenza.setFont(textFieldPartenzaFont);
        textFieldPartenza.setForeground(new Color(-16777216));
        textFieldPartenza.setMinimumSize(new Dimension(49, 40));
        textFieldPartenza.setPreferredSize(new Dimension(251, 40));
        textFieldPartenza.setText("");
        opt1Panel.add(textFieldPartenza);
        btnRicercaPartenza = new JButton();
        Font btnRicercaPartenzaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnRicercaPartenza.getFont());
        if (btnRicercaPartenzaFont != null) btnRicercaPartenza.setFont(btnRicercaPartenzaFont);
        btnRicercaPartenza.setMaximumSize(new Dimension(80, 40));
        btnRicercaPartenza.setMinimumSize(new Dimension(80, 40));
        btnRicercaPartenza.setPreferredSize(new Dimension(80, 40));
        btnRicercaPartenza.setText("Cerca");
        opt1Panel.add(btnRicercaPartenza);
        opt2Panel = new JPanel();
        opt2Panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        opt2Panel.setMaximumSize(new Dimension(450, 85));
        opt2Panel.setMinimumSize(new Dimension(450, 85));
        opt2Panel.setPreferredSize(new Dimension(450, 85));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(opt2Panel, gbc);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setHorizontalTextPosition(11);
        label3.setText("Ricerca per Città di Destinazione");
        opt2Panel.add(label3);
        textFieldDestinazione = new JTextField();
        textFieldDestinazione.setColumns(35);
        Font textFieldDestinazioneFont = this.$$$getFont$$$("JetBrains Mono Light", Font.PLAIN, 12, textFieldDestinazione.getFont());
        if (textFieldDestinazioneFont != null) textFieldDestinazione.setFont(textFieldDestinazioneFont);
        textFieldDestinazione.setForeground(new Color(-16777216));
        textFieldDestinazione.setMinimumSize(new Dimension(49, 40));
        textFieldDestinazione.setPreferredSize(new Dimension(251, 40));
        textFieldDestinazione.setText("");
        opt2Panel.add(textFieldDestinazione);
        btnRicercaDestinazione = new JButton();
        Font btnRicercaDestinazioneFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnRicercaDestinazione.getFont());
        if (btnRicercaDestinazioneFont != null) btnRicercaDestinazione.setFont(btnRicercaDestinazioneFont);
        btnRicercaDestinazione.setMaximumSize(new Dimension(80, 40));
        btnRicercaDestinazione.setMinimumSize(new Dimension(80, 40));
        btnRicercaDestinazione.setPreferredSize(new Dimension(80, 40));
        btnRicercaDestinazione.setText("Cerca");
        opt2Panel.add(btnRicercaDestinazione);
        homePanel = new JPanel();
        homePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        homePanel.setMaximumSize(new Dimension(300, 55));
        homePanel.setMinimumSize(new Dimension(300, 50));
        homePanel.setPreferredSize(new Dimension(300, 55));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(homePanel, gbc);
        btnHomepage = new JButton();
        Font btnHomepageFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnHomepage.getFont());
        if (btnHomepageFont != null) btnHomepage.setFont(btnHomepageFont);
        btnHomepage.setHorizontalTextPosition(0);
        btnHomepage.setMaximumSize(new Dimension(100, 40));
        btnHomepage.setMinimumSize(new Dimension(100, 40));
        btnHomepage.setPreferredSize(new Dimension(100, 40));
        btnHomepage.setText("Homepage");
        homePanel.add(btnHomepage);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(0, 0));
        bottomPanel.setMaximumSize(new Dimension(580, 250));
        bottomPanel.setMinimumSize(new Dimension(580, 250));
        bottomPanel.setPreferredSize(new Dimension(580, 250));
        panel1.add(bottomPanel, BorderLayout.SOUTH);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setMaximumSize(new Dimension(750, 300));
        scrollPane1.setMinimumSize(new Dimension(750, 300));
        scrollPane1.setPreferredSize(new Dimension(750, 300));
        bottomPanel.add(scrollPane1, BorderLayout.CENTER);
        tabellaRisultati = new JTable();
        tabellaRisultati.setAutoCreateRowSorter(true);
        tabellaRisultati.setAutoResizeMode(4);
        tabellaRisultati.setFillsViewportHeight(true);
        Font tabellaRisultatiFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, tabellaRisultati.getFont());
        if (tabellaRisultatiFont != null) tabellaRisultati.setFont(tabellaRisultatiFont);
        scrollPane1.setViewportView(tabellaRisultati);
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