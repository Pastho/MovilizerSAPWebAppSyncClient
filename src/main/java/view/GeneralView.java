package view;

import controller.FileController;
import controls.*;
import model.ProjectFile;
import model.WebAppVersion;
import service.MovilizerWebAppSyncHandler;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The General View of the Movilizer WebApp Webservice Client.
 */
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

    /**
     * Send a WebApp to the selected SAP connection.
     */
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
                if (answer != null && !answer.isEmpty()) {
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

    /**
     * Opens a pop up where a WebApp project can be selected over an existing SAP connection.
     */
    private void getWebApp() {
        // get available SAP connections and show the dialog
        Object[] sapConnections = getSapConnectionPanel().getAvailableSAPConnections();

        if (sapConnections.length > 0) {
            String answerSAPConnection = (String) JOptionPane.showInputDialog(
                    frame,
                    "Please select an available SAP connection from the list:",
                    "Select SAP Connection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    sapConnections,
                    sapConnections[0]);

            // if a SAP connection was selected try to put the WebApp to the SAP system
            if (answerSAPConnection != null && !answerSAPConnection.isEmpty()) {

                // set the selected SAP connection as the current SAP connection
                getMovilizerWebAppSyncHandler().setSapConnection(getSapConnectionPanel().getSAPConnection(answerSAPConnection));

                // get the list of available WebApps
                Object[] webAppVersionsAsText = getMovilizerWebAppSyncHandler().getWebAppsList().toArray();

                if (webAppVersionsAsText.length > 0) {
                    WebAppVersion answerWebApp = (WebAppVersion) JOptionPane.showInputDialog(
                            frame,
                            "Please select an available WebApp Project from the list:",
                            "Select WebApp Project for Receiving",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            webAppVersionsAsText,
                            webAppVersionsAsText[0]);

                    if (answerWebApp != null) {
                        // get the selected WebApp from the SAP system
                        getFileController().generateZIPFileFromByteArray(
                                getMovilizerWebAppSyncHandler().getWebApp(answerWebApp.getId()), getSessionUsername(), answerWebApp.getId());

                        JOptionPane.showMessageDialog(frame, "The requested project was successfully downloaded.", "Project Successfully Downloaded", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "There are no projects available in the selected SAP connection.", "No projects available", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please maintain at least one valid SAP connection.", "No SAP Connection maintained", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Deletes the selected WebApp from the selected SAP system.
     */
    private void deleteWebApp() {
        // get available SAP connections and show the dialog
        Object[] sapConnections = getSapConnectionPanel().getAvailableSAPConnections();

        if (sapConnections.length > 0) {
            String answerSAPConnection = (String) JOptionPane.showInputDialog(
                    frame,
                    "Please select an available SAP connection from the list:",
                    "Select SAP Connection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    sapConnections,
                    sapConnections[0]);

            // if a SAP connection was selected try to put the WebApp to the SAP system
            if (answerSAPConnection != null && !answerSAPConnection.isEmpty()) {

                // set the selected SAP connection as the current SAP connection
                getMovilizerWebAppSyncHandler().setSapConnection(getSapConnectionPanel().getSAPConnection(answerSAPConnection));

                // get the list of available WebApps
                Object[] webAppVersionsAsText = getMovilizerWebAppSyncHandler().getWebAppsList().toArray();

                if (webAppVersionsAsText.length > 0) {
                    WebAppVersion answerWebApp = (WebAppVersion) JOptionPane.showInputDialog(
                            frame,
                            "Please select an available WebApp Project from the list:",
                            "Select WebApp Project for Deleting",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            webAppVersionsAsText,
                            webAppVersionsAsText[0]);

                    if (answerWebApp != null) {
                        // delete the selected WebApp from the SAP system
                        getMovilizerWebAppSyncHandler().deleteWebApp(answerWebApp.getId());

                        JOptionPane.showMessageDialog(frame, "The requested project was successfully deleted.", "Project Successfully Deleted", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "There are no projects available in the selected SAP connection.", "No projects available", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please maintain at least one valid SAP connection.", "No SAP Connection maintained", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case "menuItem-file-openFolder":
                setSessionProject(getFileController().openFolder(this, projectStructure));
                break;
            case "menuItem-file-showProject":
                showMainPanel();
                break;
            case "menuItem-file-getProject":
                getWebApp();
                break;
            case "menuItem-file-deleteProject":
                deleteWebApp();
                break;
            case "menuItem-settings-SAPConnection":
                showSAPConnectionPanel();
                break;
            case "button-create-sapConnection":
                getSapConnectionPanel().createSAPConnection();
                break;
            case "button-save-sapConnection":
                getSapConnectionPanel().updateSAPConnection();
                break;
            case "button-delete-sapConnection":
                getSapConnectionPanel().deleteSAPConnection();
                break;
            case "menuItem-settings-Transportrequest":
                showSAPTransportPanel();
                break;
            case "button-send-webapp":
                sendWebApp();
                break;
            default:
                System.err.println("error: on action handler was found");
        }
    }
}