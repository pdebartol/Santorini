package it.polimi.ingsw.model;

/**
 * Test suite for Worker class
 * @author aledimaio
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {

    Board gameBoard;
    Worker worker;

    @BeforeEach
    void setUp() {
        gameBoard = new Board();
        worker = new Worker(Color.GREY, "female");
    }

    @Test
    void removeFromGame() {
        worker.setWorkerOnBoard(gameBoard.getSquare(4,4));
        worker.removeFromGame();
        assertNull(worker.getCurrentSquare());
        assertEquals(false, worker.getInGame());
        assertNull(worker.getLastSquareMove());
        assertNull(worker.getLastSquareBuild());
    }

    @Test
    void setWorkerOnBoard() {
        // Check invalid argument exception
        assertThrows(IllegalArgumentException.class, () -> worker.setWorkerOnBoard(null));

        // Check normal functioning
        worker.setWorkerOnBoard(gameBoard.getSquare(1,1));
        assertEquals(true, worker.getInGame());
        assertEquals(worker, gameBoard.getSquare(1,1).getWorker());
        assertEquals(gameBoard.getSquare(1,1),worker.getCurrentSquare());
    }

    @Test
    void updateWorkerPosition() {
        // Check invalid argument exception
        assertThrows(IllegalArgumentException.class, () -> worker.updateWorkerPosition(null));

        // Check normal functioning
        worker.setWorkerOnBoard(gameBoard.getSquare(2,1));
        assertEquals(worker,gameBoard.getSquare(2,1).getWorker());
        assertEquals(worker.getCurrentSquare(),gameBoard.getSquare(2,1));
        assertNull(worker.getLastSquareMove());
        worker.updateWorkerPosition(gameBoard.getSquare(2,2));
        assertEquals(worker, gameBoard.getSquare(2,2).getWorker());
        assertEquals(worker.getLastSquareMove(), gameBoard.getSquare(2,1));
        assertEquals(worker.getCurrentSquare(), gameBoard.getSquare(2,2));
    }
}