package it.polimi.ingsw.model.decorators;


import it.polimi.ingsw.model.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;

public class BuildBeforeMove extends PowerDecorator {

    //constructors

    public BuildBeforeMove(Power p) {
        super(p);
    }

    //methods

    @Override
    public ArrayList<Error> checkMove(Worker w, int x, int y) {
        return super.checkMove(w, x, y);
    }

    @Override
    public ArrayList<Error> checkBuild(Worker w, int x, int y) {
        return super.checkBuild(w, x, y);
    }

}
