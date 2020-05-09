package it.polimi.ingsw.msgUtilities.client;

import it.polimi.ingsw.model.enums.Color;
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

    private static  final int LOGIN_INDEX = 0;
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


    private void setStandardRequestValues(String user,String mod){
        Node mode = document.getElementsByTagName("Mode").item(0);
        Node username = document.getElementsByTagName("Username").item(0);

        mode.setTextContent(mod);
        username.setTextContent(user);
    }

    private Node initializeTagList(String tagName, int modeIndex){
        Node tag = document.getElementsByTagName(tagName).item(modeIndex);
        return tag;
    }

    private Node appendTag(Node father, String tagName, String textContent){
        Element tag = document.createElement(tagName);
        tag.setTextContent(textContent);
        father.appendChild(tag);
        return document.getElementsByTagName(tagName).item(0);
    }

    private Node appendTagWithAttribute(Node father, String tagName, String textContent, String attributeName, String attributeValue){
        Element tag = document.createElement(tagName);
        tag.setAttribute(attributeName,attributeValue);
        tag.setTextContent(textContent);
        father.appendChild(tag);
        return document.getElementsByTagName(tagName).item(0);
    }

    private void appendWorkerPosition(int index, String workerGender, String xPosition, String yPosition){
        Node position = document.getElementsByTagName("Position").item(index);
        appendTag(position, "WorkerGender", workerGender);
        appendTag(position,"xPosition",xPosition);
        appendTag(position,"yPosition",yPosition);
    }


    //action methods

    public Document loginRequest(String user, Color c){
        setStandardRequestValues(user,"login");
        Node updateTag = initializeTagList("Request",LOGIN_INDEX);
        appendTag(updateTag,"Color",Color.labelOfEnum(c));
        return document;
    }

    public Document startGameRequest(String user){
        setStandardRequestValues(user,"startGame");
        return document;
    }

    public Document chooseStartingPlayerRequest(String user, String chosenPlayer){
        setStandardRequestValues(user,"choseStartingPlayer");
        Node updateTag = initializeTagList("Request", CHOOSE_STARTER_INDEX);
        appendTag(updateTag,"PlayerChosen",chosenPlayer);
        return document;
    }

    public Document createGodsRequest(String user, ArrayList<Integer> godIds){
        setStandardRequestValues(user,"createGods");
        Node updateTag = initializeTagList("Request", CREATE_GODS_INDEX);
        Node gods = appendTag(updateTag,"Gods","");
        for (int i=0;i < godIds.size(); i++){
            appendTagWithAttribute(gods,"God",godIds.get(i).toString(),"n",String.valueOf(i));
        }
        return document;
    }

    public Document chooseGodRequest(String user, Integer godId){
        setStandardRequestValues(user,"choseGod");
        Node updateTag = initializeTagList("Request", CHOOSE_GOD_INDEX);
        appendTag(updateTag,"God",godId.toString());
        return document;
    }

    public Document setWorkerOnBoardRequest(String user, String workerGender, Integer xPosition, Integer yPosition){
        setStandardRequestValues(user,"SetWorkerOnBoard");
        Node updateTag = initializeTagList("Request", SET_WORKER_INDEX);
        Node position = appendTag(updateTag,"Position","");

        appendWorkerPosition(0, workerGender,xPosition.toString(),yPosition.toString());
        return document;
    }

    public Document moveRequest(String user, String workerGender, Integer xPosition, Integer yPosition){
        setStandardRequestValues(user,"move");
        Node updateTag = initializeTagList("Request", MOVE_INDEX);
        appendTag(updateTag,"Position","");

        appendWorkerPosition(1, workerGender,xPosition.toString(),yPosition.toString());
        return document;
    }

    public Document buildRequest(String user, String workerGender, Integer xPosition, Integer yPosition){
        setStandardRequestValues(user,"build");
        Node updateTag = initializeTagList("Request", BUILD_INDEX);
        Node position = appendTag(updateTag,"Position","");

        appendWorkerPosition(2, workerGender,xPosition.toString(),yPosition.toString());
        return document;
    }

    public Document endOfTurnRequest(String user){
        setStandardRequestValues(user,"endOfTurn");

        return document;
    }

    public Document endRequest(String user){
        setStandardRequestValues(user,"end");

        return document;
    }
}
