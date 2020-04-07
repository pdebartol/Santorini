package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;

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
    public ArrayList<Error> checkBuild(Worker w, int x, int y, int l) {
        return super.checkBuild(w, x, y, l);
    }

    public boolean isOnlySameSpace() {
        return onlySameSpace;
    }

    public boolean isNotSameSpace() {
        return notSameSpace;
    }

    public boolean isNotPerimeter() {
        return notPerimeter;
    }
}
