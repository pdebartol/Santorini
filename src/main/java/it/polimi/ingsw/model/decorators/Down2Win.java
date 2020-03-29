package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

public class Down2Win extends PowerDecorator {

    //constructors

    public Down2Win(Power p) {
        super(p);
    }

    //methods

    @Override
    public boolean checkWin(Worker w) {
        return super.checkWin(w);
    }
}
