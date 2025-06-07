package GUI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;
import controller.Controller;

public class UserDashboard {
    private JFrame FrameUser;
    private JPanel buttonPanel;
    private JButton btnPrenotaVolo;
    private JButton btnAreaPersonale;
    private JButton btnViewVoli;
    private JButton btnLogout;
    private JPanel topPanel;
    private JLabel welcomeText;
    private JPanel panel1;
    private Controller controller;

    public UserDashboard(JFrame frame) {

        controller = Controller.getInstance();
        FrameUser = frame;                             // Non creiamo un nuovo frame, ma utilizziamo quello passato!

        FrameUser.setTitle("Dashboard Utente");
        FrameUser.setContentPane(panel1);
        FrameUser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FrameUser.pack();
        FrameUser.setLocationRelativeTo(null);         // Centra la finestra sullo schermo
        FrameUser.setVisible(true);
        FrameUser.setResizable(false);

        // Aggiorniamo il messaggio di benvenuto con il nome utente, così da fornire un risultato più efficace!
        if (controller.getUtenteLoggato() != null) {
            welcomeText.setText("Bentornato, " + controller.getUtenteLoggato().getUsername() + "!");
        }

        setupButtons();

        // Listener per il pulsante Visualizza Voli
        btnViewVoli.addActionListener(e -> {
            FrameUser.setVisible(false);  // Nascondi la dashboard admin
            new VoliAdmin(FrameUser);     // Apri l'interfaccia VoliAdmin passando il frame
            /* Non facciamo il dispose, è inutile cancellare l'istanza della dashboard, per poi ricrearla.
             * L'utente ci tornerà per forza qui! Di conseguenza, possiamo semplicemente nasconderla (setVisible(false))
             * e riaprirla (setVisible(true)) quando necessario.
             */
        });

        // Aggiungiamo funzionalità al pulsante logout
        btnLogout.addActionListener(e -> {
            controller.logout();
            FrameUser.dispose();
            // Apriamo di nuovo la pagina di login
            LandingPageLogin.showLoginPage();
        });

    }

    public UserDashboard() {
    controller = Controller.getInstance();
    setupButtons();

    }

    private void setupButtons() {
        /* Quanto segue è la personalizzazione dei 4 pulsanti della Dashboard Admin
         * La personalizzazione è la medesima per tutti e 4 i pulsanti!
         * Il colore dello sfondo (sfondoLeggermenteScuro) è stato creato come oggetto, quindi creato solo una volta
         * Questo implica che il colore sarà uguale per tutti e 4 i pulsanti, e facilmente modificabile!
         */

        Color sfondoLeggermenteScuro = new Color(214, 214, 214);                // Colore sfondo pulsante
        btnPrenotaVolo.setBackground(sfondoLeggermenteScuro);
        btnPrenotaVolo.setForeground(new Color(78, 78, 78));                   // Colore testo pulsante
        btnPrenotaVolo.setFocusPainted(false);
        btnPrenotaVolo.setBorder(BorderFactory.createCompoundBorder(                    // Colore, spessore e spaziatura del bordo del pulsante
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnPrenotaVolo.setOpaque(true);

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
        welcomeText.setText("Bentornato, User!");
        welcomeText.setVerticalAlignment(3);
        welcomeText.setVerticalTextPosition(3);
        topPanel.add(welcomeText, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 20, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("È il momento di prenotare una vacanza!");
        topPanel.add(label1, BorderLayout.SOUTH);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel2.setMaximumSize(new Dimension(800, 25));
        panel2.setMinimumSize(new Dimension(800, 25));
        panel2.setPreferredSize(new Dimension(800, 25));
        panel1.add(panel2);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout(0, 0));
        buttonPanel.setMaximumSize(new Dimension(500, 270));
        buttonPanel.setMinimumSize(new Dimension(500, 270));
        buttonPanel.setPreferredSize(new Dimension(500, 270));
        panel1.add(buttonPanel);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel3.setMaximumSize(new Dimension(250, 250));
        panel3.setMinimumSize(new Dimension(250, 250));
        panel3.setPreferredSize(new Dimension(250, 250));
        buttonPanel.add(panel3, BorderLayout.WEST);
        btnPrenotaVolo = new JButton();
        Font btnPrenotaVoloFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 16, btnPrenotaVolo.getFont());
        if (btnPrenotaVoloFont != null) btnPrenotaVolo.setFont(btnPrenotaVoloFont);
        btnPrenotaVolo.setHorizontalTextPosition(0);
        btnPrenotaVolo.setMargin(new Insets(0, 0, 0, 0));
        btnPrenotaVolo.setMaximumSize(new Dimension(150, 125));
        btnPrenotaVolo.setMinimumSize(new Dimension(150, 125));
        btnPrenotaVolo.setPreferredSize(new Dimension(150, 125));
        btnPrenotaVolo.setText("Prenota Volo");
        panel3.add(btnPrenotaVolo, BorderLayout.NORTH);
        btnAreaPersonale = new JButton();
        Font btnAreaPersonaleFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 16, btnAreaPersonale.getFont());
        if (btnAreaPersonaleFont != null) btnAreaPersonale.setFont(btnAreaPersonaleFont);
        btnAreaPersonale.setMaximumSize(new Dimension(150, 125));
        btnAreaPersonale.setMinimumSize(new Dimension(150, 125));
        btnAreaPersonale.setPreferredSize(new Dimension(150, 125));
        btnAreaPersonale.setText("Area Personale");
        panel3.add(btnAreaPersonale, BorderLayout.CENTER);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        panel4.setMaximumSize(new Dimension(250, 250));
        panel4.setMinimumSize(new Dimension(250, 250));
        panel4.setPreferredSize(new Dimension(250, 250));
        buttonPanel.add(panel4, BorderLayout.CENTER);
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
        panel4.add(btnLogout, BorderLayout.CENTER);
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
