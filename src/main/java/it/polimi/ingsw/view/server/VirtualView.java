package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.model.enums.State;
import it.polimi.ingsw.msgUtilities.server.AnswerMsgWriter;
import it.polimi.ingsw.msgUtilities.server.ToDoMsgWriter;
import it.polimi.ingsw.msgUtilities.server.UpdateMsgWriter;
import it.polimi.ingsw.network.MsgSender;
import it.polimi.ingsw.network.server.ClientDisconnectionListener;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * This class simulates the presence of a view on the server (it takes care of receiving the translations of the
 * requests, addressing them to the controller, receiving responses from the latter and sending them back to the
 * real view located on the client.
 * @author marcoDige
 */

public class VirtualView implements ViewInterface {

    //attributes

    private final ControllerInterface controllerListener;

    private final Map<String,Socket> clients;

    private final Map<String,Color> players;

    /**
     * This list contains the players who entered the game but haven't logged in yet.
     */
    private final List<Socket> waitList;

    private final ClientDisconnectionListener clientDisconnectionListener;

    /**
     * This attribute represents the match creator.
     */

    private String creator;

    /**
     * This attribute represents the match challenger.
     */

    private String challenger;

    /**
     * This attribute contains the colors available during the startup stage of the match.
     */

    private final List<Color> availableColor;

    /**
     * This attribute indicates the number of the lobby (sequential number on the server).
     */

    private final int lobbyNumber;

    /**
     * This boolean attribute indicates if this virtual view has been removed.
     */

    private boolean on;

    /**
     * This boolean attribute indicates if the match has started.
     */

    boolean matchStarted;

    //constructors

    public VirtualView(ControllerInterface l, ClientDisconnectionListener cdl, int lobbyNumber){
        this.controllerListener = l;
        this.clients = new HashMap<>();
        this.players = new HashMap<>();
        this.waitList = new ArrayList<>();
        this.matchStarted = false;
        this.clientDisconnectionListener = cdl;
        this.lobbyNumber = lobbyNumber;
        this.controllerListener.setViewInterface(this);
        this.availableColor = Color.getColorList();
        this.on = true;
    }

    //methods

    /**
     * This methods adds a client into the wait list (the list which contains clients who aren't logged yet)
     * @param socket is the new client connected's socket
     */

    public synchronized void addInWaitList(Socket socket){
        this.waitList.add(socket);
    }

    public synchronized int getLobbySize(){
        return clients.size();
    }

    public synchronized boolean getMatchStarted(){
        return matchStarted;
    }

    public boolean isOn() {
        return on;
    }

    /**
     * This method allows to send a XML message to client identified by socket.
     * @param socket is the socket of the client that should receive the message
     * @param msg is the XML message to send
     */

    public void sendMsg(Socket socket, Document msg){
        if(!new MsgSender(socket,msg).sendMsg()) clientDown(socket);
    }

    //Request Methods

    /**
     * This method handles a login request, turns it over to the controller and, based on the server response, directs
     * the response. It also assigns a random color to the player.
     * @param username is the player who want to log in username
     * @param socket is the socket associated with the new player
     */

    public synchronized void loginRequest(String username, Socket socket) {
        Color color;
        if (availableColor.size() > 1)
            color = availableColor.get((int) (Math.random() * 10) % (availableColor.size() - 1));
        else {
            color = availableColor.get(0);
        }
        List<Error> errors = controllerListener.onNewPlayer(username, color);

        if(on) {
            if (errors.isEmpty()) {
                onLoginAcceptedRequest(username, color, socket);
                availableColor.remove(color);
            } else
                onLoginRejectedRequest(username, errors, socket);
        }
    }

    /**
     * This method handles a startGame request, turns it over to the controller and directs the response.
     * @param username is the username of the player who's starting the game
     */

    public synchronized void startGameRequest(String username){
        if(username.equals(creator) && on){
            challenger = controllerListener.onStartGame();

            onStartGameAcceptedRequest(username);
        }
    }

    /**
     * This method handles a createGods request, turns it over to the controller and, based on the server response, directs
     * the response.
     * @param username is the player who made the request
     * @param ids is the list of the gods chosen by the player
     */

    public void createGodsRequest(String username, ArrayList<Integer> ids){
        List<Error> errors = controllerListener.onChallengerChooseGods(username, ids);

        if(on) {

            if (errors.isEmpty())
                onCreateGodsAcceptedRequest(username, ids);
            else
                onRejectedRequest(username, errors, "createGods");
        }
    }

    /**
     * This method handles a choseGods request, turns it over to the controller and, based on the server response, directs
     * the response.
     * @param username is the player who made the request
     * @param godId is the id of the god chosen by the player
     */

    public void choseGodRequest(String username, int godId){
       List<Error> errors = controllerListener.onPlayerChooseGod(username, godId);

       if(on) {
           if (errors.isEmpty())
               onChoseGodAcceptedRequest(username, godId);
           else
               onRejectedRequest(username, errors, "choseGod");
       }

    }

    /**
     * This method handles a choseStartingPlayer request, turns it over to the controller and, based on the server
     * response, directs the response.
     * @param username is the player who made the request
     * @param playerChosen is the player chosen to start the game
     */

    public void choseStartingPlayerRequest(String username, String playerChosen){
        List<Error> errors = controllerListener.onChallengerChooseStartingPlayer(username, playerChosen);

        if(on) {
            if (errors.isEmpty())
                onChoseStartingPlayerAcceptedRequest(username, playerChosen);
            else
                onRejectedRequest(username, errors, "choseStartingPlayer");

        }
    }

    /**
     * This method handles a setupOnBoard request, turns it over to the controller and, based on the server response,
     * directs the response.
     * @param username is the player who made the request
     * @param workerGender is the gender of the worker that user request to place on board
     * @param x is the x coordinate where user placed his worker
     * @param y is the y coordinate where user placed his worker
     */

    public void setupOnBoardRequest(String username, String workerGender, int x, int y){
        List<Error> errors = controllerListener.onPlayerSetWorker(username,workerGender,x,y);

        if(on) {
            if (errors.isEmpty())
                onSetupOnBoardAcceptedRequest(username, workerGender, x, y);
            else
                onSetWorkerOnBoardRejectedRequest(username, errors, "setWorkerOnBoard", workerGender);
        }
    }

    /**
     * This method handles a move request, turns it over to the controller and, based on the server response,
     * directs the response.
     * @param username is the player who made the request
     * @param workerGender is the gender of the worker that user request to move
     * @param x is the x coordinate where user wants to move
     * @param y is the y coordinate where user wants to move
     */

    public void moveRequest(String username, String workerGender, int x, int y){
        List<Error> errors = controllerListener.onWorkerMove(username,workerGender,x,y);

        if(on) {
            if (errors.isEmpty()) {
                controllerListener.sendAnswerMoveAccepted(username);
                System.out.println("Lobby number " + lobbyNumber + " : " + username + " has moved his " + workerGender + " worker to " + "[" + x + "," + y + "] position!");
            } else
                onRejectedRequest(username, errors, "move");
        }
    }

    /**
     * This method handles a move request, turns it over to the controller and, based on the server response,
     * directs the response.
     * @param username is the player who made the request
     * @param workerGender is the gender of the worker that user request to build
     * @param x is the x coordinate where user wants to build
     * @param y is the y coordinate where user wants to build
     * @param level is the level that the worker want to build
     */

    public void buildRequest(String username, String workerGender, int x, int y, int level){
        List<Error> errors = controllerListener.onWorkerBuild(username,workerGender,x,y,level);

        if(on) {
            if (errors.isEmpty()) {
                controllerListener.sendAnswerBuildAccepted(username);
                System.out.println("Lobby number " + lobbyNumber + " : " + username + " has built with his " + workerGender + " worker in " + "[" + x + "," + y + "] position a level " + level + "!");
            } else
                onRejectedRequest(username, errors, "build");
        }
    }

    /**
     * This method handles an endOfTurn request, turns it over to the controller and, based on the server response,
     * directs the response.
     * @param username is the player who made the request
     */

    public void endOfTurn(String username){
        List<Error> errors = controllerListener.onPlayerEndTurn(username);

        if(on) {
            if (errors.isEmpty())
                controllerListener.sendAnswerEndOfTurnAccepted(username);
            else
                onRejectedRequest(username, errors, "endOfTurn");
        }
    }

    /**
     * This method handles an end request and remove the username from the clients list
     * @param username is the player who made the request
     */

    public void endRequest(String username){clients.remove(username);}

    // Answer Methods

    /**
     * This method handles the case where the login request from the new player has been accepted. It removes the client
     * from the wait list, sends a positive response message to the client, and updates it to other clients in the match.
     * It warns unconnected clients that the lobby is no longer available when the lobby is filled.
     * @param username is the new player's username
     * @param color is the new player's color
     * @param socket is the new player's socket
     */

    public void onLoginAcceptedRequest(String username,Color color, Socket socket){
        waitList.remove(socket);

        if (clients.isEmpty()) creator = username;
        clients.put(username, socket);
        players.put(username, color);

        System.out.print(username + " logged in lobby number " + lobbyNumber + " with color " + Color.labelOfEnum(color) + "\n");

        Document updateMsg = new UpdateMsgWriter().loginUpdate(username, color);
        for (String user : clients.keySet()) {
            if (!user.equals(username)) {
                sendMsg(clients.get(user), updateMsg);
            }
        }
        sendMsg(socket, new AnswerMsgWriter().loginAcceptedAnswer(username, color, players));

        if(clients.size() == 3) lobbyNoLongerAvailable();
        //Send to the first client connected a to do message to starting match
        if(clients.size() == 2 || clients.size() == 3) toDoStartMatch();
    }

    /**
     * This method handles the case where the login request from the player has been rejected. It sends a message where
     * it alerts the client that the request has been rejected.
     * @param username is the is the username chosen by the player
     * @param errors is the error list where the errors that led to the rejection are contained
     * @param socket is the payer's socket
     */

    public void onLoginRejectedRequest(String username,List<Error> errors, Socket socket){
        sendMsg(socket, new AnswerMsgWriter().rejectedAnswer(username, "login", errors));
    }

    /**
     * This method handles the case where the startGame request from the player has been accepted. It informs the client
     * that the request has been accepted and updates the other clients that the match has started. It warns the
     * challenger that he has to choose the gods.
     * @param username is the username of the player who started the game
     */

    public void onStartGameAcceptedRequest(String username){
        Document updateMsg = new UpdateMsgWriter().startGameUpdate(username);
        for (String user : clients.keySet())
            if(!user.equals(username)) {
                sendMsg(clients.get(user), updateMsg);
            }
        sendMsg(clients.get(username), new AnswerMsgWriter().startGameAcceptedAnswer(username));

        matchStarted = true;
        System.out.println("Match in lobby " + lobbyNumber + " started!");
        lobbyNoLongerAvailable();

        //Send to the challenger a to do message to create gods
        toDoCreateGods();
    }

    //TODO : javadoc

    @Override

    public void onRejectedRequest(String username, List<Error> errors, String mode){
        sendMsg(clients.get(username), new AnswerMsgWriter().rejectedAnswer(username,mode,errors));
    }

    /**
     * This method handles the case where the setWorkerOnBoard request from the player has been rejected. It sends
     * a message where it alerts the client that the request has been rejected.
     * @param username is the is the username of the player who made the request
     * @param errors is the error list where the errors that led to the rejection are contained
     * @param mode is the mode of the request
     * @param gender is the gender of the worker for whom the request was made
     */

    public void onSetWorkerOnBoardRejectedRequest(String username, List<Error> errors, String mode,String gender){
        sendMsg(clients.get(username), new AnswerMsgWriter().rejectedSetWorkerOnBoardAnswer(username,mode,errors,gender));
    }

    /**
     * This method handles the case where the createGods request from the player has been accepted. It sends a
     * positive response message to the client, and updates it to other clients in the match. It warns the next player
     * that he has to choose his god.
     * @param username is the is the username of the player who made the request
     * @param ids is the list of the gods chosen by the player
     */

    public void onCreateGodsAcceptedRequest(String username, ArrayList<Integer> ids){
        Document updateMsg = new UpdateMsgWriter().createGodsUpdate(username,ids);
        for (String user : clients.keySet())
            if(!user.equals(username)) {
                sendMsg(clients.get(user), updateMsg);
            }
        sendMsg(clients.get(username), new AnswerMsgWriter().createGodsAcceptedAnswer(username,ids));

        //Next step in game flow
        controllerListener.sendNextToDoChoseGod();

        System.out.println("Lobby number " + lobbyNumber + " : " + username + " has chosen " + players.size() + " gods!");
    }

    /**
     * This method handles the case where the choseGod request from the player has been accepted. It sends a
     * positive response message to the client, and updates it to other clients in the match. It warns the next player
     * that he has to choose his god or, if this is the last player in turn, it warns the player who has to chose the
     * starting player.
     * @param username is the is the username of the player who made the request
     * @param godId is the id of the god that the player has chosen
     */

    public void onChoseGodAcceptedRequest(String username, int godId){
        Document updateMsg = new UpdateMsgWriter().choseGodUpdate(username,godId);
        for (String user : clients.keySet())
            if(!user.equals(username)) {
                sendMsg(clients.get(user), updateMsg);
            }
        sendMsg(clients.get(username), new AnswerMsgWriter().choseGodAcceptedAnswer(username,godId));

        //Next step in game flow
        if(controllerListener.getGameState().equals(State.CHOOSE_GOD))
            controllerListener.sendNextToDoChoseGod();
        else
            controllerListener.sendNextToDoChoseStartingPlayer();

        System.out.println("Lobby number " + lobbyNumber + " : " + username + " has chosen his god!");
    }

    /**
     * This method handles the case where the choseStartingPlayer request from the player has been accepted. It sends a
     * positive response message to the client, and updates it to other clients in the match. It warns the next player
     * that he has to start to place his workers on the board.
     * @param username is the is the username of the player who made the request
     * @param playerChosen is the username of the player chosen to start the game
     */

    public void onChoseStartingPlayerAcceptedRequest(String username, String playerChosen){
        Document updateMsg = new UpdateMsgWriter().choseStartingPlayerUpdate(username,playerChosen);
        for (String user : clients.keySet())
            if(!user.equals(username)) {
                sendMsg(clients.get(user), updateMsg);
            }
        sendMsg(clients.get(username), new AnswerMsgWriter().choseStartingPlayerAcceptedAnswer(username,playerChosen));

        //Next step in game flow
        controllerListener.sendNextToDoSetupWorkerOnBoard("Male");

        System.out.println("Lobby number " + lobbyNumber + " : " + username + " has chosen " + playerChosen + " as starter player!");
    }

    /**
     * This method handles the case where the setupOnBoard request from the player has been accepted. It sends a
     * positive response message to the client, and updates it to other clients in the match. It warns the next player
     * that he has to place his workers on the board or, if this player has to place the other worker on board, it warns
     * this player that he has to place his female worker on the board.
     * @param username is the player who made the request
     * @param workerGender is the gender of the worker that user request to place on board
     * @param x is the x coordinate where user placed his worker
     * @param y is the y coordinate where user placed his worker
     */

    public void onSetupOnBoardAcceptedRequest(String username, String workerGender, int x, int y){
        Document updateMsg = new UpdateMsgWriter().setupOnBoardUpdate(username,workerGender,x,y);
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), updateMsg);
        sendMsg(clients.get(username), new AnswerMsgWriter().setupOnBoardAcceptedAnswer(username,workerGender,x,y));

        //Next step in game flow
        if(controllerListener.getGameState().equals(State.SET_WORKERS)) {
            if (workerGender.equals("female")) controllerListener.sendNextToDoSetupWorkerOnBoard("Male");
            else controllerListener.sendNextToDoSetupWorkerOnBoard("Female");
        }else
            controllerListener.sendNextToDoTurn();

        System.out.println("Lobby number " + lobbyNumber + " : " + username + " has placed his " + workerGender + " worker on " + "[" + x + "," + y + "] position!");
    }

    //TODO : javadoc

    @Override

    public void onMoveAcceptedRequest(String username, Document answerMsg, Document updateMsg) {
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), updateMsg);
        sendMsg(clients.get(username), answerMsg);
    }

    //TODO : javadoc

    @Override

    public void onBuildAcceptedRequest(String username, Document answerMsg, Document updateMsg) {
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), updateMsg);
        sendMsg(clients.get(username), answerMsg);
    }

    //TODO : javadoc

    @Override

    public void onEndOfTurnAcceptedRequest(String username, Document answerMsg, Document updateMsg){
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), updateMsg);
        sendMsg(clients.get(username), answerMsg);

        //Next step in game flow
        controllerListener.sendNextToDoTurn();

        System.out.println("Lobby number " + lobbyNumber + " : " + username + " has finished his turn!");
    }

    // To do communication Methods

    /**
     * This method sends a warning message to the client that needs to log in
     * @param socket is the client's socket
     */

    public void toDoLogin(Socket socket){
        System.out.println(socket + " have to log in");
        sendMsg(socket, new ToDoMsgWriter().toDoAction("login"));
    }

    /**
     * This method sends a warning message to the client that needs to start the match and a wait message to the other
     * clients.
     */
    
    public void toDoStartMatch(){
        sendMsg(clients.get(creator), new ToDoMsgWriter().toDoAction("canStartMatch"));
        for (String user : clients.keySet())
            if(!user.equals(creator))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(creator,"startMatch"));
    }

    /**
     * This method sends a warning message to the client that needs to create gods and a wait message to the other
     * clients.
     */

    public void toDoCreateGods(){
        sendMsg(clients.get(challenger), new ToDoMsgWriter().toDoAction("createGods"));
        for (String user : clients.keySet())
            if(!user.equals(challenger))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(challenger,"createGods"));
    }

    /**
     * This method sends a warning message to the client that needs to chose his god and a wait message to the other
     * clients.
     * @param username is the username of the player who has to chose his god
     * @param ids is the list of remaining gods you can choose
     */

    @Override

    public void toDoChoseGod(String username, List<Integer> ids){
        sendMsg(clients.get(username), new ToDoMsgWriter().toDoChoseGod(ids));
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(username,"choseGod"));
    }

    //TODO : javadoc

    public void toDoChoseStartingPlayer(){
        sendMsg(clients.get(challenger), new ToDoMsgWriter().toDoAction("choseStartingPlayer"));
        for (String user : clients.keySet())
            if(!user.equals(challenger))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(challenger,"choseStartingPlayer"));
    }

    //TODO : javadoc

    @Override

    public void toDoSetupWorkerOnBoard(String username, String gender){
        sendMsg(clients.get(username), new ToDoMsgWriter().toDoAction("setup" + gender + "WorkerOnBoard"));
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(username,"setup" + gender + "WorkerOnBoard"));
    }

    //TODO : javadoc

    @Override

    public void toDoTurn(String username, String firstOperation){
        sendMsg(clients.get(username), new ToDoMsgWriter().toDoTurn(firstOperation));
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(username,"hisTurn"));
    }

    // Win/Lose cases methods

    //TODO : javadoc

    @Override
    public void directlyWinCase(String winnerUsername){
        sendMsg(clients.get(winnerUsername), new UpdateMsgWriter().winLoseUpdate(winnerUsername,"youWinDirectly"));
        for (String user : clients.keySet())
            if(!user.equals(winnerUsername)){
                sendMsg(clients.get(user), new UpdateMsgWriter().winLoseUpdate(winnerUsername,"youLoseForDirectWin"));
            }
        matchFinished();
    }

    //TODO : javadoc

    @Override
    public void match2PlayerLose(String winnerUsername){
        sendMsg(clients.get(winnerUsername), new UpdateMsgWriter().winLoseUpdate(winnerUsername,"youWinForAnotherLose"));
        for (String user : clients.keySet())
            if(!user.equals(winnerUsername)){
                sendMsg(clients.get(user), new UpdateMsgWriter().winLoseUpdate(winnerUsername,"youLoseForBlocked"));
            }
        matchFinished();

        System.out.println("Lobby number " + lobbyNumber + " : " + winnerUsername + " won!");
    }

    //TODO : javadoc

    @Override
    public void match3PlayerLose(String loserUsername){
        sendMsg(clients.get(loserUsername), new UpdateMsgWriter().extraUpdate("youLoseForBlocked"));
        for (String user : clients.keySet())
            if(!user.equals(loserUsername)){
                sendMsg(clients.get(user), new UpdateMsgWriter().loseUpdate(loserUsername,"loser"));
            }

        try {
            clients.get(loserUsername).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        clients.remove(loserUsername);

        System.out.println("Lobby number " + lobbyNumber + " : " + loserUsername + " lost!");
    }


    // Disconnection methods

    /**
     * This method works when a client is disconnected. It alerts other clients to the incident with a sign-out message
     * and calls the method that will delete the lobby (game over).
     * @param disconnectedClient is the disconnected client's socket
     */

    public synchronized void clientDown(Socket disconnectedClient){
        if(clients.containsValue(disconnectedClient)){
            for(Socket c : clients.values())
                if(!c.isClosed()) {
                    sendMsg(c, new UpdateMsgWriter().extraUpdate("disconnection"));
                }
            if (on) clientDisconnectionListener.onClientDown(this);
            on = false;
        }
    }

    /**
     * This method works when the match has finished. It closes all sockets and removes the lobby from the list on
     * the server.
     */

    public void matchFinished(){
        for(Socket c : clients.values())
            if(!c.isClosed()) {
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        System.out.println("Disconnection lobby number " + lobbyNumber + " for match finished");
        clientDisconnectionListener.onMatchFinish(this);
        on = false;
    }

    /**
     * This method works when the lobby is full or the game has started. It alerts clients in the wait list that the
     * lobby is no longer available.
     */

    public void lobbyNoLongerAvailable(){
        for(Socket socket : waitList) {
            sendMsg(socket, new UpdateMsgWriter().extraUpdate("disconnectionForLobbyNoLongerAvailable"));
        }
        waitList.clear();
    }
}
