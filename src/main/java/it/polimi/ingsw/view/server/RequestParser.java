package it.polimi.ingsw.view.server;

import it.polimi.ingsw.model.enums.Color;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.net.Socket;
import java.util.*;

/** This class is responsible for parsing the msgIn XML and call VirtualView methods to starting
 * the request processing.
 * @author marcoDige
 */

public class RequestParser {

    //attributes

    private Document document;

    public RequestParser(){
        try{
            this.document = this.getDocument();
        }
        catch (Exception e){
            System.out.println("Error during XML parsing.");
        }
    }

    //method

    /**
     * This method reads the request mode in msgIn, extracts data which client sent and call the
     * method in virtualView which corresponds to the request mode
     */

    public void parseRequest(VirtualView vrtV){
        String mode = Objects.requireNonNull(evaluateXPath("/Requests/Mode/text()")).get(0);
        String username = Objects.requireNonNull(evaluateXPath("/Requests/Username/text()")).get(0);
        String standardPath = "/Requests/Request[@Mode=\"" + mode + "\"]";

        switch (mode){
            case "startGame" :
                vrtV.startGameRequest(username);
                break;
            case "choseStartingPlayer" :
                String playerChosen = Objects.requireNonNull(evaluateXPath(standardPath +"/PlayerChosen/text()")).get(0);
                vrtV.chooseStartingPlayerRequest(username,playerChosen);
                break;
            case "createGods" :
                ArrayList<Integer> ids = new ArrayList<>();
                for(int i = 0; i < 3; ++i){
                    int id = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Gods/God[@n=" + i + "]/text()")).get(0));
                    if(id != 0) ids.add(id);
                }
                vrtV.createGodsRequest(username,ids);
                break;
            case "choseGod" :
                int godId = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/God/text()")).get(0));
                vrtV.choseGodRequest(username, godId);
                break;
            case "setWorkersOnBoard" :
                int x,y;
                x = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Positions/Position[@WorkerId='0']/xPosition/text()")).get(0));
                y = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Positions/Position[@WorkerId='0']/yPosition/text()")).get(0));
                vrtV.setupOnBoardRequest(username,0,x,y);
                x = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Positions/Position[@WorkerId='1']/xPosition/text()")).get(0));
                y = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Positions/Position[@WorkerId='1']/yPosition/text()")).get(0));
                vrtV.setupOnBoardRequest(username,1,x,y);
                break;
            case "move" :
                int xm,ym;
                int mWorkerId = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/WorkerId/text()")).get(0));
                xm = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/xPosition/text()")).get(0));
                ym = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/yPosition/text()")).get(0));
                vrtV.moveRequest(username,mWorkerId,xm,ym);
                break;
            case "build":
                int xb,yb;
                int bWorkerId = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/WorkerId/text()")).get(0));
                xb = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/xPosition/text()")).get(0));
                yb = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/yPosition/text()")).get(0));
                vrtV.buildRequest(username,bWorkerId,xb,yb);
                break;
            case "endOfTurn":
                vrtV.endOfTurn(username);
                break;
        }
    }

    /**
     * This method verify if mode is login (client want to login in the match). If it's true, it parses login data from
     * msgIn.xml and call virtualView function to process the login request.
     * @return true -> mode is "login"
     *         false -> mode isn't "login"
     */

    public boolean parseLoginRequest(VirtualView vrtV, Socket socket){
        String mode = Objects.requireNonNull(evaluateXPath("/Requests/Mode/text()")).get(0);
        if(mode.equals("login")){
            String standardPath = "/Requests/Request[@Mode=\"" + mode + "\"]";
            String username = Objects.requireNonNull(evaluateXPath("/Requests/Username/text()")).get(0);
            String color = Objects.requireNonNull(evaluateXPath(standardPath + "/Color/text()")).get(0);
            vrtV.loginRequest(username, Color.valueOfLabel(color),socket);
            return true;
        }
        return false;
    }

    /**
     * This method verify if mode is end (client want to exit from game)
     * @return true -> mode is "end"
     *         false -> mode isn't "end"
     */

    public boolean parseEndRequest(VirtualView vrtV){
        String mode = Objects.requireNonNull(evaluateXPath("/Requests/Mode/text()")).get(0);
        if(mode.equals("end")){
            String username = Objects.requireNonNull(evaluateXPath("/Requests/Username/text()")).get(0);
            vrtV.onEndRequest(username);
            return true;
        }
        return false;
    }

    /**
     * This method creates the document object and parses 'msgIn' file
     * @return the parsed xml file
     * @throws Exception error during xml parsing
     */

    private Document getDocument() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse("src/main/resources/server/msgIn");
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
