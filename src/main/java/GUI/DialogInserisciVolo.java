package GUI;

import controller.Controller;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

/**
 * The type Dialog inserisci volo.
 */
public class DialogInserisciVolo extends JDialog {
    private JPanel panel1;
    private JPanel topPanel;
    private JLabel labelTitolo;
    private JPanel fieldsPanel;
    private JPanel numeroVoloPanel;
    private JTextField txtNumeroVolo;
    private JPanel compagniaPanel;
    private JPanel orarioPanel;
    private JPanel dataPanel;
    private JPanel statoPanel;
    private JPanel partenzaPanel;
    private JPanel destinazionePanel;
    private JPanel gatePanel;
    private JComboBox cbGate;
    private JTextField txtData;
    private JTextField txtOrario;
    private JTextField txtCompagnia;
    private JTextField txtDestinazione;
    private JTextField txtPartenza;
    private JComboBox cbStato;
    private JPanel buttonPanel;
    private JButton btnSalva;
    private JButton btnAnnulla;
    private JButton buttonOK;
    private JButton buttonCancel;

    private Controller controller;

    // Non visto a lezione: È un pattern per comunicare al dialog che bisogna aggiornare la tabella dopo una modifica
    // Altrimenti la tabella (in questo caso di "VoliAdmin") non visualizzerebbe i nuovi dati impostati in seguito alla modifica/aggiornamento del volo/gate
    private Runnable onSaveCallback;        // L'abbiamo commentato in modo equivalente in altri dialog implementati allo stesso modo


    /**
     * Instantiates a new Dialog inserisci volo.
     *
     * @param parent         the parent
     * @param onSaveCallback the on save callback
     */
    public DialogInserisciVolo(JFrame parent, Runnable onSaveCallback) {
        super(parent, "Inserisci Nuovo Volo", true);

        this.controller = Controller.getInstance();
        this.onSaveCallback = onSaveCallback;       // Commentato nei dialog precedenti.

        setContentPane(panel1);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);

        setupComboBoxes();
        setupButtons();
        setupEvents();

        setVisible(true);
    }

    private void setupComboBoxes() {
        // Configurazione ComboBox Stato
        String[] statiDisponibili = controller.getStatiVoloDisponibili();
        for (String stato : statiDisponibili) {
            cbStato.addItem(stato);
        }

        // Configurazione ComboBox Gate
        String[] gateOptions = {
                "Nessun gate",
                "Gate 1", "Gate 2", "Gate 3", "Gate 4",
                "Gate 5", "Gate 6", "Gate 7", "Gate 8", "Gate 9"
        };

        DefaultComboBoxModel<String> gateModel = new DefaultComboBoxModel<>(gateOptions);
        cbGate.setModel(gateModel);
        cbGate.setSelectedItem("Nessun gate"); // Valore predefinito
    }

    private void setupButtons() {

        Color sfondoLeggermenteScuro = new Color(214, 214, 214);

        // Pulsante Salva
        btnSalva.setBackground(sfondoLeggermenteScuro);
        btnSalva.setForeground(new Color(78, 78, 78));
        btnSalva.setFocusPainted(false);
        btnSalva.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnSalva.setOpaque(true);

        // Pulsante Annulla
        btnAnnulla.setBackground(sfondoLeggermenteScuro);
        btnAnnulla.setForeground(new Color(78, 78, 78));
        btnAnnulla.setFocusPainted(false);
        btnAnnulla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        btnAnnulla.setOpaque(true);
    }

    private void setupEvents() {
        btnSalva.addActionListener(e -> salvaVolo());
        btnAnnulla.addActionListener(e -> dispose());
    }

    private void salvaVolo() {
        // Validazione campi obbligatori
        if (!validaCampi()) {
            return;
        }

        // Raccogliamo i dati dai campi
        String numeroVolo = txtNumeroVolo.getText().trim();
        String compagnia = txtCompagnia.getText().trim();
        String orario = txtOrario.getText().trim();
        String data = txtData.getText().trim();
        String stato = (String) cbStato.getSelectedItem();
        String partenza = txtPartenza.getText().trim();
        String destinazione = txtDestinazione.getText().trim();

        // Gestiamo il Gate
        Short gate = null;
        String selectedGate = (String) cbGate.getSelectedItem();
        if (!"Nessun gate".equals(selectedGate)) {
            // Facciamo in modo che la stringa "gate 3" (3 numero generico) sia "troncata" per far rimanere solo il numero e poter fare il cast in Short
            String numeroGate = selectedGate.replace("Gate ", "");
            gate = Short.parseShort(numeroGate);
        }

        // Inseriamo il volo tramite il metodo apposito presente nel Controller
        boolean success = controller.inserisciVolo(numeroVolo, compagnia, orario, data,
                stato, partenza, destinazione, gate);

        // Verifichiamo se l'aggiunta ha avuto successo, così da poter mostrare un messaggio all'utente tramite MessageDialog
        if (success) {
            JOptionPane.showMessageDialog(this, "Volo inserito con successo!");
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            dispose();
        } else {
            mostraErrore("Errore durante l'inserimento del volo");
        }

    }

    // Qui validiamo i campi, per essere sicuri che non siano stati fatti errori (umani, dall'amministratore in questo caso) in fase di inserimento per ciascun campo
    private boolean validaCampi() {
        // Verifica dei campi obbligatori
        if (txtNumeroVolo.getText().trim().isEmpty()) {
            mostraErrore("Il numero volo è obbligatorio!");
            txtNumeroVolo.requestFocus();
            return false;
        }

        if (txtCompagnia.getText().trim().isEmpty()) {
            mostraErrore("La compagnia aerea è obbligatoria!");
            txtCompagnia.requestFocus();
            return false;
        }

        if (txtOrario.getText().trim().isEmpty()) {
            mostraErrore("L'orario è obbligatorio!");
            txtOrario.requestFocus();
            return false;
        }

        if (txtData.getText().trim().isEmpty()) {
            mostraErrore("La data è obbligatoria!");
            txtData.requestFocus();
            return false;
        }

        String partenza = txtPartenza.getText().trim();
        if (partenza.isEmpty()) {
            mostraErrore("L'aeroporto di partenza è obbligatorio!");
            txtPartenza.requestFocus();
            return false;
        }

        String destinazione = txtDestinazione.getText().trim();
        if (destinazione.isEmpty()) {
            mostraErrore("L'aeroporto di destinazione è obbligatorio!");
            txtDestinazione.requestFocus();
            return false;
        }

        // Validazione formato orario (HH:MM)
        String orario = txtOrario.getText().trim();
        if (!orario.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {     // Formattazione richiesta, andiamo a definire i valori del formato obbligatorio!
            mostraErrore("Formato orario non valido! Usa il formato HH:MM (es: 14:30)");
            txtOrario.requestFocus();
            return false;
        }

        // Validazione formato data (DD-MM-YYYY)
        String data = txtData.getText().trim();
        if (!data.matches("^([0-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-[0-9]{4}$")) {      // Formattazione richiesta, andiamo a definire i valori del formato obbligatorio!
            mostraErrore("Formato data non valido! Usa il formato DD-MM-YYYY (es: 25-12-2024)");
            txtData.requestFocus();
            return false;
        }

        // Il sistema gestisce solo voli da/per Napoli. Se non viene inserito in Partenza o Destinazione, allora mostriamo errore!
        if (!partenza.equalsIgnoreCase("Napoli") && !destinazione.equalsIgnoreCase("Napoli")) {
            mostraErrore("Il volo deve avere come partenza o destinazione 'Napoli'!\n" +
                    "Questo sistema gestisce solo voli da/per l'aeroporto di Napoli.");
            txtPartenza.requestFocus();
            return false;
        }

        // Se è un volo in arrivo (destinazione = Napoli), il gate deve essere vuoto! Non ci sono gate per voli in Arrivo...
        if (destinazione.equalsIgnoreCase("Napoli")) {
            String selectedGate = (String) cbGate.getSelectedItem();
            if (!"Nessun gate".equals(selectedGate)) {
                mostraErrore("Per i voli in ARRIVO a Napoli il gate non può essere assegnato!\n" +
                        "I gate sono solo per i voli in PARTENZA da Napoli.\n" +
                        "Seleziona 'Nessun gate'.");
                cbGate.setSelectedItem("Nessun gate");
                cbGate.requestFocus();
                return false;
            }
        }

        // Se la validazione supera tutti i vincoli imposti, allora restituiamo true!
        return true;
    }

    private void mostraErrore(String messaggio) {
        JOptionPane.showMessageDialog(this,
                messaggio,
                "Errore di Validazione",
                JOptionPane.WARNING_MESSAGE);
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
        panel1.setMaximumSize(new Dimension(600, 500));
        panel1.setMinimumSize(new Dimension(600, 500));
        panel1.setPreferredSize(new Dimension(600, 500));
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(580, 60));
        topPanel.setMinimumSize(new Dimension(580, 60));
        topPanel.setPreferredSize(new Dimension(580, 60));
        panel1.add(topPanel);
        labelTitolo = new JLabel();
        Font labelTitoloFont = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.BOLD, 24, labelTitolo.getFont());
        if (labelTitoloFont != null) labelTitolo.setFont(labelTitoloFont);
        labelTitolo.setHorizontalAlignment(0);
        labelTitolo.setHorizontalTextPosition(0);
        labelTitolo.setText("Inserisci Nuovo Volo");
        labelTitolo.setVerticalAlignment(3);
        labelTitolo.setVerticalTextPosition(3);
        topPanel.add(labelTitolo, BorderLayout.CENTER);
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        fieldsPanel.setMaximumSize(new Dimension(580, 340));
        fieldsPanel.setMinimumSize(new Dimension(580, 340));
        fieldsPanel.setPreferredSize(new Dimension(580, 340));
        panel1.add(fieldsPanel);
        numeroVoloPanel = new JPanel();
        numeroVoloPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        numeroVoloPanel.setMaximumSize(new Dimension(560, 35));
        numeroVoloPanel.setMinimumSize(new Dimension(560, 35));
        numeroVoloPanel.setPreferredSize(new Dimension(560, 35));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        fieldsPanel.add(numeroVoloPanel, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setMaximumSize(new Dimension(180, 25));
        label1.setMinimumSize(new Dimension(180, 25));
        label1.setPreferredSize(new Dimension(180, 25));
        label1.setText("Numero Volo:");
        numeroVoloPanel.add(label1);
        txtNumeroVolo = new JTextField();
        Font txtNumeroVoloFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtNumeroVolo.getFont());
        if (txtNumeroVoloFont != null) txtNumeroVolo.setFont(txtNumeroVoloFont);
        txtNumeroVolo.setMaximumSize(new Dimension(300, 30));
        txtNumeroVolo.setMinimumSize(new Dimension(300, 30));
        txtNumeroVolo.setPreferredSize(new Dimension(300, 30));
        numeroVoloPanel.add(txtNumeroVolo);
        compagniaPanel = new JPanel();
        compagniaPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        compagniaPanel.setMaximumSize(new Dimension(560, 35));
        compagniaPanel.setMinimumSize(new Dimension(560, 35));
        compagniaPanel.setPreferredSize(new Dimension(560, 35));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 1;
        fieldsPanel.add(compagniaPanel, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setMaximumSize(new Dimension(180, 25));
        label2.setMinimumSize(new Dimension(180, 25));
        label2.setPreferredSize(new Dimension(180, 25));
        label2.setText("Compagnia Aerea:");
        compagniaPanel.add(label2);
        txtCompagnia = new JTextField();
        Font txtCompagniaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtCompagnia.getFont());
        if (txtCompagniaFont != null) txtCompagnia.setFont(txtCompagniaFont);
        txtCompagnia.setMaximumSize(new Dimension(300, 30));
        txtCompagnia.setMinimumSize(new Dimension(300, 30));
        txtCompagnia.setPreferredSize(new Dimension(300, 30));
        compagniaPanel.add(txtCompagnia);
        orarioPanel = new JPanel();
        orarioPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        orarioPanel.setMaximumSize(new Dimension(560, 35));
        orarioPanel.setMinimumSize(new Dimension(560, 35));
        orarioPanel.setPreferredSize(new Dimension(560, 35));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 2;
        fieldsPanel.add(orarioPanel, gbc);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setMaximumSize(new Dimension(180, 25));
        label3.setMinimumSize(new Dimension(180, 25));
        label3.setPreferredSize(new Dimension(180, 25));
        label3.setText("Orario (HH:MM):");
        orarioPanel.add(label3);
        txtOrario = new JTextField();
        Font txtOrarioFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtOrario.getFont());
        if (txtOrarioFont != null) txtOrario.setFont(txtOrarioFont);
        txtOrario.setMaximumSize(new Dimension(300, 30));
        txtOrario.setMinimumSize(new Dimension(300, 30));
        txtOrario.setPreferredSize(new Dimension(300, 30));
        orarioPanel.add(txtOrario);
        dataPanel = new JPanel();
        dataPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        dataPanel.setMaximumSize(new Dimension(560, 35));
        dataPanel.setMinimumSize(new Dimension(560, 35));
        dataPanel.setPreferredSize(new Dimension(560, 35));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 3;
        fieldsPanel.add(dataPanel, gbc);
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setMaximumSize(new Dimension(180, 25));
        label4.setMinimumSize(new Dimension(180, 25));
        label4.setPreferredSize(new Dimension(180, 25));
        label4.setText("Data (DD-MM-YYYY):");
        dataPanel.add(label4);
        txtData = new JTextField();
        Font txtDataFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtData.getFont());
        if (txtDataFont != null) txtData.setFont(txtDataFont);
        txtData.setMaximumSize(new Dimension(300, 30));
        txtData.setMinimumSize(new Dimension(300, 30));
        txtData.setPreferredSize(new Dimension(300, 30));
        dataPanel.add(txtData);
        statoPanel = new JPanel();
        statoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        statoPanel.setMaximumSize(new Dimension(560, 35));
        statoPanel.setMinimumSize(new Dimension(560, 35));
        statoPanel.setPreferredSize(new Dimension(560, 35));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 4;
        fieldsPanel.add(statoPanel, gbc);
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setMaximumSize(new Dimension(180, 25));
        label5.setMinimumSize(new Dimension(180, 25));
        label5.setPreferredSize(new Dimension(180, 25));
        label5.setText("Stato:");
        statoPanel.add(label5);
        cbStato = new JComboBox();
        cbStato.setMaximumSize(new Dimension(300, 30));
        cbStato.setMinimumSize(new Dimension(300, 30));
        cbStato.setPreferredSize(new Dimension(300, 30));
        statoPanel.add(cbStato);
        partenzaPanel = new JPanel();
        partenzaPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        partenzaPanel.setMaximumSize(new Dimension(560, 35));
        partenzaPanel.setMinimumSize(new Dimension(560, 35));
        partenzaPanel.setPreferredSize(new Dimension(560, 35));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 5;
        fieldsPanel.add(partenzaPanel, gbc);
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setMaximumSize(new Dimension(180, 25));
        label6.setMinimumSize(new Dimension(180, 25));
        label6.setPreferredSize(new Dimension(180, 25));
        label6.setText("Partenza:");
        partenzaPanel.add(label6);
        txtPartenza = new JTextField();
        Font txtPartenzaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtPartenza.getFont());
        if (txtPartenzaFont != null) txtPartenza.setFont(txtPartenzaFont);
        txtPartenza.setMaximumSize(new Dimension(300, 30));
        txtPartenza.setMinimumSize(new Dimension(300, 30));
        txtPartenza.setPreferredSize(new Dimension(300, 30));
        partenzaPanel.add(txtPartenza);
        destinazionePanel = new JPanel();
        destinazionePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        destinazionePanel.setMaximumSize(new Dimension(560, 35));
        destinazionePanel.setMinimumSize(new Dimension(560, 35));
        destinazionePanel.setPreferredSize(new Dimension(560, 35));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 6;
        fieldsPanel.add(destinazionePanel, gbc);
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setMaximumSize(new Dimension(180, 25));
        label7.setMinimumSize(new Dimension(180, 25));
        label7.setPreferredSize(new Dimension(180, 25));
        label7.setText("Destinazione:");
        destinazionePanel.add(label7);
        txtDestinazione = new JTextField();
        Font txtDestinazioneFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtDestinazione.getFont());
        if (txtDestinazioneFont != null) txtDestinazione.setFont(txtDestinazioneFont);
        txtDestinazione.setMaximumSize(new Dimension(300, 30));
        txtDestinazione.setMinimumSize(new Dimension(300, 30));
        txtDestinazione.setPreferredSize(new Dimension(300, 30));
        destinazionePanel.add(txtDestinazione);
        gatePanel = new JPanel();
        gatePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gatePanel.setMaximumSize(new Dimension(560, 35));
        gatePanel.setMinimumSize(new Dimension(560, 35));
        gatePanel.setPreferredSize(new Dimension(560, 35));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 7;
        fieldsPanel.add(gatePanel, gbc);
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 14, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setMaximumSize(new Dimension(180, 25));
        label8.setMinimumSize(new Dimension(180, 25));
        label8.setPreferredSize(new Dimension(180, 25));
        label8.setText("Gate (opzionale):");
        gatePanel.add(label8);
        cbGate = new JComboBox();
        cbGate.setMaximumSize(new Dimension(300, 30));
        cbGate.setMinimumSize(new Dimension(300, 30));
        cbGate.setPreferredSize(new Dimension(300, 30));
        gatePanel.add(cbGate);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setMaximumSize(new Dimension(580, 60));
        buttonPanel.setMinimumSize(new Dimension(580, 60));
        buttonPanel.setPreferredSize(new Dimension(580, 60));
        panel1.add(buttonPanel);
        btnSalva = new JButton();
        Font btnSalvaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnSalva.getFont());
        if (btnSalvaFont != null) btnSalva.setFont(btnSalvaFont);
        btnSalva.setMaximumSize(new Dimension(80, 35));
        btnSalva.setMinimumSize(new Dimension(80, 35));
        btnSalva.setPreferredSize(new Dimension(80, 35));
        btnSalva.setText("Salva");
        buttonPanel.add(btnSalva);
        btnAnnulla = new JButton();
        Font btnAnnullaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnAnnulla.getFont());
        if (btnAnnullaFont != null) btnAnnulla.setFont(btnAnnullaFont);
        btnAnnulla.setMaximumSize(new Dimension(80, 35));
        btnAnnulla.setMinimumSize(new Dimension(80, 35));
        btnAnnulla.setPreferredSize(new Dimension(80, 35));
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
