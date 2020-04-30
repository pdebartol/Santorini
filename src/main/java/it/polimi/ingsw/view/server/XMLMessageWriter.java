package it.polimi.ingsw.view.server;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLMessageWriter {

    //attributes

    private Document document;

    //constructors

    public XMLMessageWriter(String type) throws Exception {
        try{
            this.document = this.getDocument("src/main/resources/server/" + type);
        }
        catch (Exception e){
            System.out.println("Error during XML parsing.");
        }
    }

    //methods

    /**
     * This method creates the document object and parses file's path
     * @return the parsed xml file
     * @throws Exception error during xml parsing
     */

    private Document getDocument(String path) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(path);
    }
}
