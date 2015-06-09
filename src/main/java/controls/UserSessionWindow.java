package controls;

import javax.swing.*;

public class UserSessionWindow extends JPanel {

    private static String sessionUsername;
    private static String sessionProject;

    public UserSessionWindow() {
        super();
    }

    public static String getSessionUsername() {
        return sessionUsername;
    }

    public static void setSessionUsername(String sessionUsername) {
        UserSessionWindow.sessionUsername = sessionUsername;
    }

    public static String getSessionProject() {
        return sessionProject;
    }

    public static void setSessionProject(String sessionProject) {
        UserSessionWindow.sessionProject = sessionProject;
    }
}
