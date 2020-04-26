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

public class MatchController implements ActionListener {

    /**
     * playerController handles the players ArrayList.
     */
    PlayerController playerController;

    /**
     * The current match's board.
     */

    Board gameBoard;

    /**
     * selectedGods contains the gods selected by the challenger indexed by their ids.
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
     */

    @Override
    public void onNewPlayer(String playerUsername, Color workerColor) {
        playerController.addPlayer(new Player(playerUsername, workerColor));
    }

    /**
     * This method is triggered when the first player starts the game.
     * @return a String containing the challenger username.
     */

    @Override
    public String onStartGame() {
        Random randomGenerator = new Random();
        int challengerIndex = randomGenerator.nextInt(playerController.getNumberOfPlayers());
        playerController.setCurrentPlayerByIndex((challengerIndex+1)%playerController.getNumberOfPlayers());
        return playerController.getPlayerByIndex(challengerIndex).getUsername();
    }

    /**
     * This method is triggered in the initial phase, when the challenger choose the gods for the match.
     * @param godIds contains the ids of the chosen gods.
     */

    @Override
    public void onChallengerChooseGods(ArrayList<Integer> godIds) {
        GodsFactory factory = new GodsFactory(gameBoard);
        ArrayList<God> temp_gods = factory.getGods(godIds);
        for(int i=0; i< godIds.size();i++){
            selectedGods.put(godIds.get(i), temp_gods.get(i));
        }
    }


    /**
     * This method is triggered when the player chooses his god.
     * @param godId id of the god chosen
     */

    @Override
    public void onPlayerChooseGod(String playerUsername, Integer godId) {
        if(!playerController.getCurrentPlayer().getUsername().equals(playerUsername)){
            //TODO notify view that it's not their turn
        }
        playerController.getCurrentPlayer().setGod(selectedGods.get(godId));
        //TODO return next player
    }

    /**
     * This method is triggered in the initial phase, when the challenger chooses the player starting the match.
     * @param playerUsername id of the chosen player.
     */

    @Override
    public void onChallengerChooseStartingPlayer(String playerUsername) {
        playerController.setCurrentPlayer(playerController.getPlayerByUsername(playerUsername));
        //TODO notify view of the starting player
    }


    /**
     * This method is triggered in the initial phase, when the player places his workers on the board.
     * @param workerGender gender of the worker placed
     * @param x is the x square coordinate where the worker is set
     * @param y is the y square coordinate where the worker is set
     */

    @Override
    public void onPlayerSetWorker(String playerUsername, String workerGender, int x, int y) {
        if(!playerController.getCurrentPlayer().getUsername().equals(playerUsername)){
            //TODO notify view that it's not their turn

        }
        //TODO handle square occupied case
        playerController.getCurrentPlayer().getWorkerByGender(workerGender).setWorkerOnBoard(gameBoard.getSquare(x,y));
    }

    /**
     * This method is triggered when the player moves his worker.
     * @param workerGender gender of the worker moved
     * @param x is the x square coordinate where the worker wants to move
     * @param y is the y square coordinate where the worker wants to move
     */

    @Override
    public void onWorkerMove(String playerUsername, String workerGender, int x, int y) {
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
