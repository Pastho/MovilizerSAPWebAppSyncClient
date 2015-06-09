package view;

import controller.FileController;
import controls.UserSessionWindow;
import model.SAPConnection;
import service.MovilizerWebAppSyncHandler;
import service.UserConfigService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends UserSessionWindow implements ActionListener {

    private JFrame frame;
    private JPanel mainPanel;
    private JTextField usernameTextField, passwordTextField;
    private UserConfigService userConfigService;

    public LoginView(UserConfigService userConfigService) {
        this.userConfigService = userConfigService;

        initComponents();
    }

    private void initComponents() {

        // build the frame
        frame = new JFrame("Movilizer SAP Client Login");
        frame.setSize(new Dimension(400, 130));
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

        JLabel usernameTitle = new JLabel("Username:");
        usernameTextField = new JTextField();

        JLabel passwordTitle = new JLabel("Password:");
        passwordTextField = new JPasswordField();

        // button register
        JButton buttonRegister = new JButton("Register");
        buttonRegister.setActionCommand("register");
        buttonRegister.addActionListener(this);

        // button send
        JButton buttonSend = new JButton("Login");
        buttonSend.setActionCommand("login");
        buttonSend.addActionListener(this);

        // button cancel
        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.setActionCommand("cancel");
        buttonCancel.addActionListener(this);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(usernameTitle)
                                        .addComponent(passwordTitle)
                                        .addComponent(buttonRegister)
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
                                        .addComponent(usernameTitle)
                                        .addComponent(usernameTextField)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(passwordTitle)
                                        .addComponent(passwordTextField)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonRegister)
                                        .addComponent(buttonSend)
                                        .addComponent(buttonCancel)
                        )
        );

        mainPanel.setLayout(layout);
    }

    public UserConfigService getUserConfigService() {
        return userConfigService;
    }

    public JTextField getUsernameTextField() {
        return usernameTextField;
    }

    public JTextField getPasswordTextField() {
        return passwordTextField;
    }

    /**
     * Select the entered username and password and try to login.
     *
     * @return Return true if the login was ok else if not
     */
    private boolean login() {

        if (getUserConfigService().doesUserExists(getUsernameTextField().getText())) {

            // do the login process
            // TODO


            setSessionUsername(getUsernameTextField().getText());
            return true;
        } else {
            JOptionPane.showMessageDialog(frame, "Login not possible. Please enter a valid user.", "Login failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        /*
        String usernameAndPassword = getUsernameTextField().getText() + ":" + getPasswordTextField().getText();

        SecurityService securityService = new SecurityService();

        String encryptedText = securityService.encrypt(usernameAndPassword, "000102030405060708090A0B0C0D0E0F");
        String decryptedText = securityService.decrypt(encryptedText, "000102030405060708090A0B0C0D0E0F");

        System.out.println(usernameAndPassword);
        System.out.println(encryptedText);
        System.out.println(decryptedText);
        */

    }

    /**
     * Register a new user to the application XML file.
     */
    private void registerUser() {

        String username = getUsernameTextField().getText();

        // does user exists? no --> create new entry
        if (!getUserConfigService().doesUserExists(username)) {
            getUserConfigService().registerNewUser(username, getPasswordTextField().getText());
        } else {
            // TODO show error message
            System.err.println("error: user already exists");
        }
    }

    /**
     * Closes the application.
     */
    private void closeApplication() {
        System.exit(0);
    }

    /**
     * Checks the input fields if they're valid.
     *
     * @return Return true if the fields are ok else false
     */
    private boolean checkInputFields() {
        boolean result = true;
        String errorMessage = "";

        if (getUsernameTextField().getText().isEmpty()) {
            result = false;
            errorMessage += "Please enter an username \n";
        }

        if (getPasswordTextField().getText().isEmpty()) {
            result = false;
            errorMessage += "Please enter a password \n";
        }

        if (!result) {
            JOptionPane.showMessageDialog(frame, errorMessage, "Bad user credentials", JOptionPane.WARNING_MESSAGE);
        }

        return result;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case "register":
                System.out.println("clicked: register");
                if (checkInputFields()) registerUser();
                break;
            case "login":
                System.out.println("clicked: login");
                if (checkInputFields()) {
                    if (login()) {
                        frame.dispose();
                        SAPConnection sapConnection = new SAPConnection("http://example.com", "testuser", "testpassword");
                        new GeneralView(new FileController(), new MovilizerWebAppSyncHandler(sapConnection));
                    } else {
                        // TODO show error message --> login not possible
                    }
                }
                break;
            case "cancel":
                System.out.println("clicked: cancel");
                closeApplication();
                break;
            default:
                System.err.println("error: on action handler was found");
        }
    }
}