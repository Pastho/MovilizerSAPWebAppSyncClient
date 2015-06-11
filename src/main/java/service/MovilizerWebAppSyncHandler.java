package service;

import model.SAPConnection;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class MovilizerWebAppSyncHandler {

    private final String MOVILIZERWEBAPPWEBSERVICE = "/movilizer/webappsync/webapps/{webappid}";
    private final String MOVILIZERWEBAPPSLISTWEBSERVICE = "/movilizer/webappsync/webapps";

    private List<String> sessionCookies;
    private String csrfToken;
    private String userCredentials;
    private SAPConnection sapConnection;

    public MovilizerWebAppSyncHandler(SAPConnection sapConnection) {
        this.sapConnection = sapConnection;
    }

    protected String getCSRFToken() {
        return csrfToken;
    }

    protected void setCSRFToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public List<String> getSessionCookies() {
        return sessionCookies;
    }

    public void setSessionCookies(List<String> sessionCookies) {
        this.sessionCookies = sessionCookies;
    }

    public SAPConnection getSapConnection() {
        return this.sapConnection;
    }

    public void setSapConnection(SAPConnection sapConnection) {
        this.sapConnection = sapConnection;
    }

    /**
     * Puts a WebApp to the SAP system.
     *
     * @param webApp The WebApp which should be added to the SAP system
     */
    public void putWebApp(File webApp) {
        String url = sapConnection.getUrl() + MOVILIZERWEBAPPWEBSERVICE;
        String filename = webApp.getName().split(".zip")[0];

        // authenticate the user to prepare the webservice call
        authenticateUser();

        // update session and parameters
        updateCSRFToken();

        // build HTTP request with header and content
        HttpHeaders httpHeaders = generateHeaders();

        try {
            byte[] byteArray = Files.readAllBytes(webApp.toPath());
            
            HttpEntity request = new HttpEntity<>(byteArray, httpHeaders);

            // send WebApp to SAP System
            ResponseEntity<Object> response = new RestTemplate().
                    exchange(url, HttpMethod.PUT, request, Object.class, filename);

            // check the response
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a ZIP-file from an existing byte array.
     *
     * @param byteArray The byte array which contains the data for the file
     */
    private void generateZIPFileFromByteArray(byte[] byteArray) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./resources/test.zip");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            for (int i = 0; i < byteArray.length; i++) {
                byteArrayOutputStream.write(byteArray[i]);
            }

            byteArrayOutputStream.writeTo(fileOutputStream);

            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the requested WebApp from the SAP system.
     *
     * @param project The requested WebApp
     */
    public void getWebApp(String project) {
        String url = sapConnection.getUrl() + MOVILIZERWEBAPPWEBSERVICE;

        // authenticate the user to prepare the webservice call
        authenticateUser();

        // update session and parameters
        updateCSRFToken();

        // build HTTP request with header and content
        HttpHeaders httpHeaders = generateHeaders();
        HttpEntity request = new HttpEntity<>(httpHeaders);

        // send WebApp to SAP System
        ResponseEntity<String> response = new RestTemplate().
                exchange(url, HttpMethod.GET, request, String.class, project);

        generateZIPFileFromByteArray(response.getBody().getBytes());
    }

    /**
     * Gets a list of available WebApps in the selected SAP system.
     * @return The list of available WebApps
     */
    public String[] getWebAppsList() {
        String url = sapConnection.getUrl() + MOVILIZERWEBAPPSLISTWEBSERVICE;

        // authenticate the user to prepare the webservice call
        authenticateUser();

        // update session and parameters
        updateCSRFToken();

        // build HTTP request with header and content
        HttpHeaders httpHeaders = generateHeaders();
        HttpEntity request = new HttpEntity<>(httpHeaders);

        // send WebApp to SAP System
        ResponseEntity<String> response = new RestTemplate().
                exchange(url, HttpMethod.GET, request, String.class);

        System.out.println(request.getBody());

        return null;
    }

    /**
     * Generates the HTTP header for the webservice request.
     *
     * @return The HTTP header object
     */
    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + userCredentials);
        headers.add("x-csrf-token", getCSRFToken());

        // build the session cookies
        if (getSessionCookies() != null) {
            String agregatedCookies = "";
            for (String cookie : getSessionCookies()) {
                agregatedCookies += cookie + "; ";
            }
            headers.add("cookie", agregatedCookies);
        }

        return headers;
    }

    /**
     * Authenticate an user by using the username and password from the SAP connection.
     */
    private void authenticateUser() {
        userCredentials = new String(Base64.
                encodeBase64((getSapConnection().getUsername() + ":" + getSapConnection().getPassword()).getBytes()));
    }

    /**
     * Updates the CSRF token by using the HEAD operation on the selected SAP connection.
     */
    private void updateCSRFToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + userCredentials);
        headers.add("x-csrf-token", "fetch");
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Object> response = new RestTemplate().
                exchange(getSapConnection().getURLforCSRF(), HttpMethod.HEAD, request, Object.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            setSessionCookies(response.getHeaders().get("set-cookie"));
            setCSRFToken(response.getHeaders().get("x-csrf-token").get(0));
            System.out.println("web-service-call: x-csrf-token received and stored");
        } else {
            System.out.println("web-service-call: x-csrf-token couldn't received and stored");
        }
    }
}