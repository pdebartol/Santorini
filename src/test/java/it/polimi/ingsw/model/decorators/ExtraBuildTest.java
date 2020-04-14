package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Error;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

 /**
 * Test suite for ExtraBuild
 * @author pierobartolo
 */

class ExtraBuildTest {
    Board b;
    Player p1, p2, p3, p4;

     /**
      * Setup for testing :
      *  - 3 players
      *  - p1 --> Demeter
      *  - p2 --> Hephaestus
      *  - p3 --> Hestia
      *  - p4 --> Athena
      */

     @BeforeEach
     public void setUp(){
         //Object creation
         b = new Board();
         GodsFactory factory = new GodsFactory(b);
         p1 = new Player("pierobartolo", Color.WHITE);
         p2 = new Player("marcodige",Color.GREY);
         p3 = new Player("aledimaio",Color.AZURE);
         p4 = new Player("athenaTester",Color.WHITE);
         ArrayList<God> gods = factory.getGods( new ArrayList<>(Arrays.asList(5,6,21,3)));
         p1.setGod(Objects.requireNonNull(gods.get(0))); // Demeter
         p2.setGod(Objects.requireNonNull(gods.get(1))); // Hephaestus
         p3.setGod(Objects.requireNonNull(gods.get(2))); // Hestia
         p4.setGod(Objects.requireNonNull(gods.get(3))); // Athena

         // Athena setup
         p4.getWorkers().get(0).setWorkerOnBoard(b.getSquare(4,4));
         p4.getWorkers().get(1).setWorkerOnBoard(b.getSquare(4,3));
     }

     /**
      * This method tests Demeter's power with Athena present on board.
      * Demeter's power: Your Worker may build one additional time, but not on the same space.
      */

     @Test
     public void DemeterAndAthena(){

         // Worker SetUp
         p1.getWorkers().get(0).setWorkerOnBoard(b.getSquare(0,0));
         p1.getWorkers().get(1).setWorkerOnBoard(b.getSquare(1,0));

         //Activate Athena's Power
         //chose worker for the turn
         p4.getWorkers().get(1).IsMovingOn();

         b.getSquare(3,4).buildLevel(1);
         assertTrue(p4.move(p4.getWorkers().get(1), 3, 4).isEmpty());
         b.incNBuild();
         p4.endTurn();

         // Check Demeter cannot move up because of Athena's power
         b.getSquare(0,1).buildLevel(1);
         //chose worker for the turn
         p1.getWorkers().get(0).IsMovingOn();
         List<Error> temp_errors = p1.move(p1.getWorkers().get(0),0,1);
         assertTrue(temp_errors.contains(Error.BLOCK_MOVE_UP));
         assertEquals(1,temp_errors.size());

         // Check Demeter first move and build
         assertTrue(p1.move(p1.getWorkers().get(0), 1, 1).isEmpty());
         assertTrue(p1.build(p1.getWorkers().get(0), 0, 0, 1).isEmpty());

         // Check Demeter can't build again on the same space
         temp_errors = p1.build(p1.getWorkers().get(0),0,0,2);
         assertTrue(temp_errors.contains(Error.EXTRA_BUILD_NOT_SAME_SPACE));
         assertEquals(1,temp_errors.size());

         // Check Demeter can build again
         assertTrue(p1.build(p1.getWorkers().get(0), 2, 1, 1).isEmpty());

         // Check Demeter can't build three times
         temp_errors = p1.build(p1.getWorkers().get(0),1,2,1);
         assertTrue(temp_errors.contains(Error.BUILDS_EXCEEDED));
         assertEquals(1,temp_errors.size());
     }

     /**
      * This method tests Hephaestus' power with Athena present on board.
      * Hephaestus' power: Your Worker may build one additional block (not dome) on top of your first block.
      */

     @Test
     public void HephaestusAndAthena(){
         // Worker SetUp
         p2.getWorkers().get(0).setWorkerOnBoard(b.getSquare(0,0));
         p2.getWorkers().get(1).setWorkerOnBoard(b.getSquare(1,0));

         //Activate Athena's Power
         b.getSquare(3,4).buildLevel(1);
         //chose worker for the turn
         p4.getWorkers().get(1).IsMovingOn();
         assertTrue(p4.move(p4.getWorkers().get(1), 3, 4).isEmpty());
         b.incNBuild();
         p4.endTurn();

         // Check Demeter cannot move up because of Athena's power
         b.getSquare(0,1).buildLevel(1);
         //chose worker for the turn
         p2.getWorkers().get(0).IsMovingOn();
         List<Error> temp_errors = p2.move(p2.getWorkers().get(0),0,1);
         assertTrue(temp_errors.contains(Error.BLOCK_MOVE_UP));
         assertEquals(1,temp_errors.size());

         // Check Hephaestus first move and build
         assertTrue(p2.move(p2.getWorkers().get(0), 1, 1).isEmpty());
         assertTrue(p2.build(p2.getWorkers().get(0), 0, 0, 1).isEmpty());

         // Check Hephaestus can't build again on a different square
         temp_errors =  p2.build(p2.getWorkers().get(0),2,1,1);
         assertTrue(temp_errors.contains(Error.EXTRA_BUILD_ONLY_SAME_SPACE));
         assertEquals(1,temp_errors.size());

         // Check Hephaestus can build again
         assertTrue(p2.build(p2.getWorkers().get(0), 0, 0, 2).isEmpty());

         // Check Hephaestus can't build three times
         temp_errors = p2.build(p2.getWorkers().get(0),1,2,1);
         assertTrue(temp_errors.contains(Error.BUILDS_EXCEEDED));
         assertEquals(1,temp_errors.size());

         p2.endTurn();

         // Deactivate Athena's power
         //chose worker for the turn
         p4.getWorkers().get(1).IsMovingOn();
         assertTrue(p4.move(p4.getWorkers().get(1), 3, 3).isEmpty());
         b.incNBuild();
         p4.endTurn();

         // Check Hephaestus can't ExtraBuild a dome
         //chose worker for the turn
         p2.getWorkers().get(0).IsMovingOn();
         assertTrue(p2.move(p2.getWorkers().get(0), 0, 1).isEmpty());
         assertTrue(p2.build(p2.getWorkers().get(0), 0, 0, 3).isEmpty());
         temp_errors = p2.build(p2.getWorkers().get(0),0,0,4);
         assertTrue(temp_errors.contains(Error.EXTRA_BUILD_NOT_DOME));
         assertEquals(1,temp_errors.size());
     }

     /**
      * This method tests Hestia's power with Athena present on board.
      * Hestia's power: : Your Worker may build one additional time, but this cannot be on a perimeter space.
      */

     @Test
     public void HestiaAndAthena(){
         // Worker SetUp
         p3.getWorkers().get(0).setWorkerOnBoard(b.getSquare(0,0));
         p3.getWorkers().get(1).setWorkerOnBoard(b.getSquare(1,0));

         //Activate Athena's Power
         b.getSquare(3,4).buildLevel(1);
         //chose worker for the turn
         p4.getWorkers().get(1).IsMovingOn();
         assertTrue(p4.move(p4.getWorkers().get(1), 3, 4).isEmpty());
         b.incNBuild();
         p4.endTurn();

         // Check Hestia cannot move up because of Athena's power
         b.getSquare(0,1).buildLevel(1);
         //chose worker for the turn
         p3.getWorkers().get(0).IsMovingOn();
         List<Error> temp_errors = p3.move(p3.getWorkers().get(0),0,1);
         assertTrue(temp_errors.contains(Error.BLOCK_MOVE_UP));
         assertEquals(1,temp_errors.size());

         // Check Hestia first move and build
         assertTrue(p3.move(p3.getWorkers().get(0), 1, 1).isEmpty());
         assertTrue(p3.build(p3.getWorkers().get(0), 0, 0, 1).isEmpty());

         // Check Hestia can't build again on a perimeter space
         temp_errors = p3.build(p3.getWorkers().get(0),0,0,2);
         assertTrue(temp_errors.contains(Error.EXTRA_BUILD_NOT_PERIMETER));
         assertEquals(1,temp_errors.size());

         // Check Hestia can build again
         assertTrue(p3.build(p3.getWorkers().get(0), 2, 1, 1).isEmpty());

         // Check Hestia can't build three times
         temp_errors = p3.build(p3.getWorkers().get(0),1,2,1);
         assertTrue(temp_errors.contains(Error.BUILDS_EXCEEDED));
         assertEquals(1,temp_errors.size());
     }


}