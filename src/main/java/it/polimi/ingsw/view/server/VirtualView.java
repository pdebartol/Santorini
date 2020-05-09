package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ControllerActionListener;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.msgUtilities.server.AnswerMsgWriter;
import it.polimi.ingsw.msgUtilities.server.UpdateMsgWriter;
import it.polimi.ingsw.network.MsgSender;
import it.polimi.ingsw.network.server.ClientDisconnectionListener;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class VirtualView implements ViewActionListener{

    //attributes

    private final ControllerActionListener controllerListener;
    private final Map<String,Socket> clients;
    private final ClientDisconnectionListener clientDisconnectionListener;
    private String starter;
    private final int lobbyNumber;

    boolean matchStarted;

    //constructors

    public VirtualView(ControllerActionListener l, ClientDisconnectionListener cdl,int lobbyNumber){
        this.controllerListener = l;
        this.clients = new HashMap<>();
        matchStarted = false;
        this.clientDisconnectionListener = cdl;
        this.lobbyNumber = lobbyNumber;
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
            onRejectedRequest(username,errors,"createGods");

    }

    public void choseGodRequest(String username, int godId){
       List<Error> errors = controllerListener.onPlayerChooseGod(username, godId);

        if(errors.isEmpty())
            onChoseGodAcceptedRequest(username,godId);
        else
            onRejectedRequest(username,errors,"choseGod");

    }

    public void choseStartingPlayerRequest(String username, String playerChosen){
        List<Error> errors = controllerListener.onChallengerChooseStartingPlayer(username, playerChosen);

        if(errors.isEmpty())
            onChoseStartingPlayerAcceptedRequest(username,playerChosen);
        else
            onRejectedRequest(username,errors,"choseStartingPlayer");

    }

    public void setupOnBoardRequest(String username, String workerGender, int x, int y){
        List<Error> errors = controllerListener.onPlayerSetWorker(username,workerGender,x,y);

        if(errors.isEmpty())
            onSetupOnBoardAcceptedRequest(username,workerGender,x,y);
        else
            onRejectedRequest(username,errors,"setupOnBoard");
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

        System.out.print(username + " logged in lobby number " + lobbyNumber + "\n");

        Document updateMsg = new UpdateMsgWriter().loginUpdate(username, color);
        for (String user : clients.keySet())
            if (!user.equals(username)) {
                new MsgSender(clients.get(user), updateMsg).sendMsg();
            }
        new MsgSender(socket, new AnswerMsgWriter().loginAcceptedAnswer(username, color)).sendMsg();
    }

    public void onLoginRejectedRequest(String username,List<Error> errors, Socket socket){
        new MsgSender(socket, new AnswerMsgWriter().rejectedAnswer(username, "login", errors)).sendMsg();
    }

    public void onStartGameAcceptedRequest(String username){
        Document updateMsg = new UpdateMsgWriter().startGameUpdate(username);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), updateMsg).sendMsg();
        new MsgSender(clients.get(username), new AnswerMsgWriter().startGameAcceptedAnswer(username)).sendMsg();

        matchStarted = true;
    }

    @Override

    public void onRejectedRequest(String username, List<Error> errors, String mode){
        new MsgSender(clients.get(username), new AnswerMsgWriter().rejectedAnswer(username,mode,errors)).sendMsg();
    }

    public void onCreateGodsAcceptedRequest(String username, ArrayList<Integer> ids){
        Document updateMsg = new UpdateMsgWriter().createGodsUpdate(username,ids);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), updateMsg).sendMsg();
        new MsgSender(clients.get(username), new AnswerMsgWriter().createGodsAcceptedAnswer(username,ids)).sendMsg();
    }

    public void onChoseGodAcceptedRequest(String username, int godId){
        Document updateMsg = new UpdateMsgWriter().choseGodUpdate(username,godId);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), updateMsg).sendMsg();
        new MsgSender(clients.get(username), new AnswerMsgWriter().choseGodAcceptedAnswer(username,godId)).sendMsg();
    }

    public void onChoseStartingPlayerAcceptedRequest(String username, String playerChosen){
        Document updateMsg = new UpdateMsgWriter().choseStartingPlayerUpdate(username,playerChosen);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), updateMsg).sendMsg();
        new MsgSender(clients.get(username), new AnswerMsgWriter().choseStartingPlayerAcceptedAnswer(username,playerChosen)).sendMsg();
    }

    public void onSetupOnBoardAcceptedRequest(String username, String workerGender, int x, int y){
        Document updateMsg = new UpdateMsgWriter().setupOnBoardUpdate(username,workerGender,x,y);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), updateMsg).sendMsg();
        new MsgSender(clients.get(username), new AnswerMsgWriter().setupOnBoardAcceptedAnswer(username,workerGender,x,y)).sendMsg();
    }

    @Override

    public void onMoveAcceptedRequest() {

    }

    @Override

    public void onBuildAcceptedRequest() {

    }

    @Override

    public void onEndOfTurnAcceptedRequest(){

    }

    // To do communication Methods

    // Client disconnection methods

    public void clientDown(Socket disconnectedClient){

        if(clients.containsValue(disconnectedClient)){
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
}
