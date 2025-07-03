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
    }

    // Metodo per la modifica (in questo caso estetica) dei vari pulsanti
    private void setupButtons() {
        // Impostiamo i pulsanti con lo stesso stile utilizzato nelle altre interfacce!
        Color sfondoLeggermenteScuro = new Color(214, 214, 214);

        // Pulsante UPDATE FLIGHT
        btnUpdateFlight.setBackground(sfondoLeggermenteScuro);
        btnUpdateFlight.setForeground(new Color(78, 78, 78));
        btnUpdateFlight.setFocusPainted(false);
        btnUpdateFlight.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnUpdateFlight.setOpaque(true);

        // Pulsante HOMEPAGE
        btnHomepage.setBackground(sfondoLeggermenteScuro);
        btnHomepage.setForeground(new Color(78, 78, 78));
        btnHomepage.setFocusPainted(false);
        btnHomepage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnHomepage.setOpaque(true);

    }

    // Metodo per la configurazione della tabella di visualizzazione dei voli (per un admin)
    private void setupTable() {
        String[] colonne = {"Numero Volo", "Compagnia", "Data", "Orario", "Stato", "Tipo"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;           // Rende la tabella non editabile
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
            tableModel.addRow(new Object[]{"N/A", "Nessun volo", "presente", "nel sistema", "", ""});
            btnUpdateFlight.setEnabled(false);      // In tal caso, non permettiamo all'admin di cliccare il pulsante "modifica Volo"
        }
        else {
            for (var volo : voli) {     // L'utilizzo di var ci permette di non avere problemi, visto che non importiamo la classe volo, essendo classe del package model!
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
            btnUpdateFlight.setEnabled(true);
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
