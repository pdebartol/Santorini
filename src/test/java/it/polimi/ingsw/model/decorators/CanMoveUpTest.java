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
 * Test suite for CanMoveUp and BlockMoveUp
 * @author pierobartolo & marcoDige
 */

class CanMoveUpTest {
    Board b;
    Player p1, p2, p3;

    /**
     * Setup for testing :
     *  - 3 players (one of them uses Athena's power, the choice of the other two gods is insignificant)
     *  - all workers set on board
     */

    @BeforeEach
    public void blockMoveUp(){

        //Object creation
        b = new Board();
        GodsFactory factory = new GodsFactory(b);
        p1 = new Player("pierobartolo", Color.WHITE);
        p2 = new Player("marcodige",Color.GREY);
        p3 = new Player("aledimaio",Color.AZURE);
        ArrayList<God> gods = factory.getGods( new ArrayList<>(Arrays.asList(1,2,3)));
        p1.setGod(Objects.requireNonNull(gods.get(2))); // Athena
        p2.setGod(Objects.requireNonNull(gods.get(1))); // Artemis
        p3.setGod(Objects.requireNonNull(gods.get(0))); // Apollo

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
     * This method tests that, when Athena's player moves up in his turn, the others player can't move up in their turn.
     */

    @Test
    public void athenaBlock(){
        // Athena moves up
        b.getSquare(1,0).buildLevel(1);
        b.resetCounters();
        p1.move(p1.getWorkers().get(0),1,0);

        //Artemis check
        b.resetCounters();
        b.getSquare(3,4).buildLevel(1);
        assertEquals(true,p2.move(p2.getWorkers().get(0),3,4).contains(Error.BLOCK_MOVE_UP));
        assertEquals(true,p2.move(p2.getWorkers().get(0),4,3).isEmpty());

        // Apollo check
        b.resetCounters();
        b.getSquare(2,1).buildLevel(1);
        assertEquals(true,p3.move(p3.getWorkers().get(0),2,1).contains(Error.BLOCK_MOVE_UP));
        assertEquals(true,p3.move(p3.getWorkers().get(0),1,2).isEmpty());
    }

    /**
     * This method tests that, when Athena's player doesn't move up in his turn, the others can move up in their turn.
     */

    @Test
    public void notAthenaBlock(){
        //Athena doesn't move up
        b.resetCounters();
        p1.move(p1.getWorkers().get(0),1,0);

        // Artemis check
        b.resetCounters();
        b.getSquare(3,4).buildLevel(1);
        assertEquals(true,p2.move(p2.getWorkers().get(0),3,4).isEmpty());

        // Apollo check
        b.resetCounters();
        b.getSquare(2,1).buildLevel(1);
        assertEquals(true,p3.move(p3.getWorkers().get(0),2,1).isEmpty());
    }

    /**
     * This method tests 2 turns:
     *  first turn -> Athena's player moves up one of his worker and other's player can't move up
     *  second turn -> Athena's player doesn't move up and other's  player can move up
     */

    @Test
    public void twoTurnMix(){
        // Athena moves up
        b.getSquare(1,0).buildLevel(1);
        b.resetCounters();
        p1.move(p1.getWorkers().get(0),1,0);

        //Artemis check
        b.resetCounters();
        b.getSquare(3,4).buildLevel(1);
        assertEquals(true,p2.move(p2.getWorkers().get(0),3,4).contains(Error.BLOCK_MOVE_UP));
        assertEquals(true,p2.move(p2.getWorkers().get(0),4,3).isEmpty());

        // Apollo check
        b.resetCounters();
        b.getSquare(2,1).buildLevel(1);
        assertEquals(true,p3.move(p3.getWorkers().get(0),2,1).contains(Error.BLOCK_MOVE_UP));
        assertEquals(true,p3.move(p3.getWorkers().get(0),1,2).isEmpty());

        //Athena doesn't moves up
        b.getSquare(1,1).buildLevel(1);
        b.resetCounters();
        p1.move(p1.getWorkers().get(0),1,1);

        // Artemis check
        b.resetCounters();
        b.getSquare(4,4).buildLevel(1);
        assertEquals(true,p2.move(p2.getWorkers().get(0),4,4).isEmpty());

        // Apollo check
        b.resetCounters();
        b.getSquare(2,2).buildLevel(1);
        assertEquals(true,p3.move(p3.getWorkers().get(0),2,2).isEmpty());

    }

}