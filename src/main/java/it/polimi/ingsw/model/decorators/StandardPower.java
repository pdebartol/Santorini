package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class StandardPower implements Power {

    @Override
    public boolean checkMove(Worker w, Square s) {
        return false;
    }

    @Override
    public boolean checkBuild(Worker w, Square s) {
        return false;
    }

    @Override
    public boolean checkWin() {
        return false;
    }

    @Override
    public void updateMove(Worker w, Square s) {

    }

    @Override
    public void updateBuild(Worker w, Square s) {

    }

    @Override
    public Integer getMaxMoves() {
        return null;
    }

    @Override
    public Integer getMaxBuilds() {
        return null;
    }

    @Override
    public Board getBoard() {
        return null;
    }
}
