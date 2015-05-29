package controls;

import controller.SAPConnectionConfigController;
import model.SAPConnection;
import service.MovilizerWebAppSyncHandler;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SAPConnectionPanel extends UserSessionWindow {

    private ActionListener parent;
    private JTextField sapSystemURLValue, sapSystemUsername, sapSystemPassword;
    private MovilizerWebAppSyncHandler movilizerWebAppSyncHandler;
    private SAPConnectionConfigController sapConnectionConfigController;
    JComboBox<String> availableSAPConnectionsList;

    /**
     * Constructor for SAPConnection panel.
     *
     * @param parent                     The parent form object.
     * @param movilizerWebAppSyncHandler The Movilizer Web App Sync Handler.
     */
    public SAPConnectionPanel(ActionListener parent, MovilizerWebAppSyncHandler movilizerWebAppSyncHandler) {
        this.parent = parent;
        this.movilizerWebAppSyncHandler = movilizerWebAppSyncHandler;
        this.sapConnectionConfigController = new SAPConnectionConfigController(getSessionUsername());
        initComponents();
    }

    /**
     * Initialize the components of the SAP connection panel.
     */
    private void initComponents() {
        GroupLayout layout = new GroupLayout(this);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // available SAP connections
        JLabel availableSAPConnectionsLabel = new JLabel("Available SAP Connections:");
        availableSAPConnectionsList = new JComboBox<>();
        getAvailableSAPConnectionsList().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED && event.getItem() != null) {

                    updateInputFields(event.getItem().toString());
                }
            }
        });
        getSapConnectionConfigController().updateComboBox(availableSAPConnectionsList);

        // SAP system connection
        JLabel sapSystemURLTitle = new JLabel("SAP System URL:");
        sapSystemURLValue = new JTextField(getMovilizerWebAppSyncHandler().getSapConnection().getUrl());

        // SAP system user
        JLabel sapSystemUsernameTitle = new JLabel("Username:");
        sapSystemUsername = new JTextField(getMovilizerWebAppSyncHandler().getSapConnection().getUsername());

        // SAP system password
        JLabel sapSystemPasswordTitle = new JLabel("Password:");
        sapSystemPassword = new JPasswordField(getMovilizerWebAppSyncHandler().getSapConnection().getPassword());

        // create connection button
        JButton createButton = new JButton("Create");
        createButton.setActionCommand("button-create-sapConnection");
        createButton.addActionListener(parent);

        // delete connection button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("button-delete-sapConnection");
        deleteButton.addActionListener(parent);

        // save button
        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("button-save-sapConnection");
        saveButton.addActionListener(parent);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(availableSAPConnectionsLabel)
                                        .addComponent(sapSystemURLTitle)
                                        .addComponent(sapSystemUsernameTitle)
                                        .addComponent(sapSystemPasswordTitle)

                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(availableSAPConnectionsList)
                                        .addComponent(sapSystemURLValue)
                                        .addComponent(sapSystemUsername)
                                        .addComponent(sapSystemPassword)
                                        .addGroup(layout.createSequentialGroup()
                                                        .addComponent(createButton)
                                                        .addComponent(saveButton)
                                                        .addComponent(deleteButton)
                                        )
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(availableSAPConnectionsLabel)
                                        .addComponent(availableSAPConnectionsList)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(sapSystemURLTitle)
                                        .addComponent(sapSystemURLValue)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(sapSystemUsernameTitle)
                                        .addComponent(sapSystemUsername)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(sapSystemPasswordTitle)
                                        .addComponent(sapSystemPassword)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(createButton)
                                        .addComponent(saveButton)
                                        .addComponent(deleteButton)
                        )
        );

        this.setLayout(layout);
    }

    /**
     * Returns the values of the input fields as a new SAP connection.
     *
     * @return The SAP connection which contains the values from the input fields.
     */
    public SAPConnection getSAPConnection() {
        return new SAPConnection(getURLValue(), getUsernameValue(), getPasswordValue());
    }

    public JTextField getSapSystemPassword() {
        return sapSystemPassword;
    }

    public JTextField getSapSystemURLValue() {
        return sapSystemURLValue;
    }

    public JTextField getSapSystemUsername() {
        return sapSystemUsername;
    }

    public JComboBox<String> getAvailableSAPConnectionsList() {
        return availableSAPConnectionsList;
    }

    public void setAvailableSAPConnectionsList(JComboBox<String> availableSAPConnectionsList) {
        this.availableSAPConnectionsList = availableSAPConnectionsList;
    }

    public SAPConnectionConfigController getSapConnectionConfigController() {
        return sapConnectionConfigController;
    }

    public MovilizerWebAppSyncHandler getMovilizerWebAppSyncHandler() {
        return movilizerWebAppSyncHandler;
    }

    /**
     * Returns the input value of the URL input field.
     *
     * @return The entered URL
     */
    private String getURLValue() {
        return getSapSystemURLValue().getText();
    }

    /**
     * Returns the input value if the username input field.
     *
     * @return The entered username value
     */
    private String getUsernameValue() {
        return getSapSystemUsername().getText();
    }

    /**
     * Returns the input value of the password input field.
     *
     * @return The entered password value
     */
    private String getPasswordValue() {
        return getSapSystemPassword().getText();
    }

    /**
     * This method updates the input fields with the SAP connection stored in the config file.
     *
     * @param selectedURL The URL which is used to search for the SAP connection
     */
    private void updateInputFields(String selectedURL) {
        // select the SAP connection by searching for the selected URL
        SAPConnection sapConnection = getSapConnectionConfigController().getSAPConnection(selectedURL);

        if (sapConnection != null) {
            // update input fields only of they're already initialized
            if (getSapSystemURLValue() != null && getSapSystemUsername() != null && getSapSystemPassword() != null) {
                getSapSystemURLValue().setText(sapConnection.getUrl());
                getSapSystemUsername().setText(sapConnection.getUsername());
                getSapSystemPassword().setText(sapConnection.getPassword());
                System.out.println("success: sap connection panel --> sap connection input fields were updated");
            }
        } else {
            System.err.println("error: sap connection panel --> couldn't find the selected sap connection");
        }
    }

    /**
     * Creates a new SAP connection with the entered values in the input form.
     */
    public void createSAPConnection() {
        if (getSapConnectionConfigController().createSAPConnection(getSAPConnection())) {
            getSapConnectionConfigController().updateComboBox(getAvailableSAPConnectionsList());
            JOptionPane.showMessageDialog(getParent(), "SAP Connection was created and added to the connections list.", "SAP Connection created", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(getParent(), "SAP Connection already exists.", "SAP Connection not created", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Update the SAP connection by using the values entered in the input fields.
     */
    public void updateSAPConnection() {
        if (getSapConnectionConfigController().updateSAPConnection(getSAPConnection())) {
            JOptionPane.showMessageDialog(getParent(), "SAP Connection was updated", "SAP Connection updated", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(getParent(), "SAP Connection was not updated because the Entry was not found", "SAP Connection not updated", JOptionPane.ERROR_MESSAGE);
        }
    }
}