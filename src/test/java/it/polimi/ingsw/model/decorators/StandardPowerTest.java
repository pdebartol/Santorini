package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for StandardPower class
 * @author marcoDige
 */
class StandardPowerTest {

    Board b;
    Player p1,p2,p3;
    Worker wmp1,wfp1,wmp2,wfp2;
    Power p;

    @BeforeEach
    public void setUp() {
        b = new Board();
        p1 = new Player("marcoDige", Color.AZURE);
        p2 = new Player("pierobartolo", Color.WHITE);
        p3 = new Player("aledimaio",Color.GREY);
        wmp1 = p1.getWorkers().get(0);
        wfp1 = p1.getWorkers().get(1);
        wmp2 = p2.getWorkers().get(0);
        wfp2 = p2.getWorkers().get(1);

        wmp1.setWorkerOnBoard(b.getSquare(1,1));
        wfp1.setWorkerOnBoard(b.getSquare(3,4));
        wmp2.setWorkerOnBoard(b.getSquare(1,3));
        wfp2.setWorkerOnBoard(b.getSquare(3,2));

        p = new StandardPower(1,1, b);
    }

    @Test
    public void checkMove() {
    }

    @Test
    public void checkBuild() {
    }

    @Test
    public void checkWin() {
        //creating a ladder to win (we test checkWin with wmp1)
        Square l2,l3;
        l2 = b.getSquare(2,0);
        l3 = b.getSquare(3,0);
        //level 2
        for(int i = 0 ; i < 2 ; i++)
            l2.buildLevel();
        //level 3
        for(int i = 0 ; i < 3 ; i++)
            l3.buildLevel();

        assertEquals(2,l2.getLevel());
        assertEquals(3,l3.getLevel());
        //set wmp1 on level 2 position;
        wmp1.updateWorkerPosition(l2);
        assertEquals(wmp1,l2.getWorker());
        assertEquals(false,p.checkWin(wmp1));
        //move wmp2 on level 3 position;
        wmp1.updateWorkerPosition(l3);
        assertEquals(wmp1,l3.getWorker());
        assertEquals(true,p.checkWin(wmp1));

    }

    @Test
    public void updateMove() {
        //move wmp1 to 2,1
        p.updateMove(wmp1,b.getSquare(2,1));
        assertEquals(wmp1,b.getSquare(2,1).getWorker());
        assertEquals(wmp1.getCurrentSquare(),b.getSquare(2,1));
        assertEquals(b.getSquare(1,1),wmp1.getLastSquareMove());
        assertEquals(1,b.getNMoves());
        //move wmp1 to 2,2
        p.updateMove(wmp1,b.getSquare(2,2));
        assertEquals(wmp1,b.getSquare(2,2).getWorker());
        assertEquals(wmp1.getCurrentSquare(),b.getSquare(2,2));
        assertEquals(b.getSquare(2,1),wmp1.getLastSquareMove());
        assertEquals(2,b.getNMoves());
    }

    @Test
    public void updateBuild() {

    }

    @Test
    public void checkTurn() {
    }
}