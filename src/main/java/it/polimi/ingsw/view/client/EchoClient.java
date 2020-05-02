package it.polimi.ingsw.view.client;

import java.io.*;
import java.net.Socket;

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

    public static void main(String[] args){
        EchoClient echoClient;
        if (args.length == 2)
            echoClient = new EchoClient(args[0], Integer.parseInt(args[1]));
        else
            echoClient = new EchoClient("localhost", 1234);

        echoClient.start();
    }



    private void start(){

        File inFile = new File("src/main/resources/client/msgIn");
        initializeClientConnection();
        new MsgSender(server).sendMsg();

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
