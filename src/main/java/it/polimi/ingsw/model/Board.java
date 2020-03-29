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
     * the constructor builds the table for the entire game
     */

    public Board() {
        this.moveUp = true;
        this.table = new Square[5][5];

        for(int x = 0; x < 5 ; x++){
            for(int y = 0; y < 5 ; y++)
                this.table[x][y] = new Square(x, y);
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

    /**
     * This method control if 2 squares are adjacent.
     * @param s1 is the first square
     * @param s2 is the second square
     * @return true --> s1 and s2 are adjacent
     *         false --> s1 and s2 aren't adjacent
     */
    public boolean isAdjacent(Square s1, Square s2){
        int xdist = s1.getXPosition() - s2.getXPosition();
        int ydist = s1.getYPosition() - s2.getYPosition();

        if(xdist == 0 && ydist == 0) return false; //move on same square
        if(xdist < -1 || xdist > 1 || ydist < -1 || ydist > 1) return false;

        return true;
    }


}
