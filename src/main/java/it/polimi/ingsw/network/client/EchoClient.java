package it.polimi.ingsw.network.client;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;


/**
 * This class manages a TCP socket connection (client side).
 * @author marcoDige
 */

public class EchoClient {

    //attributes

    private final String hostName;
    private final int port;
    private Socket server;

    //constructor

    public EchoClient(String hostname, int port){
        this.hostName = hostname;
        this.port = port;
    }

    //methods

    public void start(){

        File inFile = new File("src/main/resources/xml/client/msgIn");
        initializeClientConnection();

        try {
            InputStream in = server.getInputStream();
            FileOutputStream fileToWrite = new FileOutputStream(inFile, false);

            while(true) {
                byte[] buffer = new byte[2000];
                int r = in.read(buffer);
                fileToWrite.write(buffer,0,r);
                if(isEndMode()){
                    break;
                }else{
                    processMsg();
                    fileToWrite.flush();
                }
            }

            fileToWrite.close();
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

    private boolean isEndMode(){
        return false;
    }

    private void processMsg(){

    }
}
