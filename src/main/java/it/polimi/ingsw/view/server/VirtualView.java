package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ControllerActionListener;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.view.server.msgHandler.MsgOutWriter;
import it.polimi.ingsw.view.server.networkHandler.MsgSender;

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

            new MsgOutWriter().loginAcceptedAnswer(username,color);
            for (String user : clients.keySet())
                if(!user.equals(username))
                    new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        }else{
            new MsgOutWriter().loginRejectedAnswer(username,errors);
        }

        new MsgSender(socket).sendMsg("toSendAnswer");
    }

    public void startGameRequest(String username){
        String challengerUsername = controllerListener.onStartGame();
        System.out.println("OK");

        new MsgOutWriter().startGameAcceptedAnswer(username);

        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void createGodsRequest(String username, ArrayList<Integer> ids){
        ArrayList<Error> errors = controllerListener.onChallengerChooseGods(username, ids);

        if(errors.isEmpty()){
            new MsgOutWriter().createGodsAcceptedAnswer(username,ids);
            for (String user : clients.keySet())
                new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        }else{
            new MsgOutWriter().createGodsRejectedAnswer(username,errors);
            new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
        }
    }

    public void choseGodRequest(String username, int godId){
       ArrayList<Error> errors = controllerListener.onPlayerChooseGod(username, godId );

        //TODO if !empty send errors back to client
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
