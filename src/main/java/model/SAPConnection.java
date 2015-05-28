package model;

public class SAPConnection {

    private String url;
    private String username;
    private String password;

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
        // TODO store the SAP connection in the application config
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
