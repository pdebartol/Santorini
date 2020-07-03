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

    /**
     * This method allows to insert the server ip.
     */

    public abstract void setMyIp();

    /**
     * This method allows to insert the server port.
     */

    public abstract void setMyPort();

    /**
     * This method allows to insert the player's username
     * @param rejectedBefore is a boolean that indicates if the username has been rejected from the server and the player
     *                       has to insert it newly
     */

    public abstract void setUsername(boolean rejectedBefore);

    /**
     * This method allows to start the match.
     */

    public abstract void startMatch();

    /**
     * This method allows to select 2 or 3 gods (is reserved for the challenger).
     */

    public abstract void selectGods();

    /**
     * This method allows to select a god from a list.
     * @param ids is the list of gods among which the player can choose his god
     */

    public abstract void selectGod(List<Integer> ids);

    /**
     * This method allows to select the starting player.
     */

    public abstract void selectStartingPlayer();

    /**
     * This method allows to set player's worker on board.
     * @param gender is the gender of the worker to set
     * @param rejectedBefore is boolean that indicates if the coordinates have been rejected from the server and the
     *                       player has to insert them newly
     */

    public abstract void setWorkerOnBoard(String gender, boolean rejectedBefore);

    /**
     * This method allows to start a turn.
     * @param firstOperation is the first operation player can do in his turn
     */

    public abstract void turn(String firstOperation);

    /**
     * This method allows to move a worker from a position to another position.
     */

    public abstract void move();

    /**
     * This method allows to build, with a worker, a building on a square.
     */

    public abstract void build();

    /**
     * This method allows to give the player a choice between move and build.
     */

    public abstract void moveOrBuild();

    /**
     * This method allows to give the player a choice between build and end.
     */

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

    /**
     * This method calls a method on the clientHandler which handles an another client disconnection
     */

    public void anotherClientDisconnection(){clientHandler.anotherClientDisconnection();}

    /**
     * This method calls a method on the clientHandler which handles a disconnection for Lobby No Longer Available
     */

    public void disconnectionForLobbyNoLongerAvailable(){
        clientHandler.disconnectionForLobbyNoLongerAvailable();
    }

    /**
     * This method calls a method on the clientHandler which handles a disconnection for input timeout expired
     */

    public void disconnectionForInputExpiredTimeout(){clientHandler.disconnectionForTimeout();}


    //show methods

    /**
     * This method allows to show to the player that the login has been done.
     */

    public abstract void showLoginDone();

    /**
     * This method allows to show to the player that a new user has been logged.
     * @param username is the new player's username
     * @param color is the new player's color
     */

    public abstract void showNewUserLogged(String username, Color color);

    /**
     * This method allows to show to the player a wait message.
     * @param waitFor is the motivation of waiting
     * @param author is the player who is acting
     */

    public abstract void showWaitMessage(String waitFor, String author);

    /**
     * This method allows to show that the match has started
     */

    public abstract void showMatchStarted();

    /**
     * This method allows to show that the gods choice has been done
     * @param ids is the id list of the gods
     */

    public abstract void showGodsChoiceDone(ArrayList<Integer> ids);

    /**
     * This method allows to show the gods challenger selected
     * @param username is the challenger's username
     * @param ids is the id list of the gods selected by the challenger
     */

    public abstract void showGodsChallengerSelected(String username, ArrayList<Integer> ids);

    /**
     * This method allows to show the god selected by the player
     */

    public abstract void showMyGodSelected();

    /**
     * This method allows to show the God selected by another player
     * @param username is the player who selected the god shown
     */

    public abstract void showGodSelected(String username);

    /**
     * This method allows to show the Starting player username
     * @param username is the starting player's username
     */

    public abstract void showStartingPlayer(String username);

    /**
     * This method allows to show the game board.
     */

    public abstract void showBoard();

    /**
     * This method allows to show that the player identified by "username" has finished his turn.
     * @param username is the player who finished his turn
     */

    public abstract void showTurnEnded(String username);

    /**
     * This method allows to show that the turn of the player has finished.
     */

    public abstract void showMyTurnEnded();

    /**
     * This method allows to show errors encountered during the player turn.
     * @param errors is the errors to show list
     */

    public abstract void showTurnErrors(List<String> errors);

    /**
     * This method allows to show that the server being searched could not be found
     */

    public abstract void serverNotFound();

    //show disconnection methods

    /**
     * This method allows to show that another client disconnected.
     */

    public abstract void showAnotherClientDisconnection();

    /**
     * This method allows to show to player that he has been disconnected from the server because the lobby where he was
     * is no longer available.
     */

    public abstract void showDisconnectionForLobbyNoLongerAvailable();

    /**
     * This method allows to show that the server disconnected.
     */

    public abstract void showServerDisconnection();

    /**
     * This method allows to show to player that he has been disconnected from the server because the timeout to insert
     * an input expired.
     */

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
