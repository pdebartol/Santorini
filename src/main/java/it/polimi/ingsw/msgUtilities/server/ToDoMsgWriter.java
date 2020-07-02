package it.polimi.ingsw.msgUtilities.server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.List;

/**
 * This class contains all the methods useful to write an XML schema message which represents an indication of what to
 * do to a client from the server.
 * @author marcoDige
 */

public class ToDoMsgWriter {

    //attributes

    /**
     * This attribute represents the XML abstract document
     */

    private Document document;

    //constructor

    /**
     * This constructor initializes the document with the standard XML schema content in toDoMsg file (content in
     * resources folder).
     */

    public ToDoMsgWriter(){
        try {
            this.document = this.getDocument(this.getClass().getResourceAsStream("/xml/server/toDoMsg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //methods

    //support methods

    /**
     * This method allows to create a new tag into the XML schema with an attribute.
     * @param father is the father tag
     * @param tagName is the name of the new tag
     * @param textContent is the content of the new tag
     * @param attributeName is the name of the attribute
     * @param attributeValue is the value of the attribute
     * @return the tag's Node object
     */

    private Node appendTagWithAttribute(Node father, String tagName, String textContent, String attributeName, String attributeValue){
        Element tag = document.createElement(tagName);
        tag.setAttribute(attributeName,attributeValue);
        tag.setTextContent(textContent);
        father.appendChild(tag);
        return document.getElementsByTagName(tagName).item(0);
    }

    /**
     * This method allows to create a new tag into the XML schema.
     * @param father is the father tag
     * @param tagName is the name of the new tag
     * @param textContent is the content of the new tag
     * @return the tag's Node object
     */

    private Node appendTag(Node father, String tagName, String textContent){
        Element tag = document.createElement(tagName);
        tag.setTextContent(textContent);
        father.appendChild(tag);
        return document.getElementsByTagName(tagName).item(0);
    }

    /**
     * This method allows to create a new tag into the XML schema (empty tag).
     * @param father is the father tag
     * @param tagName is the name of the new tag
     * @return the tag's Node object
     */

    private Node appendTag(Node father, String tagName){
        Element tag = document.createElement(tagName);
        father.appendChild(tag);
        return document.getElementsByTagName(tagName).item(0);
    }

    /**
     * This element initializes the tag to write us inside.
     * @param tagName is the tag to initialize name
     * @return the tag's Node object
     */

    private Node initializeTagList(String tagName){
        return document.getElementsByTagName(tagName).item(0);
    }

    /**
     * This method creates the document object and parses file's path
     * @return the parsed xml file
     * @throws Exception error during xml parsing
     */

    private Document getDocument(InputStream stream) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(stream);
    }

    /**
     * This method writes an Action to do indication message
     * @param action is the action that the client has to do
     * @return the updated document
     */

    public Document toDoAction(String action){
        Node actionTag = initializeTagList("Action");
        actionTag.setTextContent(action);
        return document;
    }

    /**
     * This method writes a wait to do indication message with the reason for waiting and who is acting.
     * @param inActionPlayer is the player who is acting
     * @param waitFor is the reason for waiting
     * @return the updated document
     */

    public Document toDoWaitMsg(String inActionPlayer,String waitFor){
        toDoAction("wait");
        Node infoTag = initializeTagList("Info");

        appendTag(infoTag,"WaitFor",waitFor);
        appendTag(infoTag,"InActionPlayer",inActionPlayer);
        return document;
    }

    /**
     * This method writes a Turn indication message. It informs the client that he must perform the turn
     * he can start playing and what operation he can perform
     * @param possibleOperation is the operation he can perform
     * @return the updated document
     */

    public Document toDoTurn(String possibleOperation){
        toDoAction("yourTurn");
        Node infoTag = initializeTagList("Info");

        appendTag(infoTag,"possibleOperation",possibleOperation);
        return document;
    }

    /**
     * This method writes a message to inform the client who has to chose his god.
     * @param ids is the list of gods left to choose from
     * @return the updated document
     */

    public Document toDoChoseGod(List<Integer> ids){
        toDoAction("choseGod");
        Node infoTag = initializeTagList("Info");

        Node godsTag = appendTag(infoTag,"Gods");
        for(int i = 1; i <= ids.size(); ++i){
            appendTagWithAttribute(godsTag,"God",String.valueOf(ids.get(i-1)),"n",String.valueOf(i));
        }
        return document;
    }
}
