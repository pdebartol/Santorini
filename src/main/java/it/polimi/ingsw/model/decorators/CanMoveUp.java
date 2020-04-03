package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class CanMoveUp extends PowerDecorator implements PropertyChangeListener {

    boolean moveUp;
    //constructors

    public CanMoveUp(Power p, BlockMoveUp bmu) {
        super(p);
        moveUp = true;
        bmu.addPropertyChangeListener(this);
    }

    //methods

    @Override
    public ArrayList<Error> checkMove(Worker w, int x, int y) {
        if(moveUp || w.getCurrentSquare().getLevel() >= getBoard().getSquare(x,y).getLevel()){
            return super.checkMove(w, x, y);
        }
        else{
            ArrayList<Error> errors = super.checkMove(w,x,y);
            errors.add(Error.BLOCK_MOVE_UP);
            return errors;
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("moved_up")){
            moveUp = (boolean) evt.getNewValue();
        }
    }
}
