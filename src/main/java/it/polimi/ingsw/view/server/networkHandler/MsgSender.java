package it.polimi.ingsw.view.server.networkHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This class implements sending a file to the server through a socket connection.
 * @author marcoDige
 */

public class MsgSender {

    private Socket socket;

    public MsgSender(Socket s){
        this.socket = s;
    }

    /**
     * This method allows to send filename to the client through socket. It read filename in a buffer and write content into
     * OutputStream.
     * @param fileName is the name of the file caller want to send
     */

    public void sendMsg(String fileName) {
        File file = new File("src/main/resources/server/" + fileName);

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
