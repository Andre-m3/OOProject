package GUI;

import controller.Controller;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

/**
 * The type Dialog dati passeggero.
 */
public class DialogDatiPasseggero extends JDialog {
    private JPanel panel1;
    private JPanel topPanel;
    private JLabel dialogTitle;
    private JPanel mainPanel;
    private JTextField txtNome;
    private JTextField txtCognome;
    private JTextField txtDocumento;
    private JTextField txtDataNascita;
    private JPanel buttonPanel;
    private JButton btnConferma;
    private JButton btnAnnulla;

    private String[] datiPasseggero = null;
    private boolean confermatoOK = false;

    /**
     * Instantiates a new Dialog dati passeggero.
     *
     * @param parent           the parent
     * @param numeroPasseggero the numero passeggero
     * @param totalePasseggeri the totale passeggeri
     */
    public DialogDatiPasseggero(JFrame parent, int numeroPasseggero, int totalePasseggeri) {
        super(parent, true); // Dialog modale
        setContentPane(panel1);
        setModal(true);
        getRootPane().setDefaultButton(btnConferma);
        setTitle("Dati Passeggero");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Aggiorna il titolo con il numero del passeggero
        dialogTitle.setText("Dati Passeggero " + numeroPasseggero + " di " + totalePasseggeri);

        // Setup degli eventi (Listeners)
        setupEvents();
        setupButtons();

        // Centra il dialog
        setLocationRelativeTo(parent);
        pack();
    }

    private void setupEvents() {
        btnConferma.addActionListener(e -> onOK());
        btnAnnulla.addActionListener(e -> onCancel());

        // Chiusura con X
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // Chiusura con ESC (implementata qui dopo aver valutato l'effettiva utilità)
        panel1.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setupButtons() {

        Color sfondoLeggermenteScuro = new Color(214, 214, 214);

        // Pulsante Salva
        btnConferma.setBackground(sfondoLeggermenteScuro);
        btnConferma.setForeground(new Color(78, 78, 78));
        btnConferma.setFocusPainted(false);
        btnConferma.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnConferma.setOpaque(true);

        // Pulsante Annulla
        btnAnnulla.setBackground(sfondoLeggermenteScuro);
        btnAnnulla.setForeground(new Color(78, 78, 78));
        btnAnnulla.setFocusPainted(false);
        btnAnnulla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnAnnulla.setOpaque(true);
    }

    private void onOK() {
        // Validazione dei campi
        String nome = txtNome.getText().trim();
        String cognome = txtCognome.getText().trim();
        String documento = txtDocumento.getText().trim();
        String dataNascita = txtDataNascita.getText().trim();

        if (nome.isEmpty() || cognome.isEmpty() || documento.isEmpty() || dataNascita.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tutti i campi sono obbligatori.",
                    "Campi Mancanti",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validazione formato data tramite controller
        Controller controller = Controller.getInstance();
        if (!controller.isValidDateFormat(dataNascita)) {
            JOptionPane.showMessageDialog(this,
                    "Il formato della data deve essere dd/mm/yyyy",
                    "Formato Data Non Valido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Salva i dati
        datiPasseggero = new String[]{nome, cognome, documento, dataNascita};
        confermatoOK = true;
        dispose();
    }

    private void onCancel() {
        confermatoOK = false;
        dispose();
    }

    /**
     * Ottiene i dati del passeggero se l'utente ha confermato
     *
     * @return Array con [nome, cognome, documento, dataNascita] o null se annullato
     */
    public String[] getDatiPasseggero() {
        return confermatoOK ? datiPasseggero : null;
    }

    /**
     * Verifica se l'utente ha confermato i dati
     *
     * @return true se confermato, false se annullato
     */
    public boolean isConfermato() {
        return confermatoOK;
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
        panel1.setMaximumSize(new Dimension(450, 300));
        panel1.setMinimumSize(new Dimension(450, 300));
        panel1.setPreferredSize(new Dimension(450, 300));
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(450, 50));
        topPanel.setMinimumSize(new Dimension(450, 50));
        topPanel.setPreferredSize(new Dimension(450, 50));
        panel1.add(topPanel, BorderLayout.NORTH);
        dialogTitle = new JLabel();
        Font dialogTitleFont = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.BOLD, 18, dialogTitle.getFont());
        if (dialogTitleFont != null) dialogTitle.setFont(dialogTitleFont);
        dialogTitle.setHorizontalAlignment(0);
        dialogTitle.setHorizontalTextPosition(0);
        dialogTitle.setText("Dati Passeggero");
        dialogTitle.setVerticalAlignment(3);
        dialogTitle.setVerticalTextPosition(3);
        topPanel.add(dialogTitle, BorderLayout.CENTER);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setMaximumSize(new Dimension(450, 200));
        mainPanel.setMinimumSize(new Dimension(450, 200));
        mainPanel.setPreferredSize(new Dimension(450, 200));
        panel1.add(mainPanel, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Nome:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(label1, gbc);
        txtNome = new JTextField();
        Font txtNomeFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtNome.getFont());
        if (txtNomeFont != null) txtNome.setFont(txtNomeFont);
        txtNome.setHorizontalAlignment(10);
        txtNome.setInheritsPopupMenu(false);
        txtNome.setMaximumSize(new Dimension(200, 30));
        txtNome.setMinimumSize(new Dimension(200, 30));
        txtNome.setPreferredSize(new Dimension(200, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(txtNome, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Cognome:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(label2, gbc);
        txtCognome = new JTextField();
        Font txtCognomeFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtCognome.getFont());
        if (txtCognomeFont != null) txtCognome.setFont(txtCognomeFont);
        txtCognome.setMaximumSize(new Dimension(200, 30));
        txtCognome.setMinimumSize(new Dimension(200, 30));
        txtCognome.setPreferredSize(new Dimension(200, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(txtCognome, gbc);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Numero Documento:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(label3, gbc);
        txtDocumento = new JTextField();
        Font txtDocumentoFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtDocumento.getFont());
        if (txtDocumentoFont != null) txtDocumento.setFont(txtDocumentoFont);
        txtDocumento.setMaximumSize(new Dimension(200, 30));
        txtDocumento.setMinimumSize(new Dimension(200, 30));
        txtDocumento.setPreferredSize(new Dimension(200, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(txtDocumento, gbc);
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Data Nascita:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(label4, gbc);
        txtDataNascita = new JTextField();
        Font txtDataNascitaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtDataNascita.getFont());
        if (txtDataNascitaFont != null) txtDataNascita.setFont(txtDataNascitaFont);
        txtDataNascita.setMaximumSize(new Dimension(200, 30));
        txtDataNascita.setMinimumSize(new Dimension(200, 30));
        txtDataNascita.setPreferredSize(new Dimension(200, 30));
        txtDataNascita.setText("");
        txtDataNascita.setToolTipText("Formato: dd/mm/yyyy");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(txtDataNascita, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buttonPanel.setMaximumSize(new Dimension(450, 50));
        buttonPanel.setMinimumSize(new Dimension(450, 50));
        buttonPanel.setPreferredSize(new Dimension(450, 50));
        panel1.add(buttonPanel, BorderLayout.SOUTH);
        btnConferma = new JButton();
        Font btnConfermaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnConferma.getFont());
        if (btnConfermaFont != null) btnConferma.setFont(btnConfermaFont);
        btnConferma.setMaximumSize(new Dimension(90, 35));
        btnConferma.setMinimumSize(new Dimension(90, 35));
        btnConferma.setPreferredSize(new Dimension(90, 35));
        btnConferma.setText("Conferma");
        buttonPanel.add(btnConferma);
        btnAnnulla = new JButton();
        Font btnAnnullaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnAnnulla.getFont());
        if (btnAnnullaFont != null) btnAnnulla.setFont(btnAnnullaFont);
        btnAnnulla.setMaximumSize(new Dimension(90, 35));
        btnAnnulla.setMinimumSize(new Dimension(90, 35));
        btnAnnulla.setPreferredSize(new Dimension(90, 35));
        btnAnnulla.setText("Annulla");
        buttonPanel.add(btnAnnulla);
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
