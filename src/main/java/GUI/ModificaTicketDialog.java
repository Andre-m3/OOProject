package GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import controller.Controller;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

/**
 * The type Modifica ticket dialog.
 */
public class ModificaTicketDialog extends JDialog {
    private JPanel contentPane;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField dataNascitaField;
    private JTextField documentoField;
    private JButton btnSalva;
    private JButton btnAnnulla;

    private Controller controller;
    private Object ticket;
    private boolean modificaEffettuata;

    /**
     * Instantiates a new Modifica ticket dialog.
     *
     * @param parent the parent
     * @param ticket the ticket
     */
    public ModificaTicketDialog(JDialog parent, Object ticket) {
        super(parent, "Modifica Ticket", true);

        this.controller = Controller.getInstance();
        this.ticket = ticket;
        this.modificaEffettuata = false;

        setContentPane(contentPane);
        setModal(true);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);

        // Carichiamo i dati del ticket nei campi (DATI DI PARTENZA, IMPORTANTE!) e la formattazione dei pulsanti
        loadTickets();
        setupButtons();

        // Configuriamo i listeners
        btnSalva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSalva();
            }
        });

        btnAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAnnulla();
            }
        });

        // premiamo INVIO sui campi per salvare rapidamente
        KeyListener enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onSalva();
                }
            }
        };

        // premiamo ESC per annullare rapidamente
        KeyListener escapeListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    onAnnulla();
                }
            }
        };

        nomeField.addKeyListener(enterListener);
        cognomeField.addKeyListener(enterListener);
        documentoField.addKeyListener(enterListener);
        dataNascitaField.addKeyListener(enterListener);

        contentPane.addKeyListener(escapeListener);
        contentPane.setFocusable(true);

        // Configuriamo la chiusura della finestra
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onAnnulla();
            }
        });
    }

    private void loadTickets() {
        // Popoliamo i campi con i dati ATTUALI del ticket (tramite controller)
        String[] dettagli = controller.getDettagliTicket(ticket);

        if (dettagli != null) {
            nomeField.setText(dettagli[0]);             // nome
            cognomeField.setText(dettagli[1]);          // cognome
            dataNascitaField.setText(dettagli[2]);      // data nascita
            documentoField.setText(dettagli[3]);        // documento
            // dettagli[4] è il posto assegnato, il quale non è modificabile...

        }
    }

    private void onSalva() {
        // Ottieni i nuovi valori dai campi
        String nuovoNome = nomeField.getText().trim();
        String nuovoCognome = cognomeField.getText().trim();
        String nuovoDocumento = documentoField.getText().trim();
        String nuovaDataNascita = dataNascitaField.getText().trim();

        // Validazione base, i campi vuoti non vanno bene!
        if (nuovoNome.isEmpty() || nuovoCognome.isEmpty() ||
                nuovoDocumento.isEmpty() || nuovaDataNascita.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tutti i campi sono obbligatori!",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validazione del formato data, implementato con controlli base, anche se potremmo in futuro "migliorarlo".
        if (!controller.isValidDateFormat(nuovaDataNascita)) {
            JOptionPane.showMessageDialog(this,
                    "Formato data non valido! Usa il formato: dd/mm/yyyy",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            dataNascitaField.requestFocus();
            return;
        }

        String[] dettagliAttuali = controller.getDettagliTicket(ticket);
        String postoAttuale = dettagliAttuali != null ? dettagliAttuali[4] : "";

        // Effettua la modifica tramite il controller
        if (controller.aggiornaTicket(ticket, nuovoNome, nuovoCognome,
                nuovoDocumento, nuovaDataNascita, postoAttuale)) {
            modificaEffettuata = true;
            JOptionPane.showMessageDialog(this,
                    "Ticket modificato con successo!",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Errore durante la modifica del ticket.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAnnulla() {
        // Verifica se ci sono modifiche non salvate
        if (hasUnsavedChanges()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Hai modifiche non salvate. Vuoi davvero uscire?",
                    "Conferma uscita",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        modificaEffettuata = false;
        dispose();
    }

    private void setupButtons() {
        // Impostiamo i pulsanti con lo stesso stile utilizzato nelle altre interfacce!
        Color sfondoLeggermenteScuro = new Color(214, 214, 214);

        // Pulsante SALVA
        btnSalva.setBackground(sfondoLeggermenteScuro);
        btnSalva.setForeground(new Color(78, 78, 78));
        btnSalva.setFocusPainted(false);
        btnSalva.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnSalva.setOpaque(true);

        // Pulsante ANNULLA
        btnAnnulla.setBackground(sfondoLeggermenteScuro);
        btnAnnulla.setForeground(new Color(78, 78, 78));
        btnAnnulla.setFocusPainted(false);
        btnAnnulla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnAnnulla.setOpaque(true);


    }

    private boolean hasUnsavedChanges() {
        // Qui controlliamo se i valori nei campi sono diversi da quelli originali
        String[] dettagliOriginali = controller.getDettagliTicket(ticket);

        if (dettagliOriginali == null) {
            return false;
        }

        return !nomeField.getText().trim().equals(dettagliOriginali[0]) ||
                !cognomeField.getText().trim().equals(dettagliOriginali[1]) ||
                !dataNascitaField.getText().trim().equals(dettagliOriginali[2]) ||
                !documentoField.getText().trim().equals(dettagliOriginali[3]);
    }


    /**
     * Is modifica effettuata boolean.
     *
     * @return the boolean
     */
    public boolean isModificaEffettuata() {
        return modificaEffettuata;
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
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setMaximumSize(new Dimension(700, 500));
        contentPane.setMinimumSize(new Dimension(700, 500));
        contentPane.setPreferredSize(new Dimension(700, 500));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setMaximumSize(new Dimension(700, 100));
        panel1.setMinimumSize(new Dimension(700, 100));
        panel1.setPreferredSize(new Dimension(700, 100));
        contentPane.add(panel1, BorderLayout.NORTH);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.PLAIN, 28, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Modifica Ticket");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 2, new Insets(10, 10, 10, 10), 10, 0));
        panel2.setMaximumSize(new Dimension(500, 200));
        panel2.setMinimumSize(new Dimension(500, 200));
        panel2.setPreferredSize(new Dimension(500, 200));
        contentPane.add(panel2, BorderLayout.CENTER);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.PLAIN, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setHorizontalAlignment(4);
        label2.setHorizontalTextPosition(11);
        label2.setText("Nome:");
        panel2.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.PLAIN, 14, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setHorizontalAlignment(4);
        label3.setHorizontalTextPosition(11);
        label3.setText("Cognome:");
        panel2.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.PLAIN, 14, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setHorizontalAlignment(4);
        label4.setHorizontalTextPosition(11);
        label4.setText("Data Nascita:");
        panel2.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.PLAIN, 14, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setHorizontalAlignment(4);
        label5.setHorizontalTextPosition(11);
        label5.setText("Documento:");
        panel2.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nomeField = new JTextField();
        nomeField.setColumns(15);
        Font nomeFieldFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, nomeField.getFont());
        if (nomeFieldFont != null) nomeField.setFont(nomeFieldFont);
        nomeField.setToolTipText("Inserisci il nome del passeggero");
        panel2.add(nomeField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cognomeField = new JTextField();
        cognomeField.setColumns(15);
        Font cognomeFieldFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, cognomeField.getFont());
        if (cognomeFieldFont != null) cognomeField.setFont(cognomeFieldFont);
        cognomeField.setToolTipText("Inserisci il cognome del passeggero");
        panel2.add(cognomeField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        dataNascitaField = new JTextField();
        dataNascitaField.setColumns(15);
        Font dataNascitaFieldFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, dataNascitaField.getFont());
        if (dataNascitaFieldFont != null) dataNascitaField.setFont(dataNascitaFieldFont);
        dataNascitaField.setToolTipText("Formato: dd/mm/yyyy (es.: 03/09/2003)");
        panel2.add(dataNascitaField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        documentoField = new JTextField();
        documentoField.setColumns(15);
        Font documentoFieldFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, documentoField.getFont());
        if (documentoFieldFont != null) documentoField.setFont(documentoFieldFont);
        documentoField.setToolTipText("Inserisci il numero del documento d'identità");
        panel2.add(documentoField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 7, 5));
        panel3.setMaximumSize(new Dimension(700, 100));
        panel3.setMinimumSize(new Dimension(700, 100));
        panel3.setPreferredSize(new Dimension(700, 100));
        contentPane.add(panel3, BorderLayout.SOUTH);
        btnSalva = new JButton();
        Font btnSalvaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, btnSalva.getFont());
        if (btnSalvaFont != null) btnSalva.setFont(btnSalvaFont);
        btnSalva.setMaximumSize(new Dimension(100, 35));
        btnSalva.setMinimumSize(new Dimension(100, 35));
        btnSalva.setPreferredSize(new Dimension(100, 35));
        btnSalva.setText("Salva");
        panel3.add(btnSalva);
        btnAnnulla = new JButton();
        Font btnAnnullaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, btnAnnulla.getFont());
        if (btnAnnullaFont != null) btnAnnulla.setFont(btnAnnullaFont);
        btnAnnulla.setMaximumSize(new Dimension(100, 35));
        btnAnnulla.setMinimumSize(new Dimension(100, 35));
        btnAnnulla.setPreferredSize(new Dimension(100, 35));
        btnAnnulla.setText("Annulla");
        panel3.add(btnAnnulla);
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
        return contentPane;
    }
}
