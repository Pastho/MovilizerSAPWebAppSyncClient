package service;

import controller.FileController;
import model.SAPConnection;
import view.GeneralView;

public class Application {

    public static void main(String args[]) {

        /*
        RestTemplate restTemplate = new RestTemplate();
        Page page = restTemplate.getForObject("http://router2.movilizer.com:8105/movilizer/webappsync/webapps/moep/versions", Page.class);
        System.out.println("Name:    " + page.getName());
        System.out.println("About:   " + page.getAbout());
        System.out.println("Phone:   " + page.getPhone());
        System.out.println("Website: " + page.getWebsite());
        */

        // read the stored SAP Connection settings
        SAPConnection sapConnection = new SAPConnection("http://example.com");

        // start the GUI
        new GeneralView(new FileController(), new MovilizerWebAppSyncHandler(sapConnection));
    }

}