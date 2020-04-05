package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * This class implements the power which restrict the standard power to move up if moveUp is false.
 * It listen if the class BlockMoveUp set the parameter moveUp to false.
 * This power decorates all in game god's power when a player uses Athena card (except the player who have Hera god's power).
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
