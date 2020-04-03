package it.polimi.ingsw.model.decorators;


import it.polimi.ingsw.model.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class BlockMoveUp extends PowerDecorator {

    //attributes
    private PropertyChangeSupport support;

    //constructors

    public BlockMoveUp(Power p) {
        super(p);
        this.support = new PropertyChangeSupport(this);
    }


    //methods

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    @Override
    public void updateMove(Worker w, int x, int y){
        if(w.getCurrentSquare().getLevel() < getBoard().getSquare(x,y).getLevel()){
            support.firePropertyChange("moved_up", true,false); // notifies CanMoveUp of the move
        }
        else{
            support.firePropertyChange("moved_up", false,true); // notifies CanMoveUp of the move
        }
        super.updateMove(w,x,y);

    }
}
