package controller;

import model.TransportRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransportRequestConfigController {

    private final String TRANSPORTREQUESTFILE = File.separator + "transportrequests.xml";
    private final String USERFOLDERPATH = "." + File.separator + "resources" + File.separator;
    private final String REQUESTS = "transportrequests";

    private List<String> sapConnectionsList;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private String username;

    public TransportRequestConfigController(String username) {
        this.username = username;

        // build the XML builder and load the SAP connection config file
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
            this.sapConnectionsList = loadTransportRequestsList();
        } catch (ParserConfigurationException ex) {
            System.err.println("error: application config --> parser configuration failed");
        }
    }

    public List<String> getSapConnectionsList() {
        return sapConnectionsList;
    }

    public DocumentBuilderFactory getDocumentBuilderFactory() {
        return documentBuilderFactory;
    }

    public DocumentBuilder getDocumentBuilder() {
        return documentBuilder;
    }

    public String getUsername() {
        return username;
    }

    private List<String> loadTransportRequestsList() {
        // build the XML builder and load the SAP connection config file
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            System.err.println("error: application config --> parser configuration failed");
        }

        File xmlFile = new File(USERFOLDERPATH + getUsername() + TRANSPORTREQUESTFILE);
        List<String> results = new ArrayList<>();

        if (xmlFile.exists()) {
            try {
                Document document = getDocumentBuilder().parse(xmlFile);

                NodeList nodeList = document.getElementsByTagName(REQUESTS);

                // get the connections node and try to get its children
                Node connections = nodeList.item(0);

                if (connections != null) {
                    nodeList = connections.getChildNodes();
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        if (nodeList.item(i).getAttributes() != null) {
                            /*
                            if (nodeList.item(i).getAttributes().getNamedItem("url") != null) {
                                results.add(nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue());
                            }
                            */
                        }
                    }
                } else {
                    System.out.println("info: transport request controller --> no transport requests found");
                }

                return results;
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("success: transport request controller --> transport requests loaded --> " + results);

        return results;
    }

    public boolean createTransportRequest() {
        // TODO
        return false;
    }

    public boolean deleteTransportRequest(TransportRequest transportRequest) {
        // TODO
        return false;
    }

    public boolean updateTransportRequest(TransportRequest transportRequest) {
        // TODO
        return false;
    }

}
