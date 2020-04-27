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

        File file = new File("src/main/resources/arrivedRequest.xml");

        try {
            System.out.println("Client " + client + " connection has done!");
            InputStream in = client.getInputStream();
            FileOutputStream outFile = new FileOutputStream(file, false);
            OutputStream out = client.getOutputStream();
            byte[] buffer = new byte[2000];
            int r = in.read(buffer);
            outFile.write(buffer, 0, r);

            processRequest();
            sendAnswer();

            outFile.flush();
            out.flush();
            outFile.close();
            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            System.err.println("Client disconnection!");
        }
    }

    private boolean isEndMode(){
        return new RequestParser().parseEndRequest();
    }

    /**
     * This method start a client request processing in server. It uses a RequestParser to start this process.
     */

    private void processRequest(){
        new RequestParser().parseRequest();
    }

    private void sendAnswer(){

    }
}
