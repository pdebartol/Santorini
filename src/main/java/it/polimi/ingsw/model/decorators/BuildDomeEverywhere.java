package it.polimi.ingsw.model.decorators;


import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class BuildDomeEverywhere extends PowerDecorator {

    //constructors

    public BuildDomeEverywhere(Power p) {
        super(p);
    }

    //methods

    @Override
    public boolean checkBuild(Worker w, int x, int y) {
        return super.checkBuild(w, x, y);
    }

    @Override
    public void updateBuild(Worker w, int x, int y) {
        super.updateBuild(w, x, y);
    }
}
