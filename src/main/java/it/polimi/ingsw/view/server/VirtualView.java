package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ControllerActionListener;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;

import java.net.Socket;
import java.util.*;

public class VirtualView implements ViewActionListener{

    //attributes

    private final ControllerActionListener controllerListener;
    private final Map<String,Socket> clients;

    //constructors

    public VirtualView(ControllerActionListener l){
        this.controllerListener = l;
        this.clients = new HashMap<>();
    }

    //methods

    public void removeClientBySocket(Socket socket){
        Set<String> usernames = clients.keySet();
        for (String u : usernames)
            if(socket.equals(clients.get(u))) clients.remove(u);
    }

    public void removeClientByUsername(String username){
        clients.remove(username);
    }

    //Request Methods

    public void loginRequest(String username, Color color, Socket socket){
        ArrayList<Error> errors = controllerListener.onNewPlayer(username, color);
        if(errors.isEmpty()) clients.put(username,socket);

        //TODO if !empty send errors back to client
    }

    public void startGameRequest(String username){
        String challengerUsername = controllerListener.onStartGame();

        //TODO send challenger back to clients
    }

    public void createGodsRequest(String username, ArrayList<Integer> ids){
        ArrayList<Error> errors = controllerListener.onChallengerChooseGods(username, ids);

        //TODO if !empty send errors back to client
    }

    public void choseGodRequest(String username, int godId){
       ArrayList<Error> errors = controllerListener.onPlayerChooseGod(username, godId );
    }

    public void chooseStartingPlayerRequest(String username, String playerChosen){

    }




    public void setupOnBoardRequest(String username, int workerId, int x, int y){

    }

    public void moveRequest(String username, int workerId, int x, int y){

    }

    public void buildRequest(String username, int workerId, int x, int y){

    }

    public void endOfTurn(String username){

    }

    public void onEndRequest(String username){
        removeClientByUsername(username);
    }

    // Answer Methods

}
