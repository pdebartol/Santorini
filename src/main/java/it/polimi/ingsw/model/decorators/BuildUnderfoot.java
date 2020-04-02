package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;

public class BuildUnderfoot extends PowerDecorator {

    // constructors

    public BuildUnderfoot(Power p) {
        super(p);
    }

    // metods

    @Override
    public ArrayList<Error> checkBuild(Worker w, int x, int y) {
        return super.checkBuild(w, x, y);
    }
}
