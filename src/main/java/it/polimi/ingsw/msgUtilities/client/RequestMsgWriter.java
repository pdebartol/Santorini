package it.polimi.ingsw.msgUtilities.client;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;

public class RequestMsgWriter {

    //attributes

    private Document document;

    private static final int CHOOSE_STARTER_INDEX = 1;
    private static final int CREATE_GODS_INDEX = 2;
    private static final int CHOOSE_GOD_INDEX = 3;
    private static final int SET_WORKER_INDEX = 4;
    private static final int MOVE_INDEX = 5;
    private static final int BUILD_INDEX = 6;



    //constructor

    public RequestMsgWriter(){
        try {
            this.document = this.getDocument(this.getClass().getResourceAsStream("/xml/client/toSendRequest"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //methods

    //support methods

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
     * This method sets the standard values of a request message: Mode and Username.
     * @param user a string corresponding to the username
     * @param mod a string corresponding to the message's mode.
     */

    private void setStandardRequestValues(String user,String mod){
        Node mode = document.getElementsByTagName("Mode").item(0);
        Node username = document.getElementsByTagName("Username").item(0);

        mode.setTextContent(mod);
        username.setTextContent(user);
    }

    /**
     * This method initializes a tag list, we use it when we need to append a new request tag
     * @param tagName name of the element to initialize
     * @param modeIndex element's mode
     * @return the initialized node
     */

    private Node initializeTagList(String tagName, int modeIndex){
        Node tag = document.getElementsByTagName(tagName).item(modeIndex);
        return tag;
    }

    /**
     * This method appends a new tag to an element of the XML
     * @param father new tag's father
     * @param tagName new tag's name
     * @param textContent new tag's text content
     * @return the initialized node
     */
    private Node appendTag(Node father, String tagName, String textContent){
        Element tag = document.createElement(tagName);
        tag.setTextContent(textContent);
        father.appendChild(tag);
        return document.getElementsByTagName(tagName).item(0);
    }

    /**
     * This method appends a new tag and its attribute to an element of the XML
     * @param father new tag's father
     * @param tagName new tag's name
     * @param textContent new tag's text content
     * @param attributeName new tag's attribute name
     * @param attributeValue new tag's attribute value
     * @return the initialized node
     */

    private Node appendTagWithAttribute(Node father, String tagName, String textContent, String attributeName, String attributeValue){
        Element tag = document.createElement(tagName);
        tag.setAttribute(attributeName,attributeValue);
        tag.setTextContent(textContent);
        father.appendChild(tag);
        return document.getElementsByTagName(tagName).item(0);
    }


    /**
     * This method appends the worker's position to the XML (move action)
     * @param index starting index
     * @param workerGender worker's gender
     * @param xPosition worker's x position
     * @param yPosition worker's y position
     */

    private void appendWorkerPosition(int index, String workerGender, String xPosition, String yPosition){
        Node position = document.getElementsByTagName("Position").item(index);
        appendTag(position, "WorkerGender", workerGender);
        appendTag(position,"xPosition",xPosition);
        appendTag(position,"yPosition",yPosition);
    }

    /**
     * This method appends the worker's position and its level to the XML (build action)
     * @param index starting index
     * @param workerGender worker's gender
     * @param xPosition worker's x position
     * @param yPosition worker's y position
     * @param level construction level
     */

    private void appendWorkerPositionAndLevel(int index, String workerGender, String xPosition, String yPosition,int level){
        Node position = document.getElementsByTagName("Position").item(index);
        appendTag(position, "WorkerGender", workerGender);
        appendTag(position,"xPosition",xPosition);
        appendTag(position,"yPosition",yPosition);
        appendTag(position,"Level",String.valueOf(level));

    }

    //action methods

    /**
     * This method prepares the XML for a login request
     * @param user username
     * @return the XML Document
     */

    public Document loginRequest(String user){
        setStandardRequestValues(user,"login");
        return document;
    }

    /**
     * This method prepares the XML for a startGame request
     * @param user username
     * @return the XML Document
     */

    public Document startGameRequest(String user){
        setStandardRequestValues(user,"startGame");
        return document;
    }

    /**
     * This method prepares the XML for a chooseStartingPlayer request
     * @param user username
     * @param chosenPlayer starting player
     * @return the XML Document
     */

    public Document chooseStartingPlayerRequest(String user, String chosenPlayer){
        setStandardRequestValues(user,"choseStartingPlayer");
        Node updateTag = initializeTagList("Request", CHOOSE_STARTER_INDEX);
        appendTag(updateTag,"PlayerChosen",chosenPlayer);
        return document;
    }


    /**
     * This method prepares the XML for a createGodsRequest
     * @param user username
     * @param godIds ids of the chosen gods
     * @return the XML Document
     */

    public Document createGodsRequest(String user, ArrayList<Integer> godIds){
        setStandardRequestValues(user,"createGods");
        Node updateTag = initializeTagList("Request", CREATE_GODS_INDEX);
        Node gods = appendTag(updateTag,"Gods","");
        for (int i=0;i < godIds.size(); i++){
            appendTagWithAttribute(gods,"God",godIds.get(i).toString(),"n",String.valueOf(i));
        }
        return document;
    }


    /**
     * This method prepares the XML for a chooseGod request
     * @param user username
     * @param godId id of the chosen god
     * @return the XML Document
     */

    public Document chooseGodRequest(String user, Integer godId){
        setStandardRequestValues(user,"choseGod");
        Node updateTag = initializeTagList("Request", CHOOSE_GOD_INDEX);
        appendTag(updateTag,"God",godId.toString());
        return document;
    }

    /**
     * This method prepares the XML for a setWorkerOnBoard request
     * @param user username
     * @param workerGender worker's gender
     * @param xPosition worker's x position
     * @param yPosition worker's y position
     * @return the XML Document
     */

    public Document setWorkerOnBoardRequest(String user, String workerGender, Integer xPosition, Integer yPosition){
        setStandardRequestValues(user,"setWorkerOnBoard");
        Node updateTag = initializeTagList("Request", SET_WORKER_INDEX);
        appendTag(updateTag,"Position","");

        appendWorkerPosition(0, workerGender,xPosition.toString(),yPosition.toString());
        return document;
    }

    /**
     * This method prepares the XML for a move request
     * @param user username
     * @param workerGender worker's gender
     * @param xPosition worker's x position
     * @param yPosition worker's y position
     * @return the XML Document
     */

    public Document moveRequest(String user, String workerGender, Integer xPosition, Integer yPosition){
        setStandardRequestValues(user,"move");
        Node updateTag = initializeTagList("Request", MOVE_INDEX);
        appendTag(updateTag,"Position","");
        appendWorkerPosition(0, workerGender,xPosition.toString(),yPosition.toString());
        return document;
    }

    /**
     * This method prepares the XML for a build request
     * @param user username
     * @param workerGender worker's gender
     * @param xPosition worker's x position
     * @param yPosition worker's y position
     * @param level construction level
     * @return  the XML Document
     */

    public Document buildRequest(String user, String workerGender, Integer xPosition, Integer yPosition, int level){
        setStandardRequestValues(user,"build");
        Node updateTag = initializeTagList("Request", BUILD_INDEX);
        Node position = appendTag(updateTag,"Position","");

        appendWorkerPositionAndLevel(0, workerGender,xPosition.toString(),yPosition.toString(),level);
        return document;
    }

    /**
     * This method prepares the XML for an endOfTurn request
     * @param user username
     * @return  the XML Document
     */
    public Document endOfTurnRequest(String user){
        setStandardRequestValues(user,"endOfTurn");

        return document;
    }

    // useless

    public Document endRequest(String user){
        setStandardRequestValues(user,"end");

        return document;
    }

    /**
     * This method prepares the document for a ping message
     * @return  the XML Document
     */

    public Document pingMsg(){
        setStandardRequestValues("null","ping");

        return document;
    }
}
