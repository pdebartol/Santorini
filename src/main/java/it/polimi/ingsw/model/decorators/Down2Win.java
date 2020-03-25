package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;

public class Down2Win extends PowerDecorator {
    public Down2Win(Power p) {
        super(p);
    }

    @Override
    public boolean checkWin() {
        return super.checkWin();
    }
}
