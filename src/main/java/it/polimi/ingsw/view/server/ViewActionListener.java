package it.polimi.ingsw.view.server;

import it.polimi.ingsw.model.enums.Error;
import org.w3c.dom.Document;
import java.util.List;

/**
 * This interface defines the controller's actions on which the virtual view send a message to client.
 * (it is a part of Observer pattern).
 * @author marcoDige
 */

public interface ViewActionListener {
    public void onMoveAcceptedRequest(String username, Document answerMsg, Document updateMsg);
    public void onBuildAcceptedRequest(String username, Document answerMsg, Document updateMsg);
    public void onEndOfTurnAcceptedRequest(String username, Document answerMsg, Document updateMsg);
    public void onRejectedRequest(String username, List<Error> errors, String mode);

    public void toDoTurn(String username, String firstOperation);
    public void toDoSetupWorkerOnBoard(String username);
    public void toDoChoseGod(String username, List<Integer> ids);
}
