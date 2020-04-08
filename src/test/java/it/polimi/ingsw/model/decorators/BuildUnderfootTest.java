package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for BuildUnderFoot class, Zeus's power
 * @author aledimaio
 */

class BuildUnderfootTest {

    Board b;
    Player p1, p2, p3;

    @BeforeEach
    void setUp() {

        b = new Board();
        GodsFactory factory = new GodsFactory(b);
        p1 = new Player("pierobartolo", Color.WHITE);
        p2 = new Player("marcodige",Color.GREY);
        p3 = new Player("aledimaio",Color.AZURE);
        ArrayList<God> gods = factory.getGods(new ArrayList<>(Arrays.asList(10,29,30)));
        p1.setGod(Objects.requireNonNull(gods.get(0))); // Prometheus
        p2.setGod(Objects.requireNonNull(gods.get(1))); // Triton
        p3.setGod(Objects.requireNonNull(gods.get(2))); // Zeus

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
    void checkBuild() {

        assertEquals(p3.getWorkers().get(0), b.getSquare(2,2).getWorker());
        assertEquals(0, b.getSquare(2,2).getLevel());
        p3.getGod().getPower().checkMove(p3.getWorkers().get(0),2,1);

        p3.getGod().getPower().updateMove(p3.getWorkers().get(0),2,1);
        assertEquals(p3.getWorkers().get(0), b.getSquare(2,1).getWorker());
        //assertEquals("[]", p3.getGod().getPower().checkBuild(p3.getWorkers().get(0),2,1,1));

        p3.getGod().getPower().updateBuild(p3.getWorkers().get(0),2,2,1);


    }
}