package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for BuildBeforeMove
 * @author pierobartolo
 */

class BuildBeforeMoveTest {
    Board b;
    Player p1, p2, p3;

    /**
     * Setup for testing :
     *  - 3 players
     *  - p1 --> Prometheus
     *  - p2 --> Athena
     *  - p3 --> Hera
     *  - all workers set on board
     */

    @BeforeEach
    public void setUp(){
        //Object creation
        b = new Board();
        GodsFactory factory = new GodsFactory(b);
        p1 = new Player("pierobartolo", Color.WHITE);
        p2 = new Player("marcodige",Color.GREY);
        p3 = new Player("aledimaio",Color.AZURE);

        ArrayList<God> gods = factory.getGods( new ArrayList<>(Arrays.asList(10,1,20)));
        p1.setGod(Objects.requireNonNull(gods.get(0))); // Prometheus
        p2.setGod(Objects.requireNonNull(gods.get(1))); // Apollo
        p3.setGod(Objects.requireNonNull(gods.get(2))); // Hera
        // p1 sets workers
        p1.getWorkers().get(0).setWorkerOnBoard(b.getSquare(0,0));
        p1.getWorkers().get(1).setWorkerOnBoard(b.getSquare(1,0));

        // p2 sets workers
        p2.getWorkers().get(0).setWorkerOnBoard(b.getSquare(0,4));
        p2.getWorkers().get(1).setWorkerOnBoard(b.getSquare(1,4));

        // p3 sets workers
        p3.getWorkers().get(0).setWorkerOnBoard(b.getSquare(3,4));
        p3.getWorkers().get(1).setWorkerOnBoard(b.getSquare(4,4));

    }


    /**
     * This method tests Prometheus' power with Hera present on the board.
     * Prometheus' power:  If your Worker does not move up, it may build both before and after moving
     */

    @Test
    public void checkPrometheusAndHera(){
        //Activate Athena's Power
        b.getSquare(0,3).buildLevel(1);
        b.resetCounters();
        assertTrue(p2.move(p2.getWorkers().get(1), 0, 3).isEmpty());


        // Build before move
        b.resetCounters();
        assertTrue(p1.build(p1.getWorkers().get(0),0,1,1).isEmpty());

        // Check cannot build two times before move
        ArrayList<Error> temp_errors = p1.build(p1.getWorkers().get(0),0,1,2);
        assertTrue(temp_errors.contains(Error.BUILD_BEFORE_MOVE));
        assertEquals(1,temp_errors.size());

        // Check Prometheus cannot move up
        temp_errors = p1.move(p1.getWorkers().get(0),0,1);
        assertTrue(temp_errors.contains(Error.CANT_MOVE_UP));
        assertEquals(1,temp_errors.size());


        // Check Prometheus can move (not up)
        assertTrue(p1.move(p1.getWorkers().get(0),1,1).isEmpty());

        // Check Prometheus can't move two times
        temp_errors = p1.move(p1.getWorkers().get(0),1,2);
        assertTrue(temp_errors.contains(Error.MOVES_EXCEEDED));
        assertEquals(1,temp_errors.size());

        // Check Prometheus can build two times
        assertTrue(p1.build(p1.getWorkers().get(0),0,1,2).isEmpty());

        // Check Prometheus can't build for the third time
        temp_errors = p1.build(p1.getWorkers().get(0),0,1,3);
        assertTrue(temp_errors.contains(Error.BUILDS_EXCEEDED));
        assertEquals(1,temp_errors.size());

        // Check Prometheus can't win with Hera's power
        b.getSquare(1,1).buildLevel(1);
        b.getSquare(1,1).buildLevel(2);
        b.resetCounters();
        assertTrue(p1.move(p1.getWorkers().get(0),0,1).isEmpty());
        assertFalse(p1.getGod().getPower().checkWin(p1.getWorkers().get(0)));

    }


}