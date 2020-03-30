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
        assertEquals(gameBoard.getSquare(1,1).getXPosition(),1);
        assertEquals(gameBoard.getSquare(1,1).getYPosition(),1);

    }

    @Test
    void resetCounters() {
        gameBoard.incNMoves();
        gameBoard.incNBuild();
        gameBoard.resetCounters();
        assertEquals(gameBoard.getNMoves(),0);
        assertEquals(gameBoard.getNBuild(),0);
    }

    @Test
    void incNMoves() {
        gameBoard.incNMoves();
        assertEquals(gameBoard.getNMoves(),1);
    }

    @Test
    void incNBuild() {
        gameBoard.incNBuild();
        assertEquals(gameBoard.getNBuild(),1);
    }

    @Test
    void startTurn() {
        gameBoard.startTurn();
        resetCounters();
    }

    @Test
    void isAdjacent() {
        //two Square adjacent
        assertEquals(true, gameBoard.isAdjacent(gameBoard.getSquare(1, 1), gameBoard.getSquare(1, 2)));
        //same Square
        assertEquals(false, gameBoard.isAdjacent(gameBoard.getSquare(1,1), gameBoard.getSquare(1,1)));
        //not adjacent Square
        assertEquals(false,gameBoard.isAdjacent(gameBoard.getSquare(1,1), gameBoard.getSquare(4,4)));
    }
}