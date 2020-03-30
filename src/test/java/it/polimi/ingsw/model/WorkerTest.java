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
        worker.setWorkerOnBoard(gameBoard.getSquare(1,1));
        worker.removeFromGame();
        assertEquals(null, worker.getCurrentSquare());
    }

    @Test
    void setWorkerOnBoard() {
        worker.setWorkerOnBoard(gameBoard.getSquare(1,1));
        assertEquals(true, worker.getInGame());
        assertEquals(worker, gameBoard.getSquare(1,1).getWorker());
    }

    @Test
    void updateWorkerPosition() {
        worker.setWorkerOnBoard(gameBoard.getSquare(1,1));
        worker.updateWorkerPosition(gameBoard.getSquare(1,2));
        assertEquals(worker.getLastSquareMove(), gameBoard.getSquare(1,1));
        assertEquals(worker.getCurrentSquare(), gameBoard.getSquare(1,2));
    }
}