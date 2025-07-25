
package GUI;

import controller.Controller;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

/**
 * The type Dialog modifica volo.
 */
public class DialogModificaVolo extends JDialog {
    private JPanel panel1;
    private JPanel mainPanel;
    private JTextField txtNumeroVolo;
    private JTextField txtCompagnia;
    private JTextField txtOrario;
    private JTextField txtData;
    private JSpinner spnRitardo;
    private JTextField txtPartenza;
    private JTextField txtDestinazione;
    private JPanel buttonPanel;
    private JButton btnSalva;
    private JButton btnAnnulla;
    private JPanel topPanel;
    private JLabel dialogTitle;
    private JComboBox<String> cbStato;

    private Controller controller;
    private String numeroVoloOriginale;

    // Non visto a lezione: È un pattern per comunicare al dialog che bisogna aggiornare la tabella dopo una modifica
    // Altrimenti la tabella (in questo caso di "VoliAdmin") non visualizzerebbe i nuovi dati impostati in seguito alla modifica/aggiornamento del volo/gate
    private Runnable onSaveCallback;

    /**
     * Instantiates a new Dialog modifica volo.
     *
     * @param parent         the parent
     * @param numeroVolo     the numero volo
     * @param onSaveCallback the on save callback
     */
    public DialogModificaVolo(JFrame parent, String numeroVolo, Runnable onSaveCallback) {
        super(parent, "Modifica Volo: " + numeroVolo, true);

        this.controller = Controller.getInstance();
        this.numeroVoloOriginale = numeroVolo;
        this.onSaveCallback = onSaveCallback;

        setContentPane(panel1);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);

        // Così facendo otterremo i dati del volo tramite il controller
        String[] datiVolo = controller.getDatiVolo(numeroVolo);

        // Impostiamo la ComboBox dello Stato del volo prima di precompilare i dati. Cosi avremo anche lo stato attuale del volo gia precompilato.
        setupComboBox();

        // Metodo che precompila i campi con i valori gia esistenti, così l'utente sa per certo cosa sta modificando!
        precompilaCampi(datiVolo);

        // Metodo che configura i Listeners. Li potremmo inserire anche qui, senza richiamare un metodo apposito (come fatto quasi sempre nelle varie interfacce).
        setupEvents();

        setVisible(true);
    }

    private void setupComboBox() {
        // Popoliamo la combo box stato usando il controller!
        String[] statiDisponibili = controller.getStatiVoloDisponibili();
        cbStato.removeAllItems();
        for (String stato : statiDisponibili) {
            cbStato.addItem(stato);
        }
    }

    private void precompilaCampi(String[] datiVolo) {

        // Siccome il Numero Volo è chiave primaria della nostra base di dati, lo rendiamo non modificabile!
        txtNumeroVolo.setEditable(false);
        txtNumeroVolo.setBackground(Color.LIGHT_GRAY);      // Cambiamo il colore di sfondo per far capire all'utente che non è modificabile

        txtNumeroVolo.setText(datiVolo[0]);
        txtCompagnia.setText(datiVolo[1]);
        txtOrario.setText(datiVolo[2]);
        txtData.setText(datiVolo[3]);
        spnRitardo.setValue(Integer.parseInt(datiVolo[4]));
        cbStato.setSelectedItem(datiVolo[5]);
        txtPartenza.setText(datiVolo[6]);
        txtDestinazione.setText(datiVolo[7]);
    }

    // Cosa facciamo quando viene cliccato il tasto "Salva" oppure "Annulla"? (Listeners)
    // Essendo solo due, e di lettura semplice, li carichiamo tramite metodo, per averli più compatti e visibili
    private void setupEvents() {
        btnSalva.addActionListener(e -> salvaModifiche());
        btnAnnulla.addActionListener(e -> dispose());
    }

    private void salvaModifiche() {
        // Aggiorniamo i dati del volo
        // String nuovoNumeroVolo = txtNumeroVolo.getText().trim(); // Non è più necessario, ora abbiamo capito che il numero volo non è modificabile
        String nuovaCompagnia = txtCompagnia.getText().trim();
        String nuovoOrario = txtOrario.getText().trim();
        String nuovaData = txtData.getText().trim();
        int nuovoRitardo = (Integer) spnRitardo.getValue();
        String nuovoStato = (String) cbStato.getSelectedItem();
        String nuovaPartenza = txtPartenza.getText().trim();
        String nuovaDestinazione = txtDestinazione.getText().trim();

        // Ricordiamo che vengono gestiti solo i voli da/verso Napoli!
        if (!validaCitta(nuovaPartenza, nuovaDestinazione)) {
            return;         // Il metodo validaCitta mostra già il messaggio di errore
        }

        // Prima di continuare validiamo anche gli altri campi
        if (!validaCampi(nuovaCompagnia, nuovoOrario, nuovaData, nuovoStato, nuovaPartenza, nuovaDestinazione)) {
            return;         // I metodi di validazione mostrano già i messaggi di errore
        }

        // Aggiorniamo i valori tramite il controller
        boolean success = controller.aggiornaVolo(
                numeroVoloOriginale,
                nuovaCompagnia,
                nuovoOrario,
                nuovaData,
                nuovoRitardo,
                nuovoStato,
                nuovaPartenza,
                nuovaDestinazione
        );

        if (success) {
            // (Callback) per aggiornare la tabella (ci troviamo in VoliAdmin)
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            // Salvate le modifiche (Evento "Salva") mostriamo un messaggio all'utente!
            JOptionPane.showMessageDialog(this,
                    "Volo modificato con successo!",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();      // Finito, non ci serve più!
        } else {
            JOptionPane.showMessageDialog(this,
                    "Errore durante la modifica del volo!",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
    private boolean validaCitta(String partenza, String destinazione) {
        if (!partenza.equalsIgnoreCase("Napoli") && !destinazione.equalsIgnoreCase("Napoli")) {
            JOptionPane.showMessageDialog(this,
                    "Errore: Almeno una tra partenza e destinazione deve essere 'Napoli'!\n" +
                            "L'aeroporto gestisce solo voli in partenza da Napoli o in arrivo a Napoli.",
                    "Errore di Validazione",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (partenza.equalsIgnoreCase(destinazione)) {
            JOptionPane.showMessageDialog(this,
                    "Errore: La città di partenza e destinazione non possono essere uguali!",
                    "Errore di Validazione",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    private boolean validaCampi(String compagnia, String orario, String data, String stato, String partenza, String destinazione) {
        // Controlla che i campi non siano vuoti
        if (compagnia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il campo 'Compagnia' non può essere vuoto!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (orario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il campo 'Orario' non può essere vuoto!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il campo 'Data' non può essere vuoto!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (partenza.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il campo 'Partenza' non può essere vuoto!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (destinazione.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il campo 'Destinazione' non può essere vuoto!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Valida formato orario (HH:MM)
        if (!orario.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            JOptionPane.showMessageDialog(this, "Formato orario non valido! Utilizzare il formato HH:MM (es: 14:30)",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Valida formato data utilizzando il controller
        if (!controller.isValidDateFormat(data)) {
            JOptionPane.showMessageDialog(this, "Formato data non valido! Utilizzare il formato dd-MM-yyyy (es: 25-12-2024)",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
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
        panel1.setLayout(new GridBagLayout());
        panel1.setMaximumSize(new Dimension(450, 400));
        panel1.setMinimumSize(new Dimension(450, 400));
        panel1.setPreferredSize(new Dimension(450, 400));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(buttonPanel, gbc);
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setMaximumSize(new Dimension(450, 400));
        mainPanel.setMinimumSize(new Dimension(450, 400));
        mainPanel.setPreferredSize(new Dimension(450, 400));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(mainPanel, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Numero Volo:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label1, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Compagnia:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label2, gbc);
        txtNumeroVolo = new JTextField();
        Font txtNumeroVoloFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtNumeroVolo.getFont());
        if (txtNumeroVoloFont != null) txtNumeroVolo.setFont(txtNumeroVoloFont);
        txtNumeroVolo.setMaximumSize(new Dimension(200, 30));
        txtNumeroVolo.setMinimumSize(new Dimension(200, 30));
        txtNumeroVolo.setPreferredSize(new Dimension(200, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtNumeroVolo, gbc);
        txtCompagnia = new JTextField();
        Font txtCompagniaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtCompagnia.getFont());
        if (txtCompagniaFont != null) txtCompagnia.setFont(txtCompagniaFont);
        txtCompagnia.setMaximumSize(new Dimension(200, 30));
        txtCompagnia.setMinimumSize(new Dimension(200, 30));
        txtCompagnia.setPreferredSize(new Dimension(200, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtCompagnia, gbc);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Orario:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label3, gbc);
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Data:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label4, gbc);
        txtOrario = new JTextField();
        Font txtOrarioFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtOrario.getFont());
        if (txtOrarioFont != null) txtOrario.setFont(txtOrarioFont);
        txtOrario.setMaximumSize(new Dimension(200, 30));
        txtOrario.setMinimumSize(new Dimension(200, 30));
        txtOrario.setPreferredSize(new Dimension(200, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtOrario, gbc);
        txtData = new JTextField();
        Font txtDataFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtData.getFont());
        if (txtDataFont != null) txtData.setFont(txtDataFont);
        txtData.setMaximumSize(new Dimension(200, 30));
        txtData.setMinimumSize(new Dimension(200, 30));
        txtData.setPreferredSize(new Dimension(200, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtData, gbc);
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Ritardo (min):");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label5, gbc);
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Stato:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label6, gbc);
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("Partenza:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label7, gbc);
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setText("Destinazione:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label8, gbc);
        txtPartenza = new JTextField();
        Font txtPartenzaFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtPartenza.getFont());
        if (txtPartenzaFont != null) txtPartenza.setFont(txtPartenzaFont);
        txtPartenza.setMaximumSize(new Dimension(200, 30));
        txtPartenza.setMinimumSize(new Dimension(200, 30));
        txtPartenza.setPreferredSize(new Dimension(200, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtPartenza, gbc);
        txtDestinazione = new JTextField();
        Font txtDestinazioneFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, txtDestinazione.getFont());
        if (txtDestinazioneFont != null) txtDestinazione.setFont(txtDestinazioneFont);
        txtDestinazione.setMaximumSize(new Dimension(200, 30));
        txtDestinazione.setMinimumSize(new Dimension(200, 30));
        txtDestinazione.setPreferredSize(new Dimension(200, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtDestinazione, gbc);
        spnRitardo = new JSpinner();
        Font spnRitardoFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, spnRitardo.getFont());
        if (spnRitardoFont != null) spnRitardo.setFont(spnRitardoFont);
        spnRitardo.setMaximumSize(new Dimension(200, 30));
        spnRitardo.setMinimumSize(new Dimension(200, 30));
        spnRitardo.setPreferredSize(new Dimension(200, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spnRitardo, gbc);
        cbStato = new JComboBox();
        cbStato.setMaximumSize(new Dimension(200, 30));
        cbStato.setMinimumSize(new Dimension(200, 30));
        cbStato.setPreferredSize(new Dimension(200, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(cbStato, gbc);
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(450, 70));
        topPanel.setMinimumSize(new Dimension(450, 70));
        topPanel.setPreferredSize(new Dimension(450, 70));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        panel1.add(topPanel, gbc);
        dialogTitle = new JLabel();
        Font dialogTitleFont = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.BOLD, 22, dialogTitle.getFont());
        if (dialogTitleFont != null) dialogTitle.setFont(dialogTitleFont);
        dialogTitle.setHorizontalAlignment(0);
        dialogTitle.setHorizontalTextPosition(0);
        dialogTitle.setText("Modifica del Volo");
        dialogTitle.setVerticalAlignment(3);
        dialogTitle.setVerticalTextPosition(3);
        topPanel.add(dialogTitle, BorderLayout.CENTER);
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