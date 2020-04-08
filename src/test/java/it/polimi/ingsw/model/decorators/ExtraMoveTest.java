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
 * Test suite for ExtraMove
 * @author pierobartolo
 */

class ExtraMoveTest {
    Board b;
    Player p1, p2, p3;

    /**
     * Setup for testing :
     *  - 3 players
     *  - p1 --> Artemis
     *  - p2 --> Athena
     *  - p3 --> Triton
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

        ArrayList<God> gods = factory.getGods( new ArrayList<>(Arrays.asList(2,3,29)));
        p1.setGod(Objects.requireNonNull(gods.get(0))); // Artemis
        p2.setGod(Objects.requireNonNull(gods.get(1))); // Athena
        p3.setGod(Objects.requireNonNull(gods.get(2))); // Triton
        // p1 sets workers
        p1.getWorkers().get(0).setWorkerOnBoard(b.getSquare(0,0));
        p1.getWorkers().get(1).setWorkerOnBoard(b.getSquare(1,0));

        // p2 sets workers
        p2.getWorkers().get(0).setWorkerOnBoard(b.getSquare(4,3));
        p2.getWorkers().get(1).setWorkerOnBoard(b.getSquare(0,3));

        // p3 sets workers
        p3.getWorkers().get(0).setWorkerOnBoard(b.getSquare(2,2));
        p3.getWorkers().get(1).setWorkerOnBoard(b.getSquare(1,3));

    }


    /**
     * This method tests Artemis' power with Athena present on the board.
     * Artemis' power:  Your Worker may move one additional time, but not back to its initial space.
     */
    @Test
    public void checkArtemisWithAthena(){
        b.getSquare(0,2).buildLevel(1);
        b.resetCounters();
        assertTrue(p2.move(p2.getWorkers().get(1), 0, 2).isEmpty());

        b.resetCounters();
        assertTrue(p1.move(p1.getWorkers().get(0), 0, 1).isEmpty());

        // Check Artemis cannot move back
        ArrayList<Error> temp_errors = p1.move(p1.getWorkers().get(0),0,0);
        assertTrue(temp_errors.contains(Error.EXTRA_MOVE_NOT_BACK));
        assertEquals(1,temp_errors.size());

        // Check Artemis can move again but Athena blocks him
        b.getSquare(1,1).buildLevel(1);
        temp_errors = p1.move(p1.getWorkers().get(0),1,1);
        assertTrue(temp_errors.contains(Error.BLOCK_MOVE_UP));
        assertEquals(1,temp_errors.size());

        // Check Artemis can move again
        temp_errors = p1.move(p1.getWorkers().get(0),1,2);
        assertTrue(temp_errors.isEmpty());

        // Check Artemis can't move for the third time
        temp_errors = p1.move(p1.getWorkers().get(0),2,3);
        assertTrue(temp_errors.contains(Error.MOVES_EXCEEDED));
        assertEquals(1,temp_errors.size());

        b.resetCounters();
    }

    /**
     * This method tests Triton's power with Athena present on the board.
     * Triton's power: Each time your Worker moves into a perimeter space, it may immediately move again.
     */

    @Test
    public void checkTritonWithAthena(){
        b.getSquare(0,2).buildLevel(1);

        // Activating Athena's power
        b.resetCounters();
        assertTrue(p2.move(p2.getWorkers().get(1), 0, 2).isEmpty());

        // Triton's turn
        b.resetCounters();
        assertTrue(p3.move(p3.getWorkers().get(1), 1, 4).isEmpty());

        // Check Triton can move again on perimeter
        assertTrue(p3.move(p3.getWorkers().get(1), 2, 4).isEmpty());
        assertTrue(p3.move(p3.getWorkers().get(1), 3, 4).isEmpty());

        // Check Triton can't move up because of Athena's power
        b.getSquare(2,4).buildLevel(1);
        ArrayList<Error> temp_errors = p3.move(p3.getWorkers().get(1),2,4);
        assertTrue(temp_errors.contains(Error.BLOCK_MOVE_UP));
        assertEquals(1,temp_errors.size());

        //Check Triton can't move outside perimeter
        assertTrue(p3.move(p3.getWorkers().get(1), 3, 3).isEmpty());
         temp_errors = p3.move(p3.getWorkers().get(1),3,2);
        assertTrue(temp_errors.contains(Error.MOVES_EXCEEDED));
        assertEquals(1, temp_errors.size());

    }


}