package it.polimi.ingsw.model.decorators;


import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This class implements the power which blocks opponent players to move up if the worker moves up in this turn.
 * This power decorates Athena's power.
 * @author pierobartolo
 */

public class BlockMoveUp extends PowerDecorator {

    //attributes
    private PropertyChangeSupport support;

    //constructors

    public BlockMoveUp(Power p) {
        super(p);
        this.support = new PropertyChangeSupport(this);
    }


    //methods

    /**
     * This method overrides updateMove (PowerDecorator) decorating decoratedPower with BlockMoveUp rules.
     * @param w is the worker that moves
     * @param x is the x square coordinate where the worker moves
     * @param y is the y square coordinate where the worker moves
     */

    @Override
    public void updateMove(Worker w, int x, int y){
        if(w.getCurrentSquare().getLevel() < getBoard().getSquare(x,y).getLevel()){
            support.firePropertyChange("moved_up", true,false); // notifies CanMoveUp of the move
        }
        else{
            support.firePropertyChange("moved_up", false,true); // notifies CanMoveUp of the move
        }
        decoratedPower.updateMove(w,x,y);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
}
