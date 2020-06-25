package it.polimi.ingsw.network.server;

import it.polimi.ingsw.view.server.VirtualView;

/**
 * This interface defines server's action to manage a client disconnection situation.
 * (it is a part of Observer pattern)
 * @author marcoDige
 */

public interface ClientDisconnectionListener {

    /**
     * This method is called when a client connection go down, it provides to remove the lobby where this client was
     * @param v is the virtual view represents a lobby/match
     */

    void onClientDown(VirtualView v);

    /**
     * This method is called when the match have to finish. It provides to visualize a message on the server and to
     * delete the lobby where the match was evolving.
     * @param v is the lobby's virtualView
     */

    void onMatchFinish(VirtualView v);
}
