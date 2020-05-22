package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
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
        p1 = new Player("pierobartolo", Color.ORANGE);
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

        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        // Athena moves up
        b.getSquare(1,0).buildLevel(1);
        p1.move(p1.getWorkers().get(0),1,0);
        b.incNBuild();
        p1.endTurn();

        //chose worker for the turn
        p2.getWorkers().get(0).isMovingOn();

        //Artemis check
        b.getSquare(3,4).buildLevel(1);
        assertTrue(p2.move(p2.getWorkers().get(0), 3, 4).contains(Error.BLOCK_MOVE_UP));
        assertTrue(p2.move(p2.getWorkers().get(0), 4, 3).isEmpty());
        b.incNBuild();
        p2.endTurn();

        //chose worker for the turn
        p3.getWorkers().get(0).isMovingOn();

        // Apollo check
        b.getSquare(2,1).buildLevel(1);
        assertTrue(p3.move(p3.getWorkers().get(0), 2, 1).contains(Error.BLOCK_MOVE_UP));
        assertTrue(p3.move(p3.getWorkers().get(0), 1, 2).isEmpty());
        b.incNBuild();
        p3.endTurn();
    }

    /**
     * This method tests that, when Athena's player doesn't move up in his turn, the others can move up in their turn.
     */

    @Test
    public void notAthenaBlock(){
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Athena doesn't move up
        p1.move(p1.getWorkers().get(0),1,0);
        b.incNBuild();
        p1.endTurn();

        //chose worker for the turn
        p2.getWorkers().get(0).isMovingOn();

        // Artemis check
        b.getSquare(3,4).buildLevel(1);
        assertTrue(p2.move(p2.getWorkers().get(0), 3, 4).isEmpty());
        b.incNBuild();
        p2.endTurn();

        //chose worker for the turn
        p3.getWorkers().get(0).isMovingOn();

        // Apollo check
        b.getSquare(2,1).buildLevel(1);
        assertTrue(p3.move(p3.getWorkers().get(0), 2, 1).isEmpty());
    }

    /**
     * This method tests 2 turns:
     *  first turn -> Athena's player moves up one of his worker and other's player can't move up
     *  second turn -> Athena's player doesn't move up and other's  player can move up
     */

    @Test
    public void twoTurnMix(){
        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        // Athena moves up
        b.getSquare(1,0).buildLevel(1);
        p1.move(p1.getWorkers().get(0),1,0);
        b.incNBuild();
        p1.endTurn();

        //chose worker for the turn
        p2.getWorkers().get(0).isMovingOn();

        //Artemis check
        b.getSquare(3,4).buildLevel(1);
        assertTrue(p2.move(p2.getWorkers().get(0), 3, 4).contains(Error.BLOCK_MOVE_UP));
        assertTrue(p2.move(p2.getWorkers().get(0), 4, 3).isEmpty());
        b.incNBuild();
        p2.endTurn();

        //chose worker for the turn
        p3.getWorkers().get(0).isMovingOn();

        // Apollo check
        b.getSquare(2,1).buildLevel(1);
        assertTrue(p3.move(p3.getWorkers().get(0), 2, 1).contains(Error.BLOCK_MOVE_UP));
        assertTrue(p3.move(p3.getWorkers().get(0), 1, 2).isEmpty());
        b.incNBuild();
        p3.endTurn();

        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //Athena doesn't moves up
        b.getSquare(1,1).buildLevel(1);
        b.resetCounters();
        p1.move(p1.getWorkers().get(0),1,1);
        b.incNBuild();
        p1.endTurn();

        //chose worker for the turn
        p2.getWorkers().get(0).isMovingOn();

        // Artemis check
        b.getSquare(4,4).buildLevel(1);
        assertTrue(p2.move(p2.getWorkers().get(0), 4, 4).isEmpty());
        b.incNBuild();
        p2.endTurn();

        //chose worker for the turn
        p3.getWorkers().get(0).isMovingOn();

        // Apollo check
        b.getSquare(2,2).buildLevel(1);
        assertTrue(p3.move(p3.getWorkers().get(0), 2, 2).isEmpty());

    }

}