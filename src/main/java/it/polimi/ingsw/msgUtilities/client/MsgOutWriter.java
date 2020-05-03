package it.polimi.ingsw.msgUtilities.client;

import it.polimi.ingsw.model.enums.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

public class MsgOutWriter {

    //attributes

    private Document document;
    private String filePath;

    private static  final int LOGIN_INDEX = 0;
    private static final int CHOOSE_STARTER_INDEX = 1;
    private static final int CREATE_GODS_INDEX = 2;
    private static final int CHOOSE_GOD_INDEX = 3;
    private static final int SET_WORKER_INDEX = 4;
    private static final int MOVE_INDEX = 5;
    private static final int BUILD_INDEX = 6;



    //methods

    //support methods

    private void setDocument(String fileName){
        try {
            filePath = "src/main/resources/client/" + fileName;
            this.document = this.getDocument(filePath);
        }
        catch (Exception e){
            System.out.println("Error during XML parsing.");
        }
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


    private void setStandardRequestValues(String user,String mod){
        setDocument("toSendRequest");
        Node mode = document.getElementsByTagName("Mode").item(0);
        Node username = document.getElementsByTagName("Username").item(0);

        mode.setTextContent(mod);
        username.setTextContent(user);
    }

    private void resetChildList(Node list){
        while(list.hasChildNodes()){
            Node child = list.getFirstChild();
            list.removeChild(child);
        }
    }

    private Node initializeTagList(String tagName, int modeIndex){
        Node tag = document.getElementsByTagName(tagName).item(modeIndex);
        resetChildList(tag);
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

    private void applyModification() {
        try {

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    //action methods

    public void loginRequest(String user, Color c){
        setStandardRequestValues(user,"login");
        Node aUpdateTag = initializeTagList("Request",LOGIN_INDEX);
        appendTag(aUpdateTag,"Color",Color.labelOfEnum(c));
        applyModification();
    }

    public void startGameRequest(String user){
        setStandardRequestValues(user,"startGame");
        applyModification();
    }

    public void chooseStartingPlayerRequest(String user, String chosenPlayer){
        setStandardRequestValues(user,"choseStartingPlayer");
        Node aUpdateTag = initializeTagList("Request", CHOOSE_STARTER_INDEX);
        appendTag(aUpdateTag,"PlayerChosen",chosenPlayer);
        applyModification();
    }

    public void createGodsRequest(String user, ArrayList<Integer> godIds){
        setStandardRequestValues(user,"createGods");
        Node aUpdateTag = initializeTagList("Request", CREATE_GODS_INDEX);
        Node gods = appendTag(aUpdateTag,"Gods","");
        for (int i=0;i < godIds.size(); i++){
            appendTagWithAttribute(gods,"God",godIds.get(i).toString(),"n",String.valueOf(i));
        }
        applyModification();
    }

    public void chooseGodRequest(String user, Integer godId){
        setStandardRequestValues(user,"choseGod");
        Node aUpdateTag = initializeTagList("Request", CHOOSE_GOD_INDEX);
        appendTag(aUpdateTag,"God",godId.toString());
        applyModification();
    }

    public void setWorkerOnBoardRequest(String user, String workerGender, Integer xPosition, Integer yPosition){
        setStandardRequestValues(user,"SetWorkerOnBoard");
        Node aUpdateTag = initializeTagList("Request", SET_WORKER_INDEX);
        Node position = appendTag(aUpdateTag,"Position","");

        appendWorkerPosition(0, workerGender,xPosition.toString(),yPosition.toString());
        applyModification();
    }

    public void moveRequest(String user, String workerGender, Integer xPosition, Integer yPosition){
        setStandardRequestValues(user,"move");
        Node aUpdateTag = initializeTagList("Request", MOVE_INDEX);
        appendTag(aUpdateTag,"Position","");

        appendWorkerPosition(1, workerGender,xPosition.toString(),yPosition.toString());
        applyModification();
    }

    public void buildRequest(String user, String workerGender, Integer xPosition, Integer yPosition){
        setStandardRequestValues(user,"build");
        Node aUpdateTag = initializeTagList("Request", BUILD_INDEX);
        Node position = appendTag(aUpdateTag,"Position","");

        appendWorkerPosition(2, workerGender,xPosition.toString(),yPosition.toString());
        applyModification();
    }
}
