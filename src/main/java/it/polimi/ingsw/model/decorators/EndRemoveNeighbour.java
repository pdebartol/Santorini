package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;

/**
 * This class implements the power which, at end of his turn, allows a worker to replace opponent's workers occupying lower
 * neighbour squares with a block and remove them from the game.
 * @author marcoDige
 */

public class EndRemoveNeighbour extends PowerDecorator {

    //constructors

    public EndRemoveNeighbour(Power p) {
        super(p);
    }

    //methods

    /**
     * This method overrides endOfTurn (PowerDecorator) decorating decoratedPower with EndRemoveNeighbour rules.
     * @param workers is the arrayList of worker that player uses
     * @return true -> The turn is correctly finish
     *         false -> The turn cannot finish now
     */

    @Override
    public boolean endOfTurn(ArrayList<Worker> workers){
        if(decoratedPower.endOfTurn(workers)){
            for(Worker w: workers)
                replaceAndBuild(w);
            return true;
        }
        return false;
    }

    /**
     * This method allows replacing w opponent's workers occupying lower neighbour squares with a block and remove them from the game.
     * @param w is the reference worker around whom the operation takes place
     */

    private void replaceAndBuild(Worker w){
        for(int i = -1; i <= 1 ; i++)
            for(int j = -1; j <= 1; j++){
                int x = w.getCurrentSquare().getXPosition() + i;
                int y = w.getCurrentSquare().getYPosition() + j;
                if (x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE) {
                    Square s = getBoard().getSquare(x, y);
                    if (s.getLevel() < w.getCurrentSquare().getLevel() && !s.isFree() && s.getWorker().getColor() != w.getColor()) {
                        s.getWorker().removeFromGame();
                        s.buildLevel(s.getLevel() + 1);
                    }
                }
            }
    }
}
