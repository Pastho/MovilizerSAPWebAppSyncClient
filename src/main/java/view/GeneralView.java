package view;

import controller.FileController;
import controls.*;
import model.ProjectFile;
import service.MovilizerWebAppSyncHandler;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeneralView extends UserSessionWindow implements ActionListener {

    // define controllers
    private FileController fileController;
    private MovilizerWebAppSyncHandler movilizerWebAppSyncHandler;

    // define page components
    private JFrame frame;
    private JPanel mainPanel, sapTransportPanel;
    private SAPConnectionPanel sapConnectionPanel;
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

        frame.setJMenuBar(new CustomMenuBar(this));
        setUpMainPanel();
        setUpSAPConnectionPanel();
        setUpSAPTransportPanel();

        // at first show the main panel
        showMainPanel();

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Set up the SAP transport panel which contains the controls to handle with the SAP TMS.
     */
    private void setUpSAPTransportPanel() {
        sapTransportPanel = new SAPTransportPanel(this, movilizerWebAppSyncHandler);
        sapTransportPanel.setPreferredSize(new Dimension(600, 400));
    }

    /**
     * Set up the SAP connection panel which contains two sub panels to maintain the connection to the SAP system.
     */
    private void setUpSAPConnectionPanel() {
        sapConnectionPanel = new SAPConnectionPanel(this, movilizerWebAppSyncHandler);
        sapConnectionPanel.setPreferredSize(new Dimension(600, 400));
    }

    /**
     * Set up the main panel by adding all the sub panels.
     */
    private void setUpMainPanel() {
        GridLayout gridLayout = new GridLayout(0, 2);

        mainPanel = new JPanel(gridLayout);
        mainPanel.setPreferredSize(new Dimension(600, 400));

        setUpTreePanel();
        mainPanel.add(new DetailsPanel(this, new GridLayout(2, 1), projectStructure), BorderLayout.EAST);
    }

    /**
     * Set up the tree panel which contains the tree view to visualize the project structure.
     */
    private void setUpTreePanel() {
        // create the tree panel
        JPanel treePanel = new JPanel(new GridLayout(1, 1));

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new ProjectFile("Please select a project"));
        projectStructure = new ProjectStructure(rootNode, getSessionUsername());
        JScrollPane treeView = new JScrollPane(projectStructure);
        treeView.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 5));
        treePanel.add(treeView);

        mainPanel.add(treePanel, BorderLayout.WEST);
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

    public MovilizerWebAppSyncHandler getMovilizerWebAppSyncHandler() {
        return movilizerWebAppSyncHandler;
    }

    public SAPConnectionPanel getSapConnectionPanel() {
        return sapConnectionPanel;
    }

    public FileController getFileController() {
        return fileController;
    }

    private void sendWebApp() {
        if (getSessionProject() != null) {

            // get available SAP connections and show the dialog
            Object[] sapConnections = getSapConnectionPanel().getAvailableSAPConnections();

            if (sapConnections.length > 0) {
                String answer = (String) JOptionPane.showInputDialog(
                        frame,
                        "Please select an available SAP connection from the list:",
                        "Select SAP Connection",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        sapConnections,
                        sapConnections[0]);

                // if a SAP connection was selected try to put the WebApp to the SAP system
                if (!answer.isEmpty()) {
                    getMovilizerWebAppSyncHandler().setSapConnection(getSapConnectionPanel().getSAPConnection(answer));
                    getMovilizerWebAppSyncHandler().putWebApp(getFileController().readProjectFile(getSessionUsername(), getSessionProject()));
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a SAP connection in the SAP connection menu.", "No available SAP connections", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a project before sending it.", "No project selected", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void getWebApp() {
        // get available SAP connections and show the dialog
        Object[] sapConnections = getSapConnectionPanel().getAvailableSAPConnections();

        if (sapConnections.length > 0) {
            String answer = (String) JOptionPane.showInputDialog(
                    frame,
                    "Please select an available SAP connection from the list:",
                    "Select SAP Connection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    sapConnections,
                    sapConnections[0]);

            // if a SAP connection was selected try to put the WebApp to the SAP system
            if (!answer.isEmpty()) {

                // set the selected SAP connection as the current SAP connection
                getMovilizerWebAppSyncHandler().setSapConnection(getSapConnectionPanel().getSAPConnection(answer));

                // get the list of available WebApps
                getMovilizerWebAppSyncHandler().getWebAppsList();

                // get the selected WebApp from the SAP system
                getMovilizerWebAppSyncHandler().getWebApp("HTML5_2015_06_09");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case "menuItem-file-openFolder":
                System.out.println("clicked: menuItem-file-openFolder");
                setSessionProject(getFileController().openFolder(this, projectStructure));
                break;
            case "menuItem-file-showProject":
                showMainPanel();
                break;
            case "menuItem-file-getProject":
                getWebApp();
                break;
            case "menuItem-settings-SAPConnection":
                showSAPConnectionPanel();
                System.out.println("clicked: menuItem-settings-SAPConnection");
                break;
            case "button-create-sapConnection":
                getSapConnectionPanel().createSAPConnection();
                System.out.println("clicked: button-create-sapConnection");
                break;
            case "button-save-sapConnection":
                getSapConnectionPanel().updateSAPConnection();
                System.out.println("clicked: button-save-sapConnection");
                break;
            case "button-delete-sapConnection":
                sapConnectionPanel.deleteSAPConnection();
                System.out.println("clicked: button-delete-sapConnection");
                break;
            case "menuItem-settings-Transportrequest":
                showSAPTransportPanel();
                System.out.println("clicked: menuItem-settings-Transportrequest");
                break;
            case "button-send-webapp":
                System.out.print("clicked: button-send-webapp");
                sendWebApp();
                break;
            default:
                System.err.println("error: on action handler was found");
        }
    }
}