package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.model.enums.State;
import it.polimi.ingsw.view.server.ViewInterface;

import java.util.*;

/**
 * MatchController implements the controller for the current match.
 * @author pierobartolo
 */

public class MatchController implements ControllerInterface {

    /**
     * playerController handles the players ArrayList.
     */

    PlayerController playerController;

    /**
     * The current match's board.
     */

    private final Board gameBoard;

    /**
     * selectedGods contains the gods selected by the challenger, indexed by their ids.
     */

    HashMap<Integer,God> selectedGods;

    ViewInterface viewInterface;

    public MatchController(){
        playerController = new PlayerController();
        gameBoard = new Board();
        selectedGods = new HashMap<>();
        viewInterface = null;
    }

    @Override

    public void setViewInterface(ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    /**
     * This method is triggered when a new player logs in.
     * @param playerUsername the username of the new player.
     * @param workerColor the worker's color for the new player.
     * @return an ArrayList containing the errors (if there were some):
     *         LOGIN_COLOR_NOT_AVAILABLE --> the color was taken by another player
     *         LOGIN_USERNAME_NOT_AVAILABLE --> the username was taken by another player
     *         LOGIN_TOO_MANY_PLAYERS --> there are already three players connected
     */

    @Override
    public List<Error> onNewPlayer(String playerUsername, Color workerColor) {
        if (gameBoard.getGameState() != State.LOGIN) throw new IllegalStateException("Can't add a new player in this state!");
        if(workerColor == null) throw new IllegalArgumentException("Invalid worker color during login!");
        if(playerUsername == null || playerUsername.isEmpty()) throw new IllegalArgumentException("Invalid username during login!");
        return playerController.addPlayer(new Player(playerUsername, workerColor));
    }

    /**
     * This method is triggered when the first player starts the game.
     * @return a String containing the challenger username.
     */

    //TODO check that there are no conflicts
    @Override
    public String onStartGame() {
        if(gameBoard.getGameState() != State.LOGIN) throw new IllegalStateException("Can't start the game in this state!");
        if(playerController.getNumberOfPlayers() < 2) throw new IllegalStateException("Can't start the game with less than 2 players!");
        Random randomGenerator = new Random();
        int challengerIndex = randomGenerator.nextInt(playerController.getNumberOfPlayers());
        playerController.setCurrentPlayerByIndex((challengerIndex));
        playerController.setChallengerUsername(playerController.getCurrentPlayer().getUsername());
        gameBoard.setGameState("gods_setup");
        return playerController.getPlayerByIndex(challengerIndex).getUsername();
    }

    /**
     * This method is triggered in the initial phase, when the challenger choose the gods for the match.
     * @param playerUsername username of the player making the request
     * @param godIds contains the ids of the chosen gods.
     * @return an ArrayList containing the errors (if there were some):
     *          SETUP_IS_NOT_CHALLENGER --> only the challenger chan choose the gods
     */

    @Override
    public List<Error> onChallengerChooseGods(String playerUsername, ArrayList<Integer> godIds) {
        if(gameBoard.getGameState() != State.GODS_SETUP) throw new IllegalStateException("Can't setup the gods in this state!");
        List<Error> errors = new ArrayList<>();
        if(!playerController.getChallengerUsername().equals(playerUsername)) errors.add(Error.SETUP_IS_NOT_CHALLENGER);
        if(errors.isEmpty()){
            try {
                GodsFactory factory = new GodsFactory(gameBoard);
                ArrayList<God> temp_gods = factory.getGods(godIds);
                for (int i = 0; i < godIds.size(); i++) {
                    selectedGods.put(godIds.get(i), temp_gods.get(i));
                }
                playerController.nextTurn();
                gameBoard.setGameState("choose_god");
            }

            catch(Exception e){
                throw new IllegalArgumentException("Illegal god ids!");
            }
        }

        return Collections.unmodifiableList(errors);
    }


    /**
     * This method is triggered when the player selects his god.
     * @param playerUsername username of the player making the request
     * @param godId id of the god chosen
     * @return an ArrayList containing the errors (if there were some):
     *         INGAME_NOT_YOUR_TURN --> player cannot choose the god if it is not his turn
     */

    @Override
    public List<Error> onPlayerChooseGod(String playerUsername, Integer godId) {
        if(gameBoard.getGameState() != State.CHOOSE_GOD) throw new IllegalStateException("Can't choose a god in this state!");
        List<Error> errors = new ArrayList<>();
        if(!playerController.getCurrentPlayer().getUsername().equals(playerUsername)) errors.add(Error.INGAME_NOT_YOUR_TURN);
        if(errors.isEmpty()){
            playerController.getCurrentPlayer().setGod(selectedGods.get(godId));
            selectedGods.remove(godId);
            // if it is not the challenger end the turn
            if(!playerController.getChallengerUsername().equals(playerUsername)){
                playerController.nextTurn();
            }
            else{
                gameBoard.setGameState("choose_starter");
            }
    }
        return Collections.unmodifiableList(errors);

    }


    /**
     * This method is triggered in the initial phase, when the challenger chooses the player starting the match.
     * @param playerUsername username of the player making the request
     * @param chosenPlayer username of the starting player
     * @return an ArrayList containing the errors (if there were some):
     *         SETUP_IS_NOT_CHALLENGER --> only the challenger chan choose the starting player
     *         INGAME_NOT_YOUR_TURN --> player cannot choose the god if it is not his turn
     */

    @Override
    public List<Error> onChallengerChooseStartingPlayer(String playerUsername, String chosenPlayer) {
        if(gameBoard.getGameState() != State.CHOOSE_STARTER) throw new IllegalStateException("Can't choose the starter player in this state!");
        List<Error> errors = new ArrayList<>();
        if(!playerController.getChallengerUsername().equals(playerUsername)) errors.add(Error.SETUP_IS_NOT_CHALLENGER);
        if(!playerController.getCurrentPlayer().getUsername().equals(playerUsername)) errors.add(Error.INGAME_NOT_YOUR_TURN);

        if(errors.isEmpty()){
            playerController.setCurrentPlayer(playerController.getPlayerByUsername(chosenPlayer));
            playerController.setStarterIndex(playerController.getPlayerIndex(chosenPlayer));
            gameBoard.setGameState("set_workers");
        }

        return Collections.unmodifiableList(errors);
    }


    /**
     * This method is triggered in the initial phase, when the player places his workers on the board.
     * @param playerUsername username of the player making the request
     * @param workerGender gender of the worker placed
     * @param x is the x square coordinate where the worker is set
     * @param y is the y square coordinate where the worker is set
     * @return an ArrayList containing the errors (if there were some):
     *         SETUP_WORKER_ON_OCCUPIED_SQUARE --> cannot set the worker on square that is not free
     *         INGAME_NOT_YOUR_TURN --> player cannot place a worker if it is not his turn
     *         SETUP_WORKER_ALREADY_SET --> cannot set the worker if it's already on a square
     */

    @Override
    public List<Error> onPlayerSetWorker(String playerUsername, String workerGender, int x, int y) {
        if (gameBoard.getGameState() != State.SET_WORKERS)
            throw new IllegalStateException("Can't set workers in this state!");
        List<Error> errors = new ArrayList<>();
        Worker chosenWorker = playerController.getCurrentPlayer().getWorkerByGender(workerGender);

        if (!playerController.getCurrentPlayer().getUsername().equals(playerUsername)) errors.add(Error.INGAME_NOT_YOUR_TURN);
        if (!gameBoard.getSquare(x, y).isFree()) errors.add(Error.SETUP_WORKER_ON_OCCUPIED_SQUARE);
        if (chosenWorker.getCurrentSquare() != null) errors.add(Error.SETUP_WORKER_ALREADY_SET);
            // set Worker
        if (errors.isEmpty()) {
                chosenWorker.setWorkerOnBoard(gameBoard.getSquare(x, y));
                if (workerGender.equals("female")) {
                    playerController.nextTurn();

                    // if everyone set the workers go to next state
                    if (playerController.getPlayerByIndex((playerController.getStarterIndex() + playerController.getNumberOfPlayers() - 1) % playerController.getNumberOfPlayers()).getUsername().equals(playerUsername)) {
                        gameBoard.setGameState("in_game");
                        System.out.println("Game started!");
                    }
                }
            }
        return Collections.unmodifiableList(errors);
    }

    /**
     * This method is triggered when the player moves his worker.
     * @param playerUsername username of the player making the request
     * @param workerGender gender of the worker moved
     * @param x is the x square coordinate where the worker wants to move
     * @param y is the y square coordinate where the worker wants to move
     * @return an ArrayList containing the errors (if there were some):
     *        INGAME_NOT_YOUR_TURN --> player cannot move a worker if it is not his turn
     *        INGAME_WRONG_WORKER --> player cannot move the inactive worker
     */

    @Override
    public List<Error> onWorkerMove(String playerUsername, String workerGender, int x, int y) {
        if(gameBoard.getGameState() != State.IN_GAME) throw new IllegalStateException("Can't move the worker in this state!");
        List<Error> errors = new ArrayList<>();
        Player currentPlayer = playerController.getCurrentPlayer();
        if(!currentPlayer.getUsername().equals(playerUsername)){
            errors.add(Error.INGAME_NOT_YOUR_TURN);
            return Collections.unmodifiableList(errors);
        }
        Worker selectedWorker = currentPlayer.getWorkerByGender(workerGender);
        Worker activeWorker = currentPlayer.getActiveWorker();

        gameBoard.setMsgContainer(playerUsername,"move");

        if(activeWorker != null){
            if(!selectedWorker.getGender().equals(activeWorker.getGender())){
                errors.add(Error.INGAME_WRONG_WORKER);
                return Collections.unmodifiableList(errors);
            }

            errors.addAll(currentPlayer.move(selectedWorker,x,y));
        }
        else{
            selectedWorker.isMovingOn();
            errors.addAll(currentPlayer.move(selectedWorker,x,y));
            if(!errors.isEmpty()){
                selectedWorker.isMovingOff();
            }
        }

        //Check if currentPlayer has won, if is it, it send a message to indicate that the match is finished and currentPlayer has won
        if(errors.isEmpty() && viewInterface != null){
            if(currentPlayer.checkWin(activeWorker)) viewInterface.directlyWinCase(currentPlayer.getUsername());
        }

        //if the player have to build and he can't, he lose
        if(currentPlayer.possibleOperation().equals("blocked"))
            if(errors.isEmpty() && viewInterface != null){
                if(playerController.getNumberOfPlayers() == 3){
                    if (viewInterface != null) viewInterface.match3PlayerLose(currentPlayer.getUsername());
                }
                else{
                    // notify that match is finished and currentPlayer lose -> there are only 2 players so nextPlayer is the winner
                    if (viewInterface != null) viewInterface.match2PlayerLose(playerController.getNextPlayer().getUsername());
                }
                playerController.removeCurrentPlayer();
                playerController.nextTurn();

                if (viewInterface != null) sendNextToDoTurn();
            }

        return errors;
    }


    /**
     * This method is triggered when the player builds something.
     * @param playerUsername username of the player making the request
     * @param workerGender gender of the worker moved
     * @param x is the x square coordinate where the worker wants to build
     * @param y is the y square coordinate where the worker wants to build
     * @param level is the level that the worker wants to build
     * @return an ArrayList containing the errors (if there were some):
     *        INGAME_NOT_YOUR_TURN --> player cannot move a worker if it is not his turn
     *        INGAME_WRONG_WORKER --> player cannot move the inactive worker
     */

    @Override
    public List<Error> onWorkerBuild(String playerUsername, String workerGender, int x, int y, int level) {
        if(gameBoard.getGameState() != State.IN_GAME) throw new IllegalStateException("Can't build the worker in this state!");
        List<Error> errors = new ArrayList<>();
        Player currentPlayer = playerController.getCurrentPlayer();
        Worker selectedWorker = currentPlayer.getWorkerByGender(workerGender);
        Worker activeWorker = currentPlayer.getActiveWorker();

        gameBoard.setMsgContainer(playerUsername,"build");

        if(!currentPlayer.getUsername().equals(playerUsername)){
            errors.add(Error.INGAME_NOT_YOUR_TURN);
            return Collections.unmodifiableList(errors);
        }

        if(activeWorker != null){
            if(!selectedWorker.getGender().equals(activeWorker.getGender())){
                errors.add(Error.INGAME_WRONG_WORKER);
                return Collections.unmodifiableList(errors);
            }
            return currentPlayer.build(selectedWorker,x,y,level);
        }
        else{
            selectedWorker.isMovingOn();
            List<Error> temp_errors =  currentPlayer.build(selectedWorker,x,y,level);
            if(!temp_errors.isEmpty()){
                selectedWorker.isMovingOff();
            }
            return temp_errors;
        }
    }


    /**
     * This method is triggered when the player ends his turn.
     * It sets the current player to the next player.
     * It check if the next player can move.
     * @param playerUsername username of the player making the request
     * @return an ArrayList containing the errors (if there were some):
     *         INGAME_NOT_YOUR_TURN --> player cannot move a worker if it is not his turn
     */

    @Override
    public List<Error> onPlayerEndTurn(String playerUsername) {
        if(gameBoard.getGameState() != State.IN_GAME) throw new IllegalStateException("Can't end the turn in this state!");
        Player currentPlayer = playerController.getCurrentPlayer();
        List<Error> errors = new ArrayList<>();

        if(!currentPlayer.getUsername().equals(playerUsername)){
            errors.add(Error.INGAME_NOT_YOUR_TURN);
            return Collections.unmodifiableList(errors);
        }

        gameBoard.setMsgContainer(playerUsername,"endOfTurn");

        // player can end his turn
        if(playerController.getCurrentPlayer().endTurn()){

            // next player has lost
            if(playerController.getNextPlayer().possibleOperation().equals("blocked")){
                if(playerController.getNumberOfPlayers() == 3){
                    if (viewInterface != null) viewInterface.match3PlayerLose(playerController.getNextPlayer().getUsername());
                }
                else{
                    // notify that match is finished and next player loses -> there are only 2 players so currentPlayer is the winner
                    if (viewInterface != null) viewInterface.match2PlayerLose(currentPlayer.getUsername());
                }
                playerController.removeNextPlayer();
            }

            playerController.nextTurn();
        }
        //player cannot end his turn
        else
            errors.add(Error.INGAME_CANNOT_END_TURN);
        return Collections.unmodifiableList(errors);
    }

    //Message management methods

    @Override
    public State getGameState() {
        return gameBoard.getGameState();
    }

    @Override
    public void sendAnswerMoveAccepted(String playerUsername){
        //Notify view that currentPlayer move request has been accepted
        viewInterface.onMoveAcceptedRequest(playerUsername,gameBoard.getMsgContainer().getAnswerMsg(),gameBoard.getMsgContainer().getUpdateMsg());
    }

    @Override
    public void sendAnswerBuildAccepted(String playerUsername){
        //Notify view that currentPlayer build request has been accepted
        viewInterface.onBuildAcceptedRequest(playerUsername,gameBoard.getMsgContainer().getAnswerMsg(),gameBoard.getMsgContainer().getUpdateMsg());
    }

    @Override
    public void sendAnswerEndOfTurnAccepted(String playerUsername){
        //Notify view that currentPlayer finished his turn
        viewInterface.onEndOfTurnAcceptedRequest(playerUsername,gameBoard.getMsgContainer().getAnswerMsg(),gameBoard.getMsgContainer().getUpdateMsg());
    }

    @Override
    public void sendNextToDoChoseGod(){
        //Send to currentPlayer a massage which indicates that currentPlayer have to chose a god
        viewInterface.toDoChoseGod(playerController.getCurrentPlayer().getUsername(),new ArrayList<>(selectedGods.keySet()));
    }

    @Override
    public void sendNextToDoChoseStartingPlayer(){
        //Send to challenger a msg to indicate that he have to choose the starter
        viewInterface.toDoChoseStartingPlayer();
    }

    @Override
    public void sendNextToDoSetupWorkerOnBoard(String gender){
        viewInterface.toDoSetupWorkerOnBoard(playerController.getCurrentPlayer().getUsername(),gender);
    }

    @Override
    public void sendNextToDoTurn(){
        //notify currentPlayer that his turn has started
        viewInterface.toDoTurn(playerController.getCurrentPlayer().getUsername(),playerController.getCurrentPlayer().possibleOperation());
    }
}
