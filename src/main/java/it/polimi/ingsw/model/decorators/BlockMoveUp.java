package it.polimi.ingsw.model.decorators;


import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class BlockMoveUp extends PowerDecorator {

    //constructors

    public BlockMoveUp(Power p) {
        super(p);
    }

    //methods

    @Override
    public boolean checkMove(Worker w, int x, int y) {
        return super.checkMove(w, x, y);
    }
}
