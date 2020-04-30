package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;

import java.util.ArrayList;

/**
 * This interface defines the actions on which the controller updates the model.
 * @author pierobartolo
 */

public interface ControllerActionListener {
     // setup methods
     ArrayList<Error> onNewPlayer(String playerUsername, Color workerColor);
     String onStartGame();
     ArrayList<Error> onChallengerChooseGods(String playerUsername, ArrayList<Integer> godIds);
     ArrayList<Error> onPlayerChooseGod(String playerUsername, Integer godId);
     ArrayList<Error> onChallengerChooseStartingPlayer(String playerUsername, String chosenPlayer);
     ArrayList<Error> onPlayerSetWorker(String playerUsername, String workerGender, int x, int y);

     // in-game methods
     void onWorkerMove(String playerUsername, String workerGender, int x, int y);
     void onWorkerBuild(String playerUsername, String workerGender, int x, int y, int level);
     void onPlayerEndTurn(String playerUsername);
}
