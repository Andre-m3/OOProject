package GUI;

import controller.Controller;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

public class LandingPageLogin {
    private static JFrame FrameLogin;
    private Controller controller;
    private JPanel panel1;
    private JPanel topPanel;
    private JLabel mainText;
    private JPanel midPanel;
    private JTextField emailUsernameField;
    private JPanel emailUsernameForm;
    private JLabel subText;
    private JPanel passwordForm;
    private JPasswordField passwordField;
    private JPanel loginPanel;
    private JButton btnLogin;
    private JPanel bottomPanel;
    private JLabel registerField;

    public static void main(String[] args) {
        showLoginPage();
    }

    public static void showLoginPage() {
        FrameLogin = new JFrame("Login");
        FrameLogin.setContentPane(new LandingPageLogin().panel1);
        FrameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FrameLogin.pack();
        FrameLogin.setLocationRelativeTo(null);         // Centra la finestra sullo schermo
        FrameLogin.setVisible(true);

        // Per impedire eventuali problemi, impediamo all'utente di ridimensionare la finestra di Login
        FrameLogin.setResizable(false);
    }

    public LandingPageLogin() {
        controller = Controller.getInstance();     // Utilizziamo il pattern discusso in Controller per ottenere la sua istanza! Non creiamo nuove istanze!

        setupButtons();

        /* Personalizziamo il testo per passare al form di registrazione.
         * In questo caso abbiamo fatto in modo che quando il cursore passerà sopra al testo
         *      quest'ultimo cambi colore, così facendo renderà all'utente chiara l'idea di come muoversi!
         */
        registerField.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerField.setForeground(new Color(70, 70, 70));         // Colore del testo al passaggio del cursore

        /* Listener per il testo "...Registrati qui"
         * Aggiungiamo un MouseListener per gestire il cambio di colore e il click
         */
        registerField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                // Quando il mouse passa sopra, cambia colore
                registerField.setForeground(new Color(0, 102, 204));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                // Quando il mouse esce, ripristina il colore originale
                registerField.setForeground(new Color(70, 70, 70));
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                // Quando viene cliccato, apriamo la pagina di registrazione e chiudiamo quella di login
                JFrame registerFrame = new JFrame("Registrazione");
                new Register(registerFrame);
                FrameLogin.dispose();           // "Cancelliamo" la finestra di login con il dispose
            }
        });

        /* Listener per il pulsante di login. Effettuiamo vari controlli
         * Utilizziamo l'istanza del controller (già creata) per effettuare le verifiche presenti in essa!
         */
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieni i dati inseriti dall'utente
                String usernameOrEmail = emailUsernameField.getText();
                String password = new String(passwordField.getPassword());

                // Verifichiamo che i campi non siano effettivamente vuoti
                if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(FrameLogin,
                            "Inserisci sia email/username che password!",
                            "Errore di login",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Effettua il login tramite il controller
                boolean loginSuccess = controller.login(usernameOrEmail, password);

                if (loginSuccess) {
                    // Chiudiamo la finestra di login
                    FrameLogin.dispose();

                    // Creiamo una nuova finestra per la dashboard (relativa al login)
                    JFrame dashboardFrame = new JFrame();

                    // Apri l'interfaccia appropriata in base al tipo di utente
                    if (controller.isUtenteAdmin()) {
                        new AdminDashboard(dashboardFrame);
                    } else {
                        new UserDashboard(dashboardFrame);
                    }
                } else {
                    // In caso di Login fallito per utente inesistente, mostriamo un popUp di errore!!
                    JOptionPane.showMessageDialog(FrameLogin,
                            "Username o password non validi!",
                            "Errore di login",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    private void setupButtons() {
        /*
         * Quanto segue è la personalizzazione di alcuni componenti grafici della pagina di Login
         * Nello specifico, è stato cambiato il colore del pulsante di login (e relativamente del testo Login)
         * È stato inoltre aggiunto il bordo e ricolorato.
         * Questa personalizzazione sarà riproposta anche nel costruttore dell'interfaccia Register !!
         */
        Color sfondoLeggermenteScuro = new Color(214, 214, 214);        // Colore sfondo pulsante
        btnLogin.setBackground(sfondoLeggermenteScuro);
        btnLogin.setForeground(new Color(78, 78, 78));                  // Colore testo pulsante
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createCompoundBorder(                   // Colore, spessore e spaziatura del bordo del pulsante
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnLogin.setOpaque(true);
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
        topPanel.setLayout(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(800, 180));
        topPanel.setMinimumSize(new Dimension(800, 180));
        topPanel.setOpaque(true);
        topPanel.setPreferredSize(new Dimension(800, 180));
        panel1.add(topPanel);
        mainText = new JLabel();
        mainText.setAlignmentY(0.5f);
        mainText.setEnabled(true);
        Font mainTextFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 48, mainText.getFont());
        if (mainTextFont != null) mainText.setFont(mainTextFont);
        mainText.setHorizontalAlignment(0);
        mainText.setHorizontalTextPosition(0);
        mainText.setOpaque(false);
        mainText.setText("Bentornato!");
        mainText.setVerticalAlignment(3);
        mainText.setVerticalTextPosition(3);
        topPanel.add(mainText, BorderLayout.CENTER);
        subText = new JLabel();
        Font subTextFont = this.$$$getFont$$$("Droid Sans", Font.ITALIC, 20, subText.getFont());
        if (subTextFont != null) subText.setFont(subTextFont);
        subText.setHorizontalAlignment(0);
        subText.setHorizontalTextPosition(0);
        subText.setText("Inserisci email e password per iniziare...");
        topPanel.add(subText, BorderLayout.SOUTH);
        midPanel = new JPanel();
        midPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        midPanel.setMaximumSize(new Dimension(800, 180));
        midPanel.setMinimumSize(new Dimension(800, 180));
        midPanel.setPreferredSize(new Dimension(800, 180));
        panel1.add(midPanel);
        emailUsernameForm = new JPanel();
        emailUsernameForm.setLayout(new BorderLayout(0, 0));
        emailUsernameForm.setMaximumSize(new Dimension(400, 75));
        emailUsernameForm.setMinimumSize(new Dimension(400, 75));
        emailUsernameForm.setPreferredSize(new Dimension(400, 75));
        midPanel.add(emailUsernameForm);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 16, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(2);
        label1.setHorizontalTextPosition(0);
        label1.setText("Email / Username");
        label1.setVerticalAlignment(3);
        emailUsernameForm.add(label1, BorderLayout.CENTER);
        emailUsernameField = new JTextField();
        emailUsernameField.setColumns(0);
        emailUsernameField.setEnabled(true);
        Font emailUsernameFieldFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 14, emailUsernameField.getFont());
        if (emailUsernameFieldFont != null) emailUsernameField.setFont(emailUsernameFieldFont);
        emailUsernameField.setForeground(new Color(-13487566));
        emailUsernameField.setHorizontalAlignment(2);
        emailUsernameField.setMaximumSize(new Dimension(50, 40));
        emailUsernameField.setMinimumSize(new Dimension(50, 40));
        emailUsernameField.setPreferredSize(new Dimension(24, 40));
        emailUsernameField.setText("");
        emailUsernameField.setVisible(true);
        emailUsernameForm.add(emailUsernameField, BorderLayout.SOUTH);
        passwordForm = new JPanel();
        passwordForm.setLayout(new BorderLayout(0, 0));
        passwordForm.setMaximumSize(new Dimension(400, 75));
        passwordForm.setMinimumSize(new Dimension(400, 75));
        passwordForm.setPreferredSize(new Dimension(400, 75));
        midPanel.add(passwordForm);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 16, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setHorizontalAlignment(2);
        label2.setHorizontalTextPosition(0);
        label2.setText("Password");
        label2.setVerticalAlignment(3);
        passwordForm.add(label2, BorderLayout.CENTER);
        passwordField = new JPasswordField();
        passwordField.setColumns(0);
        passwordField.setForeground(new Color(-16777216));
        passwordField.setMaximumSize(new Dimension(50, 40));
        passwordField.setMinimumSize(new Dimension(50, 40));
        passwordField.setOpaque(true);
        passwordField.setPreferredSize(new Dimension(61, 40));
        passwordField.setText("");
        passwordForm.add(passwordField, BorderLayout.SOUTH);
        loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        loginPanel.setMaximumSize(new Dimension(800, 55));
        loginPanel.setMinimumSize(new Dimension(800, 55));
        loginPanel.setPreferredSize(new Dimension(800, 55));
        panel1.add(loginPanel);
        btnLogin = new JButton();
        btnLogin.setBackground(new Color(-11645362));
        Font btnLoginFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 14, btnLogin.getFont());
        if (btnLoginFont != null) btnLogin.setFont(btnLoginFont);
        btnLogin.setHorizontalTextPosition(0);
        btnLogin.setInheritsPopupMenu(false);
        btnLogin.setMaximumSize(new Dimension(300, 45));
        btnLogin.setMinimumSize(new Dimension(300, 45));
        btnLogin.setPreferredSize(new Dimension(300, 45));
        btnLogin.setText("Login");
        loginPanel.add(btnLogin);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(bottomPanel);
        registerField = new JLabel();
        Font registerFieldFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 14, registerField.getFont());
        if (registerFieldFont != null) registerField.setFont(registerFieldFont);
        registerField.setText("Non hai un account? Registrati qui...");
        bottomPanel.add(registerField);
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
