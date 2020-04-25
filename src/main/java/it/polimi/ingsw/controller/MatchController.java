package it.polimi.ingsw.controller;

import java.util.ArrayList;

public class MatchController implements ActionListener {


    /**
     * This method is triggered when the player moves his worker.
     * @param workerId id of the worker moved
     * @param x is the x square coordinate where the worker wants to move
     * @param y is the y square coordinate where the worker wants to move
     */

    @Override
    public void onWorkerMove(int workerId, int x, int y) {

    }

    /**
     * This method is triggered when the player builds something.
     * @param workerId id of the worker moved
     * @param x is the x square coordinate where the worker wants to build
     * @param y is the y square coordinate where the worker wants to build
     * @param level is the level that the worker wants to build
     */

    @Override
    public void onWorkerBuild(int workerId, int x, int y, int level) {

    }


    /**
     * This method is triggered when the player ends his turn.
     */

    @Override
    public void onPlayerEndTurn() {

    }

    /**
     * This method is triggered in the initial phase, when the player places his workers on the board.
     * @param workerId id of the worker placed
     * @param x is the x square coordinate where the worker is set
     * @param y is the y square coordinate where the worker is set
     */

    @Override
    public void onPlayerSetWorker(int workerId, int x, int y) {

    }

    /**
     * This method is triggered when the player chooses his god.
     * @param godId id of the god chosen
     */

    @Override
    public void onPlayerChooseGod(Integer godId) {

    }

    /**
     * This method is triggered in the initial phase, when the challenger choose the gods for the match.
     * @param godIds contains the ids of the chosen gods.
     */

    @Override
    public void onChallengerChooseGods(ArrayList<Integer> godIds) {

    }

    /**
     * This method is triggered in the initial phase, when the challenger chooses the player starting the match.
     * @param playerId id of the chosen player.
     */

    @Override
    public void onChallengerChooseStartingPlayer(int playerId) {

    }


}
