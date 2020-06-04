package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;

/**
 * This class implements the power which allows worker to move into a square occupied by another worker and switch positions with him.
 * @author marcoDige
 */

public class MoveSwap extends PowerDecorator {

    //attributes

    public MoveSwap(Power p) {
        super(p);
    }

    //methods

    /**
     * This method overrides checkMove (Power Decorator) decorating decoratedPower with MoveSwap rules.
     * @param w is the worker that wants to move
     * @param x is the x square coordinate where the worker wants to move
     * @param y is the y square coordinate where the worker wants to move
     * @return an ArrayList that is empty if the move is legal, otherwise it contains the errors that prevent the worker from moving.
     */

    @Override
    public ArrayList<Error> checkMove(Worker w, int x, int y) {
        ArrayList<Error> errors = decoratedPower.checkMove(w, x, y);
        errors.remove(Error.NOT_FREE);
        Worker workerOnPosition = getBoard().getSquare(x,y).getWorker();
        if(workerOnPosition != null && workerOnPosition.getColor() == w.getColor())
            errors.add(Error.SWAP_MY_WORKER);
        return errors;
    }

    /**
     * This method overrides updateMove (PowerDecorator) decorating decoratedPower with MoveSwap rules.
     * @param w is the worker that moves
     * @param x is the x square coordinate where the worker moves
     * @param y is the y square coordinate where the worker moves
     */

    @Override
    public void updateMove(Worker w, int x, int y) {
        if(!getBoard().getSquare(x,y).isFree()) {
            Worker opw = getBoard().getSquare(x, y).removeWorker();
            decoratedPower.updateMove(w, x, y);

            opw.setWorkerOnBoard(w.getLastSquareMove());
            //Write this update into move request accepted answer
            getBoard().getMsgContainer().updateMove(1,x,y,opw.getCurrentSquare().getXPosition(),opw.getCurrentSquare().getYPosition());
        } else
            decoratedPower.updateMove(w, x, y);
    }
}
