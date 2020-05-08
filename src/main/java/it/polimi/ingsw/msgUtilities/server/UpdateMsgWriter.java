package it.polimi.ingsw.msgUtilities.server;

import it.polimi.ingsw.model.enums.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;

public class UpdateMsgWriter {
    
    //attributes

    private Document document;

    //constructor

    public UpdateMsgWriter(){
        try {
            this.document = this.getDocument(this.getClass().getResourceAsStream("/xml/server/updateMsg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //methods

    //support methods

    private void setStandardUpdateValues(String user,String mod){
        Node mode = document.getElementsByTagName("Mode").item(0);
        Node username = document.getElementsByTagName("Author").item(0);

        mode.setTextContent(mod);
        username.setTextContent(user);
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

    //action methods

    public Document loginUpdate(String user, Color c){
        setStandardUpdateValues(user,"newPlayer");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"Username",user);
        appendTag(updateTag,"Color",Color.labelOfEnum(c));
        return document;
    }

    public Document startGameUpdate(String user){
        setStandardUpdateValues(user,"startGame");

        initializeTagList("Update");
        initializeTagList("Errors");
        return document;
    }

    public Document createGodsUpdate(String user, ArrayList<Integer> ids){
        setStandardUpdateValues(user,"createGods");
        Node updateTag = initializeTagList("Update");

        for(int id : ids){
            appendTag(updateTag, String.valueOf(id));
        }
        return document;
    }

    public Document choseGodUpdate(String user, int godId){
        setStandardUpdateValues(user,"choseGod");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"godId",String.valueOf(godId));
        return document;

    }

    public Document choseStartingPlayerUpdate(String user, String playerChosen){
        setStandardUpdateValues(user,"choseStartingPlayer");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"StartingPlayer",playerChosen);
        return document;
    }

    public Document setupOnBoardUpdate(String user, String workerGender, int x, int y){
        setStandardUpdateValues(user,"setWorkerOnBoard");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"WorkerGender",workerGender);
        appendTag(updateTag,"xPosition",String.valueOf(x));
        appendTag(updateTag,"yPosition",String.valueOf(y));
        return document;
    }
}
