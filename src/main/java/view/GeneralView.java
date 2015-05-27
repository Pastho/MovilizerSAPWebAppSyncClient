package view;

import controller.FileController;
import controls.ProjectStructure;
import controls.SAPConnectionPanel;
import controls.SAPTransportPanel;
import model.ProjectFile;
import model.SAPConnection;
import service.MovilizerWebAppSyncHandler;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeneralView extends JPanel implements ActionListener {

    // define controllers
    private FileController fileController;
    private MovilizerWebAppSyncHandler movilizerWebAppSyncHandler;

    // define page components
    private JFrame frame;
    private JPanel mainPanel, sapConnectionPanel, sapTransportPanel, treePanel, detailsPanel, detailsNotesPanel, detailsActionsPanel;
    private JTextField sapSystemURLValue;
    private JMenuBar menuBar;
    private JMenu filesMenu, settingsMenu;
    private ProjectStructure projectStructure;

    public GeneralView(FileController fileController, MovilizerWebAppSyncHandler movilizerWebAppSyncHandler) {
        super();

        this.fileController = fileController;
        this.movilizerWebAppSyncHandler = movilizerWebAppSyncHandler;

        initComponents();
    }

    /**
     * Initialize the GUI components and set the main panel as the first which should be shown.
     */
    private void initComponents() {
        // build the frame
        frame = new JFrame("Movilizer SAP Connector WebApp Client");
        frame.setSize(new Dimension(600, 400));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        setUpMenu();
        setUpMainPanel();
        setUpSAPConnectionPanel();
        setUpSAPTransportPanel();

        // at first show the main panel
        showMainPanel();

        frame.pack();
        frame.setVisible(true);
    }

    private void setUpSAPTransportPanel() {
        sapTransportPanel = new SAPTransportPanel(this, movilizerWebAppSyncHandler);
        sapTransportPanel.setPreferredSize(new Dimension(600, 400));
    }

    /**
     * Set up the menu which contains several sub menus.
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
    }

    /**
     * Set up the SAP connection panel which contains two sub panels to maintain the connection to the SAP system.
     */
    private void setUpSAPConnectionPanel() {
        sapConnectionPanel = new SAPConnectionPanel(this, movilizerWebAppSyncHandler);
        sapConnectionPanel.setPreferredSize(new Dimension(600, 400));
    }

    /**
     * Set up the tree panel which contains the tree view to visualize the project structure.
     */
    private void setUpTreePanel() {
        // create the tree panel
        treePanel = new JPanel(new GridLayout(1, 1));

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new ProjectFile("Please select a project"));
        projectStructure = new ProjectStructure(rootNode);
        JScrollPane treeView = new JScrollPane(projectStructure);
        treeView.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 5));
        treePanel.add(treeView);

        mainPanel.add(treePanel, BorderLayout.WEST);
    }

    /**
     * Set up the details panel which contains two sub panels.
     */
    private void setUpDetailsPanel() {
        detailsPanel = new JPanel(new GridLayout(2, 1));

        setUpDetailsNotePanel();
        detailsPanel.add(detailsNotesPanel);
        setUpDetailsNoteActionsPanel();
        detailsPanel.add(detailsActionsPanel);

        mainPanel.add(detailsPanel, BorderLayout.EAST);
    }

    /**
     * Set up the details note panel
     */
    private void setUpDetailsNotePanel() {
        detailsNotesPanel = new JPanel();

        // define group layout for details notes
        GroupLayout layout = new GroupLayout(detailsNotesPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel projectTitle = new JLabel("Project:");
        JLabel projectValue = new JLabel("-");
        projectStructure.setProjectValueLabel(projectValue);
        projectValue.setFont(projectValue.getFont().deriveFont(Font.PLAIN));

        JLabel projectSizeTitle = new JLabel("Size:");
        JLabel projectSizeValue = new JLabel("-");
        projectStructure.setProjectSizeValueLabel(projectValue);
        projectSizeValue.setFont(projectSizeValue.getFont().deriveFont(Font.PLAIN));

        JLabel projectDateTitle = new JLabel("Date of Change:");
        JLabel projectDateValue = new JLabel("-");
        projectStructure.setProjectDateValueLabel(projectDateValue);
        projectDateValue.setFont(projectDateValue.getFont().deriveFont(Font.PLAIN));

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(projectTitle)
                                        .addComponent(projectSizeTitle)
                                        .addComponent(projectDateTitle)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(projectValue)
                                        .addComponent(projectSizeValue)
                                        .addComponent(projectDateValue)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(projectTitle)
                                        .addComponent(projectValue)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(projectSizeTitle)
                                        .addComponent(projectSizeValue)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(projectDateTitle)
                                        .addComponent(projectDateValue)
                        )
        );

        detailsNotesPanel.setLayout(layout);
    }

    /**
     * Set up the details action panel
     */
    private void setUpDetailsNoteActionsPanel() {
        detailsActionsPanel = new JPanel(new GridLayout(1, 1));

        JButton sendButton = new JButton("Send WebApp");
        sendButton.setActionCommand("button-send-webapp");
        sendButton.addActionListener(this);
        detailsActionsPanel.add(sendButton);
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

        menuItem = new JMenuItem("Show Project");
        menuItem.setActionCommand("menuItem-file-showProject");
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

        menuItem = new JMenuItem("Transportrequest");
        menuItem.setActionCommand("menuItem-settings-Transportrequest");
        menuItem.addActionListener(this);
        settingsMenu.add(menuItem);

        menuBar.add(settingsMenu);
    }

    /**
     * Sets the main panel as the active panel
     */
    private void showMainPanel() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(mainPanel);
        frame.repaint();
        frame.validate();
    }

    /**
     * Sets the SAP connection panel as the active panel
     */
    private void showSAPConnectionPanel() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(sapConnectionPanel);
        frame.repaint();
        frame.validate();
    }

    /**
     * Sets the SAP connection panel as the active panel
     */
    private void showSAPTransportPanel() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(sapTransportPanel);
        frame.repaint();
        frame.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // handle the incoming events
        switch (e.getActionCommand()) {
            case "menuItem-file-openFolder":
                System.out.println("clicked: menuItem-file-openFolder");
                this.fileController.openFolder(this, projectStructure);
                break;
            case "menuItem-file-showProject":
                showMainPanel();
                break;
            case "menuItem-settings-SAPConnection":
                showSAPConnectionPanel();
                System.out.println("clicked: menuItem-settings-SAPConnection");
                break;
            case "button-save-sapConnection":
                movilizerWebAppSyncHandler.getSapConnection()
                        .updateSAPConnection(new SAPConnection(sapSystemURLValue.getText()));
                showSAPConnectionPanel();
                System.out.println("clicked: button-save-sapConnection");
                break;
            case "button-cancel-sapConnection":
                showSAPConnectionPanel();
                System.out.println("clicked: button-cancel-sapConnection");
                break;
            case "menuItem-settings-Transportrequest":
                showSAPTransportPanel();
                System.out.println("clicked: menuItem-settings-Transportrequest");
                break;
            case "button-send-webapp":
                new LoginView();
                System.out.print("clicked: button-send-webapp");
                movilizerWebAppSyncHandler.putWebApp("THIS_IS_AN_EXAMPLE");
                break;
            default:
                System.err.println("error: on action handler was found");
        }

    }
}