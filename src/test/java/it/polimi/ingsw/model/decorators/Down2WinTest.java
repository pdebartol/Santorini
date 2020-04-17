package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Down2Win
 * @author aledimaio & marcoDige
 */

class Down2WinTest {

    Board b;
    Player p1;

    /**
     * Setup for testing:
     * - 1 player (with Pan's power)
     * - all workers set on board
     */

    @BeforeEach
    void setUp() {

        b = new Board();
        GodsFactory factory = new GodsFactory(b);
        p1 = new Player("aledimaio",Color.AZURE);
        ArrayList<God> gods = factory.getGods(new ArrayList<>(Collections.singletonList(9)));
        p1.setGod(Objects.requireNonNull(gods.get(0))); // Pan

        // p3 sets workers
        p1.getWorkers().get(0).setWorkerOnBoard(b.getSquare(2,2));
        p1.getWorkers().get(1).setWorkerOnBoard(b.getSquare(3,0));
    }

    /**
     * This method tests that p1 can win with StandardPower win condition.
     */

    @Test
    void standardWinCaseCheck(){
        //set a ladder for test Standard win case
        b.getSquare(2,3).buildLevel(1);
        b.getSquare(2,4).buildLevel(2);
        b.getSquare(3,4).buildLevel(3);

        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //move up to level 1
        p1.move(p1.getWorkers().get(0),2,3);
        assertFalse(p1.getGod().getPower().checkWin(p1.getWorkers().get(0)));
        b.incNBuild();
        p1.endTurn();

        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //move up to level 2
        p1.move(p1.getWorkers().get(0),2,4);
        assertFalse(p1.getGod().getPower().checkWin(p1.getWorkers().get(0)));
        b.incNBuild();
        p1.endTurn();

        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();

        //move up to level 3
        p1.move(p1.getWorkers().get(0),3,4);
        assertTrue(p1.getGod().getPower().checkWin(p1.getWorkers().get(0)));
        b.incNBuild();
        p1.endTurn();
    }

    /**
     * This method tests that p1 can win with Down2Win win condition.
     */

    @Test
    void down2WinDecoratedCheck(){
        b.getSquare(2,3).buildLevel(1);
        b.getSquare(2,4).buildLevel(2);

        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();
        p1.move(p1.getWorkers().get(0),2,3);
        b.incNBuild();
        p1.endTurn();

        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();
        p1.move(p1.getWorkers().get(0),2,4);
        b.incNBuild();
        p1.endTurn();

        //chose worker for the turn
        p1.getWorkers().get(0).isMovingOn();
        p1.move(p1.getWorkers().get(0),3,4);
        b.incNBuild();
        p1.endTurn();

        assertTrue(p1.getGod().getPower().checkWin(p1.getWorkers().get(0)));
    }


}