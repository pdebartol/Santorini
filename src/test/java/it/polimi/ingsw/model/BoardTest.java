package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Board class
 * @author aledimaio & pierobartolo
 */

class BoardTest {

    Board gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new Board();
    }


    @Test
    void getSquare() {
        assertThrows(IllegalArgumentException.class, () -> gameBoard.getSquare(-1,0));
        assertThrows(IllegalArgumentException.class, () -> gameBoard.getSquare(5,5));
        assertThrows(IllegalArgumentException.class, () -> gameBoard.getSquare(5,1));

        // top-left square
        assertEquals(0,gameBoard.getSquare(0,0).getXPosition());
        assertEquals(0,gameBoard.getSquare(0,0).getYPosition());

        //top-right square
        assertEquals(4,gameBoard.getSquare(4,0).getXPosition());
        assertEquals(0,gameBoard.getSquare(4,0).getYPosition());

        //bottom-left square
        assertEquals(0,gameBoard.getSquare(0,4).getXPosition());
        assertEquals(4,gameBoard.getSquare(0,4).getYPosition());

        //bottom-right square
        assertEquals(4,gameBoard.getSquare(4,4).getXPosition());
        assertEquals(4,gameBoard.getSquare(4,4).getYPosition());
    }


    @Test
    void resetCounters() {
        //  NMoves = 1 NBuild = 1 and then check reset
        gameBoard.incNMoves();
        assertEquals(1, gameBoard.getNMoves());
        gameBoard.incNBuild();
        assertEquals(1, gameBoard.getNBuild());
        gameBoard.resetCounters();
        assertEquals(0, gameBoard.getNMoves());
        assertEquals(0, gameBoard.getNBuild());

        // NMoves = 3 NBuild = 0 and then check reset
        gameBoard.incNMoves();
        gameBoard.incNMoves();
        gameBoard.incNMoves();
        assertEquals(3, gameBoard.getNMoves());
        assertEquals(0, gameBoard.getNBuild());
        gameBoard.resetCounters();
        assertEquals(0, gameBoard.getNMoves());
        assertEquals(0, gameBoard.getNBuild());

        // NMoves = 0 NBuild ) 3 and then check reset
        gameBoard.incNBuild();
        gameBoard.incNBuild();
        gameBoard.incNBuild();
        assertEquals(0, gameBoard.getNMoves());
        assertEquals(3, gameBoard.getNBuild());
        gameBoard.resetCounters();
        assertEquals(0, gameBoard.getNMoves());
        assertEquals(0, gameBoard.getNBuild());
    }



    @Test
    void isAdjacent() {

        // Invalid argument
        assertThrows(IllegalArgumentException.class, () -> gameBoard.isAdjacent(null,null));

        // Two adjacent squares
        assertEquals(true, gameBoard.isAdjacent(gameBoard.getSquare(1, 1), gameBoard.getSquare(1, 2)));
        assertEquals(true, gameBoard.isAdjacent(gameBoard.getSquare(0, 0), gameBoard.getSquare(0, 1)));

        // Same square
        assertEquals(false, gameBoard.isAdjacent(gameBoard.getSquare(0,0), gameBoard.getSquare(0,0)));
        assertEquals(false, gameBoard.isAdjacent(gameBoard.getSquare(4,4), gameBoard.getSquare(4,4)));

        // Not adjacent squares
        assertEquals(false, gameBoard.isAdjacent(gameBoard.getSquare(1,1), gameBoard.getSquare(2,3)));
        assertEquals(false, gameBoard.isAdjacent(gameBoard.getSquare(0,0), gameBoard.getSquare(4,4)));

    }
}