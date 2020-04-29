package it.polimi.ingsw.view.server;

import java.io.*;
import java.net.Socket;

/**
 * This class manages a single client TCP socket connection.
 * @author marcoDige
 */

public class ClientHandler implements Runnable{

    //attributes

    private final Socket client;

    //constructors

    public ClientHandler(Socket socket) {
        this.client = socket;
    }

    //methods

    /**
     * This method allows to take a file from connection with client, to start the Request process and send Answer to client
     * The communication go down when client send a "end" mode file.
     */

    @Override
    public void run() {

        File fileIn = new File("src/main/resources/arrivedRequest");
        File fileOut = new File("src/main/resources/toSendAnswer");

        try {
            System.out.println("Client " + client + " connection has done!");
            InputStream in = client.getInputStream();
            FileOutputStream outFileIn = new FileOutputStream(fileIn, false);
            FileInputStream inFileOut = new FileInputStream(fileOut);
            OutputStream out = client.getOutputStream();

            while(true) {
                byte[] buffer = new byte[2000];
                int rIn = in.read(buffer);
                outFileIn.write(buffer,0,rIn);
                if(isEndMode()){
                    break;
                }else{
                    processRequest();
                    outFileIn.flush();

                    int rOut = inFileOut.read(buffer);
                    out.write(buffer,0,rOut);
                    out.flush();
                }
            }

            outFileIn.close();
            inFileOut.close();
            out.close();
            in.close();
            System.out.println("Connection with " + client + " closed!");
            client.close();
        } catch (IOException e) {
            System.err.println("Client disconnection!");
        }
    }

    /**
     * This method verify if the request mode is "end".
     * @return true -> "end" request mode
     *         false -> not "end" request mode
     */

    private boolean isEndMode(){
        return new RequestParser().parseEndRequest();
    }

    /**
     * This method start a client request processing in server. It uses a RequestParser to start this process.
     */

    private void processRequest(){
        new RequestParser().parseRequest();
    }
}
