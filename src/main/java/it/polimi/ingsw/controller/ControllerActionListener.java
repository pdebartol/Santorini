package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;

import java.util.ArrayList;

/**
 * This interface defines the view's actions on which the controller updates the model
 * (it is a part of Observer pattern).
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
     ArrayList<Error> onWorkerMove(String playerUsername, String workerGender, int x, int y);
     ArrayList<Error> onWorkerBuild(String playerUsername, String workerGender, int x, int y, int level);
     ArrayList<Error> onPlayerEndTurn(String playerUsername);
}
