package GUI;

import com.intellij.uiDesigner.core.GridLayoutManager;
import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class TicketDialog {
    private JFrame Tickets;
    private Controller controller;
    private JPanel panel1;
    private JPanel topPanel;

    public TicketDialog(JFrame frameDash) {
        controller = Controller.getInstance();
        Tickets = new JFrame();
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
        panel1.setMaximumSize(new Dimension(600, 400));
        panel1.setMinimumSize(new Dimension(600, 400));
        panel1.setPreferredSize(new Dimension(600, 400));
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(0, 0));
        panel1.add(topPanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
