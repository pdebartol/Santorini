package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Board class
 * @author aledimaio
 */

class BoardTest {

    Board gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new Board();
    }

    /**
     * test to check if the created Square object is effectively correct
     */

    @Test
    void getSquare() {
        assertEquals(1,gameBoard.getSquare(1,1).getXPosition());
        assertEquals(1,gameBoard.getSquare(1,1).getYPosition());
        assertEquals(4,gameBoard.getSquare(4,4).getXPosition());
        assertEquals(4,gameBoard.getSquare(4,4).getXPosition());
    }

    @Test
    void resetCounters() {
        gameBoard.incNMoves();
        gameBoard.incNBuild();
        gameBoard.resetCounters();
        assertEquals(0,gameBoard.getNMoves());
        assertEquals(0,gameBoard.getNBuild());
    }

    @Test
    void incNMoves() {
        gameBoard.incNMoves();
        assertEquals(1,gameBoard.getNMoves());
    }

    @Test
    void incNBuild() {
        gameBoard.incNBuild();
        assertEquals(1,gameBoard.getNBuild());
    }

    @Test
    void isAdjacent() {

        //two Square adjacent
        assertEquals(true, gameBoard.isAdjacent(gameBoard.getSquare(1, 1), gameBoard.getSquare(1, 2)));
        //same Square
        assertEquals(false, gameBoard.isAdjacent(gameBoard.getSquare(1,1), gameBoard.getSquare(1,1)));
        //not adjacent Square
        assertEquals(false,gameBoard.isAdjacent(gameBoard.getSquare(1,1), gameBoard.getSquare(2,3)));
    }
}