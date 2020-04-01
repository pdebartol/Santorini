package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Player Class
 * @author pierobartolo & marcoDige
 */

class PlayerTest {
    Player player1;

    @BeforeEach
    void setUp() {
         player1 = new Player("piero", Color.WHITE);
    }


    /**
     * Check that the worker is removed from the Player's available workers.
     * This might happen if Medusa is in game.
     */
    @Test
    void checkRemoveWorker(){
        Worker worker1 = Objects.requireNonNull(player1.getWorkers().get(0));
        Worker worker2 = Objects.requireNonNull(player1.getWorkers().get(1));

        assertEquals(2, player1.getWorkers().size());
        assertEquals(true,player1.getWorkers().contains(worker1));
        assertEquals(true,player1.getWorkers().contains(worker2));
        worker1.removeFromGame();
        assertEquals(1, player1.getWorkers().size());
        assertEquals(true,player1.getWorkers().contains(worker2));

    }

    @Test
    void canMove() {
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