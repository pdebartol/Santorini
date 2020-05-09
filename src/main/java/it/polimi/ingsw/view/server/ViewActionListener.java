package it.polimi.ingsw.view.server;

import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.msgUtilities.server.AnswerMsgWriter;
import it.polimi.ingsw.network.MsgSender;
import org.w3c.dom.Document;

import java.net.Socket;
import java.util.List;

public interface ViewActionListener {
    public void onMoveAcceptedRequest(String username, Document answerMsg, Document updateMsg);
    public void onBuildAcceptedRequest(String username, Document answerMsg, Document updateMsg);
    public void onEndOfTurnAcceptedRequest();
    public void onRejectedRequest(String username, List<Error> errors, String mode);
}
