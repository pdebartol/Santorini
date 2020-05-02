package it.polimi.ingsw.view.server;

/**
 * This interface defines the model's actions on which the view send the request answer to client
 * (it is a part of Observer pattern).
 * @author marcoDige
 */

public interface ViewActionListener {

    void onMoveAcceptedRequest();
    void onMoveRejectedRequest();
    void onBuildAcceptedRequest();
    void onBuildRejectedRequest();
}
