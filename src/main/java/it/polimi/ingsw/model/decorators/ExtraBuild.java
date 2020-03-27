package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class ExtraBuild extends PowerDecorator {

    //attributes

    private boolean onlySameSpace;
    private boolean notSameSpace;
    private boolean notPerimeter;

    //constructors

    public ExtraBuild(Power p, boolean oss, boolean nss, boolean np) {
        super(p);
        this.onlySameSpace = oss;
        this.notSameSpace = nss;
        this.notPerimeter = np;
    }

    //methods

    @Override
    public boolean checkBuild(Worker w, Square s) {
        return super.checkBuild(w, s);
    }
}
