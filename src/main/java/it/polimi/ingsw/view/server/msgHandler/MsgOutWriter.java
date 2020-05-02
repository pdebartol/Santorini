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
    
    private void appendTag(Node father, String tagName, String textContent){
        Element tag = document.createElement(tagName);
        tag.setTextContent(textContent);
        father.appendChild(tag);
    }
    
    private void appendTag(Node father, String tagName){
        Element tag = document.createElement(tagName);
        father.appendChild(tag);
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

    public void loginAcceptedAnswer(String user, Color c){
        setStandardAnswerValues(user,"login","accepted");
        applyModification();

        
        setStandardUpdateValues(user,"newPlayer");
        Node updateTag = initializeTagList("Update");
        
        appendTag(updateTag,"Username",user);
        appendTag(updateTag,"Color",Color.labelOfEnum(c));
        applyModification();
    }

    public void loginRejectedAnswer(String user, List<Error> errors){
        setStandardAnswerValues(user,"login","rejected");

        setErrorList(errors);
        applyModification();
    }

    public void startGameAcceptedAnswer(String user){
        setStandardAnswerValues(user,"startGame","accepted");
        applyModification();

        setStandardUpdateValues(user,"startGame");

        initializeTagList("Update");
        initializeTagList("Errors");
        applyModification();
    }

    public void createGodsAcceptedAnswer(String user, ArrayList<Integer> ids){
        setStandardUpdateValues(user,"createGods");

        Node updateTag = initializeTagList("Update");

        for(int id : ids){
            appendTag(updateTag, String.valueOf(id));
        }
        applyModification();
    }

    public void createGodsRejectedAnswer(String user, List<Error> errors){
        setStandardAnswerValues(user,"createGods","rejected");

        setErrorList(errors);
        applyModification();
    }
}
