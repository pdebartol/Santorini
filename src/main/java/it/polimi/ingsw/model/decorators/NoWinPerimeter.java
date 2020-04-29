package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

/**
 * This class implements the power which inhibits opponent players to win on perimeter.
 * @author marcoDige
 */
public class NoWinPerimeter extends PowerDecorator {

    //constructors

    public NoWinPerimeter(Power p) {
        super(p);
    }

    //methods

    /**
     * This method extends checkWin (PowerDecorator) decorating decoratedPower with noWinPerimeter rules.
     * @return true or false to indicate if the player has won
     */

    @Override
    public boolean checkWin(Worker w) {
        if(decoratedPower.checkWin(w)){
            int x = w.getCurrentSquare().getXPosition(), y = w.getCurrentSquare().getYPosition();
            return (x != 0 && x != Board.SIZE-1 && y != 0 && y != Board.SIZE-1);
        }
        return false;
    }
}
