package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for MoveSwap class, Apollo's power
 * @author aledimaio
 */

class MoveSwapTest {

    Board b;
    Player p1, p2, p3;

    /**
     * Setup for testing:
     * - 3 Player
     * - all workers set on board
     */

    @BeforeEach
    void setUp() {

        b = new Board();
        GodsFactory factory = new GodsFactory(b);
        p1 = new Player("pierobartolo", Color.WHITE);
        p2 = new Player("marcodige",Color.GREY);
        p3 = new Player("aledimaio",Color.AZURE);
        ArrayList<God> gods = factory.getGods( new ArrayList<>(Arrays.asList(1,2,3)));
        p1.setGod(Objects.requireNonNull(gods.get(2))); // Athena
        p2.setGod(Objects.requireNonNull(gods.get(1))); // Artemis
        p3.setGod(Objects.requireNonNull(gods.get(0))); // Apollo

        // p1 sets workers
        p1.getWorkers().get(0).setWorkerOnBoard(b.getSquare(0,0));
        p1.getWorkers().get(1).setWorkerOnBoard(b.getSquare(0,1));

        // p2 sets workers
        p2.getWorkers().get(0).setWorkerOnBoard(b.getSquare(2,0));
        p2.getWorkers().get(1).setWorkerOnBoard(b.getSquare(4,2));

        // p3 sets workers
        p3.getWorkers().get(0).setWorkerOnBoard(b.getSquare(1,2));
        p3.getWorkers().get(1).setWorkerOnBoard(b.getSquare(1,1));

    }

    /**
     * This method check if a Player with Apollo can move to an occupied square
     */

    @Test
    void checkMove() {
        //chose worker for the turn
        p3.getWorkers().get(0).isMovingOn();

        //position (0,1) is occupied by player 1 female worker (with MoveSwap power Apollo can swap his position with this worker)
        assertTrue(p3.getGod().getPower().checkMove(p3.getWorkers().get(0),0,1).isEmpty());

        //chose worker for the turn
        p2.getWorkers().get(0).isMovingOn();

        //on the same position, Artemis cannot swap his position
        assertFalse(p2.getGod().getPower().checkMove(p2.getWorkers().get(0),0,1).isEmpty());
    }

    /**
     * This method check that workers will be effectively swapped when a worker with Apollo's power moves to a square
     * occupied by an opponent's worker
     */

    @Test
    void updateMove() {
        //chose worker for the turn
        p3.getWorkers().get(0).isMovingOn();

        //check if worker can move to an occupied
        p3.move(p3.getWorkers().get(0),0,1);
        assertEquals(p3.getWorkers().get(0), b.getSquare(0,1).getWorker());
        assertEquals(p3.getWorkers().get(0).getCurrentSquare(), b.getSquare(0,1));

        //chose worker for the turn
        p1.getWorkers().get(1).isMovingOn();

        //check if the other worker has been swapped
        assertEquals(p1.getWorkers().get(1), b.getSquare(1,2).getWorker());
        assertEquals(p1.getWorkers().get(1).getCurrentSquare(), b.getSquare(1,2));

    }

    /**
     * This method check that the player who uses worker swapped by another worker from level 2 to level 3, cant' win
     */

    @Test
    void cantWinSwappedCheck(){
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        b.getSquare(1,1).buildLevel(3);

        p1.move(p1.getWorkers().get(0),1,0);
        p1.build(p1.getWorkers().get(0),0,0,1);

        b.getSquare(1,0).buildLevel(2);

        p1.endTurn();

        //chose worker for the turn
        p3.getWorkers().get(1).isMovingOn();

        p3.move(p3.getWorkers().get(1),1,0);

        assertEquals(p3.getWorkers().get(1), b.getSquare(1,0).getWorker());
        assertEquals(p3.getWorkers().get(1).getCurrentSquare(), b.getSquare(1,0));

        //check if the other worker has been swapped
        assertEquals(p1.getWorkers().get(0), b.getSquare(1,1).getWorker());
        assertEquals(p1.getWorkers().get(0).getCurrentSquare(), b.getSquare(1,1));

        //check that p1 hasn't won
        assertFalse(p1.getGod().getPower().checkWin(p1.getWorkers().get(0)));
    }
}