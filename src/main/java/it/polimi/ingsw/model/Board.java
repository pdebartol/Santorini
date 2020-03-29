package it.polimi.ingsw.model;

/**
 * The Board class represents Santorini game board
 * @author aledimaio
 */

public class Board {

    //attributes

    /**
     * Square[][] table is a matrix of Square objects, that represents the set of box of the table
     */

    private boolean moveUp;
    private int nMoves;
    private int nBuild;
    private Square[][] table;


    //constructors

    /**
     * the constructor sets the board for the entire game and construct the table of square too
     * @param moveUp
     * @param nMoves maximum move allowed per player
     * @param nBuild maximum build allowed per player
     */

    public Board(boolean moveUp, int nMoves, int nBuild) {

        this.moveUp = true;
        this.nMoves = 0;
        this.nBuild = 0;
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

    public Square getSquare(int x, int y){
        return table[x][y];
    }

    /**
     * the method resetCounters() set nMove and nBuild to its original state
     */

    public void resetCounters() {}

    public void incNMoves() {}

    public void incNBuild() {}

    public void startTurn() {}


}
