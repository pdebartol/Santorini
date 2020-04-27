package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;
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
        assertFalse(challenger.isEmpty());
        ArrayList<Error> errors  = controller.onChallengerChooseGods(challenger,new ArrayList<>( Arrays.asList(1,2,3)));
        assertTrue(errors.isEmpty());
        assertEquals("Apollo",controller.selectedGods.get(1).getName());
        assertEquals("Artemis",controller.selectedGods.get(2).getName());
        assertEquals("Athena",controller.selectedGods.get(3).getName());

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
        ArrayList<Error> errors = controller.onPlayerChooseGod(challenger,2);
        assertTrue(errors.contains(Error.INGAME_NOT_YOUR_TURN));
        assertEquals(1,errors.size());

    }

    @Test
    void chooseGodInYourTurn(){
        ArrayList<Error> errors;
        threePlayersLogin();
        String challenger = controller.onStartGame();
        controller.onChallengerChooseGods(challenger,new ArrayList<>( Arrays.asList(1,2,3)));
        switch(challenger){
            case "piero":
                errors = controller.onPlayerChooseGod("marco",2);
                assertTrue(errors.isEmpty());
                assertEquals("Artemis",controller.playerController.getCurrentPlayer().getGod().getName());
                break;
            case "marco":
                errors = controller.onPlayerChooseGod("ale",2);
                assertTrue(errors.isEmpty());
                assertEquals("Artemis",controller.playerController.getCurrentPlayer().getGod().getName());
                break;
            case "ale":
                errors = controller.onPlayerChooseGod("piero",2);
                assertTrue(errors.isEmpty());
                assertEquals("Artemis",controller.playerController.getCurrentPlayer().getGod().getName());
                break;
        }


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

}