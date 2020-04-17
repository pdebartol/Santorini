package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for EndRemoveNeighbour
 * @author aledimaio & pierobartolo
 */

class EndRemoveNeighbourTest {

    Board b;
    Player p1, p2, p3;

    /**
     * Setup for testing:
     * - p1 --> Medusa
     * - p2 --> Artemis
     * - p3 --> Apollo
     * - all workers set on board
     */

    @BeforeEach
    void setUp() {

        b = new Board();
        GodsFactory factory = new GodsFactory(b);
        p1 = new Player("pierobartolo", Color.WHITE);
        p2 = new Player("marcodige",Color.GREY);
        p3 = new Player("aledimaio",Color.AZURE);
        ArrayList<God> gods = factory.getGods( new ArrayList<>(Arrays.asList(24,2,1)));
        p1.setGod(Objects.requireNonNull(gods.get(0))); // Medusa
        p2.setGod(Objects.requireNonNull(gods.get(1))); // Artemis
        p3.setGod(Objects.requireNonNull(gods.get(2))); // Apollo

        // p1 sets workers
        p1.getWorkers().get(0).setWorkerOnBoard(b.getSquare(1,2));
        p1.getWorkers().get(1).setWorkerOnBoard(b.getSquare(1,3));

        // p2 sets workers
        p2.getWorkers().get(0).setWorkerOnBoard(b.getSquare(2,1));
        p2.getWorkers().get(1).setWorkerOnBoard(b.getSquare(3,1));

        // p3 sets workers
        p3.getWorkers().get(0).setWorkerOnBoard(b.getSquare(3,2));
        p3.getWorkers().get(1).setWorkerOnBoard(b.getSquare(4,4));

    }

    /**
     * This method checks the StandardMove of Medusa.
     * No opponent's workers should be removed.
     * Corners should not be a problem for Medusa's power.
     */

    @Test
    void checkStandardMoveAndCorners() {
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        // Move
        assertTrue(p1.move(p1.getWorkers().get(0),0,3).isEmpty());

        // Build
        assertTrue(p1.build(p1.getWorkers().get(0), 0,2,1).isEmpty());

        // Check that workers were not removed
        assertNotNull(p2.getWorkers().get(0));
        assertNotNull(p2.getWorkers().get(1));
        assertNotNull(p3.getWorkers().get(0));
        assertNotNull(p3.getWorkers().get(1));

    }


    /**
     * This method tests Medusa's power:  (End Of Turn) If possible, your Workers build in lower neighboring spaces that are
     * occupied by opponent Workers, removing the opponent Workers from the game.
     * In this test only one worker activates its power.
     */

    @Test
    void checkMedusaOneWorker(){

        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        // Move & Build
        b.getSquare(2,2).buildLevel(1);
        assertTrue(p1.move(p1.getWorkers().get(0), 2,2).isEmpty());
        assertTrue(p1.build(p1.getWorkers().get(0), 1,2,1).isEmpty());

        // End Turn (Medusa's power activates)
        assertTrue(p1.endTurn());

        // Check opponent's worker was replaced with a new level
        assertEquals(1,p3.getWorkers().size());
        assertNull(b.getSquare(3,2).getWorker());
        assertEquals(1,b.getSquare(3,2).getLevel());

        assertEquals(0,p2.getWorkers().size());
        assertNull(b.getSquare(2,1).getWorker());
        assertEquals(1,b.getSquare(2,1).getLevel());
        assertNull(b.getSquare(3,1).getWorker());
        assertEquals(1,b.getSquare(3,1).getLevel());

        // Check Medusa's workers were not removed
        assertEquals(2,p1.getWorkers().size());
    }

    /**
     * This method tests Medusa's power:  (End Of Turn) If possible, your Workers build in lower neighboring spaces that are
     * occupied by opponent Workers, removing the opponent Workers from the game.
     * In this test both Medusa's workers activate their power.
     */

    @Test
    void checkMedusaBothWorker(){
        //chose worker for the turn
        p1.getWorkers().get(1).isMovingOn();

        // Move & Build
        b.getSquare(2,2).buildLevel(1);
        b.getSquare(1,2).buildLevel(2);
        assertTrue(p1.move(p1.getWorkers().get(1), 2,2).isEmpty());
        assertTrue(p1.build(p1.getWorkers().get(1), 1,3,1).isEmpty());

        // End Turn (Medusa's power activates)
        assertTrue(p1.endTurn());

        // Check opponent's worker was replaced with a new level
        assertEquals(1,p3.getWorkers().size());
        assertNull(b.getSquare(3,2).getWorker());
        assertEquals(1,b.getSquare(3,2).getLevel());

        assertEquals(0,p2.getWorkers().size());
        assertNull(b.getSquare(2,1).getWorker());
        assertEquals(1,b.getSquare(2,1).getLevel());
        assertNull(b.getSquare(3,1).getWorker());
        assertEquals(1,b.getSquare(3,1).getLevel());

        // Check Medusa's workers were not removed
        assertEquals(2,p1.getWorkers().size());
    }


}