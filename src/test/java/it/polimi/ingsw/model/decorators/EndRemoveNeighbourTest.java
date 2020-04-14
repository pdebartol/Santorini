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
 * @author aledimaio 
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
     * This method checks that no workers were removed
     */

    @Test
    void checkStandardMove() {
        // Move
        assertTrue(p1.move(p1.getWorkers().get(0),2,2).isEmpty());

        // Build
        assertTrue(p1.build(p1.getWorkers().get(0), 1,2,1).isEmpty());

        // Check that workers were not removed
        assertNotNull(p2.getWorkers().get(0));
        assertNotNull(p2.getWorkers().get(1));
        assertNotNull(p3.getWorkers().get(0));
        assertNotNull(p3.getWorkers().get(1));

    }

    /**
     * This method tests that only one opponent's worker eas remove
     */

    @Test
    void checkOneNeighbourWorkerRemoved(){
        b.resetCounters();

        p1.move(p1.getWorkers().get(0), 1,1);
        p1.build(p1.getWorkers().get(0), 1,0,1);

        assertEquals(p1.getWorkers().get(1), b.getSquare(1,3).getWorker());
        //check the absence of opponent's worker
        assertNull(b.getSquare(2,1).getWorker());
        //check if a level was build
        assertEquals(1, b.getSquare(2,1).getLevel());
        assertEquals(p2.getWorkers().get(0), b.getSquare(3,1).getWorker());
        assertEquals(p3.getWorkers().get(0), b.getSquare(3,2).getWorker());
        assertEquals(p3.getWorkers().get(1), b.getSquare(4,4).getWorker());

        assertTrue(p1.endTurn());


    }

    /**
     * This method tests that more than one workers were remove
     */

    @Test
    void checkNeighbourWorkersRemoved(){

        b.getSquare(1,2).buildLevel(2);
        b.getSquare(2,2).buildLevel(1);
        b.resetCounters();

        p1.move(p1.getWorkers().get(0), 2,2);
        p1.build(p1.getWorkers().get(0),3,3,1);



    }


}