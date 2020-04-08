package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Down2Win class represents Pan's power
 * @author aledimaio
 */

class Down2WinTest {

    Board b;
    Player p1, p2, p3;

    /**
     * Setup for testing:
     * 3 players, each of them have workers set on board
     */

    @BeforeEach
    void setUp() {

        b = new Board();
        GodsFactory factory = new GodsFactory(b);
        p1 = new Player("pierobartolo", Color.WHITE);
        p2 = new Player("marcodige",Color.GREY);
        p3 = new Player("aledimaio",Color.AZURE);
        ArrayList<God> gods = factory.getGods(new ArrayList<>(Arrays.asList(8,5,9)));
        p1.setGod(Objects.requireNonNull(gods.get(0))); // Minotaur
        p2.setGod(Objects.requireNonNull(gods.get(1))); // Demeter
        p3.setGod(Objects.requireNonNull(gods.get(2))); // Pan

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

    @Test
    void checkWin() {

        Square l0, l2;
        l0 = b.getSquare(2,2);
        l2 = b.getSquare(2,3);

        assertEquals(0, b.getSquare(2,2).getLevel());
        assertEquals(0, b.getSquare(2,3).getLevel());

        for (int i = 0; i < 2; i++)
            l2.buildLevel(i+1);

        assertEquals(2, b.getSquare(2,3).getLevel());

        p3.getWorkers().get(0).updateWorkerPosition(l2);

        assertEquals(p3.getWorkers().get(0), l2.getWorker());
        assertFalse(p3.getGod().getPower().checkWin(p3.getWorkers().get(0)));

        p3.getGod().getPower().updateMove(p3.getWorkers().get(0),2,2);
        assertEquals(p3.getWorkers().get(0), b.getSquare(2,2).getWorker());
        //check if the player wins
        assertTrue(p3.getGod().getPower().checkWin(p3.getWorkers().get(0)));

    }
}