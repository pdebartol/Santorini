package it.polimi.ingsw.model.decorators;


import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class BlockMoveUp extends PowerDecorator {

    public BlockMoveUp(Power p) {
        super(p);
    }

    @Override
    public boolean checkMove(Worker w, Square s) {
        return super.checkMove(w, s);
    }
}
