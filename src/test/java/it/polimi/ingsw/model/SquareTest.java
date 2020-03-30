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
        assertEquals(null,gameBoard.getSquare(1,1).getWorker());
        gameBoard.getSquare(1,1).setWorker(worker);
        assertEquals(worker, gameBoard.getSquare(1,1).getWorker());
    }

    @Test
    void removeWorker() {
        worker = new Worker(Color.GREY, "female");
        gameBoard.getSquare(1,1).setWorker(worker);
        gameBoard.getSquare(1,1).removeWorker();
        assertEquals(null, gameBoard.getSquare(1,1).getWorker());
    }

    @Test
    void isFree() {
        worker = new Worker(Color.GREY, "female");
        assertEquals(true, gameBoard.getSquare(1,1).isFree());
        gameBoard.getSquare(1,1).setWorker(worker);
        assertEquals(false, gameBoard.getSquare(1,1).isFree());
    }

    @Test
    void isCompleteTower() {
        for (int i = 0; i < 4; i++) {
            assertEquals(false, gameBoard.getSquare(1, 1).isCompleteTower());
            gameBoard.getSquare(1, 1).buildLevel();
        }
        assertEquals(true, gameBoard.getSquare(1,1).isCompleteTower());
    }

    @Test
    void buildLevel() {
        for(int i = 0; i < 3 ; i++) {
            gameBoard.getSquare(1, 1).buildLevel();
            assertEquals(i + 1, gameBoard.getSquare(1, 1).getLevel());
        }
        gameBoard.getSquare(1,1).buildLevel();
        assertEquals(true, gameBoard.getSquare(1,1).isCompleteTower());
    }

}