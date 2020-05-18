package it.polimi.ingsw.view.client;

import it.polimi.ingsw.view.client.viewComponents.God;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.util.*;

/** This class is responsible for parsing the XML and creating the
 * gods according to the rules in 'godInfo'
 * @author marcoDige
 */

public class GodsGenerator {

    //attributes

    private Document document;

    //constructors

    public GodsGenerator() {
        try{
            this.document = this.getDocument();
        }
        catch (Exception e){
            System.out.println("Error during XML parsing.");
        }
    }

    //methods

    //TODO : javadoc

    public List<God> getGods(){
        List<God> gods = new ArrayList<>();
        NodeList godTag = document.getElementsByTagName("God");
        NodeList idTags = document.getElementsByTagName("id");
        NodeList nameTags = document.getElementsByTagName("name");
        NodeList descriptionTags = document.getElementsByTagName("description");

        for(int i = 0; i < godTag.getLength(); i++){
            int id = Integer.parseInt(idTags.item(i).getTextContent());
            String name = nameTags.item(i).getTextContent();
            String description = descriptionTags.item(i).getTextContent();
            gods.add(new God(id,name,description));
        }

        return gods;
    }

    /**
     * This method creates the document object and parses 'godInfo' file
     * @return the parsed xml file
     * @throws Exception error during xml parsing
     */

    private Document getDocument() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(this.getClass().getResourceAsStream("/xml/client/godInfo"));
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
