package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Square class
 * @author pierobartolo & aledimaio
 */

class SquareTest {

    Board gameBoard;
    Worker worker;

    @BeforeEach
    void setUp() {
        gameBoard = new Board();
        worker = new Worker(Color.GREY, "female");
    }


    @Test
    void setWorker() {
        assertNull(gameBoard.getSquare(0, 0).getWorker());
        gameBoard.getSquare(0,0).setWorker(worker);
        assertEquals(gameBoard.getSquare(0,0).getWorker(), worker);

        assertThrows(IllegalStateException.class, () -> gameBoard.getSquare(0,0).setWorker(new Worker(Color.WHITE,"male")));
    }

    @Test
    void removeWorker() {
        //check that it's impossible to remove worker from free square
        assertThrows(IllegalStateException.class, () -> gameBoard.getSquare(1,1).removeWorker());

        gameBoard.getSquare(1,1).setWorker(worker);
        gameBoard.getSquare(1,1).removeWorker();
        assertNull(gameBoard.getSquare(1, 1).getWorker());

    }

    @Test
    void isFree() {
        assertTrue(gameBoard.getSquare(1, 1).isFree());
        gameBoard.getSquare(1,1).setWorker(worker);
        assertFalse(gameBoard.getSquare(1, 1).isFree());
    }

    @Test
    void buildLevelAndIsCompleteTower() {
        assertThrows(IllegalArgumentException.class, () -> gameBoard.getSquare(2,2).buildLevel(0));

        //buildLevel() test
        for (int i = 0; i < 4; i++) {
            assertFalse(gameBoard.getSquare(4, 4).isCompleteTower());
            assertEquals(i, gameBoard.getSquare(4, 4).getLevel());
            gameBoard.getSquare(4, 4).buildLevel(i+1);
        }

        //build dome test
        assertEquals(3, gameBoard.getSquare(4, 4).getLevel());
        assertTrue(gameBoard.getSquare(4, 4).getDome());
        assertTrue(gameBoard.getSquare(4, 4).isCompleteTower());
        assertThrows(IllegalStateException.class, () -> gameBoard.getSquare(4, 4).buildLevel(3));

    }


}