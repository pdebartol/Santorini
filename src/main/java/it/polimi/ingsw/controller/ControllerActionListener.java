package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.view.server.ViewActionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface defines the view's actions on which the controller updates the model
 * (it is a part of Observer pattern).
 * @author pierobartolo
 */

public interface ControllerActionListener {
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

     void setViewActionListener(ViewActionListener viewActionListener);
}
