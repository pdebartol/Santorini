package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class ExtraBuild extends PowerDecorator {
    private boolean onlySameSpace;
    private boolean notSameSpace;
    private boolean notPerimeter;

    public ExtraBuild(Power p) {
        super(p);
    }

    @Override
    public boolean checkBuild(Worker w, Square s) {
        return super.checkBuild(w, s);
    }
}
