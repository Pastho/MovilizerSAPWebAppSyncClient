package service;

import model.SAPConnection;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class MovilizerWebAppSyncHandler {

    private final String MOVILIZERWEBAPPWEBSERVICE = "/movilizer/webappsync/webapps/{webappid}";

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
        updateCSRFToken(url, webApp.getName());

        // build HTTP request with header and content
        HttpHeaders httpHeaders = generateHeaders();
        //headers.set
        MultiValueMap<String, Object> content = new LinkedMultiValueMap<>();
        try {
            content.add("file", new ByteArrayResource(Files.readAllBytes(webApp.toPath())));
            content.add("filename", webApp.getName());
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity request = new HttpEntity<>(content, httpHeaders);

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
    private void updateCSRFToken(String url, String parameter) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + userCredentials);
        headers.add("x-csrf-token", "fetch");
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Object> response = new RestTemplate().
                exchange(url, HttpMethod.GET, request, Object.class, parameter);

        if (response.getStatusCode() == HttpStatus.OK) {
            setSessionCookies(response.getHeaders().get("set-cookie"));
            setCSRFToken(response.getHeaders().get("x-csrf-token").get(0));
            System.out.println("web-service-call: x-csrf-token = " + getCSRFToken());
            System.out.println("web-service-call: x-csrf-token received and stored");
        } else {
            System.out.println("web-service-call: x-csrf-token couldn't received and stored");
        }
    }
}