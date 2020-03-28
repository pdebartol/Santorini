package it.polimi.ingsw.model;


import it.polimi.ingsw.model.decorators.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.util.*;

public class GodsFactory {

    //attributes

    ArrayList<God> gods;
    Map<Integer,String> forAll;

    //constructors

    public GodsFactory(ArrayList<Integer> ids) {
        gods = new ArrayList<>();
        forAll = new HashMap<>();

        try{
            for (Integer currentGod : ids) {
                Stack<Integer> powers_id = new Stack<>();
                String name = Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/name/text()")).get(0);
                String description =  Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/description/text()")).get(0);
                int maxMoves =  Integer.parseInt(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/maxMoves/text()")).get(0));
                int maxBuilds = Integer.parseInt(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/maxBuilds/text()")).get(0));

                if (Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/CanMoveUp/text()")).get(0).equals("true") ){
                    powers_id.push(0);
                }

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
                    forAll.put(0,name);
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/BuildUnderfoot/text()")).get(0).equals("true")){
                    powers_id.push(12);
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/NoWinPerimeter/text()")).get(0).equals("true")){
                    forAll.put(13,name);
                }
                if(Objects.requireNonNull(evaluateXPath("/Divinities/God[id='" + currentGod + "']/EndRemoveNeighbour/text()")).get(0).equals("true")){
                    powers_id.push(14);
                }

                gods.add(new God(name, description,getPowers(powers_id,new StandardPower(maxMoves,maxBuilds))));
            }
            giveToAll();
        }
        catch(Exception e){
            System.out.println("exception");
        }
    }

    //methods

    public ArrayList<God> getGods(){
        return gods;
    }

    private Power getPowers(Stack<Integer> powers, Power temp){
        if(powers.empty()){
            return temp;
        }
        else{
            switch(powers.pop()){
                case 0:
                    temp = new CanMoveUp(temp);
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

    private void giveToAll(){
        if(!forAll.isEmpty()){
            for(Integer power: forAll.keySet())
                for(God god: gods)
                    if(!god.getName().equals(forAll.get(power))) {
                        Stack<Integer> powers_id = new Stack<>();
                        powers_id.push(power);
                        god.setPower(getPowers(powers_id,god.getPower()));
                    }
        }
    }

    private  Document getDocument() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse("godConfig.xml");
    }

    private  List<String> evaluateXPath( String xpathExpression) throws Exception {
        try {
            Document document = this.getDocument();

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

