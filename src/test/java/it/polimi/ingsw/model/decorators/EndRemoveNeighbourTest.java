package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for EndRemoveNeighbour class, Medusa's power
 * @author aledimaio
 */

class EndRemoveNeighbourTest {

    Board b;
    Player p1, p2, p3;

    /**
     * Setup for testing:
     * - 3 players
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
     * This method check tha no workers were remove
     */

    @Test
    void checkStandardMove() {

        b.getSquare(1,2).buildLevel(2);
        b.getSquare(0,2).buildLevel(1);
        b.resetCounters();

        assertTrue(p1.getGod().getPower().checkMove(p1.getWorkers().get(0),0,2).isEmpty());
        p1.move(p1.getWorkers().get(0),0,2);
        assertTrue(p1.getGod().getPower().checkBuild(p1.getWorkers().get(0), 0,3,1).isEmpty());
        p1.build(p1.getWorkers().get(0), 0,3,1);

        assertEquals(p1.getWorkers().get(1), b.getSquare(1,3).getWorker());
        assertEquals(p2.getWorkers().get(0), b.getSquare(2,1).getWorker());
        assertEquals(p2.getWorkers().get(1), b.getSquare(3,1).getWorker());
        assertEquals(p3.getWorkers().get(0), b.getSquare(3,2).getWorker());
        assertEquals(p3.getWorkers().get(1), b.getSquare(4,4).getWorker());

    }

    /**
     * This method tests that only one opponent's worker eas remove
     */

    @Test
    void checkOneNeighbourWorkerRemoved(){

        b.getSquare(1,2).buildLevel(2);
        b.getSquare(1,1).buildLevel(1);
        b.resetCounters();

        p1.move(p1.getWorkers().get(0), 1,1);
        p1.build(p1.getWorkers().get(0), 1,0,1);

        assertEquals(p1.getWorkers().get(1), b.getSquare(1,3).getWorker());
        //check the absence of opponent's worker
        assertNull(b.getSquare(2,1).getWorker());
        //check if a level was build
        assertEquals(1, b.getSquare(2,1).getLevel());
        assertEquals(p2.getWorkers().get(1), b.getSquare(3,1).getWorker());
        assertEquals(p3.getWorkers().get(0), b.getSquare(3,2).getWorker());
        assertEquals(p3.getWorkers().get(1), b.getSquare(4,4).getWorker());


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