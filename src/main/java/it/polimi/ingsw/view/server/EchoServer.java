package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ActionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    //attributes

    private int port;
    private ServerSocket server;

    //constructors

    public EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args)
    {
        EchoServer echoServer = new EchoServer(1234);
        echoServer.start();
    }

    //methods

    public void start(){
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            initializeConnection();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        while (true) {
            try {
                Socket socket = server.accept();
                executor.submit(new ClientHandler(socket));
            } catch(IOException e) {
                break;
            }
        }

        executor.shutdown();

    }

    public void initializeConnection() throws IOException{
        server = new ServerSocket(port);
        System.out.println("Server socket ready on port: " + port);
    }

}
