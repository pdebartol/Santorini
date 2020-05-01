package it.polimi.ingsw.view.server;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLMessageWriter {

    //attributes

    private Document document1,document2;

    //constructors

    public XMLMessageWriter(String type){
        try{
            if(type.equals("answer")) {
                this.document1 = this.getDocument("src/main/resources/server/toSendRequest");
                this.document2 = this.getDocument("src/main/resources/server/updateMsgOut");
            }
            if(type.equals("communication"))
                this.document1 = this.getDocument("src/main/resources/server/extraCommunication");
        }
        catch (Exception e){
            System.out.println("Error during XML parsing.");
        }
    }

    //methods

    private void applyModification(Document doc,String filepath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private void setErrorList(List<Error> errors){
        Node errorsTag = document1.getElementsByTagName("Errors").item(0);
        resetChildList(errorsTag);

        for(Error error : errors){
            String sError = Error.labelOfEnum(error);
            Element tag = document1.createElement(sError);
            errorsTag.appendChild(tag);
        }
    }

    private void setStandardAnswerValues(String usr,String md,String out){
        Node mode = document1.getElementsByTagName("Mode").item(0);
        Node username = document1.getElementsByTagName("Username").item(0);
        Node outcome = document1.getElementsByTagName("Outcome").item(0);

        mode.setTextContent(md);
        username.setTextContent(usr);
        outcome.setTextContent(out);
    }

    private void setStandardUpdateValues(String usr,String md){
        Node mode = document2.getElementsByTagName("Mode").item(0);
        Node username = document2.getElementsByTagName("Username").item(0);

        mode.setTextContent(md);
        username.setTextContent(usr);
    }

    private void resetChildList(Node list){
        while(list.hasChildNodes()){
            Node error = list.getFirstChild();
            list.removeChild(error);
        }
    }

    public void loginAcceptedAnswer(String usr, Color c){
        setStandardAnswerValues(usr,"login","accepted");
        setStandardUpdateValues(usr,"newPlayer");

        Node update2Tag = document2.getElementsByTagName("Update").item(0);
        resetChildList(update2Tag);

        Element tag = document2.createElement("Username");
        tag.setTextContent(usr);
        update2Tag.appendChild(tag);
        tag = document2.createElement("Color");
        tag.setTextContent(Color.labelOfEnum(c));
        update2Tag.appendChild(tag);
    }

    public void loginRejectedAnswer(String usr, List<Error> errors){
        setStandardAnswerValues(usr,"login","rejected");

        setErrorList(errors);
    }

    public void startGameAcceptedAnswer(String usr){
        setStandardAnswerValues(usr,"startGame","accepted");
        setStandardUpdateValues(usr,"startGame");

        Node update2Tag = document2.getElementsByTagName("Update").item(0);
        resetChildList(update2Tag);
    }

    public void createGodsAcceptedAnswer(String usr, ArrayList<Integer> ids){
        setStandardUpdateValues(usr,"createGods");

        Node update2Tag = document2.getElementsByTagName("Update").item(0);
        resetChildList(update2Tag);

        for(int id : ids){
            Element tag = document2.createElement(String.valueOf(id));
            update2Tag.appendChild(tag);
        }
    }

    public void createGodsRejectedAnswer(String usr, List<Error> errors){
        setStandardAnswerValues(usr,"createGods","rejected");

        setErrorList(errors);
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
}
