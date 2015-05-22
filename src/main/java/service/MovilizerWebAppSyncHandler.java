package service;

import model.SAPConnection;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class MovilizerWebAppSyncHandler {

    private String csrfToken;
    private String userCredentials;
    private SAPConnection sapConnection;

    public MovilizerWebAppSyncHandler(SAPConnection sapConnection) {
        this.sapConnection = sapConnection;
    }

    public void putWebApp(String webAppId) {
        String url = "http://router2.movilizer.com:8105/movilizer/webappsync/webapps/{webappid}/versions";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + userCredentials);
        headers.add("x-csrf-token", csrfToken);

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Object> response = new RestTemplate().exchange(url, HttpMethod.GET, request, Object.class, webAppId);

        System.out.println(response);
    }

    private void authenticateUser() {
        userCredentials = new String(Base64.encodeBase64((sapConnection.getUsername() + ":" + sapConnection.getPassword()).getBytes()));
    }

    private String getCSRFToken() {
        if (csrfToken == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + userCredentials);
            headers.add("x-csrf-token", "fetch");
            HttpEntity<String> request = new HttpEntity<String>(headers);
            ResponseEntity<Object> response = new RestTemplate().exchange(sapConnection.getURLforCSRF(), HttpMethod.HEAD, request, Object.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                csrfToken = response.getHeaders().get("x-csrf-token").toString();
                System.out.println(response.getHeaders());
                System.out.println("web-service-call: x-csrf-token received and stored");
            } else {
                System.out.println("web-service-call: x-csrf-token couldn't received and stored");
            }
        }

        return csrfToken;
    }
}