package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SAPConnection {

    private final String applicationConfigPath = "resources/application.config";

    private String url;
    private String username;
    private String password;

    /**
     * Default constructor
     */
    public SAPConnection() {
    }

    /**
     * Constructor for fully described SAP connections
     *
     * @param url      The URL of the SAP connection
     * @param username The username of the SAP connection
     * @param password The password of the SAP connection
     */
    public SAPConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        storeConnection();
    }

    /**
     * Stores the SAP connection in the application.config file
     */
    private void storeConnection() {
        try {
            File file = new File(applicationConfigPath);
            if (!file.exists()) {
                file.createNewFile();
            }

            Writer writer = new FileWriter(file);
            writer.write("url=" + getUrl() + "\n");
            writer.write("username=" + getUsername() + "\n");
            writer.write("password=" + getPassword());
            writer.close();
        } catch (IOException ex) {
            System.err.println("error: SAP connection couldn't be stored");
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getURLforCSRF() {
        return this.getUrl() + "/movilizer/webappsync/";
    }

    /**
     * Updates a SAP connection by passing a new SAP connection object.
     *
     * @param sapConnection The SAP connection with the latest values.
     */
    public void updateSAPConnection(SAPConnection sapConnection) {
        setUrl(sapConnection.url);
        setUsername(sapConnection.username);
        setPassword(sapConnection.password);
        storeConnection();
    }
}
