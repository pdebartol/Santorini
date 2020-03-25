package it.polimi.ingsw.model.decorators;


import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public class BuildDomeEverywhere extends PowerDecorator {

    public BuildDomeEverywhere(Power p) {
        super(p);
    }

    @Override
    public boolean checkBuild(Worker w, Square s) {
        return super.checkBuild(w, s);
    }

    @Override
    public void updateBuild(Worker w, Square s) {
        super.updateBuild(w, s);
    }
}
