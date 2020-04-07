package it.polimi.ingsw.model.decorators;


import it.polimi.ingsw.model.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;

/**
 * This class implements the power which allows worker to build dome also if level is not 3.
 * This power decorates Atlas's power.
 * @author marcoDige
 */

public class BuildDomeEverywhere extends PowerDecorator {

    //constructors

    public BuildDomeEverywhere(Power p) {
        super(p);
    }

    //methods

    /**
     * This method overrides checkBuild (PowerDecorator) decorating decoratedPower with BuildDomeEverywhere rules.
     * @param w is the worker that wants to build
     * @param x is the x square coordinate where the worker wants to build
     * @param y is the y square coordinate where the worker wants to build
     * @param l is the level the worker wants to build
     * @return an ArrayList that is empty if the build is legal, otherwise it contains the errors that prevent the worker from building.
     */

    @Override
    public ArrayList<Error> checkBuild(Worker w, int x, int y, int l) {
        ArrayList<Error> errors = decoratedPower.checkBuild(w, x, y, l);
        if(errors.contains(Error.INVALID_LEVEL_BUILD) && l == 4) errors.remove(Error.INVALID_LEVEL_BUILD);
        return errors;
    }

}
