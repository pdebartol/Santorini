package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class BuildUnderfoot extends PowerDecorator {

    // constructors

    public BuildUnderfoot(Power p) {
        super(p);
    }

    // metods

    @Override
    public boolean checkBuild(Worker w, Square s) {
        return super.checkBuild(w, s);
    }
}
