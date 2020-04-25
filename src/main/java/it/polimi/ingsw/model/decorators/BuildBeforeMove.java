package it.polimi.ingsw.model.decorators;


import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;

/**
 * This class implements the power to build both before and after moving if the worker does not move up.
 * This power decorates Prometheus' power.
 * @author pierobartolo
 */

public class BuildBeforeMove extends PowerDecorator {

    //constructors

    public BuildBeforeMove(Power p) {
        super(p);
    }

    //methods

    /**
     * This method overrides checkMove (Power Decorator) decorating decoratedPower with BuildBeforeMove rules.
     * @param w is the worker that wants to move
     * @param x is the x square coordinate where the worker wants to move
     * @param y is the y square coordinate where the worker wants to move
     * @return an ArrayList that is empty if the move is legal, otherwise it contains the errors that prevent the worker from moving.
     */

    @Override
    public ArrayList<Error> checkMove(Worker w, int x, int y) {
        ArrayList<Error> errors = decoratedPower.checkMove(w, x, y);
        if (decoratedPower.getBoard().getNBuild() > 0) { // build before move
            if(w.getCurrentSquare().getLevel() < decoratedPower.getBoard().getSquare(x,y).getLevel())
                errors.add(Error.CANT_MOVE_UP);
            errors.remove(Error.MOVE_AFTER_BUILD);
        }
        return errors;
    }

    /**
     * This method overrides checkBuild (PowerDecorator) decorating decoratedPower with BuildBeforeMove rules.
     * @param w is the worker that wants to build
     * @param x is the x square coordinate where the worker wants to build
     * @param y is the y square coordinate where the worker wants to build
     * @param l is the level the worker wants to build
     * @return an ArrayList that is empty if the build is legal, otherwise it contains the errors that prevent the worker from building.
     */

    @Override
    public ArrayList<Error> checkBuild(Worker w, int x, int y, int l) {
        ArrayList<Error> errors = super.checkBuild(w, x, y, l);
        if(errors.contains(Error.BUILD_BEFORE_MOVE) && decoratedPower.getBoard().getNBuild() == 0){ // build before move
            errors.remove(Error.BUILD_BEFORE_MOVE);
        }
        return errors;
    }

}
