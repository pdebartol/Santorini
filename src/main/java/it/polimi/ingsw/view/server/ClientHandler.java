package it.polimi.ingsw.view.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    //attributes

    private Socket client;

    //constructors

    public ClientHandler(Socket socket) {
        this.client = socket;
    }

    //methods

    @Override
    public void run() {
        File file = new File("msg.xml");

        try {
            byte[] bytearray = new byte[(int) file.length()];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            in.read(bytearray, 0, bytearray.length);
            OutputStream out = client.getOutputStream();
            out.write(bytearray, 0, bytearray.length);
            out.flush();

            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
