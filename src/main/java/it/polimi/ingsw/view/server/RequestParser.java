package it.polimi.ingsw.view.server;

import it.polimi.ingsw.model.enums.Color;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.util.*;

/** This class is responsible for parsing the arrivedRequest XML and call VirtualView methods to starting
 * the request processing.
 * @author marcoDige
 */

public class RequestParser {

    //attributes

    private Document document;
    private final VirtualView virtualView;

    public RequestParser(){
        try{
            this.document = this.getDocument();
        }
        catch (Exception e){
            System.out.println("Error during XML parsing.");
        }
        virtualView = new VirtualView();
    }

    //method

    /**
     * This method reads the request mode in arrivedRequest, extracts data which client sent and call the
     * method in virtualView which corresponds to the request mode
     */

    public void parseRequest(){
        String mode = Objects.requireNonNull(evaluateXPath("/Requests/Mode/text()")).get(0);
        String username = Objects.requireNonNull(evaluateXPath("/Requests/Username/text()")).get(0);
        String standardPath = "/Requests/Request[Mode=\"" + mode + "\"]";

        switch (mode){
            case "login" :
                String color = Objects.requireNonNull(evaluateXPath(standardPath + "/Color/text()")).get(0);
                virtualView.loginRequest(username, Color.valueOfLabel(color));
                break;
            case "startGame" :
                virtualView.startGameRequest(username);
                break;
            case "choseStartingPlayer" :
                String playerChosen = Objects.requireNonNull(evaluateXPath(standardPath +"/PlayerChosen/text()")).get(0);
                virtualView.chooseStartingPlayerRequest(username,playerChosen);
                break;
            case "createGods" :
                ArrayList<Integer> ids = new ArrayList<>();
                for(int i = 0; i < 3; ++i){
                    int id = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Gods/God[n='" + Integer.toString(i) + "']/id/text()")).get(0));
                    if(id != 0) ids.add(id);
                }
                virtualView.createGodsRequest(username,ids);
                break;
            case "choseGod" :
                int godId = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/God/id/text()")).get(0));
                virtualView.choseGodRequest(username, godId);
                break;
            case "setWorkersOnBoard" :
                int x,y;
                x = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Positions/Position[WorkerId='0']/xPosition/text()")).get(0));
                y = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Positions/Position[WorkerId='0']/yPosition/text()")).get(0));
                virtualView.setupOnBoardRequest(username,0,x,y);
                x = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Positions/Position[WorkerId='1']/xPosition/text()")).get(0));
                y = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Positions/Position[WorkerId='1']/yPosition/text()")).get(0));
                virtualView.setupOnBoardRequest(username,1,x,y);
                break;
            case "move" :
                int xm,ym;
                int mWorkerId = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/WorkerId/text()")).get(0));
                xm = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/xPosition/text()")).get(0));
                ym = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/yPosition/text()")).get(0));
                virtualView.moveRequest(username,mWorkerId,xm,ym);
                break;
            case "build":
                int xb,yb;
                int bWorkerId = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/WorkerId/text()")).get(0));
                xb = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/xPosition/text()")).get(0));
                yb = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/yPosition/text()")).get(0));
                virtualView.buildRequest(username,bWorkerId,xb,yb);
                break;
            case "endOfTurn":
                virtualView.endOfTurn(username);
                break;
        }
    }

    /**
     * This method verify if mode is end (client want to exit from game)
     * @return true -> mode is "end"
     *         false -> mode isn't "end"
     */

    public boolean parseEndRequest(){
        String mode = Objects.requireNonNull(evaluateXPath("/Requests/Mode/text()")).get(0);
        return mode.equals("end");
    }

    /**
     * This method creates the document object and parses 'arrivedRequest' file
     * @return the parsed xml file
     * @throws Exception error during xml parsing
     */

    private Document getDocument() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse("src/main/resources/arrivedRequest");
    }

    /**
     * This methods uses XPath expressions to find nodes in xml documents
     * @param xpathExpression is the expression that identifies the node in the document
     * @return a List<String> containing the strings that match the expression
     */

    private  List<String> evaluateXPath( String xpathExpression) {
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
