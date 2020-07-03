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



    void onMoveAcceptedRequest(String username, Document answerMsg, Document updateMsg);



    void onBuildAcceptedRequest(String username, Document answerMsg, Document updateMsg);



    void onEndOfTurnAcceptedRequest(String username, Document answerMsg, Document updateMsg);



    void onRejectedRequest(String username, List<Error> errors, String mode);

    //to do methods



    void toDoChoseStartingPlayer();



    void toDoTurn(String username, String firstOperation);



    void toDoSetupWorkerOnBoard(String username, String gender);



    void toDoChoseGod(String username, List<Integer> ids);

    //Win/Lose methods


    void directlyWinCase(String winnerUsername);



    void match2PlayerLose(String loserUsername);



    void match3PlayerLose(String loserUsername);
}
