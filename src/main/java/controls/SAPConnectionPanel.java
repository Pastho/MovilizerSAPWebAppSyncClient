package controls;

import model.SAPConnection;
import service.MovilizerWebAppSyncHandler;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SAPConnectionPanel extends UserSessionWindow {

    private ActionListener parent;
    private JTextField sapSystemURLValue, sapSystemUsername, sapSystemPassword;
    private MovilizerWebAppSyncHandler movilizerWebAppSyncHandler;

    /**
     * Constructor for SAPConnection panel.
     *
     * @param parent                     The parent form object.
     * @param movilizerWebAppSyncHandler The Movilizer Web App Sync Handler.
     */
    public SAPConnectionPanel(ActionListener parent, MovilizerWebAppSyncHandler movilizerWebAppSyncHandler) {
        this.parent = parent;
        this.movilizerWebAppSyncHandler = movilizerWebAppSyncHandler;
        initComponents();
    }

    /**
     * Initialize the components of the SAP connection panel.
     */
    private void initComponents() {
        GroupLayout layout = new GroupLayout(this);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // SAP system connection
        JLabel sapSystemURLTitle = new JLabel("SAP System URL:");
        sapSystemURLValue = new JTextField(movilizerWebAppSyncHandler.getSapConnection().getUrl());

        // SAP system user
        JLabel sapSystemUsernameTitle = new JLabel("Username:");
        sapSystemUsername = new JTextField(movilizerWebAppSyncHandler.getSapConnection().getUsername());

        // SAP system password
        JLabel sapSystemPasswordTitle = new JLabel("Password:");
        sapSystemPassword = new JPasswordField(movilizerWebAppSyncHandler.getSapConnection().getPassword());

        // create connection button
        JButton createButton = new JButton("Create");
        createButton.setActionCommand("button-create-sapConnection");

        // delete connection button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("button-delete-sapConnection");

        // save button
        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("button-save-sapConnection");
        saveButton.addActionListener(parent);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(sapSystemURLTitle)
                                        .addComponent(sapSystemUsernameTitle)
                                        .addComponent(sapSystemPasswordTitle)

                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
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
        return new SAPConnection(sapSystemURLValue.getText(), sapSystemUsername.getText(), sapSystemPassword.getText());
    }

}