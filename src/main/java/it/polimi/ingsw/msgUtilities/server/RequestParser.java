package it.polimi.ingsw.msgUtilities.server;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.server.VirtualView;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.xpath.*;
import java.net.Socket;
import java.util.*;

/** This class is responsible for parsing the msgIn XML and call VirtualView methods to start
 * the request processing.
 * @author marcoDige
 */

public class RequestParser {

    //attributes

    private Document document;

    public RequestParser(Document document){
        this.document = document;
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
                vrtV.choseStartingPlayerRequest(username,playerChosen);
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
            case "setWorkerOnBoard" :
                String sWorkerGender = Objects.requireNonNull(evaluateXPath(standardPath +"/Position/WorkerId/text()")).get(0);
                int x = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Positions/Position[@WorkerGender=\"male\"]/xPosition/text()")).get(0));
                int y = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Positions/Position[@WorkerGender=\"male\"]/yPosition/text()")).get(0));
                vrtV.setupOnBoardRequest(username,sWorkerGender,x,y);
                break;
            case "move" :
                String mWorkerGender = Objects.requireNonNull(evaluateXPath(standardPath +"/Position/WorkerId/text()")).get(0);
                int xm = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/xPosition/text()")).get(0));
                int ym = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/yPosition/text()")).get(0));
                vrtV.moveRequest(username,mWorkerGender,xm,ym);
                break;
            case "build":
                String bWorkerGender = Objects.requireNonNull(evaluateXPath(standardPath +"/Position/WorkerId/text()")).get(0);
                int xb = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/xPosition/text()")).get(0));
                int yb = Integer.parseInt(Objects.requireNonNull(evaluateXPath(standardPath +"/Position/yPosition/text()")).get(0));
                vrtV.buildRequest(username,bWorkerGender,xb,yb);
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
            vrtV.endRequest(username);
            return true;
        }
        return false;
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
