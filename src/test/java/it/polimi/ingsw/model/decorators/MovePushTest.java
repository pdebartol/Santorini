package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for MovePush class, Minotaur's power
 * @author aledimaio & marcoDige
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
     * This method check that Player with Minotaur's power can move his worker in occupied square and push worker who occupy
     * that square to the next square in the same direction when it's free.
     */

    @Test
    void checkValidMove() {
        //chose worker for the turn
        p1.getWorkers().get(1).isMovingOn();

        //move and check that p1 can move his female worker from (1,1) to (2,2) (the position (3,3) is free)
        assertTrue(p1.move(p1.getWorkers().get(1),2,2).isEmpty());

        //check changes in model status
        assertEquals(p1.getWorkers().get(1),b.getSquare(2,2).getWorker());
        assertEquals(b.getSquare(2,2),p1.getWorkers().get(1).getCurrentSquare());
        assertEquals(b.getSquare(1,1),p1.getWorkers().get(1).getLastSquareMove());
        assertEquals(p2.getWorkers().get(0),b.getSquare(3,3).getWorker());
        assertEquals(b.getSquare(3,3),p2.getWorkers().get(0).getCurrentSquare());
        assertNotEquals(b.getSquare(2,2),p2.getWorkers().get(0).getLastSquareMove());
    }

    /**
     * This method check that Player with Minotaur's power can't move his worker in occupied square and push worker who occupy
     * that square to the next square in the same direction when it isn't free.
     */

    @Test
    void checkIllegalMove(){
        //chose worker for the turn
        p1.getWorkers().get(1).isMovingOn();

        //move and check that p1 can move his female worker from (1,1) to (2,1) (the position (3,1) is free)
        List<Error> errors = p1.move(p1.getWorkers().get(1),2,1);
        assertFalse(errors.isEmpty());

        //check SAME_DIRECTION_NOT_FREE error
        assertTrue(errors.contains(Error.SAME_DIRECTION_NOT_FREE) && errors.size() == 1);

    }

    /**
     * This method check if an opponent's worker pushed from level 2 to level 3 results winner
     */

    @Test
    void NoWinWhenPushed(){

        b.getSquare(1,1).buildLevel(1);
        b.getSquare(2,2).buildLevel(2);
        b.getSquare(3,3).buildLevel(3);

        //chose worker for the turn
        p1.getWorkers().get(1).isMovingOn();

        //p1's male worker moves to (2,2) square and push p2's male worker to (3,3) square
        p1.move(p1.getWorkers().get(1),2,2);

        //check if player 2 wins if pushed up by minotaur's power
        assertFalse(p2.getGod().getPower().checkWin(p2.getWorkers().get(0)));
    }

}

