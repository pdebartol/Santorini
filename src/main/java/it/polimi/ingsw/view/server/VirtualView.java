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
    private String starter;

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

    //Request Methods

    public synchronized void loginRequest(String username, Color color, Socket socket){
        ArrayList<Error> errors = controllerListener.onNewPlayer(username, color);
        if(errors.isEmpty()){
            if(clients.isEmpty()) starter = username;
            clients.put(username,socket);
            new XMLMessageWriter("toSendRequest").loginAcceptedAnswer(username);
        }else{
            new XMLMessageWriter("toSendRequest").loginRejectedAnswer(username,errors);
        }
        new MsgSender(socket).sendMsg("toSendRequest");

        //TODO send msg to other clients

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
        ArrayList<Error> errors = controllerListener.onChallengerChooseStartingPlayer(username, playerChosen);

        //TODO if !empty send errors back to client
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
        clients.remove(username);
    }

    // Answer Methods

    // Communication Methods
}
