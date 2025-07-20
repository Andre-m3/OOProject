package GUI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;
import controller.Controller;

/**
 * The type Admin dashboard.
 */
public class AdminDashboard {
    private JFrame FrameAdmin;
    private Controller controller;
    private JPanel panel1;
    private JPanel topPanel;
    private JLabel welcomeText;
    private JPanel btnsPanel;
    private JButton btnAggiungiVolo;
    private JButton btnAreaPersonale;
    private JButton btnViewVoli;
    private JButton btnLogout;
    private JLabel subText;

    /**
     * Instantiates a new Admin dashboard.
     *
     * @param frame the frame
     */
    public AdminDashboard(JFrame frame) {

        controller = Controller.getInstance();
        FrameAdmin = frame;                             // Non creiamo un nuovo frame, ma utilizziamo quello passato!

        FrameAdmin.setTitle("Dashboard Admin");
        FrameAdmin.setContentPane(panel1);
        FrameAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FrameAdmin.pack();
        FrameAdmin.setLocationRelativeTo(null);
        FrameAdmin.setVisible(true);
        FrameAdmin.setResizable(false);

        // Per renderlo migliore, inseriamo l'username dell'utente nel messaggio di benvenuto!
        if (controller.getUtenteLoggato() != null) {
            welcomeText.setText("Bentornato, " + controller.getUtenteLoggato().getUsername() + "!");
        }       // EXCEPTION - PER FORZA L'UTENTE È LOGGATO! LEVARE IF

        setupButtons();

        // Listener per il pulsante Visualizza Voli
        btnViewVoli.addActionListener(e -> {
            FrameAdmin.setVisible(false);  // Nascondi la dashboard admin
            new VoliAdmin(FrameAdmin);     // Apri l'interfaccia VoliAdmin passando il frame
            /* Non facciamo il dispose, è inutile cancellare l'istanza della dashboard, per poi ricrearla.
             * L'utente ci tornerà per forza qui! Di conseguenza, possiamo semplicemente nasconderla (setVisible(false))
             * e riaprirla (setVisible(true)) quando necessario.
             */
        });

        // Listener per il pulsante Area Personale (dell'amministratore, non utente!)
        btnAreaPersonale.addActionListener(e -> {
            FrameAdmin.setVisible(false);        // Nascondiamo la dashboard admin
            new AreaPrivataAdmin(FrameAdmin);         // Apriamo l'interfaccia AreaPrivata passando il frame
        });

        // Creiamo il Listener per il pulsante di Logout, che dovrà portarci di nuovo al Login!
        btnLogout.addActionListener(e -> {
            controller.logout();
            FrameAdmin.dispose();
            // Apri di nuovo la pagina di login, importante!
            LandingPageLogin.showLoginPage();
        });

        // Listener per il pulsante "Inserisci Volo"
        btnAggiungiVolo.addActionListener(e -> {
            // Apriamo un dialog (consulta package GUI) per inserire un nuovo volo
            // Passiamo null come callback dato che dalla dashboard non abbiamo una lista da aggiornare
            new DialogInserisciVolo(FrameAdmin, null);
        });

    }

    private void setupButtons() {
        /* Quanto segue è la personalizzazione dei 4 pulsanti della Dashboard Admin
         * La personalizzazione è la medesima per tutti e 4 i pulsanti!
         * Il colore dello sfondo (sfondoLeggermenteScuro) è stato creato come oggetto, quindi creato solo una volta
         * Questo implica che il colore sarà uguale per tutti e 4 i pulsanti, e facilmente modificabile!
         */

        Color sfondoLeggermenteScuro = new Color(214, 214, 214);                // Colore sfondo pulsante
        btnAggiungiVolo.setBackground(sfondoLeggermenteScuro);
        btnAggiungiVolo.setForeground(new Color(78, 78, 78));                   // Colore testo pulsante
        btnAggiungiVolo.setFocusPainted(false);
        btnAggiungiVolo.setBorder(BorderFactory.createCompoundBorder(                    // Colore, spessore e spaziatura del bordo del pulsante
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnAggiungiVolo.setOpaque(true);

        btnAreaPersonale.setBackground(sfondoLeggermenteScuro);
        btnAreaPersonale.setForeground(new Color(78, 78, 78));                  // Colore testo pulsante
        btnAreaPersonale.setFocusPainted(false);
        btnAreaPersonale.setBorder(BorderFactory.createCompoundBorder(                   // Colore, spessore e spaziatura del bordo del pulsante
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnAreaPersonale.setOpaque(true);

        btnViewVoli.setBackground(sfondoLeggermenteScuro);
        btnViewVoli.setForeground(new Color(78, 78, 78));                  // Colore testo pulsante
        btnViewVoli.setFocusPainted(false);
        btnViewVoli.setBorder(BorderFactory.createCompoundBorder(                   // Colore, spessore e spaziatura del bordo del pulsante
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnViewVoli.setOpaque(true);

        btnLogout.setBackground(sfondoLeggermenteScuro);
        btnLogout.setForeground(new Color(78, 78, 78));                  // Colore testo pulsante
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createCompoundBorder(                   // Colore, spessore e spaziatura del bordo del pulsante
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnLogout.setOpaque(true);
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
        panel1.setMaximumSize(new Dimension(800, 600));
        panel1.setMinimumSize(new Dimension(800, 600));
        panel1.setPreferredSize(new Dimension(800, 600));
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(5, 2));
        Font topPanelFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 36, topPanel.getFont());
        if (topPanelFont != null) topPanel.setFont(topPanelFont);
        topPanel.setMaximumSize(new Dimension(800, 160));
        topPanel.setMinimumSize(new Dimension(800, 160));
        topPanel.setPreferredSize(new Dimension(800, 160));
        panel1.add(topPanel);
        welcomeText = new JLabel();
        Font welcomeTextFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 48, welcomeText.getFont());
        if (welcomeTextFont != null) welcomeText.setFont(welcomeTextFont);
        welcomeText.setHorizontalAlignment(0);
        welcomeText.setHorizontalTextPosition(0);
        welcomeText.setText("Bentornato, Admin!");
        welcomeText.setVerticalAlignment(3);
        welcomeText.setVerticalTextPosition(3);
        topPanel.add(welcomeText, BorderLayout.CENTER);
        subText = new JLabel();
        Font subTextFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 20, subText.getFont());
        if (subTextFont != null) subText.setFont(subTextFont);
        subText.setHorizontalAlignment(0);
        subText.setHorizontalTextPosition(0);
        subText.setText("Cosa desideri fare oggi?");
        topPanel.add(subText, BorderLayout.SOUTH);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel2.setMaximumSize(new Dimension(800, 25));
        panel2.setMinimumSize(new Dimension(800, 25));
        panel2.setPreferredSize(new Dimension(800, 25));
        panel1.add(panel2);
        btnsPanel = new JPanel();
        btnsPanel.setLayout(new GridBagLayout());
        btnsPanel.setMaximumSize(new Dimension(500, 270));
        btnsPanel.setMinimumSize(new Dimension(500, 270));
        btnsPanel.setPreferredSize(new Dimension(500, 270));
        panel1.add(btnsPanel);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel3.setMaximumSize(new Dimension(250, 250));
        panel3.setMinimumSize(new Dimension(250, 250));
        panel3.setPreferredSize(new Dimension(250, 250));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        btnsPanel.add(panel3, gbc);
        btnAggiungiVolo = new JButton();
        Font btnAggiungiVoloFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 16, btnAggiungiVolo.getFont());
        if (btnAggiungiVoloFont != null) btnAggiungiVolo.setFont(btnAggiungiVoloFont);
        btnAggiungiVolo.setHorizontalTextPosition(0);
        btnAggiungiVolo.setMargin(new Insets(0, 0, 0, 0));
        btnAggiungiVolo.setMaximumSize(new Dimension(150, 125));
        btnAggiungiVolo.setMinimumSize(new Dimension(150, 125));
        btnAggiungiVolo.setPreferredSize(new Dimension(150, 125));
        btnAggiungiVolo.setText("Inserisci Volo");
        panel3.add(btnAggiungiVolo, BorderLayout.NORTH);
        btnAreaPersonale = new JButton();
        Font btnAreaPersonaleFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 16, btnAreaPersonale.getFont());
        if (btnAreaPersonaleFont != null) btnAreaPersonale.setFont(btnAreaPersonaleFont);
        btnAreaPersonale.setMaximumSize(new Dimension(150, 125));
        btnAreaPersonale.setMinimumSize(new Dimension(150, 125));
        btnAreaPersonale.setPreferredSize(new Dimension(150, 125));
        btnAreaPersonale.setText("Area Personale");
        panel3.add(btnAreaPersonale, BorderLayout.SOUTH);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        panel4.setMaximumSize(new Dimension(250, 250));
        panel4.setMinimumSize(new Dimension(250, 250));
        panel4.setPreferredSize(new Dimension(250, 250));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        btnsPanel.add(panel4, gbc);
        btnViewVoli = new JButton();
        Font btnViewVoliFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 16, btnViewVoli.getFont());
        if (btnViewVoliFont != null) btnViewVoli.setFont(btnViewVoliFont);
        btnViewVoli.setHorizontalTextPosition(0);
        btnViewVoli.setMaximumSize(new Dimension(150, 125));
        btnViewVoli.setMinimumSize(new Dimension(150, 125));
        btnViewVoli.setPreferredSize(new Dimension(150, 125));
        btnViewVoli.setText("Visualizza Voli");
        panel4.add(btnViewVoli, BorderLayout.NORTH);
        btnLogout = new JButton();
        Font btnLogoutFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 16, btnLogout.getFont());
        if (btnLogoutFont != null) btnLogout.setFont(btnLogoutFont);
        btnLogout.setMaximumSize(new Dimension(150, 125));
        btnLogout.setMinimumSize(new Dimension(150, 125));
        btnLogout.setPreferredSize(new Dimension(150, 125));
        btnLogout.setText("Logout");
        panel4.add(btnLogout, BorderLayout.SOUTH);
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
