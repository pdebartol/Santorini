package it.polimi.ingsw.msgUtilities.client;

import it.polimi.ingsw.view.server.VirtualView;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** This class is responsible for parsing the msgIn XML (client-side) and call View methods to start
 * the request processing.
 * @author pierobartolo
 */

public class MsgInParser {

    //attributes

    private Document document;

    public MsgInParser(Document document){
        this.document = document;
    }


    //method

    /**
     * This method reads the server's answer and notifies the view with the data
     */

    public void parseIncomingMessage(){
        String answerType = Objects.requireNonNull(evaluateXPath("/text()")).get(0);

        switch(answerType){
            case "UpdateMsg":
                parseUpdate();
                break;
            case "Answer":
                parseAnswer();
                break;
            case "ToDo":
                parseToDo();
                break;
            default:
                break;
        }


    }

    private void parseUpdate(){

    }

    private void parseAnswer(){
        String mode = Objects.requireNonNull(evaluateXPath("/Answer/Mode/text()")).get(0);
        String username = Objects.requireNonNull(evaluateXPath("/Answer/Username/text()")).get(0);
        String outcome = Objects.requireNonNull(evaluateXPath("/Answer/Outcome/text()")).get(0);

        switch (mode){
            case "login" :
                if(outcome.equals("accepted")){
                    String color =  Objects.requireNonNull(evaluateXPath("/Answer/Update/Color/text()")).get(0);
                    String user =  Objects.requireNonNull(evaluateXPath("/Answer/Update/Username/text()")).get(0);
                    //TODO notify view
                }
                else{
                    //TODO get error list
                    //TODO notify view
                }
                break;
            case "startGame" :
                if(outcome.equals("accepted")){
                    //TODO notify view
                }
                else{
                    //TODO get error list
                    //TODO notify view
                }
                break;
            case "createGods" :
                if(outcome.equals("accepted")){
                    ArrayList<Integer> ids = new ArrayList<>();
                    for(int i = 0; i < 3; ++i){
                        int id = Integer.parseInt(Objects.requireNonNull(evaluateXPath( "/Answer/Update/Gods/God[@n=" + i + "]/text()")).get(0));
                        if(id != 0) ids.add(id);
                    }
                    //TODO notify view
                }
                else{
                    //TODO get error list
                    //TODO notify view
                }
                break;
            case "choseGod" :
                if(outcome.equals("accepted")){
                    int godId = Integer.parseInt(Objects.requireNonNull(evaluateXPath( "/Answer/Update/God/text()")).get(0));
                    //TODO notify view
                }
                else{
                    //TODO get error list
                    //TODO notify view
                }
                break;
            case "choseStartingPlayer":
                if(outcome.equals("accepted")){
                    String starter = Objects.requireNonNull(evaluateXPath( "/Answer/Update/StartingPlayer/text()")).get(0);
                    //TODO notify view

                }
                else{
                    //TODO get error list
                    //TODO notify view
                }
                break;
            case "setWorkerOnBoard":
                if(outcome.equals("accepted")){
                    String WorkerGender = Objects.requireNonNull(evaluateXPath( "/Answer/Update/WorkerGender/text()")).get(0);
                    int x = Integer.parseInt(Objects.requireNonNull(evaluateXPath( "/Answer/Update/xPosition/text()")).get(0));
                    int y = Integer.parseInt(Objects.requireNonNull(evaluateXPath( "/Answer/Update/yPosition/text()")).get(0));
                    //TODO notify view
                }
                else{
                    //TODO get error list
                    //TODO notify view
                }
                break;
        }

    }

    private void parseToDo(){

    }


    /**
     * This methods uses XPath expressions to find nodes in xml documents
     * @param xpathExpression is the expression that identifies the node in the document
     * @return a List<String> containing the strings that match the expression
     */

    private List<String> evaluateXPath(String xpathExpression) {
        try {
            // Create XPathFactory object
            XPathFactory xpathFactory = XPathFactory.newInstance();

            // Create XPath object
            XPath xpath = xpathFactory.newXPath();

            List<String> values = new ArrayList<>();
            // Create XPathExpression object
            XPathExpression expr = xpath.compile(xpathExpression);

            // Evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                values.add(nodes.item(i).getNodeValue());
            }
            return values;

        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

}
