package view;

import javax.swing.*;
import java.awt.*;

public class LoginView {

    private JFrame frame;
    private JPanel mainPanel;
    private JTextField usernameTextField, passwordTextField;

    public LoginView() {
        initComponents();
    }

    private void initComponents() {

        // build the frame
        frame = new JFrame("SAP Login");
        frame.setSize(new Dimension(300, 130));
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        setUpMainPanel();
        frame.getContentPane().add(mainPanel);

        frame.setVisible(true);
    }

    private void setUpMainPanel() {
        mainPanel = new JPanel();

        // define group layout for details notes
        GroupLayout layout = new GroupLayout(mainPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel projectTitle = new JLabel("Username:");
        usernameTextField = new JTextField();

        JLabel projectSizeTitle = new JLabel("Password:");
        passwordTextField = new JPasswordField();

        JButton buttonSend = new JButton("Send");
        JButton buttonCancel = new JButton("Cancel");

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(projectTitle)
                                        .addComponent(projectSizeTitle)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(usernameTextField)
                                        .addComponent(passwordTextField)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(buttonSend)
                                                .addComponent(buttonCancel))
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(projectTitle)
                                        .addComponent(usernameTextField)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(projectSizeTitle)
                                        .addComponent(passwordTextField)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonSend)
                                        .addComponent(buttonCancel)
                        )
        );

        mainPanel.setLayout(layout);
    }

}
