package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Power;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.enums.Error;

import java.util.ArrayList;

/**
 * StandardPower represents the standard power that all gods have in common.
 * maxMoves and maxBuild can't be changed once StandardPower has been created.
 * @author marcoDige AND pierobartolo
 */

public class StandardPower implements Power {

    //attributes

    /**
     * maxMoves represents the maximum number of moves that a player can do in one turn using a specific god who has this power
     * maxBuild represents the maximum number of build that a player can build in one turn using a specific god who has this power
     */

    private final Integer maxMoves;
    private final Integer maxBuild;
    private final Board board;

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
     * @return an ArrayList that is empty if the move is legal, otherwise it contains the errors that prevent the worker from moving.
     */

    @Override
    public ArrayList<Error> checkMove(Worker w, int x, int y) {
        if (x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE) throw new IllegalArgumentException("Invalid coordinates!");
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");

        ArrayList<Error> errors = new ArrayList<>();
        Square s = board.getSquare(x,y);

        // Check move tokens
        if(board.getNMoves() >= maxMoves) errors.add(Error.MOVES_EXCEEDED);

        // Can't move after build check
        if(board.getNBuild() > 0) errors.add(Error.MOVE_AFTER_BUILD);

        // Adjacent check
        if(!board.isAdjacent(w.getCurrentSquare(),s)) errors.add(Error.NOT_ADJACENT);

        // Level check
        int climbHeight = s.getLevel() - w.getCurrentSquare().getLevel();

        if(climbHeight > 1) errors.add(Error.INVALID_LEVEL_MOVE);

        // Occupation check
        if(!s.isFree()) errors.add(Error.NOT_FREE);

        // Can't move on dome check
        if(s.getDome()) errors.add(Error.IS_DOME);

        //Check that w is the worker chosen for this turn
        if(!w.getIsMoving()) errors.add(Error.ISNT_WORKER_CHOSEN);

        return errors;

    }

    /**
     * This method implements checkBuild (Power interface) with the standard game rules.
     * @param w is the worker that wants to build
     * @param x is the x square coordinate where the worker wants to build
     * @param y is the y square coordinate where the worker wants to build
     * @param l is the level the worker wants to build
     * @return an ArrayList that is empty if the build is legal, otherwise it contains the errors that prevent the worker from building.
     */

    @Override
    public ArrayList<Error> checkBuild(Worker w, int x, int y, int l) {
        if (x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE) throw new IllegalArgumentException("Invalid coordinates!");
        if(l < 1 || l > 4) throw new IllegalArgumentException("Invalid level value!");
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");

        ArrayList<Error>  errors = new ArrayList<>();
        Square s = board.getSquare(x,y);

        //Check build tokens
        if(board.getNBuild() >= maxBuild) errors.add(Error.BUILDS_EXCEEDED);

        //Can't build before move
        if(board.getNMoves() == 0) errors.add(Error.BUILD_BEFORE_MOVE);

        //Adjacent check
        if(!board.isAdjacent(w.getCurrentSquare(),s))  errors.add(Error.NOT_ADJACENT);

        //Level check
        if(l != s.getLevel() + 1) errors.add(Error.INVALID_LEVEL_BUILD);

        //Dome check
        if(s.getDome()) errors.add(Error.IS_DOME);

        //Occupation check
        if(!s.isFree()) errors.add(Error.NOT_FREE);

        //Check that w is the worker chosen for this turn
        if(!w.getIsMoving()) errors.add(Error.ISNT_WORKER_CHOSEN);

        return errors;
    }

    /**
     * This method implements checkWin (Power interface) with the standard game rules.
     * @param w is the worker on which the check is
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
        if (x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE) throw new IllegalArgumentException("Invalid coordinates!");
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");


        Square s = board.getSquare(x,y);

        if(s.equals(w.getCurrentSquare())) throw new IllegalStateException("Can't move on the same Square!");

        //Write this update into move request accepted answer
        board.getMsgContainer().updateMove(0,w.getCurrentSquare().getXPosition(),w.getCurrentSquare().getYPosition(),x,y);

        w.updateWorkerPosition(s);
        w.getLastSquareMove().removeWorker();
        board.incNMoves();

        //Write nexStepIndication in xml build request answer
        if (maxMoves - board.getNMoves() > 0) board.getMsgContainer().nextStepIndication("move/build");
        else board.getMsgContainer().nextStepIndication("build");
    }

    /**
     * This method implements updateBuild (Power interface) with the standard game rules.
     * @param w is the worker that builds
     * @param x is the x square coordinate where the worker builds
     * @param y is the y square coordinate where the worker builds
     * @param l is the level the worker builds
     */

    @Override
    public void updateBuild(Worker w, int x, int y, int l) {
        if (x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE) throw new IllegalArgumentException("Invalid coordinates!");
        if(l < 1 || l > 4) throw new IllegalArgumentException("Invalid level value!");
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");

        Square s = board.getSquare(x,y);

        //Write this update into build request accepted answer
        board.getMsgContainer().updateBuild(0,w.getCurrentSquare().getXPosition(),w.getCurrentSquare().getYPosition(),x,y,l);

        s.buildLevel(l);
        w.setLastSquareBuild(s);
        board.incNBuild();

        //Write nexStepIndication in xml build request answer
        if (maxBuild - board.getNBuild() > 0) board.getMsgContainer().nextStepIndication("build/end");
        else board.getMsgContainer().nextStepIndication("end");
    }

    /**
     * This method return the current board.
     * @return the current instance of Board
     */

    @Override
    public Board getBoard() {
        return board;
    }

    /**
     * This method is called when a player finish his turn. It checks that player has done standard moves and
     * reset all counters or flag for the turn in model.
     * @param workers is the arrayList of worker that player uses
     * @return true : The turn can end without problems.
     *         false : The turn cannot end because the player has not moved/built.
     */

    @Override
    public boolean endOfTurn(ArrayList<Worker> workers){
        if(workers == null) throw new IllegalArgumentException("Null worker as argument!");
        if(workers.size() == 0) throw new IllegalArgumentException("No workers passed");
        if(board.getNBuild() == 0 ||  board.getNMoves() == 0) return false;
        board.resetCounters();
        for (Worker w: workers){
            w.isMovingOff();
        }
        return true;
    }
}
