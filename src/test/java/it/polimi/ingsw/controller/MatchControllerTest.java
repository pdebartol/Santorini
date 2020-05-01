package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
import it.polimi.ingsw.model.enums.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for MatchController class
 * @author pierobartolo
 */

class MatchControllerTest {
    MatchController controller;

    @BeforeEach
    void setup(){
        controller = new MatchController();
    }

    @Test
    void StandardLogin(){
        threePlayersLogin();
    }

    @Test
    void sameUsernameLogin(){
        ArrayList<Error> errors;
        errors = controller.onNewPlayer("piero", Color.AZURE);
        assertEquals(0, errors.size());

        Player temp_player = controller.playerController.getPlayerByUsername("piero");
        assertEquals("piero",temp_player.getUsername());
        assertEquals(Color.AZURE, temp_player.getWorkers().get(0).getColor());

        // 2nd player login
        errors = controller.onNewPlayer("piero", Color.WHITE);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(Error.LOGIN_USERNAME_NOT_AVAILABLE));

    }

    @Test
    void sameColorLogin(){
        ArrayList<Error> errors;
        errors = controller.onNewPlayer("piero", Color.AZURE);
        assertEquals(0, errors.size());

        Player temp_player = controller.playerController.getPlayerByUsername("piero");
        assertEquals("piero",temp_player.getUsername());
        assertEquals(Color.AZURE, temp_player.getWorkers().get(0).getColor());

        // 2nd player login
        errors = controller.onNewPlayer("marco", Color.AZURE);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(Error.LOGIN_COLOR_NOT_AVAILABLE));
    }

    @Test
    void fourPlayerLogin(){
        ArrayList<Error> errors;
        errors = controller.onNewPlayer("piero", Color.AZURE);
        assertEquals(0, errors.size());

        Player temp_player = controller.playerController.getPlayerByUsername("piero");
        assertEquals("piero",temp_player.getUsername());
        assertEquals(Color.AZURE, temp_player.getWorkers().get(0).getColor());

        // 2nd player login
        errors = controller.onNewPlayer("marco", Color.WHITE);
        assertEquals(0, errors.size());

        temp_player = controller.playerController.getPlayerByUsername("marco");
        assertEquals("marco",temp_player.getUsername());
        assertEquals(Color.WHITE, temp_player.getWorkers().get(0).getColor());

        // 3rd player login
        errors = controller.onNewPlayer("ale", Color.GREY);
        assertEquals(0, errors.size());

        temp_player = controller.playerController.getPlayerByUsername("ale");
        assertEquals("ale",temp_player.getUsername());
        assertEquals(Color.GREY, temp_player.getWorkers().get(0).getColor());

        // 4th player login
        errors = controller.onNewPlayer("gianni", Color.GREY);
        assertEquals(2, errors.size());
        assertTrue(errors.contains(Error.LOGIN_COLOR_NOT_AVAILABLE));
        assertTrue(errors.contains(Error.LOGIN_TOO_MANY_PLAYERS));



    }

    @Test
    void createGods(){
        threePlayersLogin();
        String challenger = controller.onStartGame();
        assertEquals(State.GODS_SETUP,controller.getGameState());
        assertFalse(challenger.isEmpty());
        ArrayList<Error> errors  = controller.onChallengerChooseGods(challenger,new ArrayList<>( Arrays.asList(1,2,3)));
        assertTrue(errors.isEmpty());
        assertEquals("Apollo",controller.selectedGods.get(1).getName());
        assertEquals("Artemis",controller.selectedGods.get(2).getName());
        assertEquals("Athena",controller.selectedGods.get(3).getName());
        assertEquals(State.CHOOSE_GOD,controller.getGameState());

    }

    @Test
    void createGodsWrongId(){
        threePlayersLogin();
        String challenger = controller.onStartGame();
        assertFalse(challenger.isEmpty());
        assertThrows(IllegalArgumentException.class,
                ()->{
                    controller.onChallengerChooseGods(challenger, new ArrayList<>( Arrays.asList(1,2,37)));
                    System.out.print(controller.selectedGods);
                });
    }

    @Test
    void chooseGodNotInYourTurn(){
        threePlayersLogin();
        String challenger = controller.onStartGame();
        controller.onChallengerChooseGods(challenger,new ArrayList<>( Arrays.asList(1,2,3)));
        assertEquals(State.CHOOSE_GOD,controller.getGameState());
        ArrayList<Error> errors = controller.onPlayerChooseGod(challenger,2);
        assertTrue(errors.contains(Error.INGAME_NOT_YOUR_TURN));
        assertEquals(1,errors.size());
        assertEquals(State.CHOOSE_GOD,controller.getGameState());


    }

    @Test
    void chooseGodInYourTurn(){
        ArrayList<Error> errors;
        threePlayersLogin();
        String challenger = controller.onStartGame();
        controller.onChallengerChooseGods(challenger,new ArrayList<>( Arrays.asList(1,2,3)));
        assertEquals(State.CHOOSE_GOD, controller.getGameState());

        switch(challenger){
            case "piero":
                errors = controller.onPlayerChooseGod("marco",2);
                assertTrue(errors.isEmpty());
                assertEquals("Artemis",controller.playerController.getPlayerByUsername("marco").getGod().getName());
                break;
            case "marco":
                errors = controller.onPlayerChooseGod("ale",2);
                assertTrue(errors.isEmpty());
                assertEquals("Artemis",controller.playerController.getPlayerByUsername("ale").getGod().getName());
                break;
            case "ale":
                errors = controller.onPlayerChooseGod("piero",2);
                assertTrue(errors.isEmpty());
                assertEquals("Artemis",controller.playerController.getPlayerByUsername("piero").getGod().getName());
                break;
        }

    }

    @Test
    void chooseStartingPlayer(){
        ArrayList <Error> errors;
        threePlayersLogin();

        String challenger = threePlayersChooseGods();
        assertEquals(State.CHOOSE_STARTER, controller.getGameState());

        switch(challenger){
            case "piero":
                errors = controller.onChallengerChooseStartingPlayer("marco", "marco");
                assertTrue(errors.contains(Error.INGAME_NOT_YOUR_TURN));
                assertTrue(errors.contains(Error.SETUP_IS_NOT_CHALLENGER));
                assertEquals(2,errors.size());
                errors = controller.onChallengerChooseStartingPlayer(challenger, "marco");
                assertTrue(errors.isEmpty());
                assertEquals("marco",controller.playerController.getCurrentPlayer().getUsername());
                assertEquals(controller.playerController.getPlayerIndex("marco"),controller.playerController.getStarterIndex());

                break;

            case "marco":
                errors = controller.onChallengerChooseStartingPlayer("piero", "piero");
                assertTrue(errors.contains(Error.INGAME_NOT_YOUR_TURN));
                assertTrue(errors.contains(Error.SETUP_IS_NOT_CHALLENGER));
                assertEquals(2,errors.size());
                errors = controller.onChallengerChooseStartingPlayer(challenger, "piero");
                assertTrue(errors.isEmpty());
                assertEquals("piero",controller.playerController.getCurrentPlayer().getUsername());
                assertEquals(controller.playerController.getPlayerIndex("piero"),controller.playerController.getStarterIndex());

                break;

            case "ale":
                errors = controller.onChallengerChooseStartingPlayer("marco", "marco");
                assertTrue(errors.contains(Error.INGAME_NOT_YOUR_TURN));
                assertTrue(errors.contains(Error.SETUP_IS_NOT_CHALLENGER));
                assertEquals(2,errors.size());
                errors = controller.onChallengerChooseStartingPlayer(challenger, "piero");
                assertTrue(errors.isEmpty());
                assertEquals("piero",controller.playerController.getCurrentPlayer().getUsername());
                assertEquals(controller.playerController.getPlayerIndex("piero"),controller.playerController.getStarterIndex());

                break;

        }
        assertEquals(State.SET_WORKERS, controller.getGameState());




    }

    @Test
    void setWorkers(){
        threePlayersLogin();
        String challenger = threePlayersChooseGods();
        controller.onChallengerChooseStartingPlayer(challenger, "marco");

        ArrayList<Error> errors = controller.onPlayerSetWorker("piero","female",3,2);
        assertTrue(errors.contains(Error.INGAME_NOT_YOUR_TURN));
        assertEquals(1,errors.size());

        errors = controller.onPlayerSetWorker("marco","male",3,2);
        assertTrue(errors.isEmpty());

        errors = controller.onPlayerSetWorker("marco","male",4,3);
        assertTrue(errors.contains(Error.SETUP_WORKER_ALREADY_SET));
        assertEquals(1,errors.size());

        errors = controller.onPlayerSetWorker("marco","female",3,2);
        assertTrue(errors.contains(Error.SETUP_WORKER_ON_OCCUPIED_SQUARE));
        assertEquals(1,errors.size());

        errors = controller.onPlayerSetWorker("marco","female",4,3);
        assertTrue(errors.isEmpty());

        errors = controller.onPlayerSetWorker("ale","male",1,2);
        assertTrue(errors.isEmpty());
        errors = controller.onPlayerSetWorker("ale","female",1,3);
        assertTrue(errors.isEmpty());

        errors = controller.onPlayerSetWorker("piero","male",3,3);
        assertTrue(errors.isEmpty());
        errors = controller.onPlayerSetWorker("piero","female",4,4);
        assertTrue(errors.isEmpty());

        assertEquals(State.IN_GAME, controller.getGameState());
    }

    @Test
    void StandardMoveAndBuild(){
        threePlayersLogin();
        setupWorkers();
        ArrayList<Error> errors  = controller.onWorkerMove("marco","male",3,3 );
        assertTrue(errors.isEmpty());
        errors = controller.onWorkerBuild("marco","male",3,4,1);
        assertTrue(errors.isEmpty());

    }

    @Test
    void notInYourTurnMoveAndBuild(){
        threePlayersLogin();
        setupWorkers();
        ArrayList<Error> errors  = controller.onWorkerMove("piero","male",3,3 );
        assertTrue(errors.contains(Error.INGAME_NOT_YOUR_TURN));
        errors = controller.onWorkerBuild("piero","male",3,4,1);
        assertTrue(errors.contains(Error.INGAME_NOT_YOUR_TURN));
    }

    @Test
    void  chooseInactiveWorker(){
        threePlayersLogin();
        setupWorkers();
        ArrayList<Error> errors  = controller.onWorkerMove("marco","male",3,1 );
        assertTrue(errors.isEmpty());
        errors = controller.onWorkerBuild("marco","female",3,2,1);
        assertTrue(errors.contains(Error.INGAME_WRONG_WORKER));
    }

    @Test
    void simulatedTurn(){
        threePlayersLogin();
        setupWorkers();

        // Marco's turn
        ArrayList<Error> errors  = controller.onWorkerMove("marco","male",3,1 );
        assertTrue(errors.isEmpty());
        errors = controller.onWorkerBuild("marco","male",3,2,1);
        assertTrue(errors.isEmpty());
        errors = controller.onPlayerEndTurn("marco");
        assertTrue(errors.isEmpty());

        // Ale's turn
        errors  = controller.onWorkerMove("ale","female",1,4);
        assertTrue(errors.isEmpty());
        errors = controller.onWorkerBuild("ale","female",1,3,1);
        assertTrue(errors.isEmpty());
        errors = controller.onPlayerEndTurn("ale");
        assertTrue(errors.isEmpty());

        // Piero's turn
        errors  = controller.onWorkerMove("piero","male",2,3);
        assertTrue(errors.isEmpty());
        errors = controller.onWorkerBuild("piero","male",3,3,1);
        assertTrue(errors.isEmpty());
        errors = controller.onPlayerEndTurn("piero");
        assertTrue(errors.isEmpty());

        assertEquals("marco",controller.playerController.getCurrentPlayer().getUsername());
    }

    // support method for the login
    void threePlayersLogin(){
        ArrayList<Error> errors;
        errors = controller.onNewPlayer("piero", Color.AZURE);
        assertEquals(0, errors.size());

        Player temp_player = controller.playerController.getPlayerByUsername("piero");
        assertEquals("piero",temp_player.getUsername());
        assertEquals(Color.AZURE, temp_player.getWorkers().get(0).getColor());

        // 2nd player login
        errors = controller.onNewPlayer("marco", Color.WHITE);
        assertEquals(0, errors.size());

        temp_player = controller.playerController.getPlayerByUsername("marco");
        assertEquals("marco",temp_player.getUsername());
        assertEquals(Color.WHITE, temp_player.getWorkers().get(0).getColor());

        // 3rd player login
        errors = controller.onNewPlayer("ale", Color.GREY);
        assertEquals(0, errors.size());

        temp_player = controller.playerController.getPlayerByUsername("ale");
        assertEquals("ale",temp_player.getUsername());
        assertEquals(Color.GREY, temp_player.getWorkers().get(0).getColor());



    }

    //support method for the god setup
    String threePlayersChooseGods(){
        ArrayList<Error> errors;
        String challenger = controller.onStartGame();
        controller.onChallengerChooseGods(challenger,new ArrayList<>( Arrays.asList(4,8,3)));

        // choosing gods
        switch(challenger){
            case "piero":
                errors = controller.onPlayerChooseGod("marco",4);
                assertTrue(errors.isEmpty());
                assertEquals("Atlas",controller.playerController.getPlayerByUsername("marco").getGod().getName());

                errors = controller.onPlayerChooseGod("ale",3);
                assertTrue(errors.isEmpty());
                assertEquals("Athena",controller.playerController.getPlayerByUsername("ale").getGod().getName());

                errors = controller.onPlayerChooseGod("piero",8);
                assertTrue(errors.isEmpty());
                assertEquals("Minotaur",controller.playerController.getPlayerByUsername("piero").getGod().getName());
                break;

            case "marco":
                errors = controller.onPlayerChooseGod("ale",4);
                assertTrue(errors.isEmpty());
                assertEquals("Atlas",controller.playerController.getPlayerByUsername("ale").getGod().getName());

                errors = controller.onPlayerChooseGod("piero",3);
                assertTrue(errors.isEmpty());
                assertEquals("Athena",controller.playerController.getPlayerByUsername("piero").getGod().getName());

                errors = controller.onPlayerChooseGod("marco",8);
                assertTrue(errors.isEmpty());
                assertEquals("Minotaur",controller.playerController.getPlayerByUsername("marco").getGod().getName());

                break;
            case "ale":
                errors = controller.onPlayerChooseGod("piero",4);
                assertTrue(errors.isEmpty());
                assertEquals("Atlas",controller.playerController.getPlayerByUsername("piero").getGod().getName());

                errors = controller.onPlayerChooseGod("marco",3);
                assertTrue(errors.isEmpty());
                assertEquals("Athena",controller.playerController.getPlayerByUsername("marco").getGod().getName());

                errors = controller.onPlayerChooseGod("ale",8);
                assertTrue(errors.isEmpty());
                assertEquals("Minotaur",controller.playerController.getPlayerByUsername("ale").getGod().getName());

                break;
        }

        return  challenger;
    }

    // support method for workers setup (marco --> ale --> piero)
    void setupWorkers(){
        String challenger = threePlayersChooseGods();
        controller.onChallengerChooseStartingPlayer(challenger, "marco");

        ArrayList<Error> errors = controller.onPlayerSetWorker("piero","female",3,2);
        assertTrue(errors.contains(Error.INGAME_NOT_YOUR_TURN));
        assertEquals(1,errors.size());

        errors = controller.onPlayerSetWorker("marco","male",3,2);
        assertTrue(errors.isEmpty());

        errors = controller.onPlayerSetWorker("marco","male",4,3);
        assertTrue(errors.contains(Error.SETUP_WORKER_ALREADY_SET));
        assertEquals(1,errors.size());

        errors = controller.onPlayerSetWorker("marco","female",3,2);
        assertTrue(errors.contains(Error.SETUP_WORKER_ON_OCCUPIED_SQUARE));
        assertEquals(1,errors.size());

        errors = controller.onPlayerSetWorker("marco","female",4,3);
        assertTrue(errors.isEmpty());

        errors = controller.onPlayerSetWorker("ale","male",1,2);
        assertTrue(errors.isEmpty());
        errors = controller.onPlayerSetWorker("ale","female",1,3);
        assertTrue(errors.isEmpty());

        errors = controller.onPlayerSetWorker("piero","male",3,3);
        assertTrue(errors.isEmpty());
        errors = controller.onPlayerSetWorker("piero","female",4,4);
        assertTrue(errors.isEmpty());

        assertEquals(State.IN_GAME, controller.getGameState());
    }

}