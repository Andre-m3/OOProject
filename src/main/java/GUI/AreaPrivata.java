package GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import controller.Controller;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Locale;

/**
 * The type Area privata.
 */
public class AreaPrivata {
    private JFrame FrameAreaPersonale;
    private Controller controller;
    private JPanel panel1;
    private JPanel topPanel;
    private JLabel welcomeText;
    private JLabel subText;
    private JPanel infoPanel;
    private JPanel bookingPanel;
    private JPanel bottomPanel;
    private JLabel usernameValue;
    private JLabel emailValue;
    private JLabel bookingText;
    private JButton btnTickets;
    private JLabel spacerBypass;
    private JButton btnHomepage;
    private JButton btnModificaPrenotazione;
    private JTable tabellaPrenotazioni;

    /**
     * Instantiates a new Area privata.
     *
     * @param frameDash the frame dash
     */
    public AreaPrivata(JFrame frameDash) {

        controller = Controller.getInstance();
        FrameAreaPersonale = new JFrame("Area Personale");
        FrameAreaPersonale.setContentPane(panel1);
        /* Non vogliamo che alla chiusura venga terminata l'esecuzione del programma! (exit_on_cose)
         * Facendo 'dispose_on_close' abbiamo il controllo sulla chiusura della finestra...
         * Aggiungeremo un Listener (WindowsListener) per gestire la chiusura della finestra e tornare alla Dashboard Admin
         */
        FrameAreaPersonale.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        FrameAreaPersonale.pack();
        FrameAreaPersonale.setLocationRelativeTo(null);
        FrameAreaPersonale.setVisible(true);
        FrameAreaPersonale.setResizable(false);

        setupButtons();
        loadUserInfo();
        loadPrenotazioni();


        // Aggiungiamo il listener per il pulsante Homepage
        btnHomepage.addActionListener(e -> {
            frameDash.setVisible(true);
            FrameAreaPersonale.dispose();
        });

        /* Questa volta necessitiamo di un WindowListener
         * Il metodo windowClosing() viene chiamato quando si preme il pulsante X
         * Così torniamo alla dashboard admin quando questa finestra viene chiusa
         */
        FrameAreaPersonale.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameDash.setVisible(true);
            }
        });

        /* Il pulsante "Modifica Prenotazione" dovrà modificare esclusivamente la prenotazione selezionata
         * In poche parole, l'utente potrà selezionare dalla lista delle prenotazioni personali direttamente quella da modificare
         * Così cliccando sul pulsante "Modifica Prenotazione", riusciremo a passare al Dialog esattamente quella prenotazione
         * Questo ci garantisce un approccio più "user-friendly", il che è meglio!
         */
        btnModificaPrenotazione.addActionListener(e -> modificaPrenotazioneSelezionata());
        btnTickets.addActionListener(e -> visualizzaTickets());

    }

    private void setupButtons() {
        // Impostiamo i pulsanti con lo stesso stile utilizzato nelle altre interfacce!
        Color sfondoLeggermenteScuro = new Color(214, 214, 214);

        // Pulsante HOMEPAGE
        btnHomepage.setBackground(sfondoLeggermenteScuro);
        btnHomepage.setForeground(new Color(78, 78, 78));
        btnHomepage.setFocusPainted(false);
        btnHomepage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnHomepage.setOpaque(true);

        // Pulsante TICKETS
        btnTickets.setBackground(sfondoLeggermenteScuro);
        btnTickets.setForeground(new Color(78, 78, 78));
        btnTickets.setFocusPainted(false);
        btnTickets.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnTickets.setOpaque(true);

        // Pulsante MODIFICA PRENOTAZIONE
        btnModificaPrenotazione.setBackground(sfondoLeggermenteScuro);
        btnModificaPrenotazione.setForeground(new Color(78, 78, 78));
        btnModificaPrenotazione.setFocusPainted(false);
        btnModificaPrenotazione.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(193, 193, 193), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        btnModificaPrenotazione.setOpaque(true);


    }

    private void loadUserInfo() {
        // Carichiamo i dati dell'utente (username ed email) nell'infoPanel
        usernameValue.setText(controller.getUtenteLoggato().getUsername());
        emailValue.setText(controller.getUtenteLoggato().getEmail());
    }

    private void loadPrenotazioni() {
        // Definisci le colonne della tabella
        String[] colonne = {
                "Cod. Prenotazione",
                "Passeggeri",
                "Prenotazione",
                "Cod. Volo",
                "Tratta",
                "Stato Volo",
                "Orario",
                "Ritardo"
        };

        // Ottieni i dati delle prenotazioni tramite il controller
        Object[][] dati = controller.getDatiPrenotazioniUtente();

        // Crea il modello della tabella (non editabile)
        DefaultTableModel modelloTabella = new DefaultTableModel(dati, colonne) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabellaPrenotazioni.setModel(modelloTabella);
        tabellaPrenotazioni.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Abilita/disabilita i pulsanti in base alle prenotazioni disponibili
        int numeroPrenotazioni = controller.getNumeroPrenotazioniUtente();
        boolean hasPrenotazioni = numeroPrenotazioni > 0;
        btnModificaPrenotazione.setEnabled(hasPrenotazioni);
        btnTickets.setEnabled(hasPrenotazioni);

    }

    private void visualizzaTickets() {
        int selectedRow = tabellaPrenotazioni.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(tabellaPrenotazioni,
                    "Seleziona una prenotazione per visualizzare i ticket.",
                    "Nessuna Selezione",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ottieni il codice prenotazione dalla riga selezionata
        String codicePrenotazione = (String) tabellaPrenotazioni.getValueAt(selectedRow, 0);

        // Apri il dialog per visualizzare i ticket
        TicketDialog dialog = new TicketDialog(FrameAreaPersonale, codicePrenotazione);
        dialog.setVisible(true);
    }

    private void modificaPrenotazioneSelezionata() {
        int selectedRow = tabellaPrenotazioni.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(FrameAreaPersonale,
                    "Seleziona una prenotazione da modificare.",
                    "Nessuna prenotazione selezionata",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Controlliamo se non ci sono prenotazioni! Importante verificare questa condizione. È utile non verificare la condizione inversa.
        if (controller.getNumeroPrenotazioniUtente() == 0) {
            JOptionPane.showMessageDialog(FrameAreaPersonale,
                    "Non hai prenotazioni da modificare.",
                    "Nessuna prenotazione",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Ora otteniamo "le informazioni" della prenotazione selezionata
        String codicePrenotazione = (String) tabellaPrenotazioni.getValueAt(selectedRow, 0);

        // Otteniamo (tramite il controller...) la prenotazione selezionata
        var prenotazioneSelezionata = controller.getPrenotazionePerCodice(codicePrenotazione);

        // Verifichiamo se la prenotazione selezionata esiste
        if (prenotazioneSelezionata == null) {
            JOptionPane.showMessageDialog(FrameAreaPersonale,
                    "Errore nel recupero della prenotazione.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Adesso non ci resta che aprire il dialog di modifica, passando insieme al frame anche la prenotazione selezionata!
        ModificaPrenotazioneDialog dialog = new ModificaPrenotazioneDialog(FrameAreaPersonale, prenotazioneSelezionata);
        dialog.setVisible(true);

        // Ricarica le prenotazioni dopo la modifica (in caso di eliminazione)
        loadPrenotazioni();
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
        topPanel.setLayout(new BorderLayout(5, 2));
        topPanel.setMaximumSize(new Dimension(800, 125));
        topPanel.setMinimumSize(new Dimension(800, 125));
        topPanel.setPreferredSize(new Dimension(800, 125));
        panel1.add(topPanel);
        welcomeText = new JLabel();
        Font welcomeTextFont = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.PLAIN, 48, welcomeText.getFont());
        if (welcomeTextFont != null) welcomeText.setFont(welcomeTextFont);
        welcomeText.setHorizontalAlignment(0);
        welcomeText.setHorizontalTextPosition(0);
        welcomeText.setMaximumSize(new Dimension(800, 64));
        welcomeText.setMinimumSize(new Dimension(800, 64));
        welcomeText.setPreferredSize(new Dimension(800, 64));
        welcomeText.setText("Area Personale");
        welcomeText.setVerticalAlignment(3);
        welcomeText.setVerticalTextPosition(3);
        topPanel.add(welcomeText, BorderLayout.CENTER);
        subText = new JLabel();
        Font subTextFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 20, subText.getFont());
        if (subTextFont != null) subText.setFont(subTextFont);
        subText.setHorizontalAlignment(0);
        subText.setHorizontalTextPosition(0);
        subText.setMaximumSize(new Dimension(800, 17));
        subText.setMinimumSize(new Dimension(800, 17));
        subText.setPreferredSize(new Dimension(800, 17));
        subText.setText("Ecco tutte le tue informazioni private!");
        subText.setVerticalAlignment(0);
        subText.setVerticalTextPosition(0);
        topPanel.add(subText, BorderLayout.SOUTH);
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayoutManager(2, 2, new Insets(5, 0, 5, 100), 10, 0));
        infoPanel.setMaximumSize(new Dimension(700, 120));
        infoPanel.setMinimumSize(new Dimension(700, 120));
        infoPanel.setPreferredSize(new Dimension(700, 120));
        panel1.add(infoPanel);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.PLAIN, 16, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(2);
        label1.setText("Username: ");
        label1.setVerticalAlignment(0);
        label1.setVerticalTextPosition(0);
        infoPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, 30), new Dimension(300, 30), new Dimension(300, 30), 0, false));
        usernameValue = new JLabel();
        Font usernameValueFont = this.$$$getFont$$$("JetBrains Mono Light", Font.PLAIN, 16, usernameValue.getFont());
        if (usernameValueFont != null) usernameValue.setFont(usernameValueFont);
        usernameValue.setText("miaUsername");
        infoPanel.add(usernameValue, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, 30), new Dimension(300, 30), new Dimension(300, 30), 0, false));
        emailValue = new JLabel();
        Font emailValueFont = this.$$$getFont$$$("JetBrains Mono Light", Font.PLAIN, 16, emailValue.getFont());
        if (emailValueFont != null) emailValue.setFont(emailValueFont);
        emailValue.setText("miaEmail");
        infoPanel.add(emailValue, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, 30), new Dimension(300, 30), new Dimension(300, 30), 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("JetBrains Mono SemiBold", Font.PLAIN, 16, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setHorizontalAlignment(2);
        label2.setText("Email: ");
        infoPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, 30), new Dimension(300, 30), new Dimension(300, 30), 0, false));
        bookingPanel = new JPanel();
        bookingPanel.setLayout(new BorderLayout(0, 0));
        bookingPanel.setMaximumSize(new Dimension(700, 270));
        bookingPanel.setMinimumSize(new Dimension(700, 270));
        bookingPanel.setPreferredSize(new Dimension(700, 270));
        panel1.add(bookingPanel);
        bookingText = new JLabel();
        Font bookingTextFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 24, bookingText.getFont());
        if (bookingTextFont != null) bookingText.setFont(bookingTextFont);
        bookingText.setHorizontalAlignment(2);
        bookingText.setPreferredSize(new Dimension(320, 35));
        bookingText.setText("Le tue prenotazioni:");
        bookingPanel.add(bookingText, BorderLayout.NORTH);
        final JScrollPane scrollPane1 = new JScrollPane();
        bookingPanel.add(scrollPane1, BorderLayout.CENTER);
        tabellaPrenotazioni = new JTable();
        scrollPane1.setViewportView(tabellaPrenotazioni);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        bottomPanel.setMaximumSize(new Dimension(705, 80));
        bottomPanel.setMinimumSize(new Dimension(705, 80));
        bottomPanel.setPreferredSize(new Dimension(705, 80));
        panel1.add(bottomPanel);
        btnHomepage = new JButton();
        Font btnHomepageFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnHomepage.getFont());
        if (btnHomepageFont != null) btnHomepage.setFont(btnHomepageFont);
        btnHomepage.setLabel("Homepage");
        btnHomepage.setMaximumSize(new Dimension(90, 40));
        btnHomepage.setMinimumSize(new Dimension(90, 40));
        btnHomepage.setPreferredSize(new Dimension(90, 40));
        btnHomepage.setText("Homepage");
        bottomPanel.add(btnHomepage);
        spacerBypass = new JLabel();
        spacerBypass.setMaximumSize(new Dimension(400, 0));
        spacerBypass.setMinimumSize(new Dimension(400, 0));
        spacerBypass.setPreferredSize(new Dimension(400, 0));
        spacerBypass.setText("");
        bottomPanel.add(spacerBypass);
        btnModificaPrenotazione = new JButton();
        btnModificaPrenotazione.setMaximumSize(new Dimension(80, 40));
        btnModificaPrenotazione.setMinimumSize(new Dimension(80, 40));
        btnModificaPrenotazione.setPreferredSize(new Dimension(80, 40));
        btnModificaPrenotazione.setText("Modifica");
        bottomPanel.add(btnModificaPrenotazione);
        btnTickets = new JButton();
        Font btnTicketsFont = this.$$$getFont$$$("JetBrains Mono Medium", Font.PLAIN, 12, btnTickets.getFont());
        if (btnTicketsFont != null) btnTickets.setFont(btnTicketsFont);
        btnTickets.setMaximumSize(new Dimension(110, 40));
        btnTickets.setMinimumSize(new Dimension(110, 40));
        btnTickets.setPreferredSize(new Dimension(110, 40));
        btnTickets.setText("Vedi Tickets");
        bottomPanel.add(btnTickets);
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
