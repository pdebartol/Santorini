package it.polimi.ingsw.view.client;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.msgUtilities.client.RequestMsgWriter;
import it.polimi.ingsw.network.client.EchoClient;
import it.polimi.ingsw.view.client.viewComponents.*;

import java.util.ArrayList;
import java.util.Map;

public abstract class View {

    //attributes
    protected Player myPlayer;
    protected ArrayList<Player> players;
    protected Board gameBoard;
    protected EchoClient clientHandler;
    protected String myIp;
    protected int myPort;

    //constructors
    public View(){
        myPlayer = null;
        players = new ArrayList<>();
        gameBoard = new Board();
        setMyIp();
        setMyPort();
    }

    //methods

    public void start(){
        //TODO : delete testLoginClass() and replace it with this
        clientHandler = new EchoClient(myIp,myPort,new TestLoginClass());
        clientHandler.start();
    }

    //setup methods

    public abstract void setMyIp();

    public abstract void setMyPort();

    public abstract void setUsername(boolean rejectedBefore);

    public abstract void startMatch();

    //update method

    public void updateLoginDone(Map<String,Color> otherPlayers, String myUsername, Color myColor){
        myPlayer = new Player(myUsername,myColor);
        for(String username : otherPlayers.keySet()){
            players.add(new Player(username,otherPlayers.get(username)));
        }

        showLoginDone();
    }

    public void updateNewUserLogged(String username, Color color){
        players.add(new Player(username,color));

        showNewUserLogged(username,color);
    }

    //show methods

    public abstract void showLoginDone();

    public abstract void showNewUserLogged(String username, Color color);

    public abstract void showWaitMessage(String waitFor, String author);

    public abstract void showMatchStarted();

    //show disconnection methods

    public abstract void showAnotherClientDisconnection();

    public abstract void showDisconnectionForLobbyNoLongerAvailable();

    public abstract void showServerDisconnection();

    //sendRequest method

    public void sendLoginRequest(){
        clientHandler.sendMsg(new RequestMsgWriter().loginRequest(myPlayer.getUsername()));
    }

    public void sendStartGameRequest(){
        clientHandler.sendMsg(new RequestMsgWriter().startGameRequest(myPlayer.getUsername()));
    }

    public void sendCreateGodsRequest(ArrayList<Integer> ids){
        clientHandler.sendMsg(new RequestMsgWriter().createGodsRequest(myPlayer.getUsername(),ids));
    }

    public void sendChooseGodRequest(int godId){
        clientHandler.sendMsg(new RequestMsgWriter().chooseGodRequest(myPlayer.getUsername(),godId));
    }

    public void sendChooseStartingPlayerRequest(String starter){
        clientHandler.sendMsg(new RequestMsgWriter().chooseStartingPlayerRequest(myPlayer.getUsername(),starter));
    }

    public void sendSetWorkerOnBoardRequest(String gender, int x, int y){
        clientHandler.sendMsg(new RequestMsgWriter().setWorkerOnBoardRequest(myPlayer.getUsername(),gender,x,y));
    }

    public void sendMoveRequest(String gender, int x, int y){
        clientHandler.sendMsg(new RequestMsgWriter().moveRequest(myPlayer.getUsername(),gender,x,y));
    }

    public void sendBuildRequest(String gender, int x, int y, int level){
        clientHandler.sendMsg(new RequestMsgWriter().buildRequest(myPlayer.getUsername(),gender,x,y,level));
    }

    public void sendEndOfTurnRequest(){
        clientHandler.sendMsg(new RequestMsgWriter().endOfTurnRequest(myPlayer.getUsername()));
    }

}
