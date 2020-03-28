package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;

public class NoWinPerimeter extends PowerDecorator {

    //constructors

    public NoWinPerimeter(Power p) {
        super(p);
    }

    //methods

    @Override
    public boolean checkWin() {
        return super.checkWin();
    }
}
