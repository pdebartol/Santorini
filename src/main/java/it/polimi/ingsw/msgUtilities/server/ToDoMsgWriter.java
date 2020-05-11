package it.polimi.ingsw.msgUtilities.server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.List;

public class ToDoMsgWriter {

    //attributes

    private Document document;

    //constructor

    public ToDoMsgWriter(){
        try {
            this.document = this.getDocument(this.getClass().getResourceAsStream("/xml/server/toDoMsg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //methods

    //support methods

    private Node appendTagWithAttribute(Node father, String tagName, String textContent, String attributeName, String attributeValue){
        Element tag = document.createElement(tagName);
        tag.setAttribute(attributeName,attributeValue);
        tag.setTextContent(textContent);
        father.appendChild(tag);
        return document.getElementsByTagName(tagName).item(0);
    }

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

    private Document getDocument(InputStream stream) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(stream);
    }

    public Document toDoAction(String action){
        Node actionTag = initializeTagList("Action");
        actionTag.setTextContent(action);
        return document;
    }

    public Document toDoWaitMsg(String inActionPlayer,String waitFor){
        toDoAction("wait");
        Node infoTag = initializeTagList("Info");

        appendTag(infoTag,"WaitFor",waitFor);
        appendTag(infoTag,"InActionPlayer",inActionPlayer);
        return document;
    }

    public Document toDoTurn(String possibleOperation){
        toDoAction("yourTurn");
        Node infoTag = initializeTagList("Info");

        appendTag(infoTag,"possibleOperation",possibleOperation);
        return document;
    }

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
