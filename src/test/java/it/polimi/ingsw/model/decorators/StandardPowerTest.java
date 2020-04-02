package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for StandardPower class
 *
 * @author marcoDige & pierobartolo
 */
class StandardPowerTest {

    Board b;
    Player p1, p2, p3;
    Worker wmp1, wfp1, wmp2, wfp2;
    Power p;

    @BeforeEach
    public void setUp() {
        b = new Board();
        p1 = new Player("marcoDige", Color.AZURE);
        p2 = new Player("pierobartolo", Color.WHITE);
        p3 = new Player("aledimaio", Color.GREY);
        wmp1 = p1.getWorkers().get(0);
        wfp1 = p1.getWorkers().get(1);
        wmp2 = p2.getWorkers().get(0);
        wfp2 = p2.getWorkers().get(1);

        wmp1.setWorkerOnBoard(b.getSquare(1, 1));
        wfp1.setWorkerOnBoard(b.getSquare(3, 4));
        wmp2.setWorkerOnBoard(b.getSquare(0, 1));
        wfp2.setWorkerOnBoard(b.getSquare(3, 2));

        p = new StandardPower(1, 1, b);
    }

    @Test
    void checkMove() {
        assertThrows(IllegalArgumentException.class, () -> p.checkMove(wfp1, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> p.checkMove(wfp1, 1, 8));
        assertThrows(IllegalArgumentException.class, () -> p.checkMove(wfp1, 5, 4));
        assertThrows(IllegalArgumentException.class, () -> p.checkMove(null, 3, 4));

    }

    @Test
    void checkValidMove() {
        assertEquals(true, p.checkMove(wmp1, 2, 1).isEmpty());
        b.getSquare(1, 0).buildLevel();
        assertEquals(true, p.checkMove(wmp1, 2, 1).isEmpty());
        b.getSquare(1, 0).buildLevel();
        wmp1.updateWorkerPosition(b.getSquare(1, 0));
        b.getSquare(1, 1).removeWorker();
        assertEquals(true, p.checkMove(wmp1, 1, 1).isEmpty());
    }

    @Test
    void checkMoveNotFree() {
        // Check NOT_FREE
        ArrayList<Error> errors = p.checkMove(wmp1, 0, 1);
        assertEquals(true, errors.contains(Error.NOT_FREE));
        assertEquals(1, errors.size());
    }

    @Test
    void checkMovesNotAdjacent() {
        //Check NOT_ADJACENT
        ArrayList<Error> errors = p.checkMove(wmp1, 3, 1);
        assertEquals(true, errors.contains(Error.NOT_ADJACENT));
        assertEquals(1, errors.size());
    }

    @Test
    void checkMovesInvalidLevel(){
        b.getSquare(0,0).buildLevel();
        b.getSquare(0,0).buildLevel();
        ArrayList<Error> errors = p.checkMove(wmp1, 0, 0);
        assertEquals(true, errors.contains(Error.INVALID_LEVEL));
        assertEquals(1, errors.size());
    }

    @Test
    void checkMovesExceeded(){
        p.updateMove(wmp1,1,2);
        ArrayList<Error> errors = p.checkMove(wmp1, 1, 3);
        assertEquals(true,errors.contains(Error.MOVES_EXCEEDED));
        assertEquals(1, errors.size());
    }

    @Test
    void checkMoveIsDome(){
        b.getSquare(0,0).setDome(true);
        ArrayList<Error> errors = p.checkMove(wmp1, 0, 0);
        assertEquals(true,errors.contains(Error.IS_DOME));
        assertEquals(1, errors.size());
    }

    @Test
    void checkMoveAfterBuild(){
        p.updateBuild(wmp1,0,0);
        ArrayList<Error> errors = p.checkMove(wmp1,0,0);
        assertEquals(true,errors.contains(Error.MOVE_AFTER_BUILD));
        assertEquals(1, errors.size());
    }

    @Test
    void checkAllIllegalMoves() {
        p.updateMove(wmp1, 1, 2);
        p.updateBuild(wfp1, 4, 4);
        p.updateBuild(wfp1, 4, 4);
        p.updateBuild(wfp1, 4, 4);
        p.updateBuild(wfp1, 4, 4);
        ArrayList<Error> errors = p.checkMove(wmp1, 4, 4);
        assertEquals(true, errors.contains(Error.MOVES_EXCEEDED));
        assertEquals(true, errors.contains(Error.MOVE_AFTER_BUILD));
        assertEquals(true, errors.contains(Error.NOT_ADJACENT));
        assertEquals(true, errors.contains(Error.INVALID_LEVEL));
        assertEquals(true, errors.contains(Error.IS_DOME));
        assertEquals(5, errors.size());
    }


    @Test
    void checkBuild() {
        assertThrows(IllegalArgumentException.class, () -> p.checkBuild(wfp1, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> p.checkBuild(wfp1, 1, 8));
        assertThrows(IllegalArgumentException.class, () -> p.checkBuild(wfp1, 5, 4));
        assertThrows(IllegalArgumentException.class, () -> p.checkBuild(null, 3, 4));

    }

    @Test
    void checkBuildLegal() {
        // Check a legal move
        p.updateMove(wmp1, 2, 1);
        assertEquals(true, p.checkBuild(wmp2, 1, 1).isEmpty());
    }

    @Test
    void checkAllIllegalBuild(){
        p.updateBuild(wmp1, 1, 1);
        p.updateBuild(wfp2, 3, 3);
        p.updateBuild(wfp2, 3, 3);
        p.updateBuild(wfp2, 3, 3);
        p.updateBuild(wfp2, 3, 3);


        ArrayList<Error> errors = p.checkBuild(wmp1, 3, 3);
        assertEquals(true, errors.contains(Error.BUILDS_EXCEEDED));
        assertEquals(true, errors.contains(Error.BUILD_BEFORE_MOVE));
        assertEquals(true, errors.contains(Error.NOT_ADJACENT));
        assertEquals(true, errors.contains(Error.IS_DOME));
        assertEquals(4, errors.size());
    }

    @Test
    void checkBuildNotFree() {
        p.updateMove(wfp2, 3, 3);
        ArrayList<Error> errors = p.checkBuild(wfp2,3,4);
        assertEquals(true, errors.contains(Error.NOT_FREE));
        assertEquals(1, errors.size());
    }

    @Test
    void checkBuildsExceeded(){
        p.updateMove(wmp2,0,2);
        p.updateBuild(wmp2,0,3);
        ArrayList<Error> errors = p.checkBuild(wmp2,1,3);
        assertEquals(true, errors.contains(Error.BUILDS_EXCEEDED));
        assertEquals(1, errors.size());
    }

    @Test
    void checkBuildNotAdjacent(){
        p.updateMove(wfp1,4,4);
        ArrayList<Error> errors = p.checkBuild(wfp1,4,2);
        assertEquals(true, errors.contains(Error.NOT_ADJACENT));
        assertEquals(1, errors.size());
    }

    @Test
    void checkBuildIsDome(){
        p.updateMove(wfp1,3,3);
        b.getSquare(4,4).setDome(true);
        ArrayList<Error> errors = p.checkBuild(wfp1,4,4);
        assertEquals(true, errors.contains(Error.IS_DOME));
        assertEquals(1, errors.size());
    }

    @Test
    void checkBuildBeforeMove(){
        ArrayList<Error> errors = p.checkBuild(wfp1,4,4);
        assertEquals(true, errors.contains(Error.BUILD_BEFORE_MOVE));
        assertEquals(1, errors.size());
    }


    @Test
    void checkWin() {
        assertThrows(IllegalArgumentException.class, () -> p.checkWin(null));

        //creating a ladder to win (we test checkWin with wmp1)
        Square l2, l3;
        l2 = b.getSquare(2, 0);
        l3 = b.getSquare(3, 0);
        //level 2
        for (int i = 0; i < 2; i++)
            l2.buildLevel();
        //level 3
        for (int i = 0; i < 3; i++)
            l3.buildLevel();

        assertEquals(2, l2.getLevel());
        assertEquals(3, l3.getLevel());
        //set wmp1 on level 2 position;
        wmp1.updateWorkerPosition(l2);
        assertEquals(wmp1, l2.getWorker());
        assertEquals(false, p.checkWin(wmp1));
        //move wmp2 on level 3 position;
        wmp1.updateWorkerPosition(l3);
        assertEquals(wmp1, l3.getWorker());
        assertEquals(true, p.checkWin(wmp1));

    }

    @Test
    void updateMove() {
        assertThrows(IllegalArgumentException.class, () -> p.updateMove(wfp1, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> p.updateMove(wfp1, 1, 8));
        assertThrows(IllegalArgumentException.class, () -> p.updateMove(wfp1, 5, 4));
        assertThrows(IllegalArgumentException.class, () -> p.updateMove(null, 3, 4));
        assertThrows(IllegalStateException.class, () -> p.updateMove(wfp1, 3, 4));

        //move wmp1 to 2,1
        p.updateMove(wmp1, 2, 1);
        assertEquals(wmp1, b.getSquare(2, 1).getWorker());
        assertEquals(wmp1.getCurrentSquare(), b.getSquare(2, 1));
        assertEquals(b.getSquare(1, 1), wmp1.getLastSquareMove());
        assertNull(wmp1.getLastSquareMove().getWorker());
        assertEquals(1, b.getNMoves());
        assertEquals(true, b.getSquare(1, 1).isFree());
        //move wmp1 to 2,2
        p.updateMove(wmp1, 2, 2);
        assertEquals(wmp1, b.getSquare(2, 2).getWorker());
        assertEquals(wmp1.getCurrentSquare(), b.getSquare(2, 2));
        assertEquals(b.getSquare(2, 1), wmp1.getLastSquareMove());
        assertNull(wmp1.getLastSquareMove().getWorker());
        assertEquals(2, b.getNMoves());
        assertEquals(true, b.getSquare(2, 1).isFree());
    }

    @Test
    void updateBuild() {
        assertThrows(IllegalArgumentException.class, () -> p.updateBuild(wfp1, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> p.updateBuild(wfp1, 1, 8));
        assertThrows(IllegalArgumentException.class, () -> p.updateBuild(wfp1, 5, 4));
        assertThrows(IllegalArgumentException.class, () -> p.updateMove(null, 3, 4));
        assertThrows(IllegalStateException.class, () -> p.updateMove(wfp1, 3, 4));

        //wmp1 builds on 2,1
        p.updateBuild(wmp1, 2, 1);
        assertEquals(wmp1.getLastSquareBuild(), b.getSquare(2, 1));
        assertEquals(1, b.getSquare(2, 1).getLevel());
        assertEquals(false, b.getSquare(2, 1).getDome());
        assertEquals(1, b.getNBuild());
    }

    @Test
    void checkTurn() {
        assertEquals(true, p.checkTurn(0));
        assertEquals(true, p.checkTurn(1));
        p.updateMove(wmp1, 2, 1);
        assertEquals(false, p.checkTurn(0));
        assertEquals(true, p.checkTurn(1));
        p.updateBuild(wmp1, 3, 1);
        assertEquals(false, p.checkTurn(1));
    }
}