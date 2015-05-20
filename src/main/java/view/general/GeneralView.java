package view.general;

import controller.FileController;
import controls.ProjectStructure;
import model.ProjectFile;

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
    private JPanel mainPanel, treePanel, detailsPanel, detailsNotesPanel, detailsActionsPanel;
    private JMenuBar menuBar;
    private JMenu filesMenu, settingsMenu;
    private ProjectStructure projectStructure;

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
        frame = new JFrame("Movilizer SAP Connector WebApp Client");
        frame.setSize(new Dimension(600, 400));
        frame.setResizable(false);
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

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new ProjectFile("Please select a project"));
        projectStructure = new ProjectStructure(rootNode);
        JScrollPane treeView = new JScrollPane(projectStructure);
        treeView.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 5));
        treePanel.add(treeView);
        treePanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));

        mainPanel.add(treePanel, BorderLayout.WEST);
    }

    /**
     * Set up the details panel which contains two sub panels.
     */
    private void setUpDetailsPanel() {
        // create the details panel
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
        detailsNotesPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));

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

    @Override
    public void actionPerformed(ActionEvent e) {
        // handle the incoming events
        switch (e.getActionCommand()) {
            case "menuItem-file-openFolder":
                System.out.println("clicked: menuItem-file-openFolder");
                this.fileController.openFolder(this, projectStructure);
                break;
            case "menuItem-settings-SAPConnection":
                System.out.println("clicked: menuItem-settings-SAPConnection");
                break;
            case "menuItem-settings-Transportrequest":
                System.out.println("clicked: menuItem-settings-Transportrequest");
                break;
            default:
                System.err.println("error: on action handler was found");
        }

    }
}