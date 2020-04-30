package it.polimi.ingsw.view.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * This class manages a single client TCP socket connection.
 * @author marcoDige
 */

public class ClientHandler implements Runnable{

    //attributes

    private final Socket client;
    private final VirtualView virtualView;

    //constructors

    public ClientHandler(Socket socket, VirtualView vrtV) {
        this.client = socket;
        this.virtualView = vrtV;
    }

    //methods

    /**
     * This method allows to take a file from connection with client, to start the Request process and send Answer to client
     * The communication go down when client send a "end" mode file.
     */

    @Override
    public void run() {

        File requestFile = new File("src/main/resources/server/msgIn");
        System.out.println("Client " + client + " has connected!");

        try {
            InputStream in = client.getInputStream();
            FileOutputStream fileIn = new FileOutputStream(requestFile, false);

            while(true) {
                byte[] buffer = new byte[2000];
                int r = in.read(buffer);
                fileIn.write(buffer,0,r);
                if(isEndMode()){
                    break;
                }else{
                    if(!isLoginRequest())
                        processRequest();
                    fileIn.flush();
                }
            }

            fileIn.close();
            in.close();
            System.out.println("Connection with " + client + " closed!");
            client.close();
        } catch(IOException e) {
            //TODO: Manage client disconnection
            virtualView.removeClientBySocket(client);
            System.err.println("Client " + client + " has disconnected!");
        }
    }

    /**
     * This method verify if the request mode is "end".
     * @return true -> "end" request mode
     *         false -> not "end" request mode
     */

    private boolean isEndMode(){
        return new RequestParser().parseEndRequest(virtualView);
    }

    /**
     * This method start a client request processing in server. It uses a RequestParser to start this process.
     */

    private void processRequest(){
        new RequestParser().parseRequest(virtualView);
    }

    /**
     * This method verify if the request mode is "login".
     * @return true -> "login" request mode
     *         false -> not "login" request mode
     */

    private boolean isLoginRequest() {return new RequestParser().parseLoginRequest(virtualView,client);}
}
