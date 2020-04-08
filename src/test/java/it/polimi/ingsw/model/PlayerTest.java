package it.polimi.ingsw.model;

import it.polimi.ingsw.model.decorators.StandardPower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Player Class
 * This suit tests Player's methods using the standard power (without gods). The decorated powers have been individually
 * tested in PowerTest suites
 * @author pierobartolo & marcoDige
 */

class PlayerTest {
    Player p1, p2, p3;
    Board b;

    /**
     * Setup for testing :
     *  - 3 players (all with StandardPower God)
     *  - all workers set on board
     */

    @BeforeEach
    void setUp() {

        p1 = new Player("pierobartolo", Color.WHITE);
        p2 = new Player("marcoDige",Color.AZURE);
        p3 = new Player("aledimaio",Color.GREY);

        b = new Board();

        p1.setGod(new God("Standard","Santorini standard rules",new StandardPower(1,1,b)));
        p2.setGod(new God("Standard","Santorini standard rules",new StandardPower(1,1,b)));
        p3.setGod(new God("Standard","Santorini standard rules",new StandardPower(1,1,b)));

        // p1 sets workers
        p1.getWorkers().get(0).setWorkerOnBoard(b.getSquare(0,0));
        p1.getWorkers().get(1).setWorkerOnBoard(b.getSquare(0,1));

        // p2 sets workers
        p2.getWorkers().get(0).setWorkerOnBoard(b.getSquare(4,4));
        p2.getWorkers().get(1).setWorkerOnBoard(b.getSquare(4,2));

        // p3 sets workers
        p3.getWorkers().get(0).setWorkerOnBoard(b.getSquare(2,2));
        p3.getWorkers().get(1).setWorkerOnBoard(b.getSquare(3,0));
    }


    /**
     * Check that the worker is removed from the Player's available workers.
     * This might happen if Medusa is in game.
     */

    @Test
    void checkRemoveWorker(){
        Worker worker1 = Objects.requireNonNull(p1.getWorkers().get(0));
        Worker worker2 = Objects.requireNonNull(p1.getWorkers().get(1));

        assertEquals(2, p1.getWorkers().size());
        assertTrue(p1.getWorkers().contains(worker1));
        assertTrue(p1.getWorkers().contains(worker2));
        worker1.removeFromGame();
        assertEquals(1, p1.getWorkers().size());
        assertTrue(p1.getWorkers().contains(worker2));
    }

    /**
     *
     */

    @Test
    void canMoveCheck(){

    }

    @Test
    void cantMoveCheck(){

    }

    @Test
    void canBuild() {
    }

    @Test
    void move() {
    }

    @Test
    void build() {
    }
}