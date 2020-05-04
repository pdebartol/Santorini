package it.polimi.ingsw.network.server;

import it.polimi.ingsw.view.server.VirtualView;

public interface ClientDisconnectionListener {
    void onClientDown(VirtualView v);
}
