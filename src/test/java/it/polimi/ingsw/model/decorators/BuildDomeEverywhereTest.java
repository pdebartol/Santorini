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
 * Test suite for BuildDomeEverywhere class, Atlas's power
 * @author aledimaio
 */

class BuildDomeEverywhereTest {

    Board b;
    Player p1;

    /**
     * Setup for testing:
     * - 1 Player
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
     * This method tests if standard checkBuild still works
     */

    @Test
    void checkStandardBuild() {

        //Check if can build before moves or under itself
        assertFalse(p1.getGod().getPower().checkBuild(p1.getWorkers().get(0),2,2,1).isEmpty());
        //Player moves worker before build
        p1.move(p1.getWorkers().get(0),2,1);
        //Check if worker cannot build a "middle level"
        assertFalse(p1.getGod().getPower().checkBuild(p1.getWorkers().get(0),2,2,2).isEmpty());
        //Check if worker can build a level 1 from level 0
        assertTrue(p1.getGod().getPower().checkBuild(p1.getWorkers().get(0),2,2,1).isEmpty());

    }

    /**
     * This method tests the BuildDomeEverywhere functionality
     */

    @Test
    void checkBuildDomeEverywhere(){

        //Player moves worker before build
        p1.move(p1.getWorkers().get(0),2,3);
        //Check if worker can build directly a dome (Atlas's power)
        assertTrue(p1.getGod().getPower().checkBuild(p1.getWorkers().get(0),2,2,4).isEmpty());

    }
}