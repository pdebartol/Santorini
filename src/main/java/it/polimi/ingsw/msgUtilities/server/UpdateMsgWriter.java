package it.polimi.ingsw.msgUtilities.server;

import it.polimi.ingsw.model.enums.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This class contains all the methods useful to write an XML schema message which represents a update to all clients
 * from the server after an accepted request from one of these clients.
 * @author marcoDige
 */

public class UpdateMsgWriter {
    
    //attributes

    /**
     * This attribute represents the XML abstract document
     */

    private Document document;

    //constructor

    /**
     * This constructor initializes the document with the standard XML schema content in updateMsg file (content in
     * resources folder).
     */

    public UpdateMsgWriter(){
        try {
            this.document = this.getDocument(this.getClass().getResourceAsStream("/xml/server/updateMsg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //methods

    //support methods

    /**
     * This method writes under the two standard tags of the update message.
     * @param user is the user who made the request
     * @param mod is the request mode (login, startGame...)
     * @return the document updated
     */

    public Document setStandardUpdateValues(String user, String mod){
        Node mode = document.getElementsByTagName("Mode").item(0);
        Node username = document.getElementsByTagName("Author").item(0);

        mode.setTextContent(mod);
        username.setTextContent(user);

        return document;
    }

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
     * This method allows to create a new tag (list element) into the XML schema (empty tag).
     * @param father is the father tag
     * @param tagName is the name of the new tag
     * @return the tag's Node object
     */

    private Node appendTag(int index, Node father,String tagName){
        Element tag = document.createElement(tagName);
        father.appendChild(tag);
        return document.getElementsByTagName(tagName).item(index);
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

    //action methods

    /**
     * This method provides to format an update after a login request. It sets the standard values and provides to
     * report new player color and username.
     * @param user is the new player username
     * @param c is the color assigned to the new player
     * @return the updated document
     */

    public Document loginUpdate(String user, Color c){
        setStandardUpdateValues(user,"newPlayer");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"Username",user);
        appendTag(updateTag,"Color",Color.labelOfEnum(c));
        return document;
    }

    /**
     * This method provides to format an update after a startGame request. It only sets standard values.
     * @param user is the user who made the request.
     * @return the updated document
     */

    public Document startGameUpdate(String user){
        setStandardUpdateValues(user,"startGame");

        initializeTagList("Update");
        return document;
    }

    /**
     * This method provides to format an update after a createGods request. It sets standard values
     * and provides to report the god's id list generated.
     * @param user is the user who made the request
     * @param ids is the god's id list
     * @return the updated document
     */

    public Document createGodsUpdate(String user, ArrayList<Integer> ids){
        setStandardUpdateValues(user,"createGods");
        Node updateTag = initializeTagList("Update");
        Node godsTag = appendTag(updateTag,"Gods");

        for(int i = 0; i < ids.size(); i++){
            appendTagWithAttribute(godsTag,"God",String.valueOf(ids.get(i)),"n",String.valueOf(i));
        }
        return document;
    }

    /**
     * This method provides to format an update after a choseGods request. It sets standard values
     * and provides to report the god chosen id.
     * @param user is the user who made the request
     * @param godId is the chosen god id
     * @return the updated document
     */

    public Document choseGodUpdate(String user, int godId){
        setStandardUpdateValues(user,"choseGod");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"godId",String.valueOf(godId));
        return document;

    }

    /**
     * This method provides to format an update after a choseStartingPlayer request. It sets standard values
     * and provides to report the player chosen to start the match.
     * @param user is the user who made the request
     * @param playerChosen is the player chosen to start the match
     * @return the updated document
     */

    public Document choseStartingPlayerUpdate(String user, String playerChosen){
        setStandardUpdateValues(user,"choseStartingPlayer");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"StartingPlayer",playerChosen);
        return document;
    }

    /**
     * This method provides to format an update after a setupOnBoard request. It sets standard values
     * and provides to report the worker gender and the coordinates where user placed his worker.
     * @param user is the user who made the request
     * @param workerGender is the worker user request to place on board gender
     * @param x is the x coordinate where user placed his worker
     * @param y is the y coordinate where user placed his worker
     * @return the updated document
     */

    public Document setupOnBoardUpdate(String user, String workerGender, int x, int y){
        setStandardUpdateValues(user,"setWorkerOnBoard");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"WorkerGender",workerGender);
        appendTag(updateTag,"xPosition",String.valueOf(x));
        appendTag(updateTag,"yPosition",String.valueOf(y));
        return document;
    }

    /**
     * This method provides to format an update after a move request. It sets only the updates after
     * this move.
     * @param index is the progressive number of the update that move generated
     * @param startX is the x coordinate where worker was before he moves
     * @param startY is the y coordinate where worker was before he moves
     * @param x is the x coordinate where user placed his worker
     * @param y is the y coordinate where user placed his worker
     * @return the updated document
     */

    public Document moveUpdate(int index, int startX, int startY, int x, int y){
        Node updateTag = initializeTagList("Update");

        Node position = appendTag(index, updateTag,"Position");
        appendTag(position,"startXPosition",String.valueOf(startX));
        appendTag(position,"startYPosition",String.valueOf(startY));
        appendTag(position,"xPosition",String.valueOf(x));
        appendTag(position,"yPosition",String.valueOf(y));
        return document;
    }

    /**
     * This method provides to format an update after a build request. It sets only the updates after
     * this built.
     * @param index is the progressive number of the update that move generated
     * @param startX is the x coordinate where worker is when he builds
     * @param startY is the y coordinate where worker is when he builds
     * @param x is the x coordinate where user placed a new block
     * @param y is the y coordinate where user placed a new block
     * @param level is the level of the new block
     * @return the updated document
     */

    public Document buildUpdate(int index, int startX, int startY, int x, int y, int level){
        Node updateTag = initializeTagList("Update");

        Node height = appendTag(index, updateTag,"Height");
        appendTag(height,"startXPosition",String.valueOf(startX));
        appendTag(height,"startYPosition",String.valueOf(startY));
        appendTag(height,"xPosition",String.valueOf(x));
        appendTag(height,"yPosition",String.valueOf(y));
        appendTag(height,"Level",String.valueOf(level));
        return document;
    }

    /**
     * This method is triggered when the player in turn's god is Medusa. This method provides to write updates after he
     * finished his turn (if workers on board have been removed).
     * @param index is the progressive number of the update that Medusa's power generated
     * @param startX is the x position where worker removed was
     * @param startY is the y position where worker removed was
     * @param level is the new block built instead of the worker removed
     * @return the updated document
     */

    public Document endOfTurnRemoveAndBuildUpdate(int index, int startX, int startY,int level){
        Node updateTag = initializeTagList("Update");

        Node removeAndBuild = appendTag(index, updateTag,"RemoveAndBuild");
        appendTag(removeAndBuild,"startXPosition",String.valueOf(startX));
        appendTag(removeAndBuild,"startYPosition",String.valueOf(startY));
        appendTag(removeAndBuild,"Level",String.valueOf(level));
        return document;
    }

    /**
     * This method writes an extraUpdate message, it sets only the mode.
     * @param mode is the extraUpdate mode
     * @return the updated document
     */

    public Document extraUpdate(String mode){
        setStandardUpdateValues("null",mode);
        return document;
    }

    /**
     * This method writes a Win or Lose update message when the match is a 2 player match.
     * @param winner is the player who won
     * @param mode indicate if the client who receive this message is the winner or the loser
     * @return the updated document
     */

    public Document winLoseUpdate(String winner,String mode){
        setStandardUpdateValues(winner,mode);
        return document;
    }

    /**
     * This method writes a lose update when a player loses in a 3 players match.
     * @param loser is the player who loses
     * @param mode is the message mode
     * @return the updated document
     */

    public Document loseUpdate(String loser, String mode){
        setStandardUpdateValues(loser,mode);
        return document;
    }
}
