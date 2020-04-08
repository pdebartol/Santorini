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
 * Test suite for MovePush class, Minotaur's power
 * @author aledimaio
 */

class MovePushTest {

    Board b;
    Player p1, p2, p3;

    @BeforeEach
    void setUp() {

        b = new Board();
        GodsFactory factory = new GodsFactory(b);
        p1 = new Player("aledimaio", Color.WHITE);
        p2 = new Player("marcodige",Color.GREY);
        p3 = new Player("pierobartolo", Color.AZURE);
        ArrayList<God> gods = factory.getGods( new ArrayList<>(Arrays.asList(8,3,1)));
        p1.setGod(Objects.requireNonNull(gods.get(0))); // Minotaur
        p2.setGod(Objects.requireNonNull(gods.get(1))); // Athena
        p3.setGod(Objects.requireNonNull(gods.get(2))); // Apollo

        // p1 sets workers
        p1.getWorkers().get(0).setWorkerOnBoard(b.getSquare(0,0));
        p1.getWorkers().get(1).setWorkerOnBoard(b.getSquare(1,1));

        // p2 sets workers
        p2.getWorkers().get(0).setWorkerOnBoard(b.getSquare(2,2));
        p2.getWorkers().get(1).setWorkerOnBoard(b.getSquare(4,2));

        // p3 sets workers
        p3.getWorkers().get(0).setWorkerOnBoard(b.getSquare(2,1));
        p3.getWorkers().get(1).setWorkerOnBoard(b.getSquare(3,1));
    }

    /**
     * This method check Player with Minotaur's power can move his worker in a level 2 occupied square from a level 1 square
     * (the level two square is empty next the same direction of moves
     */

    @Test
    void checkMove() {

        b.getSquare(1,1).buildLevel(1);
        b.getSquare(2,2).buildLevel(2);

        //check if setup is correct
        assertEquals(p1.getWorkers().get(1), b.getSquare(1,1).getWorker());
        assertEquals(1, b.getSquare(1,1).getLevel());
        assertEquals(p2.getWorkers().get(0), b.getSquare(2,2).getWorker());
        assertEquals(2, b.getSquare(2,2).getLevel());

        //check if checkMove decorated is correct
        assertTrue(p1.getGod().getPower().checkMove(p1.getWorkers().get(1),2,2).isEmpty());

    }

    @Test
    void checkMoveOccupiedSquare(){

        b.getSquare(1,1).buildLevel(1);
        b.getSquare(2,2).buildLevel(2);
        b.resetCounters();

        assertFalse(p1.getGod().getPower().checkMove(p1.getWorkers().get(1),2,1).isEmpty());

    }

    @Test
    void updateMove() {

        b.getSquare(1,1).buildLevel(1);
        b.getSquare(2,2).buildLevel(2);
        b.resetCounters();

        assertTrue(p1.getGod().getPower().checkMove(p1.getWorkers().get(1),2,2).isEmpty());
        p1.getGod().getPower().updateMove(p1.getWorkers().get(1), 2,2);
        //p1.move(p1.getWorkers().get(1), 2,2);

    }

    /**
     * This method check if an opponent's worker pushed could bring him to victory
     */

    @Test
    void NoWinWhenPushed(){

        b.getSquare(1,1).buildLevel(1);
        b.getSquare(2,2).buildLevel(2);
        b.getSquare(3,3).buildLevel(3);
        b.resetCounters();

        //check of setup
        assertEquals(p1.getWorkers().get(1), b.getSquare(1,1).getWorker());
        assertEquals(p2.getWorkers().get(0), b.getSquare(2,2).getWorker());

        //move to 2,2 square and push worker 0 of player 2 to 3,3 square
        assertTrue(p1.getGod().getPower().checkMove(p1.getWorkers().get(1),2,2).isEmpty()); //questa tutto ok!
        p1.move(p1.getWorkers().get(1), 2,2);

        //check if player 2 wins if pushed up by minotaur's power
        assertFalse(p2.getGod().getPower().checkWin(p2.getWorkers().get(0)));

    }

}

