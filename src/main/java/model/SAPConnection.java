package model;

import java.io.*;

public class SAPConnection {

    private final String applicationConfigPath = "resources/application.config";

    private String url;

    public SAPConnection() { }

    public SAPConnection(String url) {
        this.url = url;
        storeConnection();
    }

    private void storeConnection() {
        try {
            File file = new File(applicationConfigPath);
            if (!file.exists()) {
                file.createNewFile();
            }

            Writer writer = new FileWriter(file);
            writer.write("url=" + getUrl() + "\n");
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

    public String getURLforCSRF() {
        return this.getUrl() + "/movilizer/webappsync/";
    }


    /**
     * Updates a SAP connection by passing a new SAP connection object.
     * @param sapConnection The SAP connection with the latest values.
     */
    public void updateSAPConnection(SAPConnection sapConnection) {
        setUrl(sapConnection.url);

        storeConnection();
    }
}
