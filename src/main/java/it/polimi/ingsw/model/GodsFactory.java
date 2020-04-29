package it.polimi.ingsw.model;


import it.polimi.ingsw.model.decorators.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.util.*;

/** This class is responsible for parsing the XML and creating the
 * gods according to the rules in 'godConfig'
 * @author pierobartolo & marcoDige
 */

public class GodsFactory {

    //attributes

    private Board gameBoard;
    private Document document;
    BlockMoveUp bmu_god;

    //constructors


    /**
     * Class constructor
     * @param gameBoard is the current Board object
     */

    public GodsFactory( Board gameBoard) {
        this.gameBoard = gameBoard;
        try{
            this.document = this.getDocument();
        }
        catch (Exception e){
            System.out.println("Error during XML parsing.");
        }
    }

    //methods

    /**
     * This method returns an ArrayList<God> with all the Gods specified in the ids parameter.
     * It parses 'godConfig' creating an instance of each God as described in the file.
     * In doing so, it uses a Stack<Integer> to save the God's powers and a Map<Integer,String>
     * to save powers which need to be applied to others Gods
     * @param ids ArrayList<Integer> containing the id of every God the controller wants to create
     *            (God's id correspond to the God's number reported in the official rules)
     *
     * @return an ArrayList<God> with all created gods
     */

    public ArrayList<God> getGods(ArrayList<Integer> ids){
        if(ids == null || ids.isEmpty()) throw new IllegalArgumentException("No ids given!");
        ArrayList<God> gods = new ArrayList<>();
        Map<Integer,String>  applyToAll = new HashMap<>();
        try{
            for (Integer currentGod : ids) {
                Stack<Integer> powers_id = new Stack<>();
                String name = Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/name/text()")).get(0);
                String description =  Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/description/text()")).get(0);
                int maxMoves =  Integer.parseInt(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/maxMoves/text()")).get(0));
                int maxBuilds = Integer.parseInt(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/maxBuilds/text()")).get(0));


                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/BuildBeforeMove/text()")).get(0).equals("true")){
                    powers_id.push(1);
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/MoveSwap/text()")).get(0).equals("true")){
                    powers_id.push(2);
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/MovePush/text()")).get(0).equals("true")){
                    powers_id.push(3);
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/Down2Win/text()")).get(0).equals("true")){
                    powers_id.push(4);
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/ExtraMove/canMove/text()")).get(0).equals("true")){
                    if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/ExtraMove/notMoveBack/text()")).get(0).equals("true")){
                        powers_id.push(5);
                    }
                    else{
                        powers_id.push(6);
                    }
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/ExtraBuild/canBuild/text()")).get(0).equals("true")){
                    if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/ExtraBuild/onlySameSpace/text()")).get(0).equals("true")){
                        powers_id.push(7);
                    }
                    else if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/ExtraBuild/notSameSpace/text()")).get(0).equals("true")) {
                        powers_id.push(8);
                    }
                    else{
                        powers_id.push(9);
                    }
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/BuildDomeEverywhere/text()")).get(0).equals("true")){
                    powers_id.push(10);
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/BlockMoveUp/text()")).get(0).equals("true")){
                    powers_id.push(11);
                    applyToAll.put(0,name);
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/BuildUnderfoot/text()")).get(0).equals("true")){
                    powers_id.push(12);
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/NoWinPerimeter/text()")).get(0).equals("true")){
                    applyToAll.put(13,name);
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/EndRemoveNeighbour/text()")).get(0).equals("true")){
                    powers_id.push(14);
                }

                if(!powers_id.empty() && powers_id.peek() == 11 ){
                    bmu_god = (BlockMoveUp) getPowers(powers_id,new StandardPower(maxMoves,maxBuilds, gameBoard));
                    gods.add(new God(name, description,bmu_god));
                }
                else{
                    gods.add(new God(name, description,getPowers(powers_id,new StandardPower(maxMoves,maxBuilds, gameBoard))));
                }
            }

            decorateOtherGods(gods, applyToAll);

        }
        catch(Exception e){
            System.out.println("Error during XML Parsing");
        }
        return gods;
    }

    /**
     * This method decorates the God with all of its power
     * @param powers is a Stack<Integer> with all the powers of a specific god
     * @param temp is the instance of Power decorated during each recursive step
     * @return the decorated Power
     */

    private Power getPowers(Stack<Integer> powers, Power temp){
        if(powers.empty()){
            return temp;
        }
        else{
            switch(powers.pop()){
                case 0:
                    temp = new CanMoveUp(temp,bmu_god);
                    return getPowers(powers, temp);
                case 1:
                    temp =  new BuildBeforeMove(temp);
                    return getPowers(powers, temp);
                case 2:
                    temp =  new MoveSwap(temp);
                    return getPowers(powers, temp);
                case 3:
                    temp =  new MovePush(temp);
                    return getPowers(powers, temp);
                case 4:
                    temp =  new Down2Win(temp);
                    return getPowers(powers, temp);
                case 5:
                    temp = new ExtraMove(temp, true, false );
                    return getPowers(powers,temp);
                case 6:
                    temp = new ExtraMove(temp, false, true );
                    return getPowers(powers,temp);
                case 7:
                    temp = new ExtraBuild(temp, true, false,false );
                    return getPowers(powers,temp);
                case 8:
                    temp = new ExtraBuild(temp, false, true,false );
                    return getPowers(powers,temp);
                case 9:
                    temp = new ExtraBuild(temp, false, false,true );
                    return getPowers(powers,temp);
                case 10:
                    temp = new BuildDomeEverywhere(temp);
                    return getPowers(powers,temp);
                case 11:
                    temp = new BlockMoveUp(temp);
                    return getPowers(powers,temp);
                case 12:
                    temp = new BuildUnderfoot(temp);
                    return getPowers(powers,temp);
                case 13:
                    temp = new NoWinPerimeter(temp);
                    return getPowers(powers,temp);
                case 14:
                    temp = new EndRemoveNeighbour(temp);
                    return getPowers(powers,temp);
            }
        }
        return temp;
    }


    /**
     * This method is used when a God's power should be applied to all the others god present in the game
     * @param gods contains all the gods created
     * @param  applyToAll contains the god's name whose power should be applied to all the other
     */

    private void decorateOtherGods(ArrayList<God> gods, Map<Integer,String> applyToAll){
        if(!applyToAll.isEmpty()){
            for(Integer power: applyToAll.keySet())
                for(God god: gods)
                    if(!god.getName().equals(applyToAll.get(power))) {
                        Stack<Integer> powers_id = new Stack<>();
                        powers_id.push(power);
                        god.setPower(getPowers(powers_id,god.getPower()));
                    }
        }
    }


    /**
     * This method creates the document object and parses 'godConfig' file
     * @return the parsed xml file
     * @throws Exception error during xml parsing
     */

    private  Document getDocument() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse("src/main/resources/godConfig");
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

