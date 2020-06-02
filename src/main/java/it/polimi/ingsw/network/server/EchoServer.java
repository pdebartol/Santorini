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

public class EchoServer implements ClientDisconnectionListener{

    //attributes

    private final int port;
    private ServerSocket server;
    private ExecutorService executor;

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
     * It allows to create a new lobby if existing lobbies are full or the match into them have already started.
     */

    public void start(){

        executor = Executors.newCachedThreadPool();
        initializeServer();

        //The server listens for a new connection and searches for a lobby where the new client can enter (if there isn't
        //server provides to create a new lobby and enters the client into this)
        while (true) {
            try {
                Socket client = server.accept();

                findAMatch(client);

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

    /**
     * This method searches a free lobby for the new connected client.
     * If there is a free lobby it inserts it in this lobby, otherwise it creates a new one of which this
     * client becomes its creator.
     * @param client is the new client's socket.
     */

    public void findAMatch(Socket client){
        boolean matchFind = false;
        for (VirtualView v : lobbies){
            if(v.getLobbySize() < 3 && !v.getMatchStarted()){
                executor.submit(new ClientHandler(client,v,lobbies.indexOf(v) + 1));
                matchFind = true;
                break;
            }
        }

        //The server have to create a new lobby because there isn't a free one.
        if(!matchFind){
            System.out.println("Server creates a new lobby...");
            VirtualView v = new VirtualView(new MatchController(),this, lobbies.size() + 1);
            lobbies.add(v);
            executor.submit(new ClientHandler(client,v,lobbies.indexOf(v) + 1));
        }
    }

    /**
     * This method is called when a client connection go down, it provides to remove the lobby where this client was
     * @param v is the virtual view represents a lobby/match
     */

    @Override
    public void onClientDown(VirtualView v) {
        System.out.println("Lobby number " + (lobbies.indexOf(v) + 1) + " deleted!");

        lobbies.remove(v);
    }

    /**
     * This method is called when the match have to finish. It provides to visualize a message on the server and to
     * delete the lobby where the match was evolving.
     * @param v is the lobby's virtualView
     */

    @Override
    public void onMatchFinish(VirtualView v){
        System.out.println("\nLobby number " + (lobbies.indexOf(v) + 1) + " deleted!");

        lobbies.remove(v);
    }
}
