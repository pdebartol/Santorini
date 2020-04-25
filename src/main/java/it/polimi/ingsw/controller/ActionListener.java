package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;

/**
 * This interface defines the actions on which the controller updates the model.
 * @author pierobartolo
 */

public interface ActionListener {
     void onWorkerMove(String workerGender, int x, int y);
     void onWorkerBuild(String workerGender, int x, int y, int level);
     void onPlayerEndTurn();
     void onPlayerSetWorker(String workerGender, int x, int y);
     void onPlayerChooseGod(Integer godId);
     void onChallengerChooseGods(ArrayList<Integer> godIds);
     void onChallengerChooseStartingPlayer(String playerUsername);
}
