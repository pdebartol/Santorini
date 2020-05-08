package it.polimi.ingsw.msgUtilities.server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ToDoMsgWriter {

    //attributes

    private Document document;

    //constructor

    public ToDoMsgWriter(){
        try {
            this.document = this.getDocument("src/main/resources/xml/server/toDoMsg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //methods

    //support methods


    private Node appendTag(Node father, String tagName, String textContent){
        Element tag = document.createElement(tagName);
        tag.setTextContent(textContent);
        father.appendChild(tag);
        return document.getElementsByTagName(tagName).item(0);
    }

    private Node appendTag(Node father, String tagName){
        Element tag = document.createElement(tagName);
        father.appendChild(tag);
        return document.getElementsByTagName(tagName).item(0);
    }

    private Node initializeTagList(String tagName){
        Node tag = document.getElementsByTagName(tagName).item(0);
        return tag;
    }

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
