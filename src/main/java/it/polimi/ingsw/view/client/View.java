package it.polimi.ingsw.view.client;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.msgUtilities.client.RequestMsgWriter;
import it.polimi.ingsw.network.client.EchoClient;
import it.polimi.ingsw.view.client.viewComponents.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO : javadoc

public abstract class View {

    //attributes
    protected Player myPlayer;
    protected ArrayList<Player> players;
    protected Board gameBoard;
    protected EchoClient clientHandler;
    protected String myIp;
    protected int myPort;
    protected List<God> gods;

    //constructors

    public View(){
        myPlayer = null;
        players = new ArrayList<>();
        gameBoard = new Board();
        gods = new GodsGenerator().getGods();
    }

    public View(String ip, int port){
        myPlayer = null;
        players = new ArrayList<>();
        gameBoard = new Board();
        gods = new GodsGenerator().getGods();
        myIp = ip;
        myPort = port;
    }

    //methods

    //TODO : javadoc

    public void start(){
        //TODO : delete testLoginClass() and replace it with this
        clientHandler = new EchoClient(myIp,myPort,this);
        clientHandler.start();
    }

    public void newGame(){
        myPlayer = null;
        players = new ArrayList<>();
        gameBoard = new Board();
    }

    //input methods

    //TODO : javadoc

    public abstract void setMyIp();

    //TODO : javadoc

    public abstract void setMyPort();

    //TODO : javadoc

    public abstract void setUsername(boolean rejectedBefore);

    //TODO : javadoc

    public abstract void startMatch();

    //TODO : javadoc

    public abstract void selectGods();

    //TODO : javadoc

    public abstract void selectGod(List<Integer> ids);

    //update method

    //TODO : javadoc

    public void updateLoginDone(Map<String,Color> otherPlayers, String myUsername, Color myColor){
        myPlayer = new Player(myUsername,myColor);
        for(String username : otherPlayers.keySet()){
            players.add(new Player(username,otherPlayers.get(username)));
        }

        showLoginDone();
    }

    //TODO : javadoc

    public void updateNewUserLogged(String username, Color color){
        players.add(new Player(username,color));

        showNewUserLogged(username,color);
    }

    //TODO : javadoc

    public void updateMyGodSelected(int id){
        myPlayer.setGod(getGodById(id));
    }

    //TODO : javadoc

    public void updateGodSelected(String username, int id){
        getPlayerByUsername(username).setGod(getGodById(id));
    }

    //disconnection methods

    //TODO : javadoc

    public void anotherClientDisconnection(){clientHandler.anotherClientDisconnection();}

    //TODO : javadoc

    public void disconnectionForLobbyNoLongerAvailable(){
        clientHandler.disconnectionForLobbyNoLongerAvailable();
    }

    //TODO : javadoc

    public void disconnectionForInputExpiredTimeout(){clientHandler.disconnectionForTimeout();}


    //show methods

    //TODO : javadoc

    public abstract void showLoginDone();

    //TODO : javadoc

    public abstract void showNewUserLogged(String username, Color color);

    //TODO : javadoc

    public abstract void showWaitMessage(String waitFor, String author);

    //TODO : javadoc

    public abstract void showMatchStarted();

    //TODO : javadoc

    public abstract void showGodsChoiceDone(ArrayList<Integer> ids);

    //TODO : javadoc

    public abstract void showGodsChallengerSelected(String username, ArrayList<Integer> ids);

    //TODO : javadoc

    public abstract void showBoard();

    //TODO : javadoc

    public abstract void serverNotFound();

    //show disconnection methods

    //TODO : javadoc

    public abstract void showAnotherClientDisconnection();

    //TODO : javadoc

    public abstract void showDisconnectionForLobbyNoLongerAvailable();

    //TODO : javadoc

    public abstract void showServerDisconnection();

    //TODO : javadoc

    public abstract void showDisconnectionForInputExpiredTimeout();

    //sendRequest method

    //TODO : javadoc

    public void sendLoginRequest(String username){
        clientHandler.sendMsg(new RequestMsgWriter().loginRequest(username));
    }

    //TODO : javadoc

    public void sendStartGameRequest(){
        clientHandler.sendMsg(new RequestMsgWriter().startGameRequest(myPlayer.getUsername()));
    }

    //TODO : javadoc

    public void sendCreateGodsRequest(ArrayList<Integer> ids){
        clientHandler.sendMsg(new RequestMsgWriter().createGodsRequest(myPlayer.getUsername(),ids));
    }

    //TODO : javadoc

    public void sendChooseGodRequest(int godId){
        clientHandler.sendMsg(new RequestMsgWriter().chooseGodRequest(myPlayer.getUsername(),godId));
    }

    //TODO : javadoc

    public void sendChooseStartingPlayerRequest(String starter){
        clientHandler.sendMsg(new RequestMsgWriter().chooseStartingPlayerRequest(myPlayer.getUsername(),starter));
    }

    //TODO : javadoc

    public void sendSetWorkerOnBoardRequest(String gender, int x, int y){
        clientHandler.sendMsg(new RequestMsgWriter().setWorkerOnBoardRequest(myPlayer.getUsername(),gender,x,y));
    }

    //TODO : javadoc

    public void sendMoveRequest(String gender, int x, int y){
        clientHandler.sendMsg(new RequestMsgWriter().moveRequest(myPlayer.getUsername(),gender,x,y));
    }

    //TODO : javadoc

    public void sendBuildRequest(String gender, int x, int y, int level){
        clientHandler.sendMsg(new RequestMsgWriter().buildRequest(myPlayer.getUsername(),gender,x,y,level));
    }

    //TODO : javadoc

    public void sendEndOfTurnRequest(){
        clientHandler.sendMsg(new RequestMsgWriter().endOfTurnRequest(myPlayer.getUsername()));
    }

    //support methods

    public God getGodById(int id){
        for(God god : gods) {
            if (god.getId() == id) return god;
        }

        return null;
    }

    public Player getPlayerByUsername(String username){
        for(Player p : players){
            if(p.getUsername().equals(username)) return p;
        }

        return null;
    }

    public int getPlayerNumber(){
        return players.size() + 1;
    }

}
