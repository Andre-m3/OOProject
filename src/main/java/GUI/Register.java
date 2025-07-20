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
import java.util.regex.Pattern;

/**
 * The type Register.
 */
public class Register {
    private JFrame FrameRegister;
    private Controller controller;
    private JPanel panel1;
    private JPanel topPanel;
    private JLabel mainText;
    private JLabel subText;
    private JPanel midPanel;
    private JPanel emailForm;
    private JTextField emailField;
    private JPanel passwordForm;
    private JPasswordField passwordField;
    private JPanel registerPanel;
    private JButton btnRegister;
    private JPanel bottomPanel;
    private JLabel registerField;
    private JPanel usernameForm;
    private JPanel isAdminCheck;
    private JCheckBox adminCheck;
    private JTextField usernameField;

    /**
     * Instantiates a new Register.
     *
     * @param frame the frame
     */
    public Register(JFrame frame) {
        controller = Controller.getInstance();
        FrameRegister = frame;              // Analogo -> AdminDashboard & UserDashboard

        FrameRegister.setTitle("Register");
        FrameRegister.setContentPane(panel1);
        FrameRegister.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FrameRegister.pack();
        FrameRegister.setLocationRelativeTo(null);         // Centra la finestra sullo schermo
        FrameRegister.setVisible(true);

        // Per impedire eventuali problemi, impediamo all'utente di ridimensionare la finestra di registrazione
        FrameRegister.setResizable(false);

        setupButtons();

        // Aggiungiamo un Listener per il pulsante di registrazione
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieni i dati inseriti dall'utente
                String email = emailField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                boolean isAdmin = adminCheck.isSelected();

                // Verifica che i campi non siano vuoti
                if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(FrameRegister,
                            "Tutti i campi sono obbligatori!",
                            "Errore di registrazione",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verifica il formato dell'email
                if (!isValidEmail(email)) {
                    JOptionPane.showMessageDialog(FrameRegister,
                            "Formato email non valido!",
                            "Errore di registrazione",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Registra l'utente tramite il controller
                boolean registrationSuccess = controller.registraUtente(email, username, password, isAdmin);

                if (registrationSuccess) {
                    // Registrazione completata con successo!
                    JOptionPane.showMessageDialog(FrameRegister,
                            "Registrazione completata con successo!",
                            "Registrazione",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Chiudiamo la finestra di registrazione
                    FrameRegister.dispose();

                    // Apri la pagina di login
                    LandingPageLogin.showLoginPage();
                } else {
                    // Registrazione fallita...
                    JOptionPane.showMessageDialog(FrameRegister,
                            "Username o email già in uso!",
                            "Errore di registrazione",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Aggiungiamo un Listener per gestire il cambio di colore e il click
        registerField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                // Quando il mouse passa sopra, cambia colore
                registerField.setForeground(new Color(0, 102, 204)); // Blu più vivace
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                // Quando il mouse esce, ripristina il colore originale
                registerField.setForeground(new Color(70, 70, 70));
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                // Quando viene cliccato, apri la pagina di login e chiudi quella di registrazione
                LandingPageLogin.showLoginPage();
                FrameRegister.dispose(); // Chiudi la finestra di registrazione
            }
        });

    }

    /**
     * Instantiates a new Register.
     */
    public Register() {
        controller = Controller.getInstance();
        setupButtons();

        // Aggiungiamo un MouseListener per gestire il cambio di colore e il click
        /* Può risultare ridondante, perchè tali Listeners sono stati precedentemente aggiunti
         *          al Costruttore precedente. Per eliminare la ridondanza avremmo potuto creare un metodo apposito,
         *          oppure inserirli nel metodo "setupButtons()", visto che viene richiamato ambo le volte.
         * Per rendere più chiaro il concetto, abbiamo preferito aggiungerlo qui per scopo didattico.
         */

        registerField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                // Quando il mouse passa sopra, cambia colore
                registerField.setForeground(new Color(0, 102, 204)); // Blu più vivace
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                // Quando il mouse esce, ripristina il colore originale
                registerField.setForeground(new Color(70, 70, 70));
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                // Quando viene cliccato, apri la pagina di login e chiudi quella di registrazione
                LandingPageLogin.showLoginPage();
                FrameRegister.dispose(); // Chiudi la finestra di registrazione
            }
        });

    }

    // Pattern per validare l'email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    /**
     * Verifica se l'email ha un formato valido
     *
     * @param email Email da verificare
     * @return true se l'email è valida, false altrimenti
     */
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }


    private void setupButtons() {
        /* Come descritto dell'interfaccia Login, abbiamo copiato la personalizzazione effettuata
         * per il pulsante di Login, e l'abbiamo riutilizzata anche per il pulsante di registrazione.
         * Non è stato cambiato nessun parametro.
         */
        Color sfondoLeggermenteScuro = new Color(214, 214, 214);            // Colore sfondo pulsante
        btnRegister.setBackground(sfondoLeggermenteScuro);
        btnRegister.setForeground(new Color(78, 78, 78));                   // Colore testo pulsante
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createCompoundBorder(                    // Colore, spessore e spaziatura del bordo del pulsante
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnRegister.setOpaque(true);

        /* Personalizziamo il testo per passare al form di registrazione.
         * In questo caso abbiamo fatto in modo che quando il cursore passerà sopra al testo
         *      quest'ultimo cambi colore, così facendo renderà all'utente chiara l'idea di come muoversi!
         * A differenza di prima (in Login), la personalizzazione del testo l'abbiamo aggiunta come da procedura
         *      nel metodo "setupButtons".
         * Non ci sono differenze effettive, se non nella pulizia, leggibilità e ridondanza del codice.
         */
        registerField.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerField.setForeground(new Color(70, 70, 70));         // Colore del testo al passaggio del cursore

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
        topPanel.setLayout(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(800, 150));
        topPanel.setMinimumSize(new Dimension(800, 150));
        topPanel.setOpaque(true);
        topPanel.setPreferredSize(new Dimension(800, 150));
        panel1.add(topPanel);
        mainText = new JLabel();
        mainText.setAlignmentY(0.5f);
        mainText.setEnabled(true);
        Font mainTextFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 48, mainText.getFont());
        if (mainTextFont != null) mainText.setFont(mainTextFont);
        mainText.setHorizontalAlignment(0);
        mainText.setHorizontalTextPosition(0);
        mainText.setOpaque(false);
        mainText.setText("Benvenuto!");
        mainText.setVerticalAlignment(3);
        mainText.setVerticalTextPosition(3);
        topPanel.add(mainText, BorderLayout.CENTER);
        subText = new JLabel();
        Font subTextFont = this.$$$getFont$$$("Droid Sans", Font.ITALIC, 20, subText.getFont());
        if (subTextFont != null) subText.setFont(subTextFont);
        subText.setHorizontalAlignment(0);
        subText.setHorizontalTextPosition(0);
        subText.setText("Inserisci i seguenti dati per registrarti...");
        topPanel.add(subText, BorderLayout.SOUTH);
        midPanel = new JPanel();
        midPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        midPanel.setMaximumSize(new Dimension(800, 310));
        midPanel.setMinimumSize(new Dimension(800, 310));
        midPanel.setPreferredSize(new Dimension(800, 310));
        panel1.add(midPanel);
        emailForm = new JPanel();
        emailForm.setLayout(new BorderLayout(0, 0));
        emailForm.setMaximumSize(new Dimension(400, 75));
        emailForm.setMinimumSize(new Dimension(400, 75));
        emailForm.setPreferredSize(new Dimension(400, 75));
        midPanel.add(emailForm);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 16, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(2);
        label1.setHorizontalTextPosition(0);
        label1.setText("Email");
        label1.setVerticalAlignment(3);
        emailForm.add(label1, BorderLayout.CENTER);
        emailField = new JTextField();
        Font emailFieldFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 14, emailField.getFont());
        if (emailFieldFont != null) emailField.setFont(emailFieldFont);
        emailField.setForeground(new Color(-13487566));
        emailField.setHorizontalAlignment(2);
        emailField.setMaximumSize(new Dimension(50, 40));
        emailField.setMinimumSize(new Dimension(50, 40));
        emailField.setPreferredSize(new Dimension(50, 40));
        emailField.setText("");
        emailField.setVisible(true);
        emailForm.add(emailField, BorderLayout.SOUTH);
        usernameForm = new JPanel();
        usernameForm.setLayout(new BorderLayout(0, 0));
        usernameForm.setMaximumSize(new Dimension(400, 75));
        usernameForm.setMinimumSize(new Dimension(400, 75));
        usernameForm.setPreferredSize(new Dimension(400, 75));
        midPanel.add(usernameForm);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 16, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setHorizontalAlignment(2);
        label2.setHorizontalTextPosition(0);
        label2.setText("Username");
        label2.setVerticalAlignment(3);
        usernameForm.add(label2, BorderLayout.CENTER);
        usernameField = new JTextField();
        Font usernameFieldFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 14, usernameField.getFont());
        if (usernameFieldFont != null) usernameField.setFont(usernameFieldFont);
        usernameField.setForeground(new Color(-13487566));
        usernameField.setHorizontalAlignment(2);
        usernameField.setMaximumSize(new Dimension(50, 40));
        usernameField.setMinimumSize(new Dimension(50, 40));
        usernameField.setPreferredSize(new Dimension(50, 40));
        usernameField.setText("");
        usernameField.setVisible(true);
        usernameForm.add(usernameField, BorderLayout.SOUTH);
        passwordForm = new JPanel();
        passwordForm.setLayout(new BorderLayout(0, 0));
        passwordForm.setForeground(new Color(-16777216));
        passwordForm.setMaximumSize(new Dimension(400, 75));
        passwordForm.setMinimumSize(new Dimension(400, 75));
        passwordForm.setPreferredSize(new Dimension(400, 75));
        midPanel.add(passwordForm);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 16, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setHorizontalAlignment(2);
        label3.setHorizontalTextPosition(0);
        label3.setText("Password");
        label3.setVerticalAlignment(3);
        passwordForm.add(label3, BorderLayout.CENTER);
        passwordField = new JPasswordField();
        passwordField.setForeground(new Color(-16777216));
        passwordField.setMaximumSize(new Dimension(50, 40));
        passwordField.setMinimumSize(new Dimension(50, 40));
        passwordField.setOpaque(true);
        passwordField.setPreferredSize(new Dimension(50, 40));
        passwordForm.add(passwordField, BorderLayout.SOUTH);
        isAdminCheck = new JPanel();
        isAdminCheck.setLayout(new BorderLayout(0, 0));
        isAdminCheck.setMaximumSize(new Dimension(400, 50));
        isAdminCheck.setMinimumSize(new Dimension(400, 50));
        isAdminCheck.setPreferredSize(new Dimension(400, 50));
        midPanel.add(isAdminCheck);
        adminCheck = new JCheckBox();
        Font adminCheckFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 14, adminCheck.getFont());
        if (adminCheckFont != null) adminCheck.setFont(adminCheckFont);
        adminCheck.setLabel("Sto registrando un admin...");
        adminCheck.setText("Sto registrando un admin...");
        isAdminCheck.add(adminCheck, BorderLayout.CENTER);
        registerPanel = new JPanel();
        registerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        registerPanel.setMaximumSize(new Dimension(800, 55));
        registerPanel.setMinimumSize(new Dimension(800, 55));
        registerPanel.setPreferredSize(new Dimension(800, 55));
        panel1.add(registerPanel);
        btnRegister = new JButton();
        Font btnRegisterFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 14, btnRegister.getFont());
        if (btnRegisterFont != null) btnRegister.setFont(btnRegisterFont);
        btnRegister.setHorizontalTextPosition(0);
        btnRegister.setInheritsPopupMenu(false);
        btnRegister.setMaximumSize(new Dimension(300, 45));
        btnRegister.setMinimumSize(new Dimension(300, 45));
        btnRegister.setPreferredSize(new Dimension(300, 45));
        btnRegister.setText("Registrati");
        registerPanel.add(btnRegister);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(bottomPanel);
        registerField = new JLabel();
        Font registerFieldFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 14, registerField.getFont());
        if (registerFieldFont != null) registerField.setFont(registerFieldFont);
        registerField.setText("Hai già un account? Clicca qui...");
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
     * $$$ get root component $$$ j component.
     *
     * @return the j component
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}