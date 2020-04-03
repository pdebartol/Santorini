package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CanMoveUpTest {
    Board b;
    Player p1, p2, p3;

    @BeforeEach
    public void blockMoveUp(){
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

        // Athena moves up
        b.getSquare(1,0).buildLevel();
        b.resetCounters();
        p1.move(p1.getWorkers().get(0),1,0);

    }

    @Test
    public void checkCanMoveUp(){
        // Artemis check
        b.resetCounters();
        b.getSquare(3,4).buildLevel();
        assertEquals(false,p2.move(p2.getWorkers().get(0),3,4));
        assertEquals(true,p2.move(p2.getWorkers().get(0),4,3));

        // Apollo check
        b.resetCounters();
        b.getSquare(2,1).buildLevel();
        assertEquals(false,p3.move(p3.getWorkers().get(0),2,1));
        assertEquals(true,p3.move(p3.getWorkers().get(0),1,2));

    }

}