package view.general;

import javax.swing.*;

public class GeneralView {
    public GeneralView() {
        initComponents();
    }

    private void initComponents() {

        JFrame frame = new JFrame("Menu");
        frame.setVisible(true);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
    }
}
