package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.State;

/**
 * Board is the class representing the entire game board.
 * @author aledimaio
 */

public class Board {

    //attributes

    /**
     * Number of rows/columns in the board.
     */

    public static final int SIZE = 5;

    /**
     * nMoves contains the number of moves made in the current turn.
     */

    private int nMoves;

    /**
     * nMoves contains the number of builds made in the current turn.
     */

    private int nBuild;

    /**
     *  table is a 5x5 matrix containing Square objects. It represents the board of our game.
     */

    private final Square[][] table;

    /**
     * this variable saves the state of the current game.
     */

    private State gameState;

    //constructors

    public Board() {
        this.gameState = State.LOGIN;
        this.table = new Square[SIZE][SIZE];
        for(int x = 0; x < SIZE ; x++){
            for(int y = 0; y < SIZE; y++)
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

    public State getGameState(){return gameState;}

    /**
     * This method gives access a certain Square position.
     * @param x the x position of Square onto board
     * @param y the y position of Square onto board
     * @return The square in (x,y) position
     */

    public Square getSquare(int x, int y){
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) throw new IllegalArgumentException("Null worker as argument!");
        return table[x][y];
    }

    public void setGameState(String state){gameState = State.valueOfLabel(state);}

    /**
     * This method reset nMoves and nBuild to 0.
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
     * This method increment the number of build done in this turn.
     */

    public void incNBuild() {
        nBuild = nBuild + 1;
    }

    /**
     * This method checks if 2 squares are adjacent.
     * @param s1 is the first square
     * @param s2 is the second square
     * @return true --> s1 and s2 are adjacent
     *         false --> s1 and s2 aren't adjacent
     */

    public boolean isAdjacent(Square s1, Square s2){
        if( s1 == null || s2 == null) throw new IllegalArgumentException("Null square as argument!");
        int xDist = s1.getXPosition() - s2.getXPosition();
        int yDist = s1.getYPosition() - s2.getYPosition();

        if(xDist == 0 && yDist == 0) return false; // Same square
        return xDist >= -1 && xDist <= 1 && yDist >= -1 && yDist <= 1;
    }
}
