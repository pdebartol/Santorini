package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for BuildUnderFoot class
 * @author aledimaio & pierobartolo
 */

class BuildUnderfootTest {

    Board b;
    Player p1;

    /**
     * Setup for tests:
     * - 1 Player --> Zeus
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
     * This method tests if the standard Build works properly.
     */

    @Test
    void checkStandardBuild() {
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Player moves worker before build
        assertTrue(p1.move(p1.getWorkers().get(0),1,1).isEmpty());
        assertEquals(0, b.getSquare(1,1).getLevel());

        //Check that worker can build a level 1 on a level 0 (this follow from standard rules)
        assertTrue(p1.build(p1.getWorkers().get(0),0,0,1).isEmpty());
    }

    /**
     * This method tests Zeus' power: Your Worker may build a block under itself.
     * It tests that a worker can build one level under its foot.
     * It tests that a worker cannot build more than one level under its foot.
     */

    @Test
    void checkBuildUnderFoot(){
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Player moves worker before build
        assertTrue(p1.move(p1.getWorkers().get(0),1,1).isEmpty());


        //check that a worker cannot build more than a level under its foot
        List<Error> temp_errors = p1.build(p1.getWorkers().get(0),1,1,2);
        assertTrue(temp_errors.contains(Error.INVALID_LEVEL_BUILD));
        assertEquals(1,temp_errors.size());

        //check that a worker can build under its foot
        assertTrue(p1.build(p1.getWorkers().get(0),1,1,1).isEmpty());
    }


    /**
     * This method tests that a worker cannot build a dome under its foot.
     */

    @Test
    void checkCannotBuildDomeUnderFoot(){
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Player moves worker before build
        p1.move(p1.getWorkers().get(0),1,1);

        // Check worker cannot build a dome under its foot
        b.getSquare(1,1).buildLevel(3);
        assertEquals(3,b.getSquare(1,1).getLevel());
        List<Error> temp_errors = p1.build(p1.getWorkers().get(0), 1,1,4);
        assertTrue(temp_errors.contains(Error.CANT_DOME_UNDERFOOT));
        assertEquals(1,temp_errors.size());

    }

    /**
     * This method tests that a player cannot win if he uses BuildUnderFoot to go from a level two to a level 3.
     */

    @Test
    void checkWinConditionUnderFoot(){
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Player moves worker before build
        p1.move(p1.getWorkers().get(0),1,1);

        // Build first two levels
        b.getSquare(1,1).buildLevel(1);
        b.getSquare(1,1).buildLevel(2);

        assertEquals(2, b.getSquare(1,1).getLevel());

        // Worker builds third level under its foot
        assertTrue(p1.build(p1.getWorkers().get(0),1,1,3).isEmpty());

        // Check player did not win
        assertFalse(p1.getGod().getPower().checkWin(p1.getWorkers().get(0)));

    }
}