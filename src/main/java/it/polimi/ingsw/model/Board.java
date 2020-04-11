package it.polimi.ingsw.model;

/**
 * Board is the entire game board.
 * @author aledimaio
 */

public class Board {

    //attributes

    /**
     * moveUp is a flag that indicate if in a specific moment a Player can move up.
     */

    private int nMoves;
    private int nBuild;

    /**
     * Square[][] table is a matrix of Square objects, that represents the set of box of the table.
     */

    private Square[][] table;


    //constructors

    public Board() {
        this.table = new Square[5][5];

        for(int x = 0; x < 5 ; x++){
            for(int y = 4; y >= 0 ; y--)
                this.table[x][y] = new Square(x, y);
        }
    }

    //methods

    public int getNMoves() {
        return nMoves;
    }

    public int getNBuild() {
        return nBuild;
    }

    /**
     * This method allows to log into a certain Square position.
     * @param x the x position of Square onto board
     * @param y the y position of Square onto board
     * @return The square in (x,y) position
     */

    public Square getSquare(int x, int y){
        if (x < 0 || x > 4 || y < 0 || y > 4) throw new IllegalArgumentException("Null worker as argument!");
        return table[x][y];
    }

    /**
     * This method reset nMove and nBuild to 0.
     */

    public void resetCounters() {
        nMoves = 0;
        nBuild = 0;
    }

    /**
     * This method increment the number of moves done in this turn.
     */

    public void incNMoves() {
        nMoves = nMoves + 1;
    }

    /**
     * This method decrement the number of moves done in this turn.
     */

    public void decNMoves() {
        nMoves = nMoves - 1;
    }

    /**
     * This method increment the number of build done in this turn.
     */

    public void incNBuild() {
        nBuild = nBuild + 1;
    }

    /**
     * This method control if 2 squares are adjacent.
     * @param s1 is the first square
     * @param s2 is the second square
     * @return true --> s1 and s2 are adjacent
     *         false --> s1 and s2 aren't adjacent
     */

    public boolean isAdjacent(Square s1, Square s2){
        if( s1 == null || s2 == null) throw new IllegalArgumentException("Null square as argument!");
        int xdist = s1.getXPosition() - s2.getXPosition();
        int ydist = s1.getYPosition() - s2.getYPosition();

        if(xdist == 0 && ydist == 0) return false; // Same square
        return xdist >= -1 && xdist <= 1 && ydist >= -1 && ydist <= 1;
    }

}
