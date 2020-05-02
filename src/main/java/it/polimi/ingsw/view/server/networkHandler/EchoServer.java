package it.polimi.ingsw.view.server.networkHandler;

import it.polimi.ingsw.controller.MatchController;
import it.polimi.ingsw.view.server.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class manages a multi-client TCP socket connection (server side).
 * @author marcoDige
 */

public class EchoServer {

    //attributes

    private final int port;
    private ServerSocket server;
    private final VirtualView virtualView;

    //constructors

    public EchoServer(int port){
        this.port = port;
        virtualView = new VirtualView(new MatchController());
    }

    //methods

    public static void main(String[] args)
    {
        int p;
        if(args.length == 1) p = Integer.parseInt(args[0]);
        else p = 1234;

        EchoServer echoServer = new EchoServer(p);
        echoServer.start();
    }

    /**
     * This method allows server to start to accept a connection from a client and create a thread which manages a
     * specific client connection (the connection is multi-client).
     */

    public void start(){
        ExecutorService executor = Executors.newCachedThreadPool();

        initializeServer();

        while (true) {
            try {
                Socket client = server.accept();
                executor.submit(new ClientHandler(client,virtualView));
            } catch(IOException e) {
                break;
            }
        }

        executor.shutdown();

    }

    /**
     * This method initializes a ServerSocket on port.
     */

    public void initializeServer(){
        try{
            server = new ServerSocket(port);
        }catch (IOException e){
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Server socket ready on port: " + port);
    }

}
