package it.polimi.ingsw.model;

import it.polimi.ingsw.model.decorators.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for GodsFactory
 * @author pierobartolo
 */
class GodsFactoryTest {
    Board gameBoard;
    ArrayList<Integer> god_ids;
    GodsFactory factory;

    @BeforeEach
    public void setUp(){
         this.gameBoard = new Board();
        this.god_ids = new ArrayList<>();
        this.factory = new GodsFactory(gameBoard);
    }




    @Test
    public void checkAllNamesAndDescription(){
        god_ids.add(1);
        god_ids.add(2);
        god_ids.add(3);
        ArrayList<God> gods = factory.getGods(god_ids);
        //TODO Check all god names

    }
    @Test
    public void Apollo_Artemis_Athena(){
        god_ids.add(1);
        god_ids.add(2);
        god_ids.add(3);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Apollo = Objects.requireNonNull(gods.get(0));
        God Artemis = Objects.requireNonNull(gods.get(1));
        God Athena = Objects.requireNonNull(gods.get(2));
        // Check Apollo's Power
        assertEquals(Apollo.getPower().getClass(),CanMoveUp.class);
        assertEquals(((PowerDecorator) Apollo.getPower()).decoratedPower.getClass(), MoveSwap.class);
        // Check Artemis Power
        assertEquals(Artemis.getPower().getClass(),CanMoveUp.class);
        assertEquals(((PowerDecorator) Artemis.getPower()).decoratedPower.getClass(), ExtraMove.class);
        // Check Athena Power
        assertEquals(Athena.getPower().getClass(), BlockMoveUp.class);
    }

    public void checkApollo(God god){
        assertEquals( "Apollo", god.getName());
        assertEquals("Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated.", god.getDescription());
    }

    public void checkAthena(God god){
        assertEquals( "Athena", god.getName());
        assertEquals("If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn.", god.getDescription());
    }

    public void checkArtemis(God god){
        assertEquals( "Artemis", god.getName());
        assertEquals("Your Worker may move one additional time, but not back to its initial space.", god.getDescription());
    }

}