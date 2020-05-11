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
     List<Error> onNewPlayer(String playerUsername, Color workerColor);
     String onStartGame();
     List<Error> onChallengerChooseGods(String playerUsername, ArrayList<Integer> godIds);
     List<Error> onPlayerChooseGod(String playerUsername, Integer godId);
     List<Error> onChallengerChooseStartingPlayer(String playerUsername, String chosenPlayer);
     List<Error> onPlayerSetWorker(String playerUsername, String workerGender, int x, int y);

     // in-game methods
     List<Error> onWorkerMove(String playerUsername, String workerGender, int x, int y);
     List<Error> onWorkerBuild(String playerUsername, String workerGender, int x, int y, int level);
     List<Error> onPlayerEndTurn(String playerUsername);
     State getGameState();

     void setViewInterface(ViewInterface viewInterface);

     void sendNextToDoChoseGod();
     void sendNextToDoChoseStartingPlayer();
     void sendNextToDoSetupWorkerOnBoard(String gender);
     void sendNextToDoTurn();
}
