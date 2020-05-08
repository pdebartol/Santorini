package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.msgUtilities.client.RequestMsgWriter;
import it.polimi.ingsw.network.MsgSender;
import it.polimi.ingsw.network.XMLInputStream;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;


/**
 * This class manages a TCP socket connection (client side).
 * @author marcoDige
 */

public class EchoClient {

    //attributes

    private final String hostName;
    private final int port;
    private Socket server;

    private Document msgIn;

    //constructor

    public EchoClient(String hostname, int port){
        this.hostName = hostname;
        this.port = port;
        this.msgIn= null;
    }

    //methods

    public void start(){

        initializeClientConnection();
        new MsgSender(server,new RequestMsgWriter().loginRequest("Marco", Color.WHITE));

        try {
            InputStream in = server.getInputStream();

            while(true) {

                receiveXML(in);
                processMsg();

                if(isEndMode()){
                    break;
                }else{
                    processMsg();
                }
            }

            in.close();
            System.out.println("Connection closed!");
            server.close();
        }catch (IOException e){
            System.err.println("Connection down!");
            //TODO Manage server disconnection
        }
    }

    public void initializeClientConnection(){
        try{
            server = new Socket(hostName, port);
        }catch (IOException e){
            System.err.println("Don't know about host " + hostName);
            return;
        }
        System.out.println("Connection established!");
    }

    private void receiveXML(InputStream in){
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        XMLInputStream xmlIn = new XMLInputStream(in);


        try {
            docBuilder = docBuilderFact.newDocumentBuilder();
            xmlIn.receive();
            msgIn = docBuilder.parse(xmlIn);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isEndMode(){
        return false;
    }

    private void processMsg(){
        System.out.println(msgIn.getXmlEncoding());
    }
}
