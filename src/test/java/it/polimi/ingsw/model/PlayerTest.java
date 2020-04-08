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
        p1.getWorkers().get(1).setWorkerOnBoard(b.getSquare(1,1));

        // p2 sets workers
        p2.getWorkers().get(0).setWorkerOnBoard(b.getSquare(4,4));
        p2.getWorkers().get(1).setWorkerOnBoard(b.getSquare(4,2));

        // p3 sets workers
        p3.getWorkers().get(0).setWorkerOnBoard(b.getSquare(0,2));
        p3.getWorkers().get(1).setWorkerOnBoard(b.getSquare(2,2));
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
     * This method tests that, if there is at least 1 adjacent square where at least 1 worker's player can move,
     * canMove() return true.
     */

    @Test
    void canMoveCheck(){
        //first check
        assertTrue(p1.canMove());

        //Only 1 square for p1 to move (p1 can move only workers.get(1) to (2,0) position)
        b.getSquare(1,0).buildLevel(2);
        b.getSquare(0,1).buildLevel(2);
        b.getSquare(2,1).buildLevel(2);
        b.getSquare(1,2).buildLevel(2);
        assertTrue(p1.canMove());
    }

    /**
     * This method tests that, if there isn't at least 1 square where at least 1 worker's player can move,
     * canMove() return false.
     */

    @Test
    void cantMoveCheck(){
        //There isn't position where p1 can move
        b.getSquare(1,0).buildLevel(2);
        b.getSquare(0,1).buildLevel(2);
        b.getSquare(2,1).buildLevel(2);
        b.getSquare(1,2).buildLevel(2);
        b.getSquare(2,0).buildLevel(2);
        assertFalse(p1.canMove());
    }

    @Test
    void canBuildException(){
        assertThrows(IllegalArgumentException.class, () -> p1.canBuild(null));
    }

    /**
     * This method tests that, if there is at least 1 square where the worker's player used for move can build,
     * canBuild() returns true.
     */

    @Test
    void canBuildCheck() {
        //first check
        p1.move(p1.getWorkers().get(0),0,1); //The player have to move worker before build with him
        assertTrue(p1.canBuild(p1.getWorkers().get(0)));

        //only 1 square for p1 to build (the rest of squares is occupied or a complete tower)
        b.getSquare(1,0).buildLevel(3);
        b.getSquare(1,0).buildLevel(4);
        b.getSquare(0,0).buildLevel(3);
        b.getSquare(0,0).buildLevel(4);
        b.getSquare(2,1).buildLevel(3);
        b.getSquare(2,1).buildLevel(4);
        assertTrue(p1.canBuild(p1.getWorkers().get(0)));
    }

    /**
     * This method tests that, if there isn't at least 1 square where the worker's player used for move can build,
     * canBuild() returns false.
     */

    @Test
    void cantBuildCheck(){
        p1.move(p1.getWorkers().get(0),0,1); //The player have to move worker before build with him

        //There isn't position where p1 can build
        b.getSquare(1,0).buildLevel(3);
        b.getSquare(1,0).buildLevel(4);
        b.getSquare(0,0).buildLevel(3);
        b.getSquare(0,0).buildLevel(4);
        b.getSquare(2,1).buildLevel(3);
        b.getSquare(2,1).buildLevel(4);
        b.getSquare(1,2).buildLevel(3);
        b.getSquare(1,2).buildLevel(4);
        assertFalse(p1.canBuild(p1.getWorkers().get(0)));
    }

    @Test
    void moveException() {
        assertThrows(IllegalArgumentException.class, () -> p1.move(null,1,2));
        assertThrows(IllegalArgumentException.class, () -> p1.move(p1.getWorkers().get(0),-1,2));
        assertThrows(IllegalArgumentException.class, () -> p1.move(p1.getWorkers().get(0),1,5));
    }

    /**
     * This method check that, after a move() call, if this move is legal, model status is updated correctly.
     */

    @Test
    void legalMove(){
    }

    /**
     * This method check that, after a move() call, if this move is illegal, model status is unchanged.
     */

    @Test
    void illegalMove(){

    }

    @Test
    void buildException(){
        assertThrows(IllegalArgumentException.class, () -> p1.build(null,1,2,1));
        assertThrows(IllegalArgumentException.class, () -> p1.build(p1.getWorkers().get(0),-1,2,1));
        assertThrows(IllegalArgumentException.class, () -> p1.build(p1.getWorkers().get(0),1,5,1));
        assertThrows(IllegalArgumentException.class, () -> p1.build(p1.getWorkers().get(0),0,1,5));
    }

    /**
     * This method check that, after a build() call, if this move is legal, model status is updated correctly.
     */

    @Test
    void legalBuild(){

    }

    /**
     * This method check that, after a build() call, if this move is legal, model status is updated correctly.
     */

    @Test
    void illegalBuild() {
    }
}