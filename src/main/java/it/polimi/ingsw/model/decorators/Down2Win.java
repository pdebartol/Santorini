package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;

public class Down2Win extends PowerDecorator {

    //constructors

    public Down2Win(Power p) {
        super(p);
    }

    //methods

    @Override
    public boolean checkWin() {
        return super.checkWin();
    }
}
