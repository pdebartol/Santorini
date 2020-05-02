package it.polimi.ingsw.view.server.msgHandler;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MsgOutWriter {

    //attributes

    private Document document;
    private String filePath;


    //methods

    //support methods

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

    private void setErrorList(List<Error> errors){
        Node errorsTag = initializeTagList("Errors");

        for(Error error : errors){
            String sError = Error.labelOfEnum(error);
            Element tag = document.createElement(sError);
            errorsTag.appendChild(tag);
        }
    }

    private void setStandardAnswerValues(String user,String mod,String out){
        setDocument("toSendAnswer");
        Node mode = document.getElementsByTagName("Mode").item(0);
        Node username = document.getElementsByTagName("Username").item(0);
        Node outcome = document.getElementsByTagName("Outcome").item(0);

        mode.setTextContent(mod);
        username.setTextContent(user);
        outcome.setTextContent(out);
    }

    private void setStandardUpdateValues(String user,String mod){
        setDocument("updateMsgOut");
        Node mode = document.getElementsByTagName("Mode").item(0);
        Node username = document.getElementsByTagName("Author").item(0);

        mode.setTextContent(mod);
        username.setTextContent(user);
    }

    private void resetChildList(Node list){
        while(list.hasChildNodes()){
            Node child = list.getFirstChild();
            list.removeChild(child);
        }
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
        resetChildList(tag);
        return tag;
    }

    private void setDocument(String fileName){
        try {
            filePath = "src/main/resources/server/" + fileName;
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

    //action methods

    public void rejectedAnswer(String user, String mod, List<Error> errors){
        setStandardAnswerValues(user,mod,"rejected");

        setErrorList(errors);
        applyModification();
    }

    public void loginAcceptedAnswer(String user, Color c){
        setStandardAnswerValues(user,"login","accepted");
        Node aUpdateTag = initializeTagList("Update");

        appendTag(aUpdateTag,"Username",user);
        appendTag(aUpdateTag,"Color",Color.labelOfEnum(c));
        applyModification();

        
        setStandardUpdateValues(user,"newPlayer");
        Node uUpdateTag = initializeTagList("Update");
        
        appendTag(uUpdateTag,"Username",user);
        appendTag(uUpdateTag,"Color",Color.labelOfEnum(c));
        applyModification();
    }

    public void startGameAcceptedAnswer(String user){
        setStandardAnswerValues(user,"startGame","accepted");

        initializeTagList("Update");
        initializeTagList("Errors");
        applyModification();

        setStandardUpdateValues(user,"startGame");

        initializeTagList("Update");
        initializeTagList("Errors");
        applyModification();
    }

    public void createGodsAcceptedAnswer(String user, ArrayList<Integer> ids){
        setStandardAnswerValues(user,"createGods","accepted");
        Node aUpdateTag = initializeTagList("Update");

        for(int id : ids){
            appendTag(aUpdateTag, String.valueOf(id));
        }
        applyModification();

        setStandardUpdateValues(user,"createGods");
        Node uUpdateTag = initializeTagList("Update");

        for(int id : ids){
            appendTag(uUpdateTag, String.valueOf(id));
        }
        applyModification();
    }

    public void choseGodAcceptedAnswer(String user, int godId){
        setStandardAnswerValues(user,"choseGod","accepted");
        Node aUpdateTag = initializeTagList("Update");

        appendTag(aUpdateTag,"godId",String.valueOf(godId));
        applyModification();


        setStandardUpdateValues(user,"choseGod");
        Node uUpdateTag = initializeTagList("Update");

        appendTag(uUpdateTag,"godId",String.valueOf(godId));
        applyModification();

    }

    public void choseStartingPlayerAcceptedAnswer(String user, String playerChosen){
        setStandardAnswerValues(user,"choseStartingPlayer","accepted");
        Node aUpdateTag = initializeTagList("Update");

        appendTag(aUpdateTag,"StartingPlayer",playerChosen);
        applyModification();


        setStandardUpdateValues(user,"choseStartingPlayer");
        Node uUpdateTag = initializeTagList("Update");

        appendTag(uUpdateTag,"StartingPlayer",playerChosen);
        applyModification();
    }

    public void setupOnBoardAcceptedAnswer(String user, String workerGender, int x, int y){
        setStandardAnswerValues(user,"setWorkerOnBoard","accepted");
        Node aUpdateTag = initializeTagList("Update");

        appendTag(aUpdateTag,"WorkerGender",workerGender);
        appendTag(aUpdateTag,"xPosition",String.valueOf(x));
        appendTag(aUpdateTag,"yPosition",String.valueOf(y));
        applyModification();


        setStandardUpdateValues(user,"setWorkerOnBoard");
        Node uUpdateTag = initializeTagList("Update");

        appendTag(aUpdateTag,"WorkerGender",workerGender);
        appendTag(aUpdateTag,"xPosition",String.valueOf(x));
        appendTag(aUpdateTag,"yPosition",String.valueOf(y));
        applyModification();
    }

}
