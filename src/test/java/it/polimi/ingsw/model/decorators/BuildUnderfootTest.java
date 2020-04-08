package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for BuildUnderFoot class, Zeus's power
 * @author aledimaio
 */

class BuildUnderfootTest {

    Board b;
    Player p1;

    /**
     * Setup for tests:
     * - 1 Player
     * - all worker set on board
     */

    @BeforeEach
    void setUp() {

        b = new Board();
        GodsFactory factory = new GodsFactory(b);
        p1 = new Player("aledimaio", Color.WHITE);
        ArrayList<God> gods = factory.getGods(new ArrayList<>(Collections.singletonList(30)));
        p1.setGod(Objects.requireNonNull(gods.get(0))); // Zeus

        // p1 sets workers
        p1.getWorkers().get(0).setWorkerOnBoard(b.getSquare(0,0));
        p1.getWorkers().get(1).setWorkerOnBoard(b.getSquare(0,1));

    }

    /**
     * This method tests if standard checkBuild still works
     */

    @Test
    void checkStandardBuild() {

        //worker moves to near square
        p1.move(p1.getWorkers().get(0),1,1);
        assertEquals(0, b.getSquare(1,1).getLevel());
        //check if everything is ok
        assertTrue(p1.getGod().getPower().checkBuild(p1.getWorkers().get(0),0,0,1).isEmpty());
        assertFalse(p1.getGod().getPower().checkBuild(p1.getWorkers().get(0),0,0,2).isEmpty());


    }

    /**
     * This method verifies if BuildUnderFoot power works, both check and build has been tested
     */

    @Test
    void checkBuildUnderFoot(){

        //worker moves to near empty square
        p1.move(p1.getWorkers().get(0),1,1);
        //check worker position
        assertEquals(0, b.getSquare(1,1).getLevel());
        //check that worker can build under its foot
        assertTrue(p1.getGod().getPower().checkBuild(p1.getWorkers().get(0),1,1,1).isEmpty());
        //check tha worker cannot build more than a level under its foot
        assertFalse(p1.getGod().getPower().checkBuild(p1.getWorkers().get(0),1,1,2).isEmpty());
        //effectively build and check everything is ok
        p1.build(p1.getWorkers().get(0),1,1,1);
        assertEquals(p1.getWorkers().get(0), b.getSquare(1,1).getWorker());

    }

    /**
     * This method tests that a worker cannot build a dome under itself
     */

    @Test
    void checkCannotBuildDomeUnderFoot(){

        b.getSquare(0,0).buildLevel(3);
        b.getSquare(1,1).buildLevel(3);
        b.resetCounters();
        //move and check if a dome can be build under worker's foot
        p1.move(p1.getWorkers().get(0),1,1);
        assertFalse(p1.getGod().getPower().checkBuild(p1.getWorkers().get(0), 1,1,4).isEmpty());

    }

    /**
     * This method tests that a player cannot win if he uses BuildUnderFoot to go from a level two to a level 3
     */

    @Test
    void checkWinConditionUnderFoot(){

        b.getSquare(0,0).buildLevel(1);
        b.getSquare(1,1).buildLevel(2);
        b.resetCounters();

        assertEquals(p1.getWorkers().get(0), b.getSquare(0,0).getWorker());
        assertEquals(1, b.getSquare(0,0).getLevel());
        p1.move(p1.getWorkers().get(0),1,1);

        //build level 3
        p1.build(p1.getWorkers().get(0),1,1,3);
        assertEquals(3, b.getSquare(1,1).getLevel());
        assertEquals(p1.getWorkers().get(0), b.getSquare(1,1).getWorker());

        //check if Player win
        assertFalse(p1.getGod().getPower().checkWin(p1.getWorkers().get(0)));

    }
}