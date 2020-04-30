package it.polimi.ingsw.view.server;

import it.polimi.ingsw.model.enums.Error;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLMessageWriter {

    //attributes

    private Document applicantDocument,attendantsDocument;

    //constructors

    public XMLMessageWriter(String type){
        try{
            this.applicantDocument = this.getDocument("src/main/resources/server/" + type);
        }
        catch (Exception e){
            System.out.println("Error during XML parsing.");
        }
    }

    //methods

    private void setStandardAttributes(String usr,String md,String out){
        Node mode = applicantDocument.getElementsByTagName("Mode").item(0);
        Node username = applicantDocument.getElementsByTagName("Username").item(0);
        Node outcome = applicantDocument.getElementsByTagName("Outcome").item(0);

        mode.setTextContent(md);
        username.setTextContent(usr);
        outcome.setTextContent(out);
    }

    public void loginAcceptedAnswer(String usr){
        setStandardAttributes(usr,"login","accepted");
        //TODO write updateMsgOut
    }

    public void loginRejectedAnswer(String usr, List<Error> errors){
        setStandardAttributes(usr,"login","rejected");
        Node errorsTag = applicantDocument.getElementsByTagName("Errors").item(0);

        for(Error error : errors){
            String sError = Error.labelOfEnum(error);
            Element attribute = applicantDocument.createElement(sError);
            errorsTag.appendChild(attribute);
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
}
