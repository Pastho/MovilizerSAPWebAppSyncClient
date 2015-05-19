package view.general;

import controller.FileController;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeneralView extends JPanel implements ActionListener {

    // define controllers
    private FileController fileController;

    // define page components
    private JFrame frame;
    private JPanel mainPanel, treePanel, detailsPanel;
    private JMenuBar menuBar;
    private JMenu filesMenu, settingsMenu;
    private JTree projectStructure;

    public GeneralView(FileController fileController) {
        super();

        this.fileController = fileController;

        initComponents();
    }

    /*
        initialize the GUI components
     */
    private void initComponents() {

        // build the frame
        frame = new JFrame("Menu");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setUpMenu();
        setUpMainPanel();

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Set up the menu
     */
    private void setUpMenu() {
        menuBar = new JMenuBar();
        setUpFileMenu();
        setUpSettingsMenu();
        frame.setJMenuBar(menuBar);
    }

    /**
     * Set up the main panel by adding all the sub panels.
     */
    private void setUpMainPanel() {
        GridLayout gridLayout = new GridLayout(0, 2);

        mainPanel = new JPanel(gridLayout);
        mainPanel.setPreferredSize(new Dimension(600, 400));

        setUpTreePanel();
        setUpDetailsPanel();

        frame.getContentPane().add(mainPanel);
    }

    /**
     * Set up the tree panel which contains the tree view to visualize the project structure.
     */
    private void setUpTreePanel() {
        // create the tree panel
        treePanel = new JPanel(new GridLayout(1, 1));

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Please select a project");
        projectStructure = new JTree(rootNode);
        JScrollPane treeView = new JScrollPane(projectStructure);
        treeView.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 10));
        treePanel.add(treeView);

        mainPanel.add(treePanel, BorderLayout.WEST);
    }

    /**
     * Set up the details panel which contains two sub panels.
     */
    private void setUpDetailsPanel() {
        // create the details panel
        detailsPanel = new JPanel(new GridLayout(2, 1));

        JPanel detailsNotesPanel = new JPanel();
        detailsNotesPanel.setBackground(new Color(255, 178, 0));
        detailsPanel.add(detailsNotesPanel);

        JPanel detailsActionsPanel = new JPanel(new BorderLayout());
        detailsActionsPanel.setBackground(new Color(0, 221, 74));

        JButton sendButton = new JButton("Send WebApp");
        detailsActionsPanel.add(sendButton, BorderLayout.SOUTH);

        detailsPanel.add(detailsActionsPanel);

        mainPanel.add(detailsPanel, BorderLayout.EAST);
    }

    /**
     * Set up the file menu.
     */
    private void setUpFileMenu() {
        // initialize the basic components
        JMenuItem menuItem;

        // create the file menu
        filesMenu = new JMenu("File");

        // build the file menu items
        menuItem = new JMenuItem("Open Folder");
        menuItem.setActionCommand("menuItem-file-openFolder");
        menuItem.addActionListener(this);

        filesMenu.add(menuItem);

        menuBar.add(filesMenu);
    }

    /**
     * Set up the settings menu.
     */
    private void setUpSettingsMenu() {
        // initialize the basic components
        JMenuItem menuItem;

        // create the settings menu
        settingsMenu = new JMenu("Settings");

        // build the settings menu
        menuItem = new JMenuItem("SAP Connection");
        menuItem.setActionCommand("menuItem-settings-SAPConnection");
        menuItem.addActionListener(this);
        settingsMenu.add(menuItem);

        menuBar.add(settingsMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // handle the incoming events
        switch (e.getActionCommand()) {
            case "menuItem-file-openFolder":
                System.out.println("Open Folder was clicked");

                this.fileController.openFolder(this, projectStructure);

                break;
            case "menuItem-settings-SAPConnection":
                System.out.println("SAP Connection was clicked");
                break;
            default:
                System.out.println("Couldn't fetch any event handler");
        }

    }
}
