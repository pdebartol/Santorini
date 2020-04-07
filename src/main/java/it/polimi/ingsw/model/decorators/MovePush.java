package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;

/**
 * This class implements the power which allows worker to move into a square occupied by another worker if the
 * next square in the same direction is unoccupied; the other worker is pushed into that free space.
 * This power decorates Minotaur's power.
 * @author marcoDige
 */

public class MovePush extends PowerDecorator {

    //constructors

    public MovePush(Power p) {
        super(p);
    }

    //methods

    /**
     * This method overrides checkMove (Power Decorator) decorating decoratedPower with MovePush rules.
     * @param w is the worker that wants to move
     * @param x is the x square coordinate where the worker wants to move
     * @param y is the y square coordinate where the worker wants to move
     * @return an ArrayList that is empty if the move is legal, otherwise it contains the errors that prevent the worker from moving.
     */

    @Override
    public ArrayList<Error> checkMove(Worker w, int x, int y) {
        ArrayList<Error> errors = decoratedPower.checkMove(w, x, y);
        if(errors.size() == 1 && errors.contains(Error.NOT_FREE)) {
            errors.remove(Error.NOT_FREE);
            int sameDirectionX = x + (x - w.getCurrentSquare().getXPosition());
            int sameDirectionY = y + (y - w.getCurrentSquare().getYPosition());
            if(!(sameDirectionX >= 0 && sameDirectionX <= 4 && sameDirectionY >= 0 && sameDirectionY <= 4 && getBoard().getSquare(sameDirectionX,sameDirectionY).isFree()))
                errors.add(Error.SAME_DIRECTION_NOT_FREE);
        }
        return errors;
    }

    /**
     * This method overrides updateMove (PowerDecorator) decorating decoratedPower with MovePush rules.
     * @param w is the worker that moves
     * @param x is the x square coordinate where the worker moves
     * @param y is the y square coordinate where the worker moves
     */

    @Override
    public void updateMove(Worker w, int x, int y) {
        if(getBoard().getSquare(x,y).getWorker() != null) {
            Worker opw = getBoard().getSquare(x, y).removeWorker();
            decoratedPower.updateMove(w, x, y);
            int sameDirectionX = x + (x - w.getCurrentSquare().getXPosition());
            int sameDirectionY = y + (y - w.getCurrentSquare().getYPosition());
            opw.updateWorkerPosition(getBoard().getSquare(sameDirectionX,sameDirectionY));
        } else
            decoratedPower.updateMove(w, x, y);
    }
}
