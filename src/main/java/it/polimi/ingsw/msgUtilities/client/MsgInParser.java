package it.polimi.ingsw.msgUtilities.client;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.*;
import java.util.*;

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

    public boolean parseDisconnectionMessage(){
        String answerType = document.getFirstChild().getNodeName();
        if(answerType.equals("UpdateMsg")){
            String mode = Objects.requireNonNull(evaluateXPath("/UpdateMsg/Mode/text()")).get(0);
            if(mode.equals("disconnection")){
                //TODO notify view
                return true;
            }
        }
        return false;
    }

    public void parseIncomingMessage(){
        String answerType = document.getFirstChild().getNodeName();

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
        String mode = Objects.requireNonNull(evaluateXPath("/UpdateMsg/Mode/text()")).get(0);
        String username = Objects.requireNonNull(evaluateXPath("/UpdateMsg/Author/text()")).get(0);

        switch (mode){
            case "login" :
                String color =  Objects.requireNonNull(evaluateXPath("/UpdateMsg/Update/Color/text()")).get(0);
                String user =  Objects.requireNonNull(evaluateXPath("/UpdateMsg/Update/Username/text()")).get(0);
                //TODO notify view
                break;
            case "startGame" :
                //TODO notify view
                break;
            case "createGods" :
                ArrayList<Integer> ids = new ArrayList<>();
                for(int i = 1; i <= 3; ++i){
                    int id = Integer.parseInt(Objects.requireNonNull(evaluateXPath( "/UpdateMsg/Update/Gods/God[@n=" + i + "]/text()")).get(0));
                    if(id != 0) ids.add(id);
                }
                //TODO notify view
                break;
            case "choseGod" :
                int godId = Integer.parseInt(Objects.requireNonNull(evaluateXPath( "/UpdateMsg/Update/godId/text()")).get(0));
                //TODO notify view
                break;
            case "choseStartingPlayer":
                String starter = Objects.requireNonNull(evaluateXPath( "/UpdateMsg/Update/StartingPlayer/text()")).get(0);
                //TODO notify view
                break;
            case "setWorkerOnBoard":
                String WorkerGender = Objects.requireNonNull(evaluateXPath( "/UpdateMsg/Update/WorkerGender/text()")).get(0);
                int x = Integer.parseInt(Objects.requireNonNull(evaluateXPath( "/UpdateMsg/Update/xPosition/text()")).get(0));
                int y = Integer.parseInt(Objects.requireNonNull(evaluateXPath( "/UpdateMsg/Update/yPosition/text()")).get(0));
                //TODO notify view
                break;
            case "move":
                NodeList positionsNode = document.getElementsByTagName("Position");
                List<HashMap<String,String>> positions = getActionData(positionsNode);
                //TODO notify view
            case "build":
                NodeList heightsNode = document.getElementsByTagName("Height");
                List<HashMap<String,String>> heights = getActionData(heightsNode);
                //TODO notify view
            case "endOfTurn":
                NodeList removeAndBuildNode = document.getElementsByTagName("RemoveAndBuild");
                List<HashMap<String,String>> removeAndBuild = getActionData(removeAndBuildNode);
                //TODO notify view


        }
    }

    private void parseAnswer(){
        String mode = Objects.requireNonNull(evaluateXPath("/Answer/Mode/text()")).get(0);
        String username = Objects.requireNonNull(evaluateXPath("/Answer/Username/text()")).get(0);
        String outcome = Objects.requireNonNull(evaluateXPath("/Answer/Outcome/text()")).get(0);
        String nextStep;
        switch (mode){
            case "login" :
                if(outcome.equals("accepted")){
                    String color =  Objects.requireNonNull(evaluateXPath("/Answer/Update/Color/text()")).get(0);
                    String user =  Objects.requireNonNull(evaluateXPath("/Answer/Update/Username/text()")).get(0);
                    ArrayList<String> users = new ArrayList<>();
                    users.add(user);
                    NodeList components = document.getElementsByTagName("Component");
                    for (int j = 0; j < components.getLength(); j++) {
                            Node component = components.item(j);
                            users.add(component.getTextContent());
                        }

                    System.out.print(users);
                    System.out.print(color);
                    //TODO notify view
                }
                else{
                    List<String> errors = getErrorList();
                    //TODO notify view
                }
                break;
            case "startGame" :
                if(outcome.equals("accepted")){
                    //TODO notify view
                }
                else{
                    List<String> errors = getErrorList();
                    //TODO notify view
                }
                break;
            case "createGods" :
                if(outcome.equals("accepted")){
                    ArrayList<Integer> ids = new ArrayList<>();
                    for(int i = 1; i <= 3; ++i){
                        int id = Integer.parseInt(Objects.requireNonNull(evaluateXPath( "/Answer/Update/Gods/God[@n=" + i + "]/text()")).get(0));
                        if(id != 0) ids.add(id);
                    }
                    //TODO notify view
                }
                else{
                    List<String> errors = getErrorList();
                    //TODO notify view
                }
                break;
            case "choseGod" :
                if(outcome.equals("accepted")){
                    int godId = Integer.parseInt(Objects.requireNonNull(evaluateXPath( "/Answer/Update/godId/text()")).get(0));
                    //TODO notify view
                }
                else{
                    List<String> errors = getErrorList();
                    //TODO notify view
                }
                break;
            case "choseStartingPlayer":
                if(outcome.equals("accepted")){
                    String starter = Objects.requireNonNull(evaluateXPath( "/Answer/Update/StartingPlayer/text()")).get(0);
                    //TODO notify view

                }
                else{
                    List<String> errors = getErrorList();
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
                    List<String> errors = getErrorList();
                    //TODO notify view
                }
                break;
            case "move":
                if(outcome.equals("accepted")){
                    nextStep =  Objects.requireNonNull(evaluateXPath("/Answer/TurnNextStep/text()")).get(0);
                    NodeList positionsNode = document.getElementsByTagName("Position");
                    List<HashMap<String,String>> positions = getActionData(positionsNode);
                    System.out.println(positions);
                    //TODO notify view
                }
                else{
                    List<String> errors = getErrorList();
                    System.out.print(errors);
                }
                break;
            case "build":
                if(outcome.equals("accepted")){
                    nextStep =  Objects.requireNonNull(evaluateXPath("/Answer/TurnNextStep/text()")).get(0);
                    NodeList heightsNode = document.getElementsByTagName("Height");
                    List<HashMap<String,String>> heights = getActionData(heightsNode);
                    //TODO notify view
                }
                else{
                    List<String> errors = getErrorList();

                }
                break;

            case "endOfTurn":
                if(outcome.equals("accepted")){
                    nextStep =  Objects.requireNonNull(evaluateXPath("/Answer/TurnNextStep/text()")).get(0);
                    NodeList removeAndBuildNode = document.getElementsByTagName("RemoveAndBuild");
                    List<HashMap<String,String>> removeBuild = getActionData(removeAndBuildNode);
                    //TODO notify view
                }
                else{
                    List<String> errors = getErrorList();

                }


        }

    }

    private void parseToDo(){

    }


    private List<String> getErrorList(){
        NodeList errorsNode = document.getElementsByTagName("Errors");
        Node errorNode = errorsNode.item(0);
        List<String>  errors = new ArrayList<>();
        NodeList allErrors = errorNode.getChildNodes();
        for (int j = 0; j < allErrors.getLength(); j++) {
            Node error = allErrors.item(j);
            errors.add(error.getNodeName());
        }
        return Collections.unmodifiableList(errors);
    }

    private List<HashMap<String,String>> getActionData(NodeList primaryNode){
        List<HashMap<String,String>> result = new ArrayList<>();
        for (int j = 0; j < primaryNode.getLength(); j++) {
            Node dataNode = primaryNode.item(j);
            NodeList dataNode_child = dataNode.getChildNodes();
            HashMap<String,String> data = new HashMap<>();
            for(int i = 0; i < dataNode_child.getLength();i++){
                Node child = dataNode_child.item(i);
                data.put(child.getNodeName(), child.getTextContent());
            }
            result.add(data);
        }
        return result;
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
