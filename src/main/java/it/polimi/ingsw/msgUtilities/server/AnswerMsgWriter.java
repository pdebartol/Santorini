package it.polimi.ingsw.msgUtilities.server;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class contains all the methods for writing an XML schema to a client request response message from the server.
 * @author marcoDige
 */

public class AnswerMsgWriter {

    //attributes

    /**
     * This attribute represents the XML abstract document
     */

    private Document document;

    //constructor

    /**
     * This constructor initializes the document with the standard XML schema content in toSendAnswer file (content in
     * resources folder).
     */

    public AnswerMsgWriter(){
        try {
            this.document = this.getDocument(this.getClass().getResourceAsStream("/xml/server/toSendAnswer"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //methods

    //support methods

    /**
     * This method writes a error list under Errors tag in the document.
     * @param errors is the error list to report to the client
     */

    private void setErrorList(List<Error> errors){
        Node errorsTag = initializeTagList("Errors");

        for(Error error : errors){
            String sError = Error.labelOfEnum(error);
            Element tag = document.createElement(sError);
            errorsTag.appendChild(tag);
        }
    }

    /**
     * This method writes a user (components) list under the Components tag in the document.
     * @param users is the user list to report to the client
     */

    private void setComponentsList(Map<String,Color> users){
        Node componentsTag = initializeTagList("Components");

        for(String u : users.keySet()){
            Element componentTag = document.createElement("Component");
            Element usernameTag = document.createElement("Username");
            Element colorTag = document.createElement("Color");
            usernameTag.setTextContent(u);
            colorTag.setTextContent(Color.labelOfEnum(users.get(u)));
            componentsTag.appendChild(componentTag);
            componentTag.appendChild(usernameTag);
            componentTag.appendChild(colorTag);
        }
    }

    /**
     * This method writes under the three standard tags (both accepted and rejected responses)
     * @param user is the user who made the request
     * @param mod is the request mode (login, startGame...)
     * @param out is the outcome of the response
     * @return the document updated
     */

    public Document setStandardAnswerValues(String user,String mod,String out){
        Node mode = document.getElementsByTagName("Mode").item(0);
        Node username = document.getElementsByTagName("Username").item(0);
        Node outcome = document.getElementsByTagName("Outcome").item(0);

        mode.setTextContent(mod);
        username.setTextContent(user);
        outcome.setTextContent(out);

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

    /**
     * This method provides to format a rejected answer. It set the standard values and provides to report the error list.
     * @param user is the user who made the request
     * @param mod is the request mode (login, startGame...)
     * @param errors is the error list to report to the client
     * @return the updated document
     */

    public Document rejectedAnswer(String user, String mod, List<Error> errors){
        setStandardAnswerValues(user,mod,"rejected");

        setErrorList(errors);
        return document;
    }

    /**
     * This method provides to format a rejected answer associated with a request to set worker on board. It sets the
     * standard values and provides to report the error list.
     * @param user is the user who made the request
     * @param mod is the request mode (login, startGame...)
     * @param errors is the error list to report to the client
     * @param gender is the worker user request to place on board gender
     * @return the updated document
     */

    public Document rejectedSetWorkerOnBoardAnswer(String user, String mod, List<Error> errors, String gender){
        setStandardAnswerValues(user,mod,"rejected");

        setErrorList(errors);

        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"WorkerGender",gender);
        return document;
    }

    /**
     * This method provides to format an accepted answer associated with a login request. It sets the standard values and
     * provides to report color received and users already present in the game.
     * @param user is the user who made the request
     * @param c is the color assigned to the user
     * @param users is the user list to report to the client
     * @return the updated document
     */

    public Document loginAcceptedAnswer(String user, Color c, Map<String,Color> users){
        setStandardAnswerValues(user,"login","accepted");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"Username",user);
        appendTag(updateTag,"Color",Color.labelOfEnum(c));
        Element tag = document.createElement("Components");
        updateTag.appendChild(tag);
        setComponentsList(users);

        return document;

    }

    /**
     * This method provides to format an accepted answer associated with a startGame request. It only sets standard values.
     * @param user is the user who made the request.
     * @return the updated document
     */

    public Document startGameAcceptedAnswer(String user){
        setStandardAnswerValues(user,"startGame","accepted");
        return document;
    }

    /**
     * This method provides to format an accepted answer associated with a createGods request. It sets standard values
     * and provides to report the god's id list generated.
     * @param user is the user who made the request
     * @param ids is the god's id list
     * @return the updated document
     */

    public Document createGodsAcceptedAnswer(String user, ArrayList<Integer> ids){
        setStandardAnswerValues(user,"createGods","accepted");
        Node updateTag = initializeTagList("Update");
        Node godsTag = appendTag(updateTag,"Gods");

        for(int i = 0; i < ids.size(); i++){
            appendTagWithAttribute(godsTag,"God",String.valueOf(ids.get(i)),"n",String.valueOf(i));
        }
        return document;
    }

    /**
     * This method provides to format an accepted answer associated with a choseGods request. It sets standard values
     * and provides to report the god chosen id.
     * @param user is the user who made the request
     * @param godId is the chosen god id
     * @return the updated document
     */

    public Document choseGodAcceptedAnswer(String user, int godId){
        setStandardAnswerValues(user,"choseGod","accepted");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"godId",String.valueOf(godId));
        return document;

    }

    /**
     * This method provides to format an accepted answer associated with a choseStartingPlayer request. It sets standard values
     * and provides to report the player chosen to start the match.
     * @param user is the user who made the request
     * @param playerChosen is the player chosen to start the match
     * @return the updated document
     */

    public Document choseStartingPlayerAcceptedAnswer(String user, String playerChosen){
        setStandardAnswerValues(user,"choseStartingPlayer","accepted");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"StartingPlayer",playerChosen);
        return document;
    }

    /**
     * This method provides to format an accepted answer associated with a setupOnBoard request. It sets standard values
     * and provides to report the worker gender and the coordinates where user placed his worker.
     * @param user is the user who made the request
     * @param workerGender is the worker user request to place on board gender
     * @param x is the x coordinate where user placed his worker
     * @param y is the y coordinate where user placed his worker
     * @return the updated document
     */

    public Document setupOnBoardAcceptedAnswer(String user, String workerGender, int x, int y){
        setStandardAnswerValues(user,"setWorkerOnBoard","accepted");
        Node updateTag = initializeTagList("Update");

        appendTag(updateTag,"WorkerGender",workerGender);
        appendTag(updateTag,"xPosition",String.valueOf(x));
        appendTag(updateTag,"yPosition",String.valueOf(y));
        return document;
    }

    /**
     * This method provides to format an accepted answer associated with a move request. It sets only the updates after
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

        Node position = appendTag(index,updateTag,"Position");
        appendTag(position,"startXPosition",String.valueOf(startX));
        appendTag(position,"startYPosition",String.valueOf(startY));
        appendTag(position,"xPosition",String.valueOf(x));
        appendTag(position,"yPosition",String.valueOf(y));
        return document;
    }

    /**
     * This method provides to format an accepted answer associated with a build request. It sets only the updates after
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
     * This method provides to write the next operation that user has to do during his turn.
     * @param nextStep is the next operation (move, build, move/build, end, build/end)
     * @return the updated document
     */

    public Document nextStepTurnIndication(String nextStep){
        document.getElementsByTagName("TurnNextStep").item(0).setTextContent(nextStep);
        return document;
    }

    /**
     * This method is triggered when the player in turn's god is Medusa. This method provides to write updates after he
     * finished his turn (if workers on board have been removed)
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

}
