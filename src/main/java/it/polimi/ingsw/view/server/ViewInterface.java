package it.polimi.ingsw.view.server;

import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.msgUtilities.server.UpdateMsgWriter;
import it.polimi.ingsw.network.MsgSender;
import org.w3c.dom.Document;
import java.util.List;

/**
 * This interface defines the view's method that controller can call.
 * @author marcoDige
 */

public interface ViewInterface {

    //Accepted/Rejected request methods

    /**
     * This method handles the case where the move request from the player has been accepted. It sends a
     * positive response message to the client, and updates it to other clients in the match.
     * @param username is the player who made the request
     * @param answerMsg is the document to send to the player who made de request
     * @param updateMsg is the document to send to the other players
     */

    void onMoveAcceptedRequest(String username, Document answerMsg, Document updateMsg);

    /**
     * This method handles the case where the build request from the player has been accepted. It sends a
     * positive response message to the client, and updates it to other clients in the match.
     * @param username is the player who made the request
     * @param answerMsg is the document to send to the player who made the request
     * @param updateMsg is the document to send to the other players
     */

    void onBuildAcceptedRequest(String username, Document answerMsg, Document updateMsg);

    /**
     * This method handles the case where the endOfTurn request from the player has been accepted. It sends a
     * positive response message to the client, and updates it to other clients in the match. It warns the next player
     * that he has to start his turn.
     * @param username is the player who made the request
     * @param answerMsg is the document to send to the player who made the request
     * @param updateMsg is the document to send to the other players
     */

    void onEndOfTurnAcceptedRequest(String username, Document answerMsg, Document updateMsg);

    /**
     * This method handles the case where the request from the player has been rejected. It sends a message where
     * it alerts the client that the request has been rejected.
     * @param username is the is the username of the player who made the request
     * @param errors is the error list where the errors that led to the rejection are contained
     * @param mode is the message mode
     */

    void onRejectedRequest(String username, List<Error> errors, String mode);

    //to do methods

    /**
     * This method sends a warning message to the client that needs to chose the starting player and a wait message
     * to the other clients.
     */

    void toDoChoseStartingPlayer();

    /**
     * This method sends a warning message to the client that has to play his turn and a wait message
     * to the other clients.
     * @param username is the username of the player who has to play his turn
     * @param firstOperation is the first operation player can do in his turn
     */

    void toDoTurn(String username, String firstOperation);

    /**
     * This method sends a warning message to the client that has to setup his gender worker on board and a wait message
     * to the other clients.
     * @param username is the username of the player who has to place his gender worker on the board
     * @param gender is the gender of the worker to place on board
     */

    void toDoSetupWorkerOnBoard(String username, String gender);

    /**
     * This method sends a warning message to the client that has to chose his god and a wait message
     * to the other clients.
     * @param username is the username of the player who has to chose his god
     * @param ids is the list of the remaining gods
     */

    void toDoChoseGod(String username, List<Integer> ids);

    //Win/Lose methods

    /**
     * This method handles the case where there is a directly winner in the game. It warns the winner player that he has
     * won and the other players that they have lost. It warns all players that the match has finished.
     * @param winnerUsername is the player who has won.
     */

    void directlyWinCase(String winnerUsername);

    /**
     * This method handles the case where there is a loser in a 2 players match. It warns the winner player that he has
     * won and loser that he lost. It warns all players that the match has finished.
     * @param winnerUsername is the player who has won.
     */

    void match2PlayerLose(String winnerUsername);

    /**
     * This method handles the case where there is loser in a 3 players match. It warns the loser player that he lost.
     * @param loserUsername is the player who has lost.
     */

    void match3PlayerLose(String loserUsername);
}
