package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Error;

import java.util.ArrayList;

/**
 * PowerDecorator is an abstract class necessary as a basis for the decoration of gods' powers (it is a part of Decorator pattern).
 * All methods call the corresponding decoratedPower method.
 * @author marcoDige
 */

public abstract class PowerDecorator implements Power {

    //attributes

    /**
     * decoratedPower represents the decorated power.
     */

    protected Power decoratedPower;

    //methods

    public PowerDecorator(Power p){
        super();
        this.decoratedPower = p;
    }

    @Override
    public ArrayList<Error> checkMove(Worker w, int x, int y) {
        return decoratedPower.checkMove(w, x ,y);
    }

    @Override
    public ArrayList<Error> checkBuild(Worker w, int x, int y, int l) {
        return decoratedPower.checkBuild(w, x, y, l);
    }

    @Override
    public boolean checkWin(Worker w) {
        return decoratedPower.checkWin(w);
    }

    @Override
    public void updateMove(Worker w, int x, int y) {
        decoratedPower.updateMove(w, x, y);
    }

    @Override
    public void updateBuild(Worker w, int x, int y, int l) {
        decoratedPower.updateBuild(w, x, y, l);
    }

    @Override
    public Board getBoard() {
        return decoratedPower.getBoard();
    }

    @Override
    public boolean endOfTurn(ArrayList<Worker> workers){return decoratedPower.endOfTurn(workers);}
}
