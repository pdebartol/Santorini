package it.polimi.ingsw.model.decorators;


import it.polimi.ingsw.model.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class BlockMoveUp extends PowerDecorator {

    //constructors

    public BlockMoveUp(Power p) {
        super(p);
    }

    //methods

    @Override
    public void updateMove(Worker w, int x, int y){
    }
}
