package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for BuildDomeEverywhere
 * @author aledimaio & pierobartolo
 */

class BuildDomeEverywhereTest {

    Board b;
    Player p1;

    /**
     * Setup for testing:
     * - 1 Player --> Atlas
     * - all workers set on board
     */

    @BeforeEach
    void setUp() {
        b = new Board();
        GodsFactory factory = new GodsFactory(b);
        p1 = new Player("aledimaio", Color.WHITE);
        ArrayList<God> gods = factory.getGods(new ArrayList<>(Collections.singletonList(4)));
        p1.setGod(Objects.requireNonNull(gods.get(0))); // Atlas

        // p1 sets workers
        p1.getWorkers().get(0).setWorkerOnBoard(b.getSquare(2,2));
        p1.getWorkers().get(1).setWorkerOnBoard(b.getSquare(0,0));
    }

    /**
     * This method tests if the standard Build works properly
     */

    @Test
    void checkStandardBuild() {
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Player moves worker before build
        assertTrue(p1.move(p1.getWorkers().get(0),2,1).isEmpty());

        //Check that worker cannot build a "middle level" not following standard rules
        List<Error> temp_errors = p1.build(p1.getWorkers().get(0), 2,2,2);
        assertTrue(temp_errors.contains(Error.INVALID_LEVEL_BUILD));
        assertEquals(1,temp_errors.size());

        //Check that worker can build a level 1 on a level 0 (this follow from standard rules)
        assertTrue(p1.build(p1.getWorkers().get(0),2,2,1).isEmpty());
    }

    /**
     * This method tests Atlas' power: Your Worker may build a dome at any level.
     * It checks that a dome can be built at level 0.
     */

    @Test
    void checkBuildDomeLevel0(){
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Player moves worker before build
        assertTrue((p1.move(p1.getWorkers().get(0),2,3).isEmpty()));

        //Check buildDome at level 0
        assertEquals(0,b.getSquare(2,2).getLevel());
        assertTrue(p1.build(p1.getWorkers().get(0),2,2,4).isEmpty());
    }

    /**
     * This method tests Atlas' power: Your Worker may build a dome at any level.
     * It checks that a dome can be built at level 1.
     */

    @Test
    void checkBuildDomeLevel1(){
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Player moves worker before build
        assertTrue((p1.move(p1.getWorkers().get(0),2,3).isEmpty()));

        //Check buildDome at level 0
        b.getSquare(2,2).buildLevel(1);
        assertEquals(1,b.getSquare(2,2).getLevel());
        assertTrue(p1.build(p1.getWorkers().get(0),2,2,4).isEmpty());
    }

    /**
     * This method tests Atlas' power: Your Worker may build a dome at any level.
     * It checks that a dome can be built at level 2.
     */

    @Test
    void checkBuildDomeLevel2(){
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Player moves worker before build
        assertTrue((p1.move(p1.getWorkers().get(0),2,3).isEmpty()));

        //Check buildDome at level 0
        b.getSquare(2,2).buildLevel(2);
        assertEquals(2,b.getSquare(2,2).getLevel());
        assertTrue(p1.build(p1.getWorkers().get(0),2,2,4).isEmpty());
    }

    /**
     * This method tests Atlas' power: Your Worker may build a dome at any level.
     * It checks that a dome can be built at level 3.
     */

    @Test
    void checkBuildDomeLevel3(){
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Player moves worker before build
        assertTrue((p1.move(p1.getWorkers().get(0),2,3).isEmpty()));

        //Check buildDome at level 0
        b.getSquare(2,2).buildLevel(3);
        assertEquals(3,b.getSquare(2,2).getLevel());
        assertTrue(p1.build(p1.getWorkers().get(0),2,2,4).isEmpty());
    }

    /**
     * This method tests Atlas' power: Your Worker may build a dome at any level.
     * It checks that a dome can't be built on top of another dome.
     */

    @Test
    void checkBuildDomeOnAnotherDome(){
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Player moves worker before build
        assertTrue((p1.move(p1.getWorkers().get(0),2,3).isEmpty()));

        //Check buildDome at level 0
        b.getSquare(2,2).buildLevel(4);
        assertEquals(0,b.getSquare(2,2).getLevel());
        assertTrue(b.getSquare(2,2).getDome());
        List<Error> temp_errors = p1.build(p1.getWorkers().get(0),2,2,4);
        assertTrue(temp_errors.contains(Error.IS_DOME));
        assertEquals(1,temp_errors.size());
    }

}