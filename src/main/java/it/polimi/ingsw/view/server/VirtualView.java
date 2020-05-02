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
            new MsgOutWriter().rejectedAnswer(username,"login",errors);
        }

        new MsgSender(socket).sendMsg("toSendAnswer");
    }

    public void startGameRequest(String username){
        if(username.equals(starter)){
            controllerListener.onStartGame();

            new MsgOutWriter().startGameAcceptedAnswer(username);

            for (String user : clients.keySet())
                if(!user.equals(username))
                    new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
            new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
        }
    }

    public void createGodsRequest(String username, ArrayList<Integer> ids){
        ArrayList<Error> errors = controllerListener.onChallengerChooseGods(username, ids);

        if(errors.isEmpty()){
            new MsgOutWriter().createGodsAcceptedAnswer(username,ids);
            for (String user : clients.keySet())
                if(!user.equals(username))
                    new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        }else{
            new MsgOutWriter().rejectedAnswer(username,"createGods",errors);
        }
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void choseGodRequest(String username, int godId){
       ArrayList<Error> errors = controllerListener.onPlayerChooseGod(username, godId);

        if(errors.isEmpty()){
            new MsgOutWriter().choseGodAcceptedAnswer(username,godId);
            for (String user : clients.keySet())
                if(!user.equals(username))
                    new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        }else{
            new MsgOutWriter().rejectedAnswer(username,"choseGod",errors);
        }
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");

    }

    public void chooseStartingPlayerRequest(String username, String playerChosen){
        ArrayList<Error> errors = controllerListener.onChallengerChooseStartingPlayer(username, playerChosen);

        if(errors.isEmpty()){
            new MsgOutWriter().choseStartingPlayerAcceptedAnswer(username,playerChosen);
            for (String user : clients.keySet())
                if(!user.equals(username))
                    new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        }else{
            new MsgOutWriter().rejectedAnswer(username,"choseStartingPlayer",errors);
        }
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void setupOnBoardRequest(String username, String workerGender, int x, int y){
        ArrayList<Error> errors = controllerListener.onPlayerSetWorker(username,workerGender,x,y);

        if(errors.isEmpty()){
            new MsgOutWriter().setupOnBoardAcceptedAnswer(username,workerGender,x,y);
            for (String user : clients.keySet())
                if(!user.equals(username))
                    new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        }else{
            new MsgOutWriter().rejectedAnswer(username,"setWorkerOnBoard",errors);
        }
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void moveRequest(String username, String workerGender, int x, int y){
        controllerListener.onWorkerMove(username,workerGender,x,y);
    }

    public void buildRequest(String username, String workerGender, int x, int y){
        controllerListener.onWorkerMove(username,workerGender,x,y);
    }

    public void endOfTurn(String username){
        controllerListener.onPlayerEndTurn(username);
    }

    public void onEndRequest(String username){ clients.remove(username); }

    // Answer Methods

    @Override
    public void onMoveAcceptedRequest() {

    }

    @Override
    public void onMoveRejectedRequest() {

    }

    @Override
    public void onBuildAcceptedRequest() {

    }

    @Override
    public void onBuildRejectedRequest() {

    }

    // Communication Methods
}
