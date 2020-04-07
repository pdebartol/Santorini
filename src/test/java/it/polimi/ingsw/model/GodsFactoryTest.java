package it.polimi.ingsw.model;

import it.polimi.ingsw.model.decorators.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for GodsFactory
 * @author pierobartolo
 */
class GodsFactoryTest {
    Board gameBoard;
    ArrayList<Integer> god_ids;
    GodsFactory factory;

    @BeforeEach
    public void setUp() {
        this.gameBoard = new Board();
        this.god_ids = new ArrayList<>();
        this.factory = new GodsFactory(gameBoard);
    }


    @Test
    public void CheckIllegalArguments(){
        assertThrows(IllegalArgumentException.class, () -> factory.getGods(null));
        assertThrows(IllegalArgumentException.class, () -> factory.getGods(god_ids));

    }

    @Test
    public void Apollo_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(1);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Apollo = Objects.requireNonNull(gods.get(2));

        // Check Apollo
        assertEquals("Apollo", Apollo.getName());
        assertEquals("Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated.", Apollo.getDescription());
        assertEquals(NoWinPerimeter.class, Apollo.getPower().getClass());
        assertEquals(CanMoveUp.class, ((PowerDecorator) Apollo.getPower()).decoratedPower.getClass()) ;
        assertEquals( MoveSwap.class, ((PowerDecorator) ((PowerDecorator) Apollo.getPower()).decoratedPower).decoratedPower.getClass());
        assertEquals( 1, ((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Apollo.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves());
        assertEquals( 1, ((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Apollo.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild());

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);

    }

    @Test
    public void Artemis_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(2);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Artemis = Objects.requireNonNull(gods.get(2));

        // Check Artemis
        assertEquals("Artemis", Artemis.getName());
        assertEquals("Your Worker may move one additional time, but not back to its initial space.", Artemis.getDescription());
        assertEquals(NoWinPerimeter.class, Artemis.getPower().getClass());
        assertEquals(CanMoveUp.class, ((PowerDecorator) Artemis.getPower()).decoratedPower.getClass());
        assertEquals(ExtraMove.class, ((PowerDecorator) ((PowerDecorator) Artemis.getPower()).decoratedPower).decoratedPower.getClass());
        assertEquals(true, ((ExtraMove) (((PowerDecorator) ((PowerDecorator) Artemis.getPower()).decoratedPower).decoratedPower)).isNotMoveBack());
        assertEquals(false, ((ExtraMove) (((PowerDecorator) ((PowerDecorator) Artemis.getPower()).decoratedPower).decoratedPower)).isOnPerimeter());
        assertEquals(((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Artemis.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves(), 2);
        assertEquals(((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Artemis.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild(), 1);

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);

    }

    @Test
    public void Atlas_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(4);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Atlas = Objects.requireNonNull(gods.get(2));

        // Check Atlas
        assertEquals("Atlas", Atlas.getName());
        assertEquals("Your Worker may build a dome at any level including the ground.", Atlas.getDescription());
        assertEquals(NoWinPerimeter.class, Atlas.getPower().getClass());
        assertEquals(CanMoveUp.class, ((PowerDecorator) Atlas.getPower()).decoratedPower.getClass());
        assertEquals( BuildDomeEverywhere.class, ((PowerDecorator) ((PowerDecorator) Atlas.getPower()).decoratedPower).decoratedPower.getClass());
        assertEquals(1,((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Atlas.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves());
        assertEquals(1,((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Atlas.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild());

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);

    }

    @Test
    public void Demeter_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(5);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Demeter = Objects.requireNonNull(gods.get(2));

        // Check Demeter
        assertEquals("Demeter", Demeter.getName());
        assertEquals("Your Worker may build one additional time, but not on the same space.", Demeter.getDescription());
        assertEquals(NoWinPerimeter.class, Demeter.getPower().getClass());
        assertEquals(CanMoveUp.class, ((PowerDecorator) Demeter.getPower()).decoratedPower.getClass());
        assertEquals(ExtraBuild.class, ((PowerDecorator) ((PowerDecorator) Demeter.getPower()).decoratedPower).decoratedPower.getClass());
        assertEquals(false, ((ExtraBuild) (((PowerDecorator) ((PowerDecorator) Demeter.getPower()).decoratedPower).decoratedPower)).isNotPerimeter());
        assertEquals(true, ((ExtraBuild) (((PowerDecorator) ((PowerDecorator) Demeter.getPower()).decoratedPower).decoratedPower)).isNotSameSpace());
        assertEquals(false, ((ExtraBuild) (((PowerDecorator) ((PowerDecorator) Demeter.getPower()).decoratedPower).decoratedPower)).isOnlySameSpace());
        assertEquals(1,((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Demeter.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves());
        assertEquals(2,((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Demeter.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild());

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);

    }

    @Test
    public void Hephaestus_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(6);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Hephaestus = Objects.requireNonNull(gods.get(2));

        // Check Hephaestus
        assertEquals("Hephaestus", Hephaestus.getName());
        assertEquals("Your Worker may build one additional block (not dome) on top of your first block.", Hephaestus.getDescription());
        assertEquals(NoWinPerimeter.class, Hephaestus.getPower().getClass());
        assertEquals(CanMoveUp.class, ((PowerDecorator) Hephaestus.getPower()).decoratedPower.getClass());
        assertEquals( ExtraBuild.class, ((PowerDecorator) ((PowerDecorator) Hephaestus.getPower()).decoratedPower).decoratedPower.getClass());
        assertEquals(false, ((ExtraBuild) (((PowerDecorator) ((PowerDecorator) Hephaestus.getPower()).decoratedPower).decoratedPower)).isNotPerimeter());
        assertEquals(false, ((ExtraBuild) (((PowerDecorator) ((PowerDecorator) Hephaestus.getPower()).decoratedPower).decoratedPower)).isNotSameSpace());
        assertEquals(true, ((ExtraBuild) (((PowerDecorator) ((PowerDecorator) Hephaestus.getPower()).decoratedPower).decoratedPower)).isOnlySameSpace());
        assertEquals(1, ((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Hephaestus.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves());
        assertEquals(2, ((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Hephaestus.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild());

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);

    }

    @Test
    public void Minotaur_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(8);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Minotaur = Objects.requireNonNull(gods.get(2));

        // Check Minotaur
        assertEquals("Minotaur", Minotaur.getName());
        assertEquals("Your Worker may move into an opponent Worker's space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.", Minotaur.getDescription());
        assertEquals(NoWinPerimeter.class, Minotaur.getPower().getClass());
        assertEquals(CanMoveUp.class, ((PowerDecorator) Minotaur.getPower()).decoratedPower.getClass());
        assertEquals( MovePush.class, ((PowerDecorator) ((PowerDecorator) Minotaur.getPower()).decoratedPower).decoratedPower.getClass());
        assertEquals(1, ((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Minotaur.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves());
        assertEquals(1, ((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Minotaur.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild());

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);

    }

    @Test
    public void Pan_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(9);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Pan = Objects.requireNonNull(gods.get(2));

        // Check Pan
        assertEquals("Pan", Pan.getName());
        assertEquals("You also win if your Worker moves down two or more levels.", Pan.getDescription());
        assertEquals(Pan.getPower().getClass(), NoWinPerimeter.class);
        assertEquals(((PowerDecorator) Pan.getPower()).decoratedPower.getClass(), CanMoveUp.class);
        assertEquals(((PowerDecorator) ((PowerDecorator) Pan.getPower()).decoratedPower).decoratedPower.getClass(), Down2Win.class);
        assertEquals(((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Pan.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves(), 1);
        assertEquals(((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Pan.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild(), 1);

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);
    }

    @Test
    public void Prometheus_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(10);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Prometheus = Objects.requireNonNull(gods.get(2));

        // Check Prometheus
        assertEquals("Prometheus", Prometheus.getName());
        assertEquals("If your Worker does not move up, it may build both before and after moving.", Prometheus.getDescription());
        assertEquals(NoWinPerimeter.class, Prometheus.getPower().getClass());
        assertEquals( CanMoveUp.class, ((PowerDecorator) Prometheus.getPower()).decoratedPower.getClass());
        assertEquals(BuildBeforeMove.class, ((PowerDecorator) ((PowerDecorator) Prometheus.getPower()).decoratedPower).decoratedPower.getClass());
        assertEquals(1, ((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Prometheus.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves());
        assertEquals(2, ((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Prometheus.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild());

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);
    }

    @Test
    public void Hestia_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(21);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Hestia = Objects.requireNonNull(gods.get(2));

        // Check Hestia
        assertEquals("Hestia", Hestia.getName());
        assertEquals("Your Worker may build one additional time, but this cannot be on a perimeter space.", Hestia.getDescription());
        assertEquals(NoWinPerimeter.class, Hestia.getPower().getClass());
        assertEquals(CanMoveUp.class, ((PowerDecorator) Hestia.getPower()).decoratedPower.getClass());
        assertEquals( ExtraBuild.class, ((PowerDecorator) ((PowerDecorator) Hestia.getPower()).decoratedPower).decoratedPower.getClass());
        assertEquals(true, ((ExtraBuild) (((PowerDecorator) ((PowerDecorator) Hestia.getPower()).decoratedPower).decoratedPower)).isNotPerimeter());
        assertEquals(false, ((ExtraBuild) (((PowerDecorator) ((PowerDecorator) Hestia.getPower()).decoratedPower).decoratedPower)).isNotSameSpace());
        assertEquals(false, ((ExtraBuild) (((PowerDecorator) ((PowerDecorator) Hestia.getPower()).decoratedPower).decoratedPower)).isOnlySameSpace());
        assertEquals(1,((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Hestia.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves());
        assertEquals(2,((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Hestia.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild());

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);

    }

    @Test
    public void Medusa_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(24);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Medusa = Objects.requireNonNull(gods.get(2));

        // Check Medusa
        assertEquals("Medusa", Medusa.getName());
        assertEquals("If possible, your Workers build in lower neighboring spaces that are occupied by opponent Workers, removing the opponent Workers from the game.", Medusa.getDescription());
        assertEquals(Medusa.getPower().getClass(), NoWinPerimeter.class);
        assertEquals(((PowerDecorator) Medusa.getPower()).decoratedPower.getClass(), CanMoveUp.class);
        assertEquals(((PowerDecorator) ((PowerDecorator) Medusa.getPower()).decoratedPower).decoratedPower.getClass(), EndRemoveNeighbour.class);
        assertEquals(((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Medusa.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves(), 1);
        assertEquals(((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Medusa.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild(), 1);

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);

    }

    @Test
    public void Triton_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(29);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Triton = Objects.requireNonNull(gods.get(2));

        // Check Triton
        assertEquals("Triton", Triton.getName());
        assertEquals("Each time your Worker moves into a perimeter space, it may immediately move again.", Triton.getDescription());
        assertEquals(NoWinPerimeter.class,Triton.getPower().getClass());
        assertEquals(CanMoveUp.class, ((PowerDecorator) Triton.getPower()).decoratedPower.getClass());
        assertEquals(ExtraMove.class, ((PowerDecorator) ((PowerDecorator) Triton.getPower()).decoratedPower).decoratedPower.getClass());
        assertEquals(false, ((ExtraMove) (((PowerDecorator) ((PowerDecorator) Triton.getPower()).decoratedPower).decoratedPower)).isNotMoveBack());
        assertEquals(true, ((ExtraMove) (((PowerDecorator) ((PowerDecorator) Triton.getPower()).decoratedPower).decoratedPower)).isOnPerimeter());
        assertEquals(1,((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Triton.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves());
        assertEquals(1,((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Triton.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild());

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);

    }

    @Test
    public void Zeus_Athena_Hera() {
        god_ids.add(3);
        god_ids.add(20);
        god_ids.add(30);
        ArrayList<God> gods = factory.getGods(god_ids);
        God Athena = Objects.requireNonNull(gods.get(0));
        God Hera = Objects.requireNonNull(gods.get(1));
        God Zeus = Objects.requireNonNull(gods.get(2));

        // Check Zeus
        assertEquals("Zeus", Zeus.getName());
        assertEquals("Your Worker may build a block under itself.", Zeus.getDescription());
        assertEquals( NoWinPerimeter.class, Zeus.getPower().getClass());
        assertEquals(CanMoveUp.class, ((PowerDecorator) Zeus.getPower()).decoratedPower.getClass());
        assertEquals( BuildUnderfoot.class, ((PowerDecorator) ((PowerDecorator) Zeus.getPower()).decoratedPower).decoratedPower.getClass());
        assertEquals(1,((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Zeus.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxMoves());
        assertEquals(1,((StandardPower) (((PowerDecorator) (((PowerDecorator) ((PowerDecorator) Zeus.getPower()).decoratedPower).decoratedPower)).decoratedPower)).getMaxBuild());

        // Check Athena and Hera
        checkAthenaAndHera(Athena,Hera);

    }




    public void checkAthenaAndHera(God Athena, God Hera) {

        assertEquals("Athena", Athena.getName());
        assertEquals("If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn.", Athena.getDescription());
        assertEquals( NoWinPerimeter.class, Athena.getPower().getClass());
        assertEquals(BlockMoveUp.class, ((PowerDecorator) Athena.getPower()).decoratedPower.getClass());
        assertEquals(StandardPower.class, ((PowerDecorator) ((PowerDecorator) Athena.getPower()).decoratedPower).decoratedPower.getClass());
        assertEquals(1, ((StandardPower) ((PowerDecorator) ((PowerDecorator) Athena.getPower()).decoratedPower).decoratedPower).getMaxMoves());
        assertEquals(1, ((StandardPower) ((PowerDecorator) ((PowerDecorator) Athena.getPower()).decoratedPower).decoratedPower).getMaxBuild());

        assertEquals("Hera", Hera.getName());
        assertEquals("An opponent cannot win by moving into a perimeter space.", Hera.getDescription());
        assertEquals(CanMoveUp.class, Hera.getPower().getClass());
        assertEquals(StandardPower.class, ((PowerDecorator) Hera.getPower()).decoratedPower.getClass());
        assertEquals(1,((StandardPower) (((PowerDecorator) Hera.getPower()).decoratedPower)).getMaxMoves());
        assertEquals(1, ((StandardPower) (((PowerDecorator) Hera.getPower()).decoratedPower)).getMaxBuild());
    }



}