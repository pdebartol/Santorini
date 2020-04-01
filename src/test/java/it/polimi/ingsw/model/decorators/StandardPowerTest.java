package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for StandardPower class
 * @author marcoDige
 */
class StandardPowerTest {

    @BeforeEach
    void setUp() {
        Board b = new Board();
        Player p = new Player("marcoDige", Color.AZURE);
        Worker w1 = p.getWorkers().get(1);
        Worker w2 = p.getWorkers().get(2);

        w1.setWorkerOnBoard(b.getSquare(1,1));
        w2.setWorkerOnBoard(b.getSquare(3,4));
    }

    @Test
    void checkMove() {
    }

    @Test
    void checkBuild() {
    }

    @Test
    void checkWin() {
    }

    @Test
    void updateMove() {
    }

    @Test
    void updateBuild() {
    }

    @Test
    void checkTurn() {
    }
}