package controls;

import javax.swing.*;

public class UserSessionWindow extends JPanel {

    private static String sessionUsername;

    public UserSessionWindow() {
        super();
    }

    public static String getSessionUsername() {
        return sessionUsername;
    }

    public static void setSessionUsername(String sessionUsername) {
        UserSessionWindow.sessionUsername = sessionUsername;
    }
}
