package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;

/**
 * This class implements the power which allows worker to build onto the same square where it is located.
 * This power decorates Zeus's power.
 * @author marcoDige
 */

public class BuildUnderfoot extends PowerDecorator {

    // constructors

    public BuildUnderfoot(Power p) {
        super(p);
    }

    // methods

    /**
     * This method overrides checkBuild (PowerDecorator) decorating decoratedPower with BuildUnderfoot rules.
     * @param w is the worker that wants to build
     * @param x is the x square coordinate where the worker wants to build
     * @param y is the y square coordinate where the worker wants to build
     * @param l is the level the worker wants to build
     * @return an ArrayList that is empty if the build is legal, otherwise it contains the errors that prevent the worker from building.
     */

    @Override
    public ArrayList<Error> checkBuild(Worker w, int x, int y, int l) {
        ArrayList<Error> errors = decoratedPower.checkBuild(w, x, y, l);
        if(!errors.isEmpty())
            if(w.getCurrentSquare().getXPosition() == x && w.getCurrentSquare().getYPosition() == y) {
                errors.remove(Error.NOT_FREE);
                errors.remove(Error.NOT_ADJACENT);
                if(w.getCurrentSquare().getLevel() == 3)
                    errors.add(Error.CANT_DOME_UNDERFOOT);
            }
        return errors;
    }
}
