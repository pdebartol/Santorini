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

    public Integer getMaxMoves(){
        return maxMoves;
    }

    public Integer getMaxBuild(){
        return maxBuild;
    }

    //methods

    /**
     * This method implements checkMove (Power interface) with the standard game rules.
     * @param w is the worker that wants to move
     * @param x is the x square coordinate where the worker wants to move
     * @param y is the y square coordinate where the worker wants to move
     * @return true or false to indicate if the move is legit or not
     */

    @Override
    public boolean checkMove(Worker w, int x, int y) {

        if (x < 0 || x > 4 || y < 0 || y > 4) throw new IllegalArgumentException("Null worker as argument!");
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");

        Square s = board.getSquare(x,y);

        //Can't move after build check
        if(board.getNBuild() > 0) return false;

        //Adjacent check
        if(!board.isAdjacent(w.getCurrentSquare(),s)) return false;

        //Level check
        int climbHeight = s.getLevel() - w.getCurrentSquare().getLevel();

        if(climbHeight > 1) return false;

        //Occupation check
        if(s.isFree()) return false;

        //Can't move on dome check

        return !s.getDome();

    }

    /**
     * This method implements checkBuild (Power interface) with the standard game rules.
     * @param w is the worker that wants to build
     * @param x is the x square coordinate where the worker wants to build
     * @param y is the y square coordinate where the worker wants to build
     * @return true or false to indicate if the build is legit or not
     */

    @Override
    public boolean checkBuild(Worker w, int x, int y) {

        if (x < 0 || x > 4 || y < 0 || y > 4) throw new IllegalArgumentException("Null worker as argument!");
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");

        Square s = board.getSquare(x,y);

        //Can't move build before check
        if(board.getNMoves() == 0) return false;

        //Adjacent check
        if(!board.isAdjacent(w.getCurrentSquare(),s)) return false;

        //Dome check
        if(s.getDome()) return false;

        //Occupation check
        return !s.isFree();
    }

    /**
     * This method implements checkWin (Power interface) with the standard game rules.
     * @return true or false to indicate if the player has won
     */

    @Override
    public boolean checkWin(Worker w) {
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");

        return (w.getCurrentSquare().getLevel() == 3 && w.getLastSquareMove().getLevel() == 2);
    }

    /**
     * This method implements updateMove (Power interface) with the standard game rules.
     * @param w is the worker that moves
     * @param x is the x square coordinate where the worker moves
     * @param y is the y square coordinate where the worker moves
     */

    @Override
    public void updateMove(Worker w, int x, int y) {

        if (x < 0 || x > 4 || y < 0 || y > 4) throw new IllegalArgumentException("Null worker as argument!");
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");

        Square s = board.getSquare(x,y);

        s.removeWorker();
        w.updateWorkerPosition(s);
        board.incNMoves();
    }

    /**
     * This method implements updateBuild (Power interface) with the standard game rules.
     * @param w is the worker that builds
     * @param x is the x square coordinate where the worker builds
     * @param y is the y square coordinate where the worker builds
     */

    @Override
    public void updateBuild(Worker w, int x, int y) {

        if (x < 0 || x > 4 || y < 0 || y > 4) throw new IllegalArgumentException("Null worker as argument!");
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");

        Square s = board.getSquare(x,y);

        s.buildLevel();
        w.setLastSquareBuild(s);
        board.incNBuild();
    }

    /**
     * This method implements checkTurn in the Power interface with the standard game rules.
     * @param mode 0 --> check if the player can move
     *             1 --> check if the player can build
     * @return true or false to indicate if  player can move or build
     */

    @Override
    public boolean checkTurn(int mode){
        if(mode == 0)
            return board.getNMoves() < maxMoves;
        else
            return board.getNBuild() < maxBuild;
    }

    /**
     * This method return the current board.
     * @return the current instance of Board
     */

    @Override
    public Board getBoard() {
        return board;
    }
}
