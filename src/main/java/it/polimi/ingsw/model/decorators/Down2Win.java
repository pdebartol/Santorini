package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Worker;

/**
 * This class implements the power which allows the player to win also if his worker moves down 2 or more levels.
 * This power decorates Pan's power.
 * @author marcoDige
 */
public class Down2Win extends PowerDecorator {

    //constructors

    public Down2Win(Power p) {
        super(p);
    }

    //methods

    /**
     * This method extends checkWin (PowerDecorator) decorating decoratedPower with Down2Win rules.
     * @return true or false to indicate if the player has won
     */

    @Override
    public boolean checkWin(Worker w) {
        if(decoratedPower.checkWin(w))
            return true;
        else
            return w.getLastSquareMove().getLevel() - w.getCurrentSquare().getLevel() > 1 ;
    }
}
