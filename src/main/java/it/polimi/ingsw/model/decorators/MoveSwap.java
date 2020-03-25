package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class MoveSwap extends PowerDecorator {
    public MoveSwap(Power p) {
        super(p);
    }

    @Override
    public boolean checkMove(Worker w, Square s) {
        return super.checkMove(w, s);
    }

    @Override
    public void updateMove(Worker w, Square s) {
        super.updateMove(w, s);
    }
}
