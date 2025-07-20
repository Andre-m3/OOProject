package GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import controller.Controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

/**
 * The type Modifica prenotazione dialog.
 */
public class ModificaPrenotazioneDialog extends JDialog {
    private JPanel contentPane;
    private JLabel codiceLabel;
    private JLabel voloLabel;
    private JLabel dataLabel;
    private JLabel trattaLabel;
    private JLabel statoLabel;
    private JLabel statoTitle;
    private JLabel trattaTitle;
    private JLabel dataTitle;
    private JLabel voloTitle;
    private JLabel codiceTitle;
    private JList ticketsList;
    private JButton btnModificaTicket;
    private JPanel bottomPanel;
    private JButton btnElimina;
    private JButton btnChiudi;

    private Controller controller;
    private Object prenotazione;
    private DefaultListModel<String> ticketsListModel;

    /**
     * Instantiates a new Modifica prenotazione dialog.
     *
     * @param parent       the parent
     * @param prenotazione the prenotazione
     */
    public ModificaPrenotazioneDialog(JFrame parent, Object prenotazione) {
        super(parent, "Modifica Prenotazione", true);

        this.controller = Controller.getInstance();
        this.prenotazione = prenotazione;
        this.ticketsListModel = new DefaultListModel<>();

        this.setContentPane(contentPane);
        this.setModal(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setResizable(false);

        // Configuriamo la JList
        ticketsList.setModel(ticketsListModel);

        loadDatiPrenotazione();
        setupButtons();

        btnElimina.addActionListener(e -> eliminaPrenotazione());
        btnModificaTicket.addActionListener(e -> modificaTicketSelezionato());
        btnChiudi.addActionListener(e -> dispose());
    }

    private void setupButtons() {
        // Impostiamo i pulsanti con lo stesso stile utilizzato nelle altre interfacce!
        Color sfondoLeggermenteScuro = new Color(214, 214, 214);

        // Pulsante ELIMINA
        btnElimina.setBackground(sfondoLeggermenteScuro);
        btnElimina.setForeground(new Color(78, 78, 78));
        btnElimina.setFocusPainted(false);
        btnElimina.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnElimina.setOpaque(true);

        // Pulsante CHIUDI
        btnChiudi.setBackground(sfondoLeggermenteScuro);
        btnChiudi.setForeground(new Color(78, 78, 78));
        btnChiudi.setFocusPainted(false);
        btnChiudi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnChiudi.setOpaque(true);

        // Pulsante MODIFICA
        btnModificaTicket.setBackground(sfondoLeggermenteScuro);
        btnModificaTicket.setForeground(new Color(78, 78, 78));
        btnModificaTicket.setFocusPainted(false);
        btnModificaTicket.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnModificaTicket.setOpaque(true);


    }

    private void loadDatiPrenotazione() {
        String[] dettagli = controller.getDettagliPrenotazioneDialog(prenotazione);

        if (dettagli != null) {
            codiceLabel.setText(dettagli[0]);
            voloLabel.setText(dettagli[1]);
            dataLabel.setText(dettagli[2]);
            trattaLabel.setText(dettagli[3]);
            statoLabel.setText(dettagli[4]);
        }

        // Carichiamo i tickets tramite il controller
        ticketsListModel.clear();
        String[] ticketsFormattati = controller.getTicketsFormattati(prenotazione);

        for (String ticketFormattato : ticketsFormattati) {
            ticketsListModel.addElement(ticketFormattato);
        }

        // Abilita/disabilita il pulsante modifica ticket
        btnModificaTicket.setEnabled(ticketsFormattati.length > 0);
    }

    private void eliminaPrenotazione() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare questa prenotazione?\nQuesta azione non può essere annullata.",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.eliminaPrenotazione(prenotazione)) {
                JOptionPane.showMessageDialog(this,
                        "Prenotazione eliminata con successo!",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Errore durante l'eliminazione della prenotazione.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modificaTicketSelezionato() {
        int selectedIndex = ticketsList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona un ticket da modificare.",
                    "Nessun ticket selezionato",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ottengo il ticket selezionato tramite il controller
        Object ticketSelezionato = controller.getTicketPerIndice(prenotazione, selectedIndex);

        if (ticketSelezionato == null) {
            JOptionPane.showMessageDialog(this,
                    "Errore nel recupero del ticket selezionato.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Apriamo il dialog di modifica
        ModificaTicketDialog dialog = new ModificaTicketDialog(this, ticketSelezionato);
        dialog.setVisible(true);

        // Se sono state effettuate modifiche, ricarichiamo la lista così da mostrarle SUBITO (come conseguenza)
        if (dialog.isModificaEffettuata()) {
            loadDatiPrenotazione();
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
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 10));
        contentPane.setMaximumSize(new Dimension(700, 600));
        contentPane.setMinimumSize(new Dimension(700, 600));
        contentPane.setPreferredSize(new Dimension(700, 600));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        Font panel1Font = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.PLAIN, 24, panel1.getFont());
        if (panel1Font != null) panel1.setFont(panel1Font);
        panel1.setMaximumSize(new Dimension(700, 150));
        panel1.setMinimumSize(new Dimension(700, 150));
        panel1.setPreferredSize(new Dimension(700, 150));
        contentPane.add(panel1, BorderLayout.NORTH);
        panel1.setBorder(BorderFactory.createTitledBorder(null, "Dettagli Prenotazione", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        codiceTitle = new JLabel();
        codiceTitle.setText("Codice:");
        panel1.add(codiceTitle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        voloTitle = new JLabel();
        voloTitle.setText("Volo");
        panel1.add(voloTitle, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dataTitle = new JLabel();
        dataTitle.setText("Data:");
        panel1.add(dataTitle, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        trattaTitle = new JLabel();
        trattaTitle.setText("Tratta");
        panel1.add(trattaTitle, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statoTitle = new JLabel();
        statoTitle.setText("Stato:");
        panel1.add(statoTitle, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        codiceLabel = new JLabel();
        codiceLabel.setText("codiceLabel");
        panel1.add(codiceLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        voloLabel = new JLabel();
        voloLabel.setText("voloLabel");
        panel1.add(voloLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dataLabel = new JLabel();
        dataLabel.setText("dataLabel");
        panel1.add(dataLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        trattaLabel = new JLabel();
        trattaLabel.setText("trattaLabel");
        panel1.add(trattaLabel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statoLabel = new JLabel();
        statoLabel.setText("statoComboBox");
        panel1.add(statoLabel, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        Font panel2Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 18, panel2.getFont());
        if (panel2Font != null) panel2.setFont(panel2Font);
        panel2.setMaximumSize(new Dimension(700, 150));
        panel2.setMinimumSize(new Dimension(700, 150));
        panel2.setPreferredSize(new Dimension(700, 150));
        contentPane.add(panel2, BorderLayout.CENTER);
        panel2.setBorder(BorderFactory.createTitledBorder(null, "Ticket Associati", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, BorderLayout.CENTER);
        ticketsList = new JList();
        Font ticketsListFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 18, ticketsList.getFont());
        if (ticketsListFont != null) ticketsList.setFont(ticketsListFont);
        ticketsList.setSelectionMode(0);
        scrollPane1.setViewportView(ticketsList);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        bottomPanel.setMaximumSize(new Dimension(700, 100));
        bottomPanel.setMinimumSize(new Dimension(700, 100));
        bottomPanel.setPreferredSize(new Dimension(700, 100));
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        btnModificaTicket = new JButton();
        Font btnModificaTicketFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnModificaTicket.getFont());
        if (btnModificaTicketFont != null) btnModificaTicket.setFont(btnModificaTicketFont);
        btnModificaTicket.setMaximumSize(new Dimension(128, 40));
        btnModificaTicket.setMinimumSize(new Dimension(128, 40));
        btnModificaTicket.setPreferredSize(new Dimension(128, 40));
        btnModificaTicket.setText("Modifica Ticket");
        bottomPanel.add(btnModificaTicket);
        btnElimina = new JButton();
        Font btnEliminaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnElimina.getFont());
        if (btnEliminaFont != null) btnElimina.setFont(btnEliminaFont);
        btnElimina.setMaximumSize(new Dimension(164, 40));
        btnElimina.setMinimumSize(new Dimension(164, 40));
        btnElimina.setPreferredSize(new Dimension(164, 40));
        btnElimina.setText("Elimina Prenotazione");
        bottomPanel.add(btnElimina);
        btnChiudi = new JButton();
        Font btnChiudiFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnChiudi.getFont());
        if (btnChiudiFont != null) btnChiudi.setFont(btnChiudiFont);
        btnChiudi.setMaximumSize(new Dimension(78, 40));
        btnChiudi.setMinimumSize(new Dimension(78, 40));
        btnChiudi.setPreferredSize(new Dimension(78, 40));
        btnChiudi.setText("Chiudi");
        bottomPanel.add(btnChiudi);
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
