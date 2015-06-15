package service;

import model.SAPConnection;
import model.WebAppVersion;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

/**
 * Implementation of the Movilizer WebApp Webservice.
 */
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
     * @return Return true if the WebApp was successfully created else false
     */
    public boolean putWebApp(File webApp) {
        String url = sapConnection.getUrl() + MOVILIZERWEBAPPWEBSERVICE;
        String filename = webApp.getName().split(".zip")[0];

        // authenticate the user to prepare the webservice call
        authenticateUser();

        // update session and parameters
        updateCSRFToken();

        // build HTTP request with header and content
        HttpHeaders httpHeaders = generateHeaders("application/octet-stream");

        try {
            byte[] byteArray = Files.readAllBytes(webApp.toPath());

            HttpEntity request = new HttpEntity<>(byteArray, httpHeaders);

            try {
                // send WebApp to SAP System
                ResponseEntity<String> response = new RestTemplate().
                        exchange(url, HttpMethod.PUT, request, String.class, filename);

                System.out.println(response);
                System.out.println("put response --> WebApp was created");
                return true;

            } catch (org.springframework.web.client.HttpClientErrorException ex) {
                System.out.println("put response --> WebApp already exists");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Returns the requested WebApp from the SAP system.
     *
     * @param project The requested WebApp
     */
    public byte[] getWebApp(String project) {
        String url = sapConnection.getUrl() + MOVILIZERWEBAPPWEBSERVICE;

        // authenticate the user to prepare the webservice call
        authenticateUser();

        // update session and parameters
        updateCSRFToken();

        // build HTTP request with header and content
        HttpHeaders httpHeaders = generateHeaders("text/plain");
        HttpEntity request = new HttpEntity<>(httpHeaders);

        // send WebApp to SAP System
        ResponseEntity<byte[]> response = new RestTemplate().
                exchange(url, HttpMethod.GET, request, byte[].class, project);

        return response.getBody();
    }

    /**
     * Gets a list of available WebApps in the selected SAP system.
     *
     * @return The list of available WebApps
     */
    public List<WebAppVersion> getWebAppsList() {
        List<WebAppVersion> webAppVersions = new ArrayList<>();
        String url = sapConnection.getUrl() + MOVILIZERWEBAPPSLISTWEBSERVICE;

        // authenticate the user to prepare the webservice call
        authenticateUser();

        // update session and parameters
        updateCSRFToken();

        // build HTTP request with header and content
        HttpHeaders httpHeaders = generateHeaders("text/plain");
        HttpEntity request = new HttpEntity<>(httpHeaders);

        // send WebApp to SAP System
        ResponseEntity<String> response = new RestTemplate().
                exchange(url, HttpMethod.GET, request, String.class);

        System.out.println(request.getBody());

        // parse the JSON result
        String body = response.getBody();

        try {
            JSONArray jsonArray = new JSONArray(body);
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                // create a new WebApp version
                webAppVersions.add(new WebAppVersion(
                        jsonObject.getString("mandt").trim(),
                        jsonObject.getString("id").trim(),
                        jsonObject.getString("version").trim(),
                        jsonObject.getString("name").trim(),
                        jsonObject.getString("description").trim(),
                        jsonObject.getString("hashcode").trim(),
                        jsonObject.getString("timestamp").trim()
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return webAppVersions;
    }

    /**
     * Deletes the project from the current selected SAP system.
     *
     * @param project The project which should be deleted
     */
    public void deleteWebApp(String project) {
        String url = sapConnection.getUrl() + MOVILIZERWEBAPPWEBSERVICE;

        // authenticate the user to prepare the webservice call
        authenticateUser();

        // update session and parameters
        updateCSRFToken();

        // build HTTP request with header and content
        HttpHeaders httpHeaders = generateHeaders("text/plain");
        HttpEntity request = new HttpEntity<>(httpHeaders);

        // send WebApp to SAP System
        ResponseEntity<String> response = new RestTemplate().
                exchange(url, HttpMethod.DELETE, request, String.class, project);

        System.out.println(response);
    }

    /**
     * Generates the HTTP header for the webservice request.
     *
     * @return The HTTP header object
     */
    private HttpHeaders generateHeaders(String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + userCredentials);
        headers.add("x-csrf-token", getCSRFToken());
        headers.add("Content-Type", contentType);

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

        if (response.getStatusCode() == OK) {
            setSessionCookies(response.getHeaders().get("set-cookie"));
            setCSRFToken(response.getHeaders().get("x-csrf-token").get(0));
            System.out.println("web-service-call: x-csrf-token received and stored");
        } else {
            System.out.println("web-service-call: x-csrf-token couldn't received and stored");
        }
    }
}