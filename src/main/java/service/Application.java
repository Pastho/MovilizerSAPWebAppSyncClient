package service;

import view.LoginView;

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

        // TODO login
        new LoginView();
    }

}