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

public class VirtualView implements ViewInterface {

    //attributes

    private final ControllerInterface controllerListener;
    private final Map<String,Socket> clients;
    private final List<Socket> waitList;
    private final ClientDisconnectionListener clientDisconnectionListener;
    private String creator;
    private String challenger;
    private final List<Color> availableColor;
    private final int lobbyNumber;
    private boolean on;

    boolean matchStarted;

    //constructors

    public VirtualView(ControllerInterface l, ClientDisconnectionListener cdl, int lobbyNumber){
        this.controllerListener = l;
        this.clients = new HashMap<>();
        this.waitList = new ArrayList<>();
        this.matchStarted = false;
        this.clientDisconnectionListener = cdl;
        this.lobbyNumber = lobbyNumber;
        this.controllerListener.setViewInterface(this);
        this.availableColor = Color.getColorList();
        this.on = true;
    }

    //methods

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

    public void sendMsg(Socket socket, Document msg){
        if(!new MsgSender(socket,msg).sendMsg()) clientDown(socket);
    }

    //Request Methods

    public synchronized void loginRequest(String username, Socket socket) {
        Color color;
        if (availableColor.size() > 1)
            color = availableColor.get((int) (Math.random() * 10) % (availableColor.size() - 1));
        else {
            color = availableColor.get(0);
        }
        List<Error> errors = controllerListener.onNewPlayer(username, color);

        if (errors.isEmpty()) {
            onLoginAcceptedRequest(username, color, socket);
            availableColor.remove(color);
        } else
            onLoginRejectedRequest(username, errors, socket);
    }

    public synchronized void startGameRequest(String username){
        if(username.equals(creator)){
            challenger = controllerListener.onStartGame();

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
        List<Error> errors = controllerListener.onWorkerMove(username,workerGender,x,y);

        if(errors.isEmpty())
            controllerListener.sendAnswerMoveAccepted(username);
        else
            onRejectedRequest(username,errors,"move");
    }

    public void buildRequest(String username, String workerGender, int x, int y, int level){
        List<Error> errors = controllerListener.onWorkerBuild(username,workerGender,x,y,level);

        if(errors.isEmpty())
            controllerListener.sendAnswerBuildAccepted(username);
        else
            onRejectedRequest(username,errors,"build");
    }

    public void endOfTurn(String username){
        List<Error> errors = controllerListener.onPlayerEndTurn(username);

        if(errors.isEmpty())
            controllerListener.sendAnswerEndOfTurnAccepted(username);
        else
            onRejectedRequest(username,errors,"endOfTurn");
    }

    public void endRequest(String username){clients.remove(username);}

    // Answer Methods

    public void onLoginAcceptedRequest(String username,Color color, Socket socket){
        waitList.remove(socket);

        if (clients.isEmpty()) creator = username;
        clients.put(username, socket);

        System.out.print(username + " logged in lobby number " + lobbyNumber + "\n");

        Document updateMsg = new UpdateMsgWriter().loginUpdate(username, color);
        for (String user : clients.keySet()) {
            if (!user.equals(username)) {
                sendMsg(clients.get(user), updateMsg);
            }
        }
        sendMsg(socket, new AnswerMsgWriter().loginAcceptedAnswer(username, color, clients.keySet()));

        //Send to the first client connected a to do message to starting match
        switch (clients.size()){
            case 2 :
                toDoStartMatch();
                break;
            case 3 :
                lobbyNoLongerAvailable();
                startGameRequest(creator);
                break;
        }
    }

    public void onLoginRejectedRequest(String username,List<Error> errors, Socket socket){
        sendMsg(socket, new AnswerMsgWriter().rejectedAnswer(username, "login", errors));
    }

    public void onStartGameAcceptedRequest(String username){
        Document updateMsg = new UpdateMsgWriter().startGameUpdate(username);
        for (String user : clients.keySet())
            if(!user.equals(username)) {
                sendMsg(clients.get(user), updateMsg);
            }
        sendMsg(clients.get(username), new AnswerMsgWriter().startGameAcceptedAnswer(username));

        matchStarted = true;
        lobbyNoLongerAvailable();

        //Send to the challenger a to do message to create gods
        toDoCreateGods();
    }

    @Override

    public void onRejectedRequest(String username, List<Error> errors, String mode){
        sendMsg(clients.get(username), new AnswerMsgWriter().rejectedAnswer(username,mode,errors));
    }

    public void onCreateGodsAcceptedRequest(String username, ArrayList<Integer> ids){
        Document updateMsg = new UpdateMsgWriter().createGodsUpdate(username,ids);
        for (String user : clients.keySet())
            if(!user.equals(username)) {
                sendMsg(clients.get(user), updateMsg);
            }
        sendMsg(clients.get(username), new AnswerMsgWriter().createGodsAcceptedAnswer(username,ids));

        //Next step in game flow
        controllerListener.sendNextToDoChoseGod();
    }

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
    }

    public void onChoseStartingPlayerAcceptedRequest(String username, String playerChosen){
        Document updateMsg = new UpdateMsgWriter().choseStartingPlayerUpdate(username,playerChosen);
        for (String user : clients.keySet())
            if(!user.equals(username)) {
                sendMsg(clients.get(user), updateMsg);
            }
        sendMsg(clients.get(username), new AnswerMsgWriter().choseStartingPlayerAcceptedAnswer(username,playerChosen));

        //Next step in game flow
        controllerListener.sendNextToDoSetupWorkerOnBoard("Male");
    }

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
    }

    @Override

    public void onMoveAcceptedRequest(String username, Document answerMsg, Document updateMsg) {
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), updateMsg);
        sendMsg(clients.get(username), answerMsg);
    }

    @Override

    public void onBuildAcceptedRequest(String username, Document answerMsg, Document updateMsg) {
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), updateMsg);
        sendMsg(clients.get(username), answerMsg);
    }

    @Override

    public void onEndOfTurnAcceptedRequest(String username, Document answerMsg, Document updateMsg){
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), updateMsg);
        sendMsg(clients.get(username), answerMsg);

        //Next step in game flow
        controllerListener.sendNextToDoTurn();
    }

    // To do communication Methods

    public void toDoLogin(Socket socket){
        System.out.println(socket + " have to log in");
        sendMsg(socket, new ToDoMsgWriter().toDoAction("login"));
    }
    
    public void toDoStartMatch(){
        sendMsg(clients.get(creator), new ToDoMsgWriter().toDoAction("canStartMatch"));
        for (String user : clients.keySet())
            if(!user.equals(creator))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(creator,"startMatch"));
    }

    public void toDoCreateGods(){
        sendMsg(clients.get(challenger), new ToDoMsgWriter().toDoAction("createGods"));
        for (String user : clients.keySet())
            if(!user.equals(challenger))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(challenger,"createGods"));
    }

    @Override

    public void toDoChoseGod(String username, List<Integer> ids){
        sendMsg(clients.get(username), new ToDoMsgWriter().toDoChoseGod(ids));
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(username,"choseGod"));
    }

    public void toDoChoseStartingPlayer(){
        sendMsg(clients.get(challenger), new ToDoMsgWriter().toDoAction("choseStartingPlayer"));
        for (String user : clients.keySet())
            if(!user.equals(challenger))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(challenger,"choseStartingPlayer"));
    }

    @Override

    public void toDoSetupWorkerOnBoard(String username, String gender){
        sendMsg(clients.get(username), new ToDoMsgWriter().toDoAction("setup" + gender + "WorkerOnBoard"));
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(username,"setup" + gender + "WorkerOnBoard"));
    }

    @Override

    public void toDoTurn(String username, String firstOperation){
        sendMsg(clients.get(username), new ToDoMsgWriter().toDoTurn(firstOperation));
        for (String user : clients.keySet())
            if(!user.equals(username))
                sendMsg(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(username,"hisTurn"));
    }

    // Win/Lose cases methods

    @Override
    public void directlyWinCase(String winnerUsername){
        sendMsg(clients.get(winnerUsername), new UpdateMsgWriter().winLoseUpdate(winnerUsername,"youWinDirectly"));
        for (String user : clients.keySet())
            if(!user.equals(winnerUsername)){
                sendMsg(clients.get(user), new UpdateMsgWriter().winLoseUpdate(winnerUsername,"youLoseForDirectWin"));
            }
        matchFinished();
    }

    @Override
    public void match2PlayerLose(String winnerUsername){
        sendMsg(clients.get(winnerUsername), new UpdateMsgWriter().winLoseUpdate(winnerUsername,"youWinForAnotherLose"));
        for (String user : clients.keySet())
            if(!user.equals(winnerUsername)){
                sendMsg(clients.get(user), new UpdateMsgWriter().winLoseUpdate(winnerUsername,""));
            }
        matchFinished();
    }

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
    }


    // Disconnection methods

    public void clientDown(Socket disconnectedClient){
        if(clients.containsValue(disconnectedClient)){
            for(Socket c : clients.values())
                if(!c.isClosed()) {
                    sendMsg(c, new UpdateMsgWriter().extraUpdate("disconnection"));
                }
            if (on) clientDisconnectionListener.onClientDown(this);
        }
        on = false;
    }

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

    public void lobbyNoLongerAvailable(){
        for(Socket socket : waitList) {
            sendMsg(socket, new UpdateMsgWriter().extraUpdate("lobbyNoLongerAvailable"));
            sendMsg(socket, new UpdateMsgWriter().extraUpdate("disconnection"));
        }
        waitList.clear();
    }
}
