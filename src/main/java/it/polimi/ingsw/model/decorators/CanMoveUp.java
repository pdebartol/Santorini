package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * This class implements the power which restricts the standard power to move up (if the boolean moveUp is set to false).
 * It listens to the class BlockMoveUp in order to update the boolean moveUp.
 * This power decorates all in game gods' powers when a player uses Athena card (except the player whose god's power is Hera).
 * @author pierobartolo
 */
public class CanMoveUp extends PowerDecorator implements PropertyChangeListener {

    boolean moveUp;
    //constructors

    public CanMoveUp(Power p, BlockMoveUp bmu) {
        super(p);
        moveUp = true;
        bmu.addPropertyChangeListener(this);
    }

    //methods

    /**
     * This method overrides checkMove (Power Decorator) decorating decoratedPower with canMoveUp rules.
     * @param w is the worker that wants to move
     * @param x is the x square coordinate where the worker wants to move
     * @param y is the y square coordinate where the worker wants to move
     * @return an ArrayList that is empty if the move is legal, otherwise it contains the errors that prevent the worker from moving.
     */

    @Override
    public ArrayList<Error> checkMove(Worker w, int x, int y) {
        if(moveUp || w.getCurrentSquare().getLevel() >= getBoard().getSquare(x,y).getLevel()){
            return decoratedPower.checkMove(w, x, y);
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
