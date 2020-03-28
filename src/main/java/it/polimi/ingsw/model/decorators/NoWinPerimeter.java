package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;

public class NoWinPerimeter extends PowerDecorator {

    boolean owner;
    //constructors

    public NoWinPerimeter(Power p, boolean owner) {
        super(p);
        this.owner = owner;
    }

    //methods

    @Override
    public boolean checkWin() {
        return super.checkWin();
    }
}
