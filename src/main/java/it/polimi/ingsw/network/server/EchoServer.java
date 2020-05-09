package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.MatchController;
import it.polimi.ingsw.view.server.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class manages a multi-client TCP socket connection (server side).
 * @author marcoDige
 */

public class EchoServer implements ClientDisconnectionListener {

    //attributes

    private final int port;
    private ServerSocket server;

    private final List<VirtualView> lobbies;

    //constructors

    public EchoServer(int port){
        this.port = port;
        lobbies = new ArrayList<>();
        lobbies.add(new VirtualView(new MatchController(),this, 1));
    }

    //methods

    /**
     * This method allows server to start to accept a connection from a client and create a thread which manages a
     * specific client connection (the connection is multi-client).
     * It allows to create a new lobby if existing lobbies are full or the matches into them have already started.
     */

    public void start(){
        ExecutorService executor = Executors.newCachedThreadPool();

        initializeServer();

        while (true) {
            try {
                Socket client = server.accept();
                boolean matchFind = false;
                for (VirtualView v : lobbies){
                    if(v.getLobbySize() < 3 && !v.getMatchStarted()){
                        executor.submit(new ClientHandler(client,v,lobbies.indexOf(v) + 1));
                        matchFind = true;
                        break;
                    }
                }

                if(!matchFind){
                    System.out.println("\nLobby number " + lobbies.size() + " full, Server creates a new lobby...");
                    VirtualView v = new VirtualView(new MatchController(),this, lobbies.size() + 1);
                    lobbies.add(v);
                    executor.submit(new ClientHandler(client,v,lobbies.indexOf(v) + 1));
                }

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

    @Override
    public void onClientDown(VirtualView v) {
        lobbies.remove(v);
    }
}
