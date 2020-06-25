package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.model.enums.State;
import it.polimi.ingsw.view.server.ViewInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface defines the controller's method that view can call.
 * @author pierobartolo
 */

public interface ControllerInterface {
     // setup methods

     /**
      * This method is triggered when a new player logs in.
      * @param playerUsername the username of the new player.
      * @param workerColor the worker's color for the new player.
      * @return an ArrayList containing the errors (if there were some):
      *         LOGIN_COLOR_NOT_AVAILABLE --> the color was taken by another player
      *         LOGIN_USERNAME_NOT_AVAILABLE --> the username was taken by another player
      *         LOGIN_TOO_MANY_PLAYERS --> there are already three players connected
      */

     List<Error> onNewPlayer(String playerUsername, Color workerColor);

     /**
      * This method is triggered when the first player starts the game.
      * @return a String containing the challenger username.
      */

     String onStartGame();

     /**
      * This method is triggered in the initial phase, when the challenger choose the gods for the match.
      * @param playerUsername username of the player making the request
      * @param godIds contains the ids of the chosen gods.
      * @return an ArrayList containing the errors (if there were some):
      *          SETUP_IS_NOT_CHALLENGER --> only the challenger chan choose the gods
      */

     List<Error> onChallengerChooseGods(String playerUsername, ArrayList<Integer> godIds);

     /**
      * This method is triggered when the player selects his god.
      * @param playerUsername username of the player making the request
      * @param godId id of the god chosen
      * @return an ArrayList containing the errors (if there were some):
      *         INGAME_NOT_YOUR_TURN --> player cannot choose the god if it is not his turn
      */

     List<Error> onPlayerChooseGod(String playerUsername, Integer godId);

     /**
      * This method is triggered in the initial phase, when the challenger chooses the player starting the match.
      * @param playerUsername username of the player making the request
      * @param chosenPlayer username of the starting player
      * @return an ArrayList containing the errors (if there were some):
      *         SETUP_IS_NOT_CHALLENGER --> only the challenger chan choose the starting player
      *         INGAME_NOT_YOUR_TURN --> player cannot choose the god if it is not his turn
      */

     List<Error> onChallengerChooseStartingPlayer(String playerUsername, String chosenPlayer);

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

     List<Error> onPlayerSetWorker(String playerUsername, String workerGender, int x, int y);

     // in-game methods

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

     List<Error> onWorkerMove(String playerUsername, String workerGender, int x, int y);

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

     List<Error> onWorkerBuild(String playerUsername, String workerGender, int x, int y, int level);

     /**
      * This method is triggered when the player ends his turn.
      * It sets the current player to the next player.
      * It check if the next player can move.
      * @param playerUsername username of the player making the request
      * @return an ArrayList containing the errors (if there were some):
      *         INGAME_NOT_YOUR_TURN --> player cannot move a worker if it is not his turn
      */

     List<Error> onPlayerEndTurn(String playerUsername);

     State getGameState();

     // virtual view interaction methods

     void setViewInterface(ViewInterface viewInterface);

     /**
      * This method call the method on virtualView that provides to send an Answer for an accepted move.
      * @param playerUsername is the player who is waiting for an answer.
      */

     void sendAnswerMoveAccepted(String playerUsername);

     /**
      * This method call the method on virtualView that provides to send an Answer for an accepted build.
      * @param playerUsername is the player who is waiting for an answer.
      */

     void sendAnswerBuildAccepted(String playerUsername);

     /**
      * This method call the method on virtualView that provides to send an Answer for an accepted end of turn.
      * @param playerUsername is the player who is waiting for an answer.
      */

     void sendAnswerEndOfTurnAccepted(String playerUsername);

     /**
      * This method call the method on virtualView that provides to send a message to notify a client that he has to chose his god.
      */

     void sendNextToDoChoseGod();

     /**
      * This method call the method on virtualView that provides to send a message to notify a client that he has to chose the starting player.
      */

     void sendNextToDoChoseStartingPlayer();

     /**
      * This method call the method on virtualView that provides to send a message to notify a client that he has to place a worker on board.
      */

     void sendNextToDoSetupWorkerOnBoard(String gender);

     /**
      * This method call the method on virtualView that provides to send a message to notify a client that he has to play his turn.
      */

     void sendNextToDoTurn();
}
