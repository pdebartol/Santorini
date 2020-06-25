package it.polimi.ingsw.view.client;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.msgUtilities.client.RequestMsgWriter;
import it.polimi.ingsw.network.client.EchoClient;
import it.polimi.ingsw.view.client.viewComponents.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This abstract class contains all the information of the game useful to the client to show it to the user, the methods
 * to update it based on the responses of the server and the methods to show the saved information (this latter will then
 * be extended by the actual interfaces).
 * @author marcoDige
 */

public abstract class View {

    //attributes

    protected Player myPlayer;
    protected ArrayList<Player> players;
    public Board gameBoard;

    /**
     * This attribute represents the client network interface.
     */

    protected EchoClient clientHandler;
    protected String myIp;
    protected int myPort;

    /**
     * This attribute contains the list of all gods in Santorini game.
     */

    protected List<God> gods;

    //Turn support attributes

    /**
     * This attribute is a support attribute useful in a worker switch / push
     */

    protected Worker removedWorker;

    /**
     * This array contains (-1,-1) if the worker for this turn has not yet been chosen. If it had already been chosen it
     * contains its coordinates
     */

    protected int[] workerForThisTurnCoordinates;

    //constructors

    /**
     * this constructor is called when the player connects to a game. It generates the list of all game gods and assign
     * (-1, -1) to the worker support coordinates this turn.
     */

    public View(){
        myPlayer = null;
        players = new ArrayList<>();
        gameBoard = new Board();
        gods = new GodsGenerator().getGods();
        removedWorker = null;
        workerForThisTurnCoordinates = new int[2];
        workerForThisTurnCoordinates[0] = workerForThisTurnCoordinates[1] = -1;
    }

    /**
     * this constructor is called when the player connects to a game through the pair (ip,port).
     * It generates the list of all game gods and assign (-1, -1) to the worker support coordinates this turn.
     */

    public View(String ip, int port){
        myPlayer = null;
        players = new ArrayList<>();
        gameBoard = new Board();
        gods = new GodsGenerator().getGods();
        removedWorker = null;
        myIp = ip;
        myPort = port;
        workerForThisTurnCoordinates = new int[2];
        workerForThisTurnCoordinates[0] = workerForThisTurnCoordinates[1] = -1;
    }

    //methods

    /**
     * This method allows you to establish a connection with the server.
     */

    public void start(){
        clientHandler = new EchoClient(myIp,myPort,this);
        clientHandler.start();
    }

    /**
     * This method is triggered when the player want to play a new game. It initializes all data for a new game through the
     * same server.
     */

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
            workerForThisTurnCoordinates[0] = x;
            workerForThisTurnCoordinates[1] = y;
        }else {
            gameBoard.getSquareByCoordinates(x, y).placeWorker(removedWorker);
            removedWorker = null;
        }

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

    public void updateMyPositionLevel(int startX, int startY, int x, int y, int level){
        gameBoard.getSquareByCoordinates(x,y).setLevel(level);

        if(workerForThisTurnCoordinates[0] == -1 && workerForThisTurnCoordinates[1] == -1) {
            workerForThisTurnCoordinates[0] = startX;
            workerForThisTurnCoordinates[1] = startY;
        }

        showBoard();
    }

    //TODO : javadoc

    public void updatePositionLevel(int x, int y, int level){
        gameBoard.getSquareByCoordinates(x,y).setLevel(level);

        showBoard();
    }

    public void updateEndOfTurn(int x, int y, int l){
        if(myPlayer.getWorkers().contains(gameBoard.getSquareByCoordinates(x,y).getWorker())){
            myPlayer.removeWorker(gameBoard.getSquareByCoordinates(x,y).getWorker());

        }
        for(Player p : players)
            if(p.getWorkers().contains(gameBoard.getSquareByCoordinates(x,y).getWorker())){
                p.removeWorker(gameBoard.getSquareByCoordinates(x,y).getWorker());
            }

        updatePositionLevel(x,y,l);
    }

    //TODO : javadoc

    public void myEndOfTurnWithoutUpdate(){
        workerForThisTurnCoordinates[0] = workerForThisTurnCoordinates[1] = -1;

        showMyTurnEnded();
    }

    public void endOfTurnWithoutUpdate(String username){
        showTurnEnded(username);
    }

    public void updateLoser(String username){
        for(Worker worker : getPlayerByUsername(username).getWorkers()){
            worker.getCurrentPosition().removeWorker();
        }
        players.remove(getPlayerByUsername(username));


        showPlayerLose(username);
        showBoard();
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

    //TODO : javadoc

    public abstract void showPlayerLose(String username);

    //TODO : javadoc

    public abstract void showYouLose(String reason, String winner);

    //TODO : javadoc

    public abstract void showYouWin(String reason);

    //Request method

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

    /**
     * This method identifies the next operation to be performed from the input string and starts it.
     * @param nextOperation is the string received by the server indicating which is the next operation to be performed
     */

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
