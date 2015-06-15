package controller;

import model.SAPConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SAPConnectionConfigController {

    private final String CONNECTIONFILE = File.separator + "connections.xml";
    private final String USERFOLDERPATH = "." + File.separator + "resources" + File.separator;
    private final String CONNECTIONS = "connections";

    private List<String> sapConnectionsList;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private String username;

    /**
     * Standard constructor to create the SAP connection controller.
     *
     * @param username The username for the SAP connection controller
     */
    public SAPConnectionConfigController(String username) {
        this.username = username;

        // build the XML builder and load the SAP connection config file
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
            this.sapConnectionsList = loadSAPConnectionList();
        } catch (ParserConfigurationException ex) {
            System.err.println("error: application config --> parser configuration failed");
        }
    }

    public List<String> getSapConnectionsList() {
        return sapConnectionsList;
    }

    public void setSapConnectionsList(List<String> sapConnectionsList) {
        this.sapConnectionsList = sapConnectionsList;
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

    /**
     * Loads the stored SAP connections from the related XML config file.
     *
     * @return The list of stored SAP connections as a string array
     */
    private List<String> loadSAPConnectionList() {
        // build the XML builder and load the SAP connection config file
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            System.err.println("error: application config --> parser configuration failed");
        }

        File xmlFile = new File(USERFOLDERPATH + getUsername() + CONNECTIONFILE);
        List<String> results = new ArrayList<>();

        if (xmlFile.exists()) {
            try {
                Document document = getDocumentBuilder().parse(xmlFile);

                NodeList nodeList = document.getElementsByTagName(CONNECTIONS);

                // get the connections node and try to get its children
                Node connections = nodeList.item(0);

                if (connections != null) {
                    nodeList = connections.getChildNodes();
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        if (nodeList.item(i).getAttributes() != null) {
                            if (nodeList.item(i).getAttributes().getNamedItem("url") != null) {
                                results.add(nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue());
                            }
                        }
                    }
                } else {
                    System.out.println("info: sap connections controller --> no sap connections found");
                }

                return results;
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("success: sap connections controller --> sap connections loaded --> " + results);

        return results;
    }

    /**
     * Creates a new SAP connection in the SAP conenction config file.
     *
     * @param sapConnection The SAP connection which should be created.
     * @return Return true if the SAP connection was created successfully else false
     */
    public boolean createSAPConnection(SAPConnection sapConnection) {
        if (!doesSAPConnectionExists(sapConnection)) {
            File xmlFile = new File(USERFOLDERPATH + getUsername() + CONNECTIONFILE);

            if (xmlFile.exists()) {
                try {
                    Document document = getDocumentBuilder().parse(xmlFile);

                    // get the connections node and try to get its children
                    NodeList nodeList = document.getElementsByTagName(CONNECTIONS);
                    Node connections = nodeList.item(0);

                    // check if the connections node was received and try to add the new connection
                    if (connections != null) {
                        Element connection = document.createElement("connection");
                        connection.setAttribute("url", sapConnection.getUrl());
                        connection.setAttribute("username", sapConnection.getUsername());
                        connection.setAttribute("password", sapConnection.getPassword());
                        connections.appendChild(connection);

                        // write content into XML file
                        try {
                            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            DOMSource source = new DOMSource(document);
                            StreamResult result = new StreamResult(xmlFile);

                            transformer.transform(source, result);


                            System.out.println("success: sap connections controller --> new sap connection created for user " + getUsername());
                            return true;
                        } catch (TransformerConfigurationException e) {
                            e.printStackTrace();
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.err.println("error: sap connections controller --> new sap connection created couldn't be created for user " + getUsername());
        return false;
    }

    /**
     * Updates the SAP connection list
     */
    private void updateSAPConnectionList() {
        setSapConnectionsList(loadSAPConnectionList());
    }

    /**
     * Updates the combo box which contains the SAP connections.
     *
     * @param availableSAPConnectionsList The combo box with the SAp connections
     */
    public void updateComboBox(JComboBox<String> availableSAPConnectionsList) {
        updateSAPConnectionList();

        // clear the current combo box
        availableSAPConnectionsList.removeAllItems();

        // update the combo box
        for (String entry : getSapConnectionsList()) {
            availableSAPConnectionsList.addItem(entry);
        }
    }

    /**
     * Checks if the SAP connection already exists.
     *
     * @param sapConnection The SAP connection which will be checked
     * @return True if the SAP connection already exists else false
     */
    private boolean doesSAPConnectionExists(SAPConnection sapConnection) {
        File xmlFile = new File(USERFOLDERPATH + getUsername() + CONNECTIONFILE);

        if (xmlFile.exists()) {
            try {
                Document document = getDocumentBuilder().parse(xmlFile);

                // get the connections node and try to get its children
                NodeList nodeList = document.getElementsByTagName(CONNECTIONS);
                Node connections = nodeList.item(0);

                // check if the connections node was received and try to add the new connection
                if (connections != null) {
                    nodeList = connections.getChildNodes();
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        if (nodeList.item(i).getAttributes() != null) {
                            if (nodeList.item(i).getAttributes().getNamedItem("url") != null) {
                                if (nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue().equals(sapConnection.getUrl()))
                                    return true;
                            }
                        }
                    }
                }
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public SAPConnection getSAPConnection(String url) {
        File xmlFile = new File(USERFOLDERPATH + getUsername() + CONNECTIONFILE);

        if (xmlFile.exists()) {
            try {
                Document document = getDocumentBuilder().parse(xmlFile);

                // get the connections node and try to get its children
                NodeList nodeList = document.getElementsByTagName(CONNECTIONS);
                Node connections = nodeList.item(0);

                // check if the connections node was received and try to add the new connection
                if (connections != null) {
                    nodeList = connections.getChildNodes();
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        if (nodeList.item(i).getAttributes() != null) {
                            if (nodeList.item(i).getAttributes().getNamedItem("url") != null) {
                                if (nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue().equals(url)) {
                                    return new SAPConnection(nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue(),
                                            nodeList.item(i).getAttributes().getNamedItem("username").getNodeValue(),
                                            nodeList.item(i).getAttributes().getNamedItem("password").getNodeValue());
                                }

                            }
                        }
                    }
                }
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Updates an existing SAP connection.
     *
     * @param sapConnection The SAP connection with the new values.
     * @return Return true if an existing SAP conenction was updated else false;
     */
    public boolean updateSAPConnection(SAPConnection sapConnection) {
        File xmlFile = new File(USERFOLDERPATH + getUsername() + CONNECTIONFILE);

        if (xmlFile.exists()) {
            try {
                Document document = getDocumentBuilder().parse(xmlFile);

                // get the connections node and try to get its children
                NodeList nodeList = document.getElementsByTagName(CONNECTIONS);
                Node connections = nodeList.item(0);

                // check if the connections node was received and try to add the new connection
                if (connections != null) {
                    nodeList = connections.getChildNodes();
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        if (nodeList.item(i).getAttributes() != null) {
                            if (nodeList.item(i).getAttributes().getNamedItem("url") != null) {
                                if (nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue().equals(sapConnection.getUrl())) {
                                    nodeList.item(i).getAttributes().getNamedItem("url").setNodeValue(sapConnection.getUrl());
                                    nodeList.item(i).getAttributes().getNamedItem("username").setNodeValue(sapConnection.getUsername());
                                    nodeList.item(i).getAttributes().getNamedItem("password").setNodeValue(sapConnection.getPassword());

                                    // write content into XML file
                                    try {
                                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                                        Transformer transformer = transformerFactory.newTransformer();
                                        DOMSource source = new DOMSource(document);
                                        StreamResult result = new StreamResult(xmlFile);

                                        transformer.transform(source, result);

                                        System.out.println("success: sap connections controller --> sap connection was updated for user " + getUsername());
                                        return true;
                                    } catch (TransformerConfigurationException e) {
                                        e.printStackTrace();
                                    } catch (TransformerException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }
                }
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Deletes an existing SAP connection
     *
     * @param sapConnection The SAP connection which should be deleted
     * @return Return true if the SAP connection was deleted successfully else false
     */
    public boolean deleteSAPConnection(SAPConnection sapConnection) {
        File xmlFile = new File(USERFOLDERPATH + getUsername() + CONNECTIONFILE);

        if (xmlFile.exists()) {
            try {
                Document document = getDocumentBuilder().parse(xmlFile);

                // get the connections node and try to get its children
                NodeList nodeList = document.getElementsByTagName(CONNECTIONS);
                Node connections = nodeList.item(0);

                // check if the connections node was received and try to add the new connection
                if (connections != null) {
                    nodeList = connections.getChildNodes();
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        if (nodeList.item(i).getAttributes() != null) {
                            if (nodeList.item(i).getAttributes().getNamedItem("url") != null) {
                                if (nodeList.item(i).getAttributes().getNamedItem("url").getNodeValue().equals(sapConnection.getUrl())) {
                                    Node node = nodeList.item(i);

                                    // TODO remove child node

                                    node.getParentNode().removeChild(node);
                                }

                                // write content into XML file
                                try {
                                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                                    Transformer transformer = transformerFactory.newTransformer();
                                    DOMSource source = new DOMSource(document);
                                    StreamResult result = new StreamResult(xmlFile);

                                    transformer.transform(source, result);

                                    System.out.println("success: sap connections controller --> sap connection was updated for user " + getUsername());
                                    return true;
                                } catch (TransformerConfigurationException e) {
                                    e.printStackTrace();
                                } catch (TransformerException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}