package model;

/**
 * Created by Thomas Pasberg on 18.05.2015.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {

    /*
    private String name;
    private String about;
    private String phone;
    private String website;

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }
    */


    /* for calling
        RestTemplate restTemplate = new RestTemplate();
        Page page = restTemplate.getForObject("http://router2.movilizer.com:8105/movilizer/webappsync/webapps/moep/versions", Page.class);
        System.out.println("Name:    " + page.getName());
        System.out.println("About:   " + page.getAbout());
        System.out.println("Phone:   " + page.getPhone());
        System.out.println("Website: " + page.getWebsite());
        */
}
