package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.PowerDecorator;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

/**
 * This class implements the power which, at end of his turn, allows a worker to replace opponent's workers occupying lower
 * neighbour squares with a block and remove them from the game.
 * This power decorates Medusa's power.
 * @author marcoDige
 */

public class EndRemoveNeighbour extends PowerDecorator {

    //constructors

    public EndRemoveNeighbour(Power p) {
        super(p);
    }

    //methods

    /**
     * This method overrides updateBuild (PowerDecorator) decorating decoratedPower with EndRemoveNeighbour rules.
     * @param w is the worker that builds
     * @param x is the x square coordinate where the worker builds
     * @param y is the y square coordinate where the worker builds
     * @param l is the level the worker builds
     */
    @Override
    public void updateBuild(Worker w, int x, int y, int l) {
        decoratedPower.updateBuild(w, x, y, l);
        replaceAndBuild(w);
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
                if (x >= 0 && x <= 4 && y >= 0 && y <= 4) {
                    Square s = getBoard().getSquare(x, y);
                    if (s.getLevel() < w.getCurrentSquare().getLevel() && !s.isFree() && s.getWorker().getColor() != w.getColor()) {
                        s.getWorker().removeFromGame();
                        s.buildLevel(s.getLevel() + 1);
                    }
                }
            }
    }
}
