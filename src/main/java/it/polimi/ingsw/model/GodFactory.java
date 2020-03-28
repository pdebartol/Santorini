package it.polimi.ingsw.model;


import it.polimi.ingsw.model.decorators.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class GodFactory {
    ArrayList<God> gods;

    public GodFactory(ArrayList<Integer> ids) {
        gods = new ArrayList<>();
        try{

            for (Integer currentGod : ids) {
                Stack<Integer> powers_id = new Stack<>();
                String name = evaluateXPath( "/Divinities/God[id='" + currentGod + "']/name/text()").get(0);
                String description =  evaluateXPath( "/Divinities/God[id='" + currentGod + "']/description/text()").get(0);
                int maxMoves =  Integer.parseInt(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/maxMoves/text()").get(0));
                int maxBuilds = Integer.parseInt(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/maxBuilds/text()").get(0));

                if (evaluateXPath( "/Divinities/God[id='" + currentGod + "']/BuildBeforeMove/text()").get(0).equals("true") ){
                    powers_id.push(-1);
                }

                if (evaluateXPath( "/Divinities/God[id='" + currentGod + "']/CanMoveUp/text()").get(0).equals("true") ){
                    powers_id.push(0);
                }

                if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/CantMoveUp/text()").get(0).equals("true")){
                    powers_id.push(1);
                }
                if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/MoveSwap/text()").get(0).equals("true")){
                    powers_id.push(2);
                }
                if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/MovePush/text()").get(0).equals("true")){
                    powers_id.push(3);
                }
                if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/Down2Win/text()").get(0).equals("true")){
                    powers_id.push(4);
                }
                if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/ExtraMove/canMove/text()").get(0).equals("true")){
                    if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/ExtraMove/notMoveBack/text()").get(0).equals("true")){
                        powers_id.push(5);
                    }
                    else{
                        powers_id.push(6);
                    }
                }
                if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/ExtraBuild/canBuild/text()").get(0).equals("true")){
                    if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/ExtraBuild/onlySameSpace/text()").get(0).equals("true")){
                        powers_id.push(7);
                    }
                    else if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/ExtraBuild/notSameSpace/text()").get(0).equals("true")) {
                        powers_id.push(8);
                    }
                    else{
                        powers_id.push(9);
                    }
                }
                if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/BuildDomeEverywhere/text()").get(0).equals("true")){
                    powers_id.push(10);
                }
                if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/BlockMoveUp/text()").get(0).equals("true")){
                    powers_id.push(11);
                }
                if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/BuildUnderfoot/text()").get(0).equals("true")){
                    powers_id.push(12);
                }
                if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/NoWinPerimeter/text()").get(0).equals("true")){
                    powers_id.push(13);
                }
                if(evaluateXPath( "/Divinities/God[id='" + currentGod + "']/EndRemoveNeighbour/text()").get(0).equals("true")){
                    powers_id.push(14);
                }

                gods.add(new God(name, description,getPower(powers_id, new StandardPower(maxMoves,maxBuilds))));
            }
        }
        catch(Exception e){
            System.out.println(e);

        }
    }

    public ArrayList<God> getGods(){
        for(God currentGod: gods){
            if(currentGod.getPower()  instanceof BlockMoveUp){
                for(God temp: gods ){
                    if (!(temp.getPower() instanceof BlockMoveUp)){
                        temp.setPower(new CantMoveUp(temp.getPower()));
                    }
                }
            }
            if(currentGod.getPower() instanceof NoWinPerimeter){
                for(God temp: gods){
                    if (!(temp.getPower() instanceof NoWinPerimeter)){
                        temp.setPower(new NoWinPerimeter(temp.getPower(),false));
                    }
                }
            }
        }


        return gods;
    }



    public Power getPower(Stack<Integer> powers, Power temp){
        if(powers.empty()){
            return temp;
        }
        else{
            switch(powers.pop()){
                case -1:
                    temp = new BuildBeforeMove(temp);
                    return getPower(powers, temp);
                case 0:
                    temp = new CanMoveUp(temp);
                    return getPower(powers, temp);
                case 1:
                    temp =  new CantMoveUp(temp);
                    return getPower(powers, temp);
                case 2:
                    temp =  new MoveSwap(temp);
                    return getPower(powers, temp);
                case 3:
                    temp =  new MovePush(temp);
                    return getPower(powers, temp);
                case 4:
                    temp =  new Down2Win(temp);
                    return getPower(powers, temp);
                case 5:
                    temp = new ExtraMove(temp, true, false );
                    return getPower(powers,temp);
                case 6:
                    temp = new ExtraMove(temp, false, true );
                    return getPower(powers,temp);
                case 7:
                    temp = new ExtraBuild(temp, true, false,false );
                    return getPower(powers,temp);
                case 8:
                    temp = new ExtraBuild(temp, false, true,false );
                    return getPower(powers,temp);
                case 9:
                    temp = new ExtraBuild(temp, false, false,true );
                    return getPower(powers,temp);
                case 10:
                    temp = new BuildDomeEverywhere(temp);
                    return getPower(powers,temp);
                case 11:
                    temp = new BlockMoveUp(temp);
                    return getPower(powers,temp);
                case 12:
                    temp = new BuildUnderfoot(temp);
                    return getPower(powers,temp);
                case 13:
                    temp = new NoWinPerimeter(temp, true);
                    return getPower(powers,temp);
                case 14:
                    temp = new EndRemoveNeighbour(temp);
                    return getPower(powers,temp);
            }


        }
        return temp;

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

