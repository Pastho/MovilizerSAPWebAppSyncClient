package controls;

import service.MovilizerWebAppSyncHandler;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SAPTransportPanel extends JPanel {

    private ActionListener parent;
    private JTextField sapSystemURLValue;
    private MovilizerWebAppSyncHandler movilizerWebAppSyncHandler;

    public SAPTransportPanel(ActionListener parent, MovilizerWebAppSyncHandler movilizerWebAppSyncHandler) {
        GroupLayout layout = new GroupLayout(this);

        this.parent = parent;
        this.movilizerWebAppSyncHandler = movilizerWebAppSyncHandler;

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel sapSystemURLTitle = new JLabel("SAP System URL:");
        sapSystemURLValue = new JTextField(movilizerWebAppSyncHandler.getSapConnection().getUrl());

        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("button-save-sapConnection");
        saveButton.addActionListener(parent);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("button-cancel-sapConnection");
        cancelButton.addActionListener(parent);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(sapSystemURLTitle)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(sapSystemURLValue)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(saveButton)
                                                .addComponent(cancelButton))
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(sapSystemURLTitle)
                                        .addComponent(sapSystemURLValue)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(saveButton)
                                        .addComponent(cancelButton)
                        )
        );

        this.setLayout(layout);
    }

}