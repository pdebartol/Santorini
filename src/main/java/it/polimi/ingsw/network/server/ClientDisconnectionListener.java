package it.polimi.ingsw.network.server;

import it.polimi.ingsw.view.server.VirtualView;

/**
 * This interface defines server's action to manage a client disconnection situation.
 * (it is a part of Observer pattern)
 * @author marcoDige
 */

public interface ClientDisconnectionListener {
    void onClientDown(VirtualView v);
}
