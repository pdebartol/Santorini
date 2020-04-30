package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * MatchController implements the controller for the current match.
 * @author pierobartolo
 */

public class MatchController implements ControllerActionListener {

    /**
     * playerController handles the players ArrayList.
     */

    PlayerController playerController;

    /**
     * The current match's board.
     */

    private Board gameBoard;

    /**
     * selectedGods contains the gods selected by the challenger, indexed by their ids.
     */

    HashMap<Integer,God> selectedGods;

    public MatchController(){
        playerController = new PlayerController();
        gameBoard = new Board();
        selectedGods = new HashMap<>();
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
    public ArrayList<Error> onNewPlayer(String playerUsername, Color workerColor) {
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
        Random randomGenerator = new Random();
        int challengerIndex = randomGenerator.nextInt(playerController.getNumberOfPlayers());
        playerController.setCurrentPlayerByIndex((challengerIndex));
        playerController.setChallengerUsername(playerController.getCurrentPlayer().getUsername());
        return playerController.getPlayerByIndex(challengerIndex).getUsername();
    }

    /**
     * This method is triggered in the initial phase, when the challenger choose the gods for the match.
     * @param godIds contains the ids of the chosen gods.
     * @return an ArrayList containing the errors (if there were some):
     *          SETUP_IS_NOT_CHALLENGER --> only the challenger chan choose the gods
     */

    @Override
    public ArrayList<Error> onChallengerChooseGods(String playerUsername, ArrayList<Integer> godIds) {
        ArrayList<Error> errors = new ArrayList<>();
        if(!playerController.getChallengerUsername().equals(playerUsername)) errors.add(Error.SETUP_IS_NOT_CHALLENGER);
        if(errors.isEmpty()){
            try {
                GodsFactory factory = new GodsFactory(gameBoard);
                ArrayList<God> temp_gods = factory.getGods(godIds);
                for (int i = 0; i < godIds.size(); i++) {
                    selectedGods.put(godIds.get(i), temp_gods.get(i));
                }
                playerController.nextTurn();
            }

            catch(Exception e){
                throw new IllegalArgumentException("Illegal god ids!");
            }
        }

        return errors;
    }


    /**
     * This method is triggered when the player selects his god.
     * @param godId id of the god chosen
     * @return an ArrayList containing the errors (if there were some):
     *         INGAME_NOT_YOUR_TURN --> player cannot choose the god if it is not his turn
     */

    @Override
    public ArrayList<Error> onPlayerChooseGod(String playerUsername, Integer godId) {
        ArrayList<Error> errors = new ArrayList<>();
        if(!playerController.getCurrentPlayer().getUsername().equals(playerUsername)) errors.add(Error.INGAME_NOT_YOUR_TURN);
        if(errors.isEmpty())
            playerController.getCurrentPlayer().setGod(selectedGods.get(godId));
            // if it is not the challenger end the turn
            if(!playerController.getChallengerUsername().equals(playerUsername))
                playerController.nextTurn();
        return errors;
    }

    /**
     * This method is triggered in the initial phase, when the challenger chooses the player starting the match.
     * @param playerUsername id of the chosen player.
     * @return an ArrayList containing the errors (if there were some):
     *         SETUP_IS_NOT_CHALLENGER --> only the challenger chan choose the starting player
     *         INGAME_NOT_YOUR_TURN --> player cannot choose the god if it is not his turn
     */

    @Override
    public ArrayList<Error> onChallengerChooseStartingPlayer(String playerUsername, String chosenPlayer) {
        ArrayList<Error> errors = new ArrayList<>();
        if(!playerController.getChallengerUsername().equals(playerUsername)) errors.add(Error.SETUP_IS_NOT_CHALLENGER);
        if(!playerController.getCurrentPlayer().getUsername().equals(playerUsername)) errors.add(Error.INGAME_NOT_YOUR_TURN);

        if(errors.isEmpty()) playerController.setCurrentPlayer(playerController.getPlayerByUsername(chosenPlayer));

        return errors;
    }


    /**
     * This method is triggered in the initial phase, when the player places his workers on the board.
     * @param workerGender gender of the worker placed
     * @param x is the x square coordinate where the worker is set
     * @param y is the y square coordinate where the worker is set
     * @return an ArrayList containing the errors (if there were some):
     *         SETUP_WORKER_ON_OCCUPIED_SQUARE --> cannot set the worker on square that is not free
     *         INGAME_NOT_YOUR_TURN --> player cannot place a worker if it is not his turn
     *         SETUP_WORKER_ALREADY_SET --> cannot set the worker if it's already on a square
     */

    @Override
    public ArrayList<Error> onPlayerSetWorker(String playerUsername, String workerGender, int x, int y) {
        ArrayList<Error> errors = new ArrayList<>();
        Worker chosenWorker = playerController.getCurrentPlayer().getWorkerByGender(workerGender);

        if(!playerController.getCurrentPlayer().getUsername().equals(playerUsername)) errors.add(Error.INGAME_NOT_YOUR_TURN);
        if(!gameBoard.getSquare(x,y).isFree()) errors.add(Error.SETUP_WORKER_ON_OCCUPIED_SQUARE);
        if(chosenWorker.getCurrentSquare() != null) errors.add(Error.SETUP_WORKER_ALREADY_SET);

        // set Worker
        if(errors.isEmpty()){
            chosenWorker.setWorkerOnBoard(gameBoard.getSquare(x,y));
            if(workerGender.equals("female")) playerController.nextTurn();
        }

        return errors;
    }

    /**
     * This method is triggered when the player moves his worker.
     * @param workerGender gender of the worker moved
     * @param x is the x square coordinate where the worker wants to move
     * @param y is the y square coordinate where the worker wants to move
     * @return an ArrayList containing the errors (if there were some):
     *        INGAME_NOT_YOUR_TURN --> player cannot move a worker if it is not his turn
     */

    @Override
    public void onWorkerMove(String playerUsername, String workerGender, int x, int y) {
        ArrayList<Error> errors = new ArrayList<>();
        Player currentPlayer = playerController.getCurrentPlayer();
        if(!currentPlayer.getUsername().equals(playerUsername)) errors.add(Error.INGAME_NOT_YOUR_TURN);
        Worker selectedWorker = currentPlayer.getWorkerByGender(workerGender);
        Worker activeWorker = currentPlayer.getActiveWorker();



        if(activeWorker != null){
            if(!selectedWorker.getGender().equals(activeWorker.getGender())){
                //TODO wrong worker
            }
            currentPlayer.move(selectedWorker,x,y);
        }
        else{
            List<Error> temp_errors =  currentPlayer.move(selectedWorker,x,y);
            if(temp_errors.isEmpty()){
                selectedWorker.isMovingOn();
            }
        }
    }

    /**
     * This method is triggered when the player builds something.
     * @param workerGender gender of the worker moved
     * @param x is the x square coordinate where the worker wants to build
     * @param y is the y square coordinate where the worker wants to build
     * @param level is the level that the worker wants to build
     */

    @Override
    public void onWorkerBuild(String playerUsername, String workerGender, int x, int y, int level) {
        Player currentPlayer = playerController.getCurrentPlayer();
        Worker selectedWorker = currentPlayer.getWorkerByGender(workerGender);
        Worker activeWorker = currentPlayer.getActiveWorker();

        if(!currentPlayer.getUsername().equals(playerUsername)){
            //TODO wrong player
        }

        if(activeWorker != null){
            if(!selectedWorker.getGender().equals(activeWorker.getGender())){
                //TODO wrong worker
            }
            currentPlayer.build(selectedWorker,x,y,level);
        }
        else{
            List<Error> temp_errors =  currentPlayer.build(selectedWorker,x,y,level);
            if(temp_errors.isEmpty()){
                selectedWorker.isMovingOn();
            }
        }
    }


    /**
     * This method is triggered when the player ends his turn.
     */

    @Override
    public void onPlayerEndTurn(String playerUsername) {
            Player currentPlayer = playerController.getCurrentPlayer();
            if(!currentPlayer.getUsername().equals(playerUsername)){
                //TODO wrong player
            }

            playerController.getCurrentPlayer().endTurn();
            playerController.getNextPlayer().canMove();
    }














}
