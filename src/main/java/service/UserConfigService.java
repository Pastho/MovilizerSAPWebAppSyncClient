package service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.Charset;

public class UserConfigService {

    private final String CONNECTIONFILE = "/connections.xml";
    private final String TRANSPORTREQUESTFILE = "/transportrequests.xml";
    private final String USERFOLDERPATH = "./resources/";
    private final String CONNECTIONS = "connections";

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    public UserConfigService() {
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            System.err.println("error: application config -> parser configuration failed");
        }
    }

    public DocumentBuilder getDocumentBuilder() {
        return documentBuilder;
    }

    /**
     * Registers a new user to the application config file
     *
     * @param username The user which should be added to the application config file
     */
    public void registerNewUser(String username, String password) {
        // create new folder for user
        File userFolder = new File(USERFOLDERPATH + username);
        userFolder.mkdir();

        // create a new connection file in user folder
        buildConnectionConfigFile(username);

        // create a new transport file in user folder
        buildTransportConfigFile(username);
    }

    private void buildConnectionConfigFile(String username) {
        File xmlFile = new File(USERFOLDERPATH + username + CONNECTIONFILE);

        // check if XML file not exists and create a new file with structure if necessary
        if (!xmlFile.exists()) {
            try {
                xmlFile.createNewFile();

                // create new application config structure
                Document document = getDocumentBuilder().newDocument();

                // create the root element
                Element rootElement = document.createElement(CONNECTIONS);
                document.appendChild(rootElement);

                // write content into XML file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(xmlFile);

                /*
                result.setOutputStream(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        // TODO decrypt the data
                    }
                });
                */

                transformer.transform(source, result);

                System.out.println("connection config: file saved for user " + username);

            } catch (IOException ex) {
                System.err.println("error: application config -> connection config file couldn't be generated");
            } catch (TransformerException ex) {
                System.err.println("error: application config -> couldn't create transformer for connection config file");
            }
        }
    }

    private void buildTransportConfigFile(String username) {
        File xmlFile = new File(USERFOLDERPATH + username + TRANSPORTREQUESTFILE);

        // check if XML file not exists and create a new file with structure if necessary
        if (!xmlFile.exists()) {
            try {
                xmlFile.createNewFile();

                // create new application config structure
                Document document = getDocumentBuilder().newDocument();

                // create the root element
                Element rootElement = document.createElement("transportrequests");
                document.appendChild(rootElement);

                // write content into XML file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(xmlFile);

                transformer.transform(source, result);

                System.out.println("transportrequest config: file saved for user " + username);

            } catch (IOException ex) {
                System.err.println("error: application config -> transportrequest config file couldn't be generated");
            } catch (TransformerException ex) {
                System.err.println("error: application config -> couldn't create transformer for transportrequest config file");
            }
        }
    }

    /**
     * Search in the user list if the username already exists.
     *
     * @param username The username which is uses as the search parameter
     * @return Return true if the user was found and false if it wasn't found
     */
    public boolean doesUserExists(String username) {
        return new File(USERFOLDERPATH + username).exists();
    }
}