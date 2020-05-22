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

    protected Worker removedWorker;
    protected int[] workerForThisTurnCoordinates;

    //constructors

    public View(){
        myPlayer = null;
        players = new ArrayList<>();
        gameBoard = new Board();
        gods = new GodsGenerator().getGods();
        removedWorker = null;
        workerForThisTurnCoordinates = new int[2];
        workerForThisTurnCoordinates[0] = workerForThisTurnCoordinates[1] = -1;
    }

    public View(String ip, int port){
        myPlayer = null;
        players = new ArrayList<>();
        gameBoard = new Board();
        gods = new GodsGenerator().getGods();
        removedWorker = null;
        myIp = ip;
        myPort = port;
        workerForThisTurnCoordinates[0] = workerForThisTurnCoordinates[1] = -1;
    }

    //methods

    //TODO : javadoc

    public void start(){
        clientHandler = new EchoClient(myIp,myPort,this);
        clientHandler.start();
    }

    public void newGame(){
        myPlayer = null;
        players = new ArrayList<>();
        gameBoard = new Board();
        removedWorker = null;
        workerForThisTurnCoordinates[0] = workerForThisTurnCoordinates[1] = -1;
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

    //TODO : javadoc

    public abstract void selectStartingPlayer();

    //TODO : javadoc

    public abstract void setWorkerOnBoard(String gender, boolean rejectedBefore);

    //TODO : javadoc

    public abstract void turn(String firstOperation);

    //TODO : javadoc

    public abstract void move();

    //TODO : javadoc

    public abstract void build();

    //TODO : javadoc

    public abstract void moveOrBuild();

    //TODO : javadoc

    public abstract void buildOrEnd();

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
        showMyGodSelected();
    }

    //TODO : javadoc

    public void updateGodSelected(String username, int id){
        getPlayerByUsername(username).setGod(getGodById(id));
        showGodSelected(username);
    }

    //TODO : javadoc

    public void updatePlaceMyWorkerOnBoard(String gender, int x, int y){
        gameBoard.getSquareByCoordinates(x,y).placeWorker(myPlayer.getWorkerByGender(gender));

        showBoard();
    }

    //TODO : javadoc

    public void updatePlaceWorkerOnBoard(String username, String gender, int x,int y){
        gameBoard.getSquareByCoordinates(x,y).placeWorker(getPlayerByUsername(username).getWorkerByGender(gender));

        showBoard();
    }

    //TODO : javadoc

    public void updateMyWorkerPosition(int startX, int startY, int x, int y){
        if(removedWorker == null) {
            if (gameBoard.getSquareByCoordinates(x, y).getWorker() != null) {
                removedWorker = gameBoard.getSquareByCoordinates(x, y).removeWorker();
            }
            gameBoard.getSquareByCoordinates(x, y).placeWorker(gameBoard.getSquareByCoordinates(startX, startY).removeWorker());
        }else {
            gameBoard.getSquareByCoordinates(x, y).placeWorker(removedWorker);
            removedWorker = null;
        }
        workerForThisTurnCoordinates[0] = x;
        workerForThisTurnCoordinates[1] = y;

        showBoard();
    }

    public void invalidMove(List<String> errors){
        showTurnErrors(errors);

        move();
    }

    public void invalidBuild(List<String> errors){
        showTurnErrors(errors);

        build();
    }

    //TODO : javadoc

    public void updateWorkerPosition(int startX, int startY, int x, int y){
        if(removedWorker == null) {
            if (gameBoard.getSquareByCoordinates(x, y).getWorker() != null) {
                removedWorker = gameBoard.getSquareByCoordinates(x, y).removeWorker();
            }
            gameBoard.getSquareByCoordinates(x, y).placeWorker(gameBoard.getSquareByCoordinates(startX, startY).removeWorker());
        }else {
            gameBoard.getSquareByCoordinates(x, y).placeWorker(removedWorker);
            removedWorker = null;
        }

        showBoard();
    }

    //TODO : javadoc

    public void updateMyPositionLevel(int x, int y, int level){
        gameBoard.getSquareByCoordinates(x,y).setLevel(level);

        workerForThisTurnCoordinates[0] = x;
        workerForThisTurnCoordinates[1] = y;

        showBoard();
    }

    //TODO : javadoc

    public void updatePositionLevel(int x, int y, int level){
        gameBoard.getSquareByCoordinates(x,y).setLevel(level);

        showBoard();
    }

    public void updateMyEndOfTurn(){
        workerForThisTurnCoordinates[0] = workerForThisTurnCoordinates[1] = -1;

        showMyTurnEnded();
    }

    public void updateEndOfTurn(String username){
        showTurnEnded(username);
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

    public abstract void showMyGodSelected();

    //TODO : javadoc

    public abstract void showGodSelected(String username);

    //TODO : javadoc

    public abstract void showStartingPlayer(String username);

    //TODO : javadoc

    public abstract void showBoard();

    //TODO : javadoc

    public abstract void showTurnEnded(String username);

    //TODO : javadoc

    public abstract void showMyTurnEnded();

    //TODO : javadoc

    public abstract void showTurnErrors(List<String> errors);

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

    public void nextOperation(String nextOperation){
        switch (nextOperation){
            case "move" :
                move();
                break;
            case "build" :
                build();
                break;
            case "move/build":
                moveOrBuild();
                break;
            case "build/end" :
                buildOrEnd();
                break;
            case "end" :
                sendEndOfTurnRequest();
        }
    }

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
