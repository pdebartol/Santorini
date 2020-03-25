package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;

public class NoWinPerimeter extends PowerDecorator {
    public NoWinPerimeter(Power p) {
        super(p);
    }

    @Override
    public boolean checkWin() {
        return super.checkWin();
    }
}
