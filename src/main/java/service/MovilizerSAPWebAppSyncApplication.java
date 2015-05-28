package service;

import view.LoginView;

public class MovilizerSAPWebAppSyncApplication {

    public static void main(String args[]) {

        // initialize the application config
        UserConfigService userConfigService = new UserConfigService();

        new LoginView(userConfigService);
    }

}