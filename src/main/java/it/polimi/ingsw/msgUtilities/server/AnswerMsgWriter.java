package it.polimi.ingsw.msgUtilities.server;

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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AnswerMsgWriter {

    //attributes

    private Document document;

    //constructor

    public AnswerMsgWriter(){
        try {
            this.document = this.getDocument(this.getClass().getResourceAsStream("/xml/server/toSendAnswer"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //methods

    //support methods

    private void setErrorList(List<Error> errors){
        Node errorsTag = initializeTagList("Errors");

        for(Error error : errors){
            String sError = Error.labelOfEnum(error);
            Element tag = document.createElement(sError);
            errorsTag.appendChild(tag);
        }
    }

    private void setStandardAnswerValues(String user,String mod,String out){
        Node mode = document.getElementsByTagName("Mode").item(0);
        Node username = document.getElementsByTagName("Username").item(0);
        Node outcome = document.getElementsByTagName("Outcome").item(0);

        mode.setTextContent(mod);
        username.setTextContent(user);
        outcome.setTextContent(out);
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

    public Document rejectedAnswer(String user, String mod, List<Error> errors){
        setStandardAnswerValues(user,mod,"rejected");

        setErrorList(errors);
        return document;
    }

    public Document loginAcceptedAnswer(String user, Color c){
        setStandardAnswerValues(user,"login","accepted");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"Username",user);
        appendTag(updateTag,"Color",Color.labelOfEnum(c));
        return document;

    }

    public Document startGameAcceptedAnswer(String user){
        setStandardAnswerValues(user,"startGame","accepted");
        return document;
    }

    public Document createGodsAcceptedAnswer(String user, ArrayList<Integer> ids){
        setStandardAnswerValues(user,"createGods","accepted");
        Node updateTag = initializeTagList("Update");

        for(int id : ids){
            appendTag(updateTag, String.valueOf(id));
        }
        return document;
    }

    public Document choseGodAcceptedAnswer(String user, int godId){
        setStandardAnswerValues(user,"choseGod","accepted");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"godId",String.valueOf(godId));
        return document;

    }

    public Document choseStartingPlayerAcceptedAnswer(String user, String playerChosen){
        setStandardAnswerValues(user,"choseStartingPlayer","accepted");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"StartingPlayer",playerChosen);
        return document;
    }

    public Document setupOnBoardAcceptedAnswer(String user, String workerGender, int x, int y){
        setStandardAnswerValues(user,"setWorkerOnBoard","accepted");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"WorkerGender",workerGender);
        appendTag(updateTag,"xPosition",String.valueOf(x));
        appendTag(updateTag,"yPosition",String.valueOf(y));
        return document;
    }

}
