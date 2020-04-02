package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Square class
 */

class SquareTest {

    Board gameBoard;
    Worker worker;

    @BeforeEach
    void setUp() {
        gameBoard = new Board();
    }


    /**
     * check if the setWorker() method works
     */

    @Test
    void setWorker() {
        worker = new Worker(Color.GREY, "female");
        assertNull(gameBoard.getSquare(0, 0).getWorker());
        gameBoard.getSquare(0,0).setWorker(worker);
        assertEquals(gameBoard.getSquare(0,0).getWorker(), worker);
    }

    @Test
    void removeWorker() {
        worker = new Worker(Color.GREY, "female");
        gameBoard.getSquare(1,1).setWorker(worker);
        gameBoard.getSquare(1,1).removeWorker();
        assertNull(gameBoard.getSquare(1, 1).getWorker());
    }

    @Test
    void isFree() {
        worker = new Worker(Color.GREY, "female");
        assertEquals(true, gameBoard.getSquare(1,1).isFree());
        gameBoard.getSquare(1,1).setWorker(worker);
        assertEquals(false, gameBoard.getSquare(1,1).isFree());
    }

    @Test
    void buildLevelAndIsCompleteTower() {

        for (int i = 0; i < 4; i++) {
            assertEquals(false, gameBoard.getSquare(4,4 ).isCompleteTower());
            assertEquals(i, gameBoard.getSquare(4, 4).getLevel());
            gameBoard.getSquare(4, 4).buildLevel();
        }
        assertEquals(true, gameBoard.getSquare(4,4).isCompleteTower());
        assertThrows(IllegalStateException.class, () -> gameBoard.getSquare(4, 4).buildLevel());
    }


}