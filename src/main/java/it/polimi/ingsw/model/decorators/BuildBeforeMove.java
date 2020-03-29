package it.polimi.ingsw.model.decorators;


import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class BuildBeforeMove extends PowerDecorator {

    //constructors

    public BuildBeforeMove(Power p) {
        super(p);
    }

    //methods

    @Override
    public boolean checkMove(Worker w, Square s) {
        return super.checkMove(w, s);
    }

    @Override
    public boolean checkBuild(Worker w, Square s) {
        return super.checkBuild(w, s);
    }

    @Override
    public void updateBuild(Worker w, Square s) {
        super.updateBuild(w, s);
    }

    @Override
    public void updateMove(Worker w, Square s) {
        super.updateMove(w, s);
    }
}
