package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ControllerActionListener;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.msgUtilities.server.MsgOutWriter;
import it.polimi.ingsw.network.server.ClientDisconnectionListener;
import it.polimi.ingsw.network.server.MsgSender;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class VirtualView{

    //attributes

    private final ControllerActionListener controllerListener;
    private final Map<String,Socket> clients;
    private final ClientDisconnectionListener clientDisconnectionListener;
    private String starter;

    boolean matchStarted;

    //constructors

    public VirtualView(ControllerActionListener l, ClientDisconnectionListener cdl){
        this.controllerListener = l;
        this.clients = new HashMap<>();
        matchStarted = false;
        this.clientDisconnectionListener = cdl;
    }

    //methods

    public synchronized int getLobbySize(){
        return clients.size();
    }

    public synchronized boolean getMatchStarted(){
        return matchStarted;
    }

    //Request Methods

    public synchronized void loginRequest(String username, Color color, Socket socket) {
        if(getLobbySize() < 3 && !getMatchStarted()) {
            List<Error> errors = controllerListener.onNewPlayer(username, color);

            if (errors.isEmpty())
                onLoginAcceptedRequest(username,color,socket);
            else
                onLoginRejectedRequest(username,errors,socket);

        }else{
            try {
                //TODO : notify that lobby is full or match has started and he have to connect another time.
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startGameRequest(String username){
        if(username.equals(starter)){
            controllerListener.onStartGame();

            onStartGameAcceptedRequest(username);
        }
    }

    public void createGodsRequest(String username, ArrayList<Integer> ids){
        List<Error> errors = controllerListener.onChallengerChooseGods(username, ids);

        if(errors.isEmpty())
            onCreateGodsAcceptedRequest(username,ids);
        else
            onCreateGodsRejectedRequest(username,errors);

    }

    public void choseGodRequest(String username, int godId){
       List<Error> errors = controllerListener.onPlayerChooseGod(username, godId);

        if(errors.isEmpty())
            onChoseGodAcceptedRequest(username,godId);
        else
            onChoseGodRejectedRequest(username,errors);

    }

    public void choseStartingPlayerRequest(String username, String playerChosen){
        List<Error> errors = controllerListener.onChallengerChooseStartingPlayer(username, playerChosen);

        if(errors.isEmpty())
            onChoseStartingPlayerAcceptedRequest(username,playerChosen);
        else
            onChoseStartingPlayerRejectedRequest(username,errors);

    }

    public void setupOnBoardRequest(String username, String workerGender, int x, int y){
        List<Error> errors = controllerListener.onPlayerSetWorker(username,workerGender,x,y);

        if(errors.isEmpty())
            onSetupOnBoardAcceptedRequest(username,workerGender,x,y);
        else
            onSetupOnBoardRejectedRequest(username,errors);
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

    public void endRequest(String username){ clients.remove(username); }

    // Answer Methods

    public void onLoginAcceptedRequest(String username,Color color, Socket socket){
        if (clients.isEmpty()) starter = username;
        clients.put(username, socket);

        new MsgOutWriter().loginAcceptedAnswer(username, color);
        for (String user : clients.keySet())
            if (!user.equals(username))
                new MsgSender(clients.get(user)).sendMsg("updateMsgOut");

        new MsgSender(socket).sendMsg("toSendAnswer");
    }

    public void onLoginRejectedRequest(String username,List<Error> errors, Socket socket){
        new MsgOutWriter().rejectedAnswer(username, "login", errors);
        new MsgSender(socket).sendMsg("toSendAnswer");
    }

    public void onStartGameAcceptedRequest(String username){
        new MsgOutWriter().startGameAcceptedAnswer(username);

        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");

        matchStarted = true;
    }

    public void onCreateGodsAcceptedRequest(String username, ArrayList<Integer> ids){
        new MsgOutWriter().createGodsAcceptedAnswer(username,ids);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void onCreateGodsRejectedRequest(String username, List<Error> errors){
        new MsgOutWriter().rejectedAnswer(username,"createGods",errors);
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void onChoseGodAcceptedRequest(String username, int godId){
        new MsgOutWriter().choseGodAcceptedAnswer(username,godId);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void onChoseGodRejectedRequest(String username, List<Error> errors){
        new MsgOutWriter().rejectedAnswer(username,"choseGod",errors);
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void onChoseStartingPlayerAcceptedRequest(String username, String playerChosen){
        new MsgOutWriter().choseStartingPlayerAcceptedAnswer(username,playerChosen);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void onChoseStartingPlayerRejectedRequest(String username, List<Error> errors){
        new MsgOutWriter().rejectedAnswer(username,"choseStartingPlayer",errors);
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void onSetupOnBoardAcceptedRequest(String username, String workerGender, int x, int y){
        new MsgOutWriter().setupOnBoardAcceptedAnswer(username,workerGender,x,y);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user)).sendMsg("updateMsgOut");
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void onSetupOnBoardRejectedRequest(String username, List<Error> errors){
        new MsgOutWriter().rejectedAnswer(username,"setWorkerOnBoard",errors);
        new MsgSender(clients.get(username)).sendMsg("toSendAnswer");
    }

    public void onMoveAcceptedRequest() {

    }

    public void onMoveRejectedRequest() {

    }

    public void onBuildAcceptedRequest() {

    }

    public void onBuildRejectedRequest() {

    }

    public void onEndOfTurnAcceptedRequest(){

    }

    public void onEndOfTurnRejectedRequest(){

    }

    // To do communication Methods

    // Client disconnection methods

    public void clientDown(){

        for(Socket c : clients.values())
            if(c.isConnected()) {
                try {
                    //TODO : send to this client a and finish match msg
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        clientDisconnectionListener.onClientDown(this);
    }
}
