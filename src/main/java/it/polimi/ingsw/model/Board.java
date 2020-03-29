package it.polimi.ingsw.model;

/**
 * The Board class represents Santorini's game board
 * @author aledimaio
 */

public class Board {

    //attributes

    /**
     * Square[][] table is a matrix of Square objects, that represents the set of box of the table
     */

    private boolean moveUp;
    private int nMoves = 0;
    private int nBuild = 0;
    private Square[][] table;


    //constructors

    /**
     * the constructor sets the board for the entire game and construct the table of square too
     * @param moveUp
     * @param nMoves maximum move allowed per player
     * @param nBuild maximum build allowed per player
     */

    public Board(boolean moveUp, int nMoves, int nBuild) {

        this.moveUp = moveUp;
        this.nMoves = nMoves;
        this.nBuild = nBuild;
        for(int x = 0; x < 5 ; x++){
            for(int y = 0; y < 5 ; y++)
                table[x][y] = new Square(x, y);
        }

    }

    //methods


    public void setMoveUp(boolean mu) {
        moveUp = mu;
    }

    public boolean getMoveUp() {
        return moveUp;
    }

    public int getNMoves() {
        return nMoves;
    }

    public int getNBuild() {
        return nBuild;
    }

    public void clearBoard() {}

    /**
     * the method resetCounters() set nMove and nBuild to its original state
     */

    public void resetCounters() {}

    public void incNMoves() {}

    public void incNBuilt() {}

    public void startTurn() {}

    public Square[][] getTable() {
        return table;
    }
}
