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

    public void start(String username, String color){

        initializeClientConnection();

        new MsgSender(server, new RequestMsgWriter().loginRequest(username, Color.valueOfLabel(color))).sendMsg();

        try {
            InputStream in = server.getInputStream();

            while(true) {

                receiveXML(in);

                if(isEndMode()){
                    break;
                }else{
                    processMsg();
                }
            }

            in.close();
            System.out.println("Connection closed!\n");
            server.close();
        }catch (IOException | SAXException | ParserConfigurationException e){
            System.err.println("Connection down!\n");
            try {
                server.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //TODO Manage server disconnection
        }
    }

    public void initializeClientConnection(){
        try{
            server = new Socket(hostName, port);
        }catch (IOException e){
            System.err.println("Don't know about host " + hostName + "\n");
            return;
        }
        System.out.println("Connection established! \n");
    }

    private void receiveXML(InputStream in) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        XMLInputStream xmlIn = new XMLInputStream(in);


        docBuilder = docBuilderFact.newDocumentBuilder();
        xmlIn.receive();
        msgIn = docBuilder.parse(xmlIn);
    }

    private boolean isEndMode(){
        return false;
    }

    private void processMsg(){
        if(msgIn.getElementsByTagName("Outcome").item(0) != null)
            System.out.println(msgIn.getElementsByTagName("Outcome").item(0).getTextContent());
        else
            System.out.println(msgIn.getElementsByTagName("Author").item(0).getTextContent());
    }
}
