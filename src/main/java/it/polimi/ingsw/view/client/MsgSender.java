package it.polimi.ingsw.view.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This class implements sending a file to a client through a socket connection.
 * @author marcoDige
 */

public class MsgSender {
    private Socket socket;

    public MsgSender(Socket s){
        this.socket = s;
    }

    /**
     * This method allows to send a Request to the server through socket. It read toSendRequest in a buffer and write content into
     * OutputStream.
     */

    public void sendMsg() {
        File file = new File("src/main/resources/client/toSendRequest");

        try {

            OutputStream out = socket.getOutputStream();
            FileInputStream fileIn = new FileInputStream(file);

            byte[] buffer = new byte[2000];
            int r = fileIn.read(buffer);
            out.write(buffer, 0, r);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
