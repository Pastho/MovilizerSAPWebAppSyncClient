package service;

import view.LoginView;

/**
 * Main Application Class
 */
public class MovilizerSAPWebAppSyncApplication {

    /**
     * Main method to start the application.
     *
     * @param args Not needed in this application
     */
    public static void main(String args[]) {

        // initialize the application config
        UserConfigService userConfigService = new UserConfigService();

        // open the first screen
        new LoginView(userConfigService);
    }

}