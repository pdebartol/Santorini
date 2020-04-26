package it.polimi.ingsw.view.server;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.util.*;

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

    public void parseRequest(){
        String mode = Objects.requireNonNull(evaluateXPath("/Request/Mode/text()")).get(0);
        String username = Objects.requireNonNull(evaluateXPath("/Request/Username/text()")).get(0);

        switch (mode){
            case "login" :
                String color = Objects.requireNonNull(evaluateXPath("/Request/Login/Color/text()")).get(0);
                virtualView.loginRequest(username,color);
            case "creategods" :
                for(int i = 0; i < 3 ; ++i){

                }
        }
    }

    /**
     * This method creates the document object and parses 'arrivedRequest.xml' file
     * @return the parsed xml file
     * @throws Exception error during xml parsing
     */

    private  Document getDocument() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse("src/main/resources/arrivedRequest.xml");
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
