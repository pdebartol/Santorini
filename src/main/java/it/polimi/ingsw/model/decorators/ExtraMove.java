package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class ExtraMove extends PowerDecorator {

    //attributes

    private boolean notMoveBack;
    private boolean notPerimeter;

    //constructors

    public ExtraMove(Power p, boolean nmb, boolean np) {
        super(p);
        this.notMoveBack = nmb;
        this.notPerimeter = np;
    }



    //methods

    @Override
    public boolean checkMove(Worker w, int x, int y) {
        return super.checkMove(w, x, y);
    }

    public boolean isNotMoveBack() {
        return notMoveBack;
    }

    public boolean isNotPerimeter() {
        return notPerimeter;
    }
}
