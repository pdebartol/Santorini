package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class EndRemoveNeighbour extends PowerDecorator {

    //constructors

    public EndRemoveNeighbour(Power p) {
        super(p);
    }

    //methods

    @Override
    public void updateBuild(Worker w, Square s) {
        super.updateBuild(w, s);
    }
}
