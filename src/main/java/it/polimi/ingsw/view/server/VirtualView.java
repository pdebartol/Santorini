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
    private final ClientDisconnectionListener clientDisconnectionListener;
    private String creator;
    private String challenger;
    private final List<Color> availableColor;
    private final int lobbyNumber;

    boolean matchStarted;

    //constructors

    public VirtualView(ControllerInterface l, ClientDisconnectionListener cdl, int lobbyNumber){
        this.controllerListener = l;
        this.clients = new HashMap<>();
        this.matchStarted = false;
        this.clientDisconnectionListener = cdl;
        this.lobbyNumber = lobbyNumber;
        this.controllerListener.setViewInterface(this);
        this.availableColor = Color.getColorList();
    }

    //methods

    public synchronized int getLobbySize(){
        return clients.size();
    }

    public synchronized boolean getMatchStarted(){
        return matchStarted;
    }

    //Request Methods

    public synchronized void loginRequest(String username, Socket socket) {
        if(getLobbySize() < 3 && !getMatchStarted()) {
            Color color = availableColor.get((int) (Math.random() * 10) % (availableColor.size() -1));
            List<Error> errors = controllerListener.onNewPlayer(username, color);

            if (errors.isEmpty()) {
                onLoginAcceptedRequest(username, color, socket);
                availableColor.remove(color);
            }
            else
                onLoginRejectedRequest(username,errors,socket);

        }else{
            try {
                new MsgSender(socket, new UpdateMsgWriter().extraUpdate("lobbyNoLongerAvailable"));
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        if (clients.isEmpty()) creator = username;
        clients.put(username, socket);

        System.out.print(username + " logged in lobby number " + lobbyNumber + "\n");

        Document updateMsg = new UpdateMsgWriter().loginUpdate(username, color);
        for (String user : clients.keySet()) {
            if (!user.equals(username)) {
                new MsgSender(clients.get(user), updateMsg).sendMsg();
            }
        }
        new MsgSender(socket, new AnswerMsgWriter().loginAcceptedAnswer(username, color, clients.keySet())).sendMsg();

        //Send to the first client connected a to do message to starting match
        switch (clients.size()){
            case 2 :
                toDoStartMatch();
                break;
            case 3 :
                startGameRequest(creator);
                break;
        }
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

        //Send to the challenger a to do message to create gods
        toDoCreateGods();
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

        //Next step in game flow
        controllerListener.sendNextToDoChoseGod();
    }

    public void onChoseGodAcceptedRequest(String username, int godId){
        Document updateMsg = new UpdateMsgWriter().choseGodUpdate(username,godId);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), updateMsg).sendMsg();
        new MsgSender(clients.get(username), new AnswerMsgWriter().choseGodAcceptedAnswer(username,godId)).sendMsg();

        //Next step in game flow
        if(controllerListener.getGameState().equals(State.CHOOSE_GOD))
            controllerListener.sendNextToDoChoseGod();
        else
            controllerListener.sendNextToDoChoseStartingPlayer();
    }

    public void onChoseStartingPlayerAcceptedRequest(String username, String playerChosen){
        Document updateMsg = new UpdateMsgWriter().choseStartingPlayerUpdate(username,playerChosen);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), updateMsg).sendMsg();
        new MsgSender(clients.get(username), new AnswerMsgWriter().choseStartingPlayerAcceptedAnswer(username,playerChosen)).sendMsg();

        //Next step in game flow
        controllerListener.sendNextToDoSetupWorkerOnBoard("Male");
    }

    public void onSetupOnBoardAcceptedRequest(String username, String workerGender, int x, int y){
        Document updateMsg = new UpdateMsgWriter().setupOnBoardUpdate(username,workerGender,x,y);
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), updateMsg).sendMsg();
        new MsgSender(clients.get(username), new AnswerMsgWriter().setupOnBoardAcceptedAnswer(username,workerGender,x,y)).sendMsg();

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
                new MsgSender(clients.get(user), updateMsg).sendMsg();
        new MsgSender(clients.get(username), answerMsg).sendMsg();
    }

    @Override

    public void onBuildAcceptedRequest(String username, Document answerMsg, Document updateMsg) {
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), updateMsg).sendMsg();
        new MsgSender(clients.get(username), answerMsg).sendMsg();
    }

    @Override

    public void onEndOfTurnAcceptedRequest(String username, Document answerMsg, Document updateMsg){
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), updateMsg).sendMsg();
        new MsgSender(clients.get(username), answerMsg).sendMsg();

        //Next step in game flow
        controllerListener.sendNextToDoTurn();
    }

    // To do communication Methods

    public void toDoLogin(Socket socket){
        new MsgSender(socket, new ToDoMsgWriter().toDoAction("login"));
    }
    
    public void toDoStartMatch(){
        new MsgSender(clients.get(creator), new ToDoMsgWriter().toDoAction("canStartMatch"));
        for (String user : clients.keySet())
            if(!user.equals(creator))
                new MsgSender(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(creator,"startMatch")).sendMsg();
    }

    public void toDoCreateGods(){
        new MsgSender(clients.get(challenger), new ToDoMsgWriter().toDoAction("createGods"));
        for (String user : clients.keySet())
            if(!user.equals(challenger))
                new MsgSender(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(challenger,"createGods")).sendMsg();
    }

    @Override

    public void toDoChoseGod(String username, List<Integer> ids){
        new MsgSender(clients.get(username), new ToDoMsgWriter().toDoChoseGod(ids));
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(username,"choseGod")).sendMsg();
    }

    public void toDoChoseStartingPlayer(){
        new MsgSender(clients.get(challenger), new ToDoMsgWriter().toDoAction("choseStartingPlayer"));
        for (String user : clients.keySet())
            if(!user.equals(challenger))
                new MsgSender(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(challenger,"choseStartingPlayer")).sendMsg();
    }

    @Override

    public void toDoSetupWorkerOnBoard(String username, String gender){
        new MsgSender(clients.get(username), new ToDoMsgWriter().toDoAction("setup" + gender + "WorkerOnBoard"));
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(username,"setup" + gender + "WorkerOnBoard")).sendMsg();
    }

    @Override

    public void toDoTurn(String username, String firstOperation){
        new MsgSender(clients.get(username), new ToDoMsgWriter().toDoTurn(firstOperation));
        for (String user : clients.keySet())
            if(!user.equals(username))
                new MsgSender(clients.get(user), new ToDoMsgWriter().toDoWaitMsg(username,"hisTurn")).sendMsg();
    }

    // Win/Lose cases methods

    @Override
    public void directlyWinCase(String winnerUsername){
        new MsgSender(clients.get(winnerUsername), new UpdateMsgWriter().winLoseUpdate(winnerUsername,"youWinDirectly"));
        for (String user : clients.keySet())
            if(!user.equals(winnerUsername)){
                new MsgSender(clients.get(user), new UpdateMsgWriter().winLoseUpdate(winnerUsername,"youLoseForDirectWin")).sendMsg();
            }
        matchFinished();
    }

    @Override
    public void match2PlayerLose(String winnerUsername){
        new MsgSender(clients.get(winnerUsername), new UpdateMsgWriter().winLoseUpdate(winnerUsername,"youWinForAnotherLose"));
        for (String user : clients.keySet())
            if(!user.equals(winnerUsername)){
                new MsgSender(clients.get(user), new UpdateMsgWriter().winLoseUpdate(winnerUsername,"youLoseForBlocked")).sendMsg();
            }
        matchFinished();
    }

    @Override
    public void match3PlayerLose(String loserUsername){
        new MsgSender(clients.get(loserUsername), new UpdateMsgWriter().extraUpdate("youLoseForBlocked")).sendMsg();
        for (String user : clients.keySet())
            if(!user.equals(loserUsername)){
                new MsgSender(clients.get(user), new UpdateMsgWriter().loseUpdate(loserUsername,"loser")).sendMsg();
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
                if(c.isConnected()) {
                    new MsgSender(c, new UpdateMsgWriter().extraUpdate("disconnection"));
                }
            clientDisconnectionListener.onClientDown(this);
        }
    }

    public void matchFinished(){
        for(Socket c : clients.values())
            if(c.isConnected()) {
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        System.out.println("Disconnection lobby number " + lobbyNumber + " for match finished");
        clientDisconnectionListener.onMatchFinish(this);
    }
}
