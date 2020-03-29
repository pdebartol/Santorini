package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

/**
 * StandardPower represents the standard power that all gods have.
 * maxMoves and maxBuild can't be changed once StandardPower has been created.
 * @author marcoDige & pierobartolo
 */
public class StandardPower implements Power {

    //attributes

    /**
     * maxMoves represents the maximum number of moves that a player can do in one turn using a specific god who has this power
     * maxBuild represents the maximum number of build that a player can build in one turn using a specific god who has this power
     */
    private Integer maxMoves;
    private Integer maxBuild;
    private Board board;

    //constructors

    /**
     * Class constructors which constructs a StandardPower with a specified maxMoves and maxBuild.
     * @param maxMoves is the maxMoves obtained from the configuration file of the specific god
     * @param maxBuild is the maxBuild obtained from the configuration file of the specific god
     */
    public StandardPower(Integer maxMoves, Integer maxBuild, Board board) {
        this.maxMoves = maxMoves;
        this.maxBuild = maxBuild;
        this.board = board;
    }


    //methods

    @Override
    public boolean checkMove(Worker w, Square s) {

        //Can't move after build check
        if(board.getNBuild() > 0) return false;

        //Adjacent check
        int xW = w.getCurrentSquare().getXPosition();
        int yW = w.getCurrentSquare().getYPosition();
        int x = s.getXPosition();
        int y = s.getYPosition();

        if(xW == x && yW == y) return false;

        int xdist = xW - x;
        int ydist = yW - y;

        return true;

    }

    @Override
    public boolean checkBuild(Worker w, Square s) {
        return false;
    }

    @Override
    public boolean checkWin() {
        return false;
    }

    @Override
    public void updateMove(Worker w, Square s) {

    }

    @Override
    public void updateBuild(Worker w, Square s) {

    }

    /**
     * This method allows to check if there are still moves or build available for the player who want to move or build.
     * @param mode is 0 for move and 1 for build
     * @return true or false depending on whether you still have moves or build available
     */
    @Override
    public boolean checkTurn(int mode){
        if(mode == 0)
            return board.getNMoves() < maxMoves;
        else
            return board.getNBuild() < maxBuild;
    }


    @Override
    public Board getBoard() {
        return board;
    }
}
