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
     * This method allows to take a file from connection with client and to start the Request process and send Answer to client.
     */

    @Override
    public void run() {

        File file = new File("src/main/resources/arrivedRequest.xml");

        try {
            InputStream in = client.getInputStream();
            FileOutputStream out = new FileOutputStream(file, false);

            byte[] buffer = new byte[2000];
            int r = in.read(buffer);
            out.write(buffer,0, r);

            processRequest();
            sendAnswer();

            out.flush();
            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
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
