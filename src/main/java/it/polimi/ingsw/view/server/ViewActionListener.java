package it.polimi.ingsw.view.server;

import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.msgUtilities.server.AnswerMsgWriter;
import it.polimi.ingsw.network.MsgSender;

import java.net.Socket;
import java.util.List;

public interface ViewActionListener {
    public void onMoveAcceptedRequest();
    public void onBuildAcceptedRequest();
    public void onEndOfTurnAcceptedRequest();
    public void onRejectedRequest(String username, List<Error> errors, String mode);
}
