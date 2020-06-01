package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.client.InputValidator;
import it.polimi.ingsw.view.client.View;
import it.polimi.ingsw.view.client.cli.graphicComponents.Box;
import it.polimi.ingsw.view.client.cli.graphicComponents.ColorCode;
import it.polimi.ingsw.view.client.cli.graphicComponents.Escapes;
import it.polimi.ingsw.view.client.cli.graphicComponents.Unicode;
import it.polimi.ingsw.view.client.viewComponents.Board;
import it.polimi.ingsw.view.client.viewComponents.God;
import it.polimi.ingsw.view.client.viewComponents.Player;
import it.polimi.ingsw.view.client.viewComponents.Square;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class represents the game flow
 *
 * @author aledimaio
 */

public class Cli extends View {

    ExecutorService inputExecutor;
    Future inputThread;
    private String state;
    private boolean disconnected;
    private God godToVisualize;

    public Cli() {
        super();
        disconnected = false;
        inputExecutor = Executors.newSingleThreadExecutor();
        cliSetup();
    }

    /**
     * This method setup the terminal for the cli
     */

    public void cliSetup() {
        state = "SETUP";
        System.out.print(Escapes.CLEAR_ENTIRE_SCREEN.escape());
        printStartTemplate();
        gameSetup();
    }

    /**
     * This method represents the game setup
     */

    public void gameSetup() {
        printInStartTextBox("Press enter button to start");
        input();

        //Connection setup
        setMyIp();
        setMyPort();

        //start connection
        start();
    }

    //View Override methods

    // TODO : javadoc

    @Override
    public void setMyIp() {
        printInStartTextBox("Insert the server IP address!");
        String ip = input();
        while (!InputValidator.validateIP(ip)) {
            printInStartTextBox("Invalid IP address! Please, try again!");
            ip = input();
        }

        myIp = ip;
    }

    // TODO : javadoc

    @Override
    public void setMyPort() {
        printInStartTextBox("Insert the server port!");
        String port = input();
        while (!InputValidator.validatePORT(port)) {
            printInStartTextBox("Invalid port! Please, try again.");
            port = input();
        }

        myPort = Integer.parseInt(port);
    }

    //TODO : javadoc

    @Override
    public void setUsername(boolean rejectedBefore) {
        disconnected = false;

        String output = "";
        if(rejectedBefore)
            output = "Username already used! ";

        output += "Insert your username (must be at least 3 characters long and no more than 10, valid characters: A-Z, a-z, 1-9, _)";
        printInStartTextBox(output);
        inputThread = inputExecutor.submit(() -> {
            String username = input();
            while (!InputValidator.validateUSERNAME(username) && !Thread.interrupted()) {
                printInStartTextBox("Invalid username! It must be at least 3 characters long and no more than 10, valid characters: A-Z, a-z, 1-9, _, try again!");
                username = input();
            }
            sendLoginRequest(username);
        });
    }

    //TODO : javadoc

    @Override
    public void startMatch() {
        if (players.size() == 1) {
            appendInStartTextBox("You are currently 2 players in the game, enter \"s\" to start the game now or \"w\" to wait for a third player!");
            inputThread = inputExecutor.submit(() -> {
                String input;
                do {
                    input = input();
                } while (!input.equals("s") && !input.equals("w") && !Thread.interrupted());

                if (input.equals("s")) sendStartGameRequest();
                if (input.equals("w")) printInStartTextBox("Wait for a third player...");
            });
        } else {
            appendInStartTextBox("You are currently 3 players in the game, press enter to start the game now! The match will automatically start in 2 minutes");
            inputThread = inputExecutor.submit(() ->{
                inputWithTimeoutStartMatch();
                if(!Thread.interrupted()) sendStartGameRequest();
            });
        }

    }

    //TODO : javadoc

    @Override
    public void selectGods() {
        inputThread = inputExecutor.submit(() -> {
            ArrayList<Integer> godsId = new ArrayList<>();
            int i = 0;

            printInGameTextBox("You are the challenger! Now you have to chose " + (players.size() + 1) + " Gods for this match! Wait...");
            try {
                Thread.sleep(4500);
            } catch (InterruptedException e) {
                return;
            }
            printInGameTextBox("The list of Santorini Gods will be shown, write \"t\" to select the God shown, \"n\" to go to next God's card, \"p\" to go to previously God's card" +
                    ". Press enter to continue...");
            inputWithTimeout();

            if(!Thread.interrupted()) {
                printInGameTextBox(gods.get(0).getId() + " " + gods.get(0).getName());
                appendInGameTextBox(gods.get(0).getDescription());

                while (godsId.size() < (players.size() + 1)) {

                    switch (inputWithTimeout()) {

                        case "n":
                            if (i < gods.size() - 1) i++;
                            else i = 0;
                            printInGameTextBox(gods.get(i).getId() + " " + gods.get(i).getName());
                            appendInGameTextBox(gods.get(i).getDescription());
                            break;

                        case "p":
                            if (i > 0) i--;
                            else i = gods.size() - 1;
                            printInGameTextBox(gods.get(i).getId() + " " + gods.get(i).getName());
                            appendInGameTextBox(gods.get(i).getDescription());
                            break;

                        case "t":
                            if (!godsId.contains(gods.get(i).getId())) {
                                godsId.add(gods.get(i).getId());

                                if ((players.size() + 1 - godsId.size()) > 0) {
                                    printInGameTextBox("You have to choose " + (players.size() + 1 - godsId.size()) + " more gods. Press enter to continue...");
                                    inputWithTimeout();
                                    printInGameTextBox(gods.get(i).getId() + " " + gods.get(i).getName());
                                    appendInGameTextBox(gods.get(i).getDescription());
                                } else {
                                    printInGameTextBox("Loading...");
                                }
                            } else {
                                printInGameTextBox("This god has already been chosen! Press enter to continue...");
                                inputWithTimeout();
                                printInGameTextBox(gods.get(i).getId() + " " + gods.get(i).getName());
                                appendInGameTextBox(gods.get(i).getDescription());
                            }
                            break;

                        case "timeoutExpired":
                            clientHandler.disconnectionForTimeout();

                    }

                    if (Thread.interrupted()) return;
                }
            }

            if (!Thread.interrupted()) sendCreateGodsRequest(godsId);
        });
    }

    //TODO : javadoc

    @Override
    public void selectGod(List<Integer> ids) {
        if(ids.size() == 1) {
            appendInGameTextBox("The last god left will be your god for this game.");
            sendChooseGodRequest(ids.get(0));
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            inputThread = inputExecutor.submit(() -> {
                appendInGameTextBox("It's your turn to choose! To discover the gods you can choose press enter ...");
                inputWithTimeout();

                if(!Thread.interrupted()) {
                    printInGameTextBox("Write \"t\" to select the God shown, \"n\" to go to next God's card, \"p\" to go to previously God's card" +
                            ". Press enter to continue...");
                    inputWithTimeout();
                }

                int godId = 0, i = 0;

                if(!Thread.interrupted()) {
                    printInGameTextBox(getGodById(ids.get(0)).getId() + " " + getGodById(ids.get(0)).getName());
                    appendInGameTextBox(getGodById(ids.get(0)).getDescription());

                    while (godId == 0 && !Thread.interrupted()) {

                        switch (inputWithTimeout()) {

                            case "n":
                                if (i < ids.size() - 1) i++;
                                else i = 0;
                                printInGameTextBox(getGodById(ids.get(i)).getId() + " " + getGodById(ids.get(i)).getName());
                                appendInGameTextBox(getGodById(ids.get(i)).getDescription());
                                break;

                            case "p":
                                if (i > 0) i--;
                                else i = ids.size() - 1;
                                printInGameTextBox(getGodById(ids.get(i)).getId() + " " + getGodById(ids.get(i)).getName());
                                appendInGameTextBox(getGodById(ids.get(i)).getDescription());
                                break;

                            case "t":
                                godId = getGodById(ids.get(i)).getId();
                                printInGameTextBox("Loading...");
                                break;
                        }
                        if (Thread.interrupted()) return;
                    }
                }

                if (!Thread.interrupted()) sendChooseGodRequest(godId);
            });
        }
    }

    //TODO : javadoc

    @Override
    public void selectStartingPlayer() {
        inputThread = inputExecutor.submit(() -> {
            printInGameTextBox("As a challenger you can choose who will start the game (yourself too), write starter name...");
            String starter = inputWithTimeout();
            while(getPlayerByUsername(starter) == null && !starter.equals(myPlayer.getUsername()) && !Thread.interrupted()) {
                printInGameTextBox("Insert an existing username...");
                starter = inputWithTimeout();
            }

            if(!Thread.interrupted()){
                printInGameTextBox("Loading...");
                sendChooseStartingPlayerRequest(starter);
            }
        });
    }

    //TODO : javadoc

    @Override
    public void setWorkerOnBoard(String gender, boolean rejectedBefore) {
        inputThread = inputExecutor.submit(() -> {
            if(rejectedBefore) printInGameTextBox("The position is occupied! Insert enter the coordinates of a free place (type #,#)...");
            else printInGameTextBox("Enter the coordinates where you want to place your " + gender + " worker (type #,#)...");
            String input = inputWithTimeout();
            while(!InputValidator.validateCOORDINATES(input) && !Thread.interrupted()){
                printInGameTextBox("Invalid coordinates! Please try again (type #,#)...");
                input = inputWithTimeout();
            }

            if(!Thread.interrupted()){
                int[] coordinates = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
                sendSetWorkerOnBoardRequest(gender,coordinates[0],coordinates[1]);
            }
        });
    }

    //TODO : javadoc

    @Override
    public void turn(String firstOperation) {
        printInGameTextBox("It’s time to play your turn! Wait...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nextOperation(firstOperation);
    }

    //TODO : javadoc

    @Override
    public void move() {
        abortInputProcessing();
        moveAfterChose();
    }

    //TODO : javadoc

    @Override
    public void build() {
        abortInputProcessing();
        buildAfterChose();
    }

    //TODO : javadoc

    @Override
    public void moveOrBuild() {
        abortInputProcessing();
        inputThread = inputExecutor.submit(() -> {
            String input;
            do {
                printInGameTextBox("You can both move and build, what do you want to do? (m,b) or type \"n\" if you want to change god to visualize in God Power box...");
                do {
                    input = inputWithTimeout();
                } while (!Thread.interrupted() && !input.equals("m") && !input.equals("b") && !input.equals("n"));

                if (!Thread.interrupted() && input.equals("n")) changeGodToVisualize();

            }while (!Thread.interrupted() && input.equals("n"));

            if (!Thread.interrupted()) {
                switch (input){
                    case "m" :
                        moveAfterChose();
                        break;
                    case "b" :
                        buildAfterChose();
                        break;
                }
            }
        });
    }

    //TODO : javadoc

    @Override
    public void buildOrEnd() {
        abortInputProcessing();
        inputThread = inputExecutor.submit(() -> {
            String input;
            do {
                printInGameTextBox("You can both build and end your turn, what do you want to do? (b,e) or type \"n\" if you want to change god to visualize in God Power box...");
                do {
                    input = inputWithTimeout();
                } while (!Thread.interrupted() && !input.equals("b") && !input.equals("e") && !input.equals("n"));

                if(!Thread.interrupted() && input.equals("n")) changeGodToVisualize();

            }while(!Thread.interrupted() && input.equals("n"));

            if (!Thread.interrupted()) {
                switch (input){
                    case "b" :
                        buildAfterChose();
                        break;
                    case "e" :
                        sendEndOfTurnRequest();
                        break;
                }
            }
        });
    }

    //TODO : javadoc

    @Override
    public void showLoginDone() {
        StringBuilder message;
        message = new StringBuilder("Hi " + myPlayer.getUsername() + ", you're in!");
        if (players.size() == 0) message.append(" You're the creator of this match, so you will decide "
                + "when to start the game. You can either start it when another player logs in or wait for a third player. "
                + "The moment the third player logs in you can start the game, which will still start automatically after 2 minutes "
                + "from the login of the third player.");
        else
            message.append(" You're currently ").append(players.size() + 1).append(" players in this game : You");
        for (Player player : players)
            message.append(", ").append(player.getUsername());
        printInStartTextBox(message.toString());
    }

    //TODO : javadoc

    @Override
    public void showNewUserLogged(String username, Color color) {
        abortInputProcessing();
        printInStartTextBox(username + " is a new player!");
    }

    //TODO : javadoc

    @Override
    public void showWaitMessage(String waitFor, String author) {
        switch (waitFor) {
            case "startMatch":
                appendInStartTextBox("Waiting for " + author + "(creator)'s start game command...");
                break;
            case "createGods":
                printInGameTextBox(author + " is the challenger, he is choosing " + (players.size() + 1) + " divinities for this game...");
                break;
            case "choseGod":
                appendInGameTextBox(author + " is choosing a god to use for this game...");
                break;
            case "choseStartingPlayer":
                appendInGameTextBox(author + " is choosing a the starter player...");
                break;
            case "setupMaleWorkerOnBoard":
                printInGameTextBox(author + " is placing his male worker on the board...");
                break;
            case "setupFemaleWorkerOnBoard":
                printInGameTextBox(author + " is placing his female worker on the board...");
                break;
            case "hisTurn":
                abortInputProcessing();
                inputThread = inputExecutor.submit(() -> {
                    while(!Thread.interrupted()){
                        printInGameTextBox(author + " is playing his turn...");
                        appendInGameTextBox("Enter \"n\" if you want to change god to visualize in God Power box...");
                        String input = input();
                        if(input.equals("n")){
                            changeGodToVisualize();
                        }
                    }
                });
        }
    }

    //TODO : javadoc

    @Override
    public void showMatchStarted() {
        state = "MATCH";
        System.out.print(Escapes.CLEAR_ENTIRE_SCREEN.escape());
        printGameTemplate();
        printBoard();
        printInGameTextBox("the match has been started...");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO : javadoc

    @Override
    public void showGodsChoiceDone(ArrayList<Integer> ids) {
        StringBuilder output = new StringBuilder();
        output.append("You have chosen the following gods : ").append(getGodById(ids.get(0)).getName());
        for(int i = 1; i < ids.size(); i++){
            output.append(", ").append(getGodById(ids.get(i)).getName());
        }

        output.append(".");

        printInGameTextBox(output.toString());
    }

    //TODO : javadoc

    @Override
    public void showGodsChallengerSelected(String username, ArrayList<Integer> ids) {
        StringBuilder output = new StringBuilder();
        output.append(username).append(" has chosen the following gods : ").append(getGodById(ids.get(0)).getName());
        for(int i = 1; i < ids.size(); i++){
            output.append(", ").append(getGodById(ids.get(i)).getName());
        }

        output.append(".");

        printInGameTextBox(output.toString());
    }

    //TODO : javadoc

    @Override
    public void showMyGodSelected() {
        printInGameTextBox("Your God is " + myPlayer.getGod().getName() + ".");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        godToVisualize = myPlayer.getGod();
        printGodInGodBox();
    }

    //TODO : javadoc

    @Override
    public void showGodSelected(String username) {
        printInGameTextBox(username + " has chosen " + getPlayerByUsername(username).getGod().getName() + ".");
    }

    //TODO : javadoc

    @Override
    public void showStartingPlayer(String username) {
        if(username.equals(myPlayer.getUsername()))
            printInGameTextBox("You will be the starter player.");
        else
            printInGameTextBox(username + " will be the starter player.");

        appendInGameTextBox("Loading...");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO : javadoc

    @Override
    public void showBoard() {
        printBoard();
    }

    //TODO : javadoc

    @Override
    public void showTurnEnded(String username) {
        printInGameTextBox(username + " turn is over!");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO : javadoc

    @Override
    public void showMyTurnEnded(){
        printInGameTextBox("Your turn is over!");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO : javadoc

    @Override
    public void showTurnErrors(List<String> errors) {
        StringBuilder output = new StringBuilder();

        output.append("Invalid action! errors encountered :");

        for (String error : errors) {
            switch (error) {
                case "BMU":
                    output.append(", ").append("Athena blocked upward movements");
                    break;
                case "CDU":
                    output.append(", ").append("you can't build a dome under yourself");
                    break;
                case "CMU":
                    output.append(", ").append("you can't move up because you build before you moved");
                    break;
                case "EBNP":
                    output.append(", ").append("the additional build can't be on a perimeter space");
                    break;
                case "EBNSS":
                    output.append(", ").append("the additional build can't be on the same space");
                    break;
                case "EBOSS":
                    output.append(", ").append("the additional build must be built on top of your first block");
                    break;
                case "EMNB":
                    output.append(", ").append("your worker can't moves back to the space it started on");
                    break;
                case "ILB":
                    output.append(", ").append("you can't build this block in the space you selected");
                    break;
                case "ILM":
                    output.append(", ").append("the space where you want to move is too high");
                    break;
                case "ID":
                    output.append(", ").append("there is a dome");
                    break;
                case "NA":
                    output.append(", ").append("the space you selected is not adjacent");
                    break;
                case "NF":
                    output.append(", ").append("the space you selected is occupied");
                    break;
                case "SDNF":
                    output.append(", ").append("you can't push the worker on this space because the space in the same direction is occupied");
                    break;
                case "EBND":
                    output.append(", ").append("the additional build block can't be a dome");
            }
        }
        output.delete(36,37);

        output.append(". Try again...");
        printInGameTextBox(output.toString());

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO : javadoc

    @Override
    public void serverNotFound() {
        printInStartTextBox("Server not found!");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //New connection
        setMyIp();
        setMyPort();
        start();
    }

    //TODO : javadoc

    @Override
    public void showAnotherClientDisconnection() {
        disconnected = true;
        abortInputProcessing();
        switch (state){
            case "SETUP" :
                printInStartTextBox("A client has disconnected from the game, the match has been deleted! Do you want to try to search a new game? (s/n)");
                break;
            case "MATCH":
                printInGameTextBox("A client has disconnected from the game, the match has been deleted! Do you want to try to search a new game? (s/n)");
                break;
            case "FINISHED":
                printInFinalTextBox("The server disconnected you! Do you want to try to reconnect? (s/n)");
                break;
        }
        String input;
        do {
            input = input();
        } while (!input.equals("s") && !input.equals("n"));

        if (input.equals("s")) {
            newGame();
            state = "SETUP";
            eraseThings("all");
            printStartTemplate();
            start();
        } else System.exit(0);
    }

    //TODO : javadoc

    @Override
    public void showDisconnectionForLobbyNoLongerAvailable() {
        disconnected = true;
        abortInputProcessing();
        printInStartTextBox("Too long, the match you were entered into has already started! Do you want to try to search a new game? (s/n)");
        String input;
        do {
            input = input();
        } while (!input.equals("s") && !input.equals("n"));

        if (input.equals("s")) {
            newGame();
            state = "SETUP";
            eraseThings("all");
            printStartTemplate();
            start();
        } else System.exit(0);
    }

    //TODO : javadoc

    @Override
    public void showServerDisconnection() {
        disconnected = true;
        abortInputProcessing();
        switch (state){
            case "SETUP" :
                printInStartTextBox("The server has disconnected! Do you want to try to reconnect? (s/n)");
                break;
            case "MATCH":
                printInGameTextBox("The server has disconnected! Do you want to try to reconnect? (s/n)");
                break;
            case "FINISHED":
                printInFinalTextBox("The server disconnected you! Do you want to try to reconnect? (s/n)");
                break;
        }
        String input;
        do {
            input = input();
        } while (!input.equals("s") && !input.equals("n"));

        if (input.equals("s")) {
            newGame();
            cliSetup();
        } else System.exit(0);
    }

    //TODO : javadoc

    @Override
    public void showDisconnectionForInputExpiredTimeout() {
        disconnected = true;
        abortInputProcessing();
        if (state.equals("SETUP")) printInStartTextBox("The timeout to do your action has expired, " +
                "you were kicked out of the game! Do you want to try to search a new game? (s/n)");
        else
            printInGameTextBox("The timeout to do your action has expired, " +
                    "you were kicked out of the game! Do you want to try to search a new game? (s/n)");
        String input;
        do {
            input = input();
        } while (!input.equals("s") && !input.equals("n"));

        if (input.equals("s")) {
            newGame();
            state = "SETUP";
            eraseThings("all");
            printStartTemplate();
            start();
        } else System.exit(0);
    }

    //TODO : javadoc

    @Override
    public void showPlayerLose(String username) {
        printInGameTextBox(username + " lost! now you are only 2 : You and " + players.get(0));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO : javadoc

    @Override
    public void showYouLose(String reason, String winner) {
        state = "FINISHED";
        eraseThings("all");
        printFinalTemplate();
        printLoser();

        StringBuilder output = new StringBuilder();
        output.append("You lose because ");
        switch (reason){
            case "youLoseForDirectWin":
                output.append(winner).append(" won instantly!");
                break;
            case "youLoseForBlocked":
                if(myPlayer.getWorkers().size() == 0) output.append(" you no longer have workers.");
                else output.append("you were trapped!");
                if(!winner.equals(myPlayer.getUsername()))
                    output.append(" The winner is ").append(winner).append("!");
        }

        printInFinalTextBox(output.toString());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO : javadoc

    @Override
    public void showYouWin(String reason) {
        state = "FINISHED";
        eraseThings("all");
        printFinalTemplate();
        printWinner();

        StringBuilder output = new StringBuilder();
        output.append("You win ");
        switch (reason){
            case "youWinDirectly":
                output.append("instantly!");
                break;
            case "youWinForAnotherLose":
                if(players.get(0).getWorkers().size() == 0) output.append(" your opponent no longer have workers.");
                else output.append("your opponent were trapped!");
        }

        printInFinalTextBox(output.toString());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Frame methods

    /**
     * The printTemplate method print the background visual elements, the frame of game
     */

    public void printStartTemplate() {

        //draw the top line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_RIGHT.escape());
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() - 1; i++) {
            if (i == Box.TEXT_START.escape()) {
                System.out.print("Login");
                i += 5;
            }
            System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_LEFT.escape());

        //draw the left line

        System.out.println(Escapes.CURSOR_HOME_0x0.escape());
        for (int i = 1; i < Box.VERTICAL_DIM.escape(); i++) {
            if (i == Box.TEXT_BOX_START.escape() - 1 || i == Box.INPUT_BOX_START.escape() - 1)
                System.out.println(Unicode.BOX_RAWINGS_HEAVY_VERTICAL_AND_RIGHT.escape());
            else
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_UP_AND_RIGHT.escape());

        //draw the bottom line

        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() - 1; i++) {
            System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_UP_AND_LEFT.escape());

        //draw the right line

        System.out.println(Escapes.CURSOR_HOME_0x0.escape());
        for (int i = 1; i < Box.VERTICAL_DIM.escape(); i++) {
            System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.HORIZONTAL_DIM.escape() - 1);
            if (i == Box.TEXT_BOX_START.escape() - 1 || i == Box.INPUT_BOX_START.escape() - 1)
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL_AND_LEFT.escape());
            else
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
        }

        //draw the text line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape(), 2);
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape(); i++) {
            if (i == Box.TEXT_START.escape()) {
                System.out.print("Text");
                i += 4;
            } else
                System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }

        //draw the input line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.INPUT_BOX_START.escape(), 2);
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape(); i++) {
            if (i == Box.TEXT_START.escape()) {
                System.out.print("Input");
                i += 5;
            } else
                System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }


        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.CREDITS_START_FROM_UP.escape(), Box.CREDITS_START_LEFT.escape());
        System.out.println(ColorCode.ANSI_CYAN.escape() + "          Software engineering project, AM10 group, credits to:");
        System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.CREDITS_START_LEFT.escape());
        System.out.print(ColorCode.ANSI_CYAN.escape() + "    Piersilvio De Bartolomeis, Marco Di Gennaro, Alessandro Di Maio" + ColorCode.ANSI_RESET.escape());

        printSantorini();
    }

    public void printFinalTemplate(){

        //draw the top line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_RIGHT.escape());
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() - 1; i++) {
            if (i == Box.TEXT_START.escape()) {
                System.out.print("End");
                i += 5;
            }
            System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_LEFT.escape());

        //draw the left line

        System.out.println(Escapes.CURSOR_HOME_0x0.escape());
        for (int i = 1; i < Box.VERTICAL_DIM.escape(); i++) {
            if (i == Box.TEXT_BOX_START.escape() - 1 || i == Box.INPUT_BOX_START.escape() - 1)
                System.out.println(Unicode.BOX_RAWINGS_HEAVY_VERTICAL_AND_RIGHT.escape());
            else
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_UP_AND_RIGHT.escape());

        //draw the bottom line

        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() - 1; i++) {
            System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_UP_AND_LEFT.escape());

        //draw the right line

        System.out.println(Escapes.CURSOR_HOME_0x0.escape());
        for (int i = 1; i < Box.VERTICAL_DIM.escape(); i++) {
            System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.HORIZONTAL_DIM.escape() - 1);
            if (i == Box.TEXT_BOX_START.escape() - 1 || i == Box.INPUT_BOX_START.escape() - 1)
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL_AND_LEFT.escape());
            else
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
        }

        //draw the text line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape(), 2);
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape(); i++) {
            if (i == Box.TEXT_START.escape()) {
                System.out.print("Text");
                i += 4;
            } else
                System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }

        //draw the input line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.INPUT_BOX_START.escape(), 2);
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape(); i++) {
            if (i == Box.TEXT_START.escape()) {
                System.out.print("Input");
                i += 5;
            } else
                System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }
    }

    public void printGameTemplate() {

        //draw the top line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_RIGHT.escape());
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() - 1; i++) {
            if (i == Box.TEXT_START.escape()) {
                System.out.print("Game Board");
                i += 10;
            }
            if(i == Box.GODS_BOX_START.escape() + 10){
                System.out.print("God Power");
                i += 9;
            }
            if(i == Box.GODS_BOX_START.escape() - 1){
                System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_HORIZONTAL.escape());
                i += 1;
            }
            System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_LEFT.escape());

        //draw the left line

        System.out.println(Escapes.CURSOR_HOME_0x0.escape());
        for (int i = 1; i < Box.VERTICAL_DIM.escape(); i++) {
            if (i == Box.TEXT_BOX_START.escape() - 1 || i == Box.INPUT_BOX_START.escape() - 1)
                System.out.println(Unicode.BOX_RAWINGS_HEAVY_VERTICAL_AND_RIGHT.escape());
            else
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_UP_AND_RIGHT.escape());

        //draw the bottom line

        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() - 1; i++) {
            System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_UP_AND_LEFT.escape());

        //draw the right line

        System.out.println(Escapes.CURSOR_HOME_0x0.escape());
        for (int i = 1; i < Box.VERTICAL_DIM.escape(); i++) {
            System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.HORIZONTAL_DIM.escape() - 1);
            if (i == Box.TEXT_BOX_START.escape() - 1 || i == Box.INPUT_BOX_START.escape() - 1)
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL_AND_LEFT.escape());
            else
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
        }

        //draw the text line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape(), 2);
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape(); i++) {
            if (i == Box.TEXT_START.escape()) {
                System.out.print("Text");
                i += 4;
            } else
                System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }

        //draw the input line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.INPUT_BOX_START.escape(), 2);
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape(); i++) {
            if (i == Box.TEXT_START.escape()) {
                System.out.print("Input");
                i += 5;
            } else
                System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }

        //draw the gods box

        System.out.println(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.GODS_BOX_START_LINE.escape() + 1, Box.GODS_BOX_START.escape());
        for (int i = Box.GODS_BOX_START_LINE.escape() + 1; i < Box.PLAYER_BOX_START_LINE.escape(); i++) {
            System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
            System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.GODS_BOX_START.escape() - 1);
        }

        //draw player box

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.PLAYER_BOX_START_LINE.escape(), Box.PLAYERS_BOX_START.escape());
        System.out.print(Unicode.BOX_RAWINGS_HEAVY_VERTICAL_AND_RIGHT.escape());
        for (int i = Box.PLAYERS_BOX_START.escape(); i < (Box.HORIZONTAL_DIM.escape() - 1); i++)
            if (i == Box.HORIZONTAL_DIM.escape() - 25) {
                System.out.print("Players");
                i += 6;
            } else
                if(i == Box.HORIZONTAL_DIM.escape() - 10){
                    System.out.print("Gods");
                    i += 3;
                }else
                    System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL_AND_LEFT.escape());

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.PLAYER_BOX_START_LINE.escape() + 1, Box.PLAYERS_BOX_START.escape());

        for (int i = (Box.PLAYER_BOX_START_LINE.escape() + 1); i < Box.TEXT_BOX_START.escape(); i++) {
            System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
            System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.PLAYERS_BOX_START.escape() - 1);
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_UP_AND_HORIZONTAL.escape());

        //print player name in players' box

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.PLAYER_BOX_START_LINE.escape() + 2, Box.PLAYERS_BOX_START.escape() + 4);
        System.out.println(Color.getColorCodeByColor(myPlayer.getWorkers().get(0).getColor()).escape() + ColorCode.ANSI_BLACK.escape() + " " + myPlayer.getUsername() + " " + ColorCode.ANSI_RESET.escape() + " : " + myPlayer.getGod().getName() + "\n");
        for (Player player : players) {
            System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.PLAYERS_BOX_START.escape() + 3);
            System.out.println(Color.getColorCodeByColor(player.getWorkers().get(0).getColor()).escape() + ColorCode.ANSI_BLACK.escape() + " " + player.getUsername() + " " + ColorCode.ANSI_RESET.escape() + " : " + player.getGod().getName() + "\n");
        }


    }

    /**
     * This method prints "Santorini" in the game frame
     */

    public void printSantorini() {

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.ASCII_ART_START_UP.escape(), Box.ASCII_ART_START_LEFT.escape() + 1);
        System.out.print(ColorCode.ANSI_CYAN.escape() +
                "  _____  ____  ____   ______   ___   ____   ____  ____   ____ \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                " / ___/ /    ||    \\ |      | /   \\ |    \\ |    ||    \\ |    |\n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "(   \\_ |  o  ||  _  ||      ||     ||  D  ) |  | |  _  | |  | \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                " \\__  ||     ||  |  ||_|  |_||  O  ||    /  |  | |  |  | |  | \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                " /  \\ ||  _  ||  |  |  |  |  |     ||    \\  |  | |  |  | |  | \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                " \\    ||  |  ||  |  |  |  |  |     ||  .  \\ |  | |  |  | |  | \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "  \\___||__|__||__|__|  |__|   \\___/ |__|\\_||____||__|__||____|\n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "                                                              \n" + ColorCode.ANSI_RESET.escape());

    }

    /**
     * This method prints "Loser" in the game frame
     */

    public void printLoser() {

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.ASCII_ART_START_UP.escape(), Box.ASCII_ART_START_LEFT.escape() + 4);
        System.out.print(ColorCode.ANSI_RED.escape() +
                " (        )   (         (     \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                " )\\ )  ( /(   )\\ )      )\\ )  \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "(()/(  )\\()) (()/( (   (()/(  \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                " /(_))((_)\\   /(_)))\\   /(_)) \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "(_))    ((_) (_)) ((_) (_))   \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "| |    / _ \\ / __|| __|| _ \\  \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "| |__ | (_) |\\__ \\| _| |   /  \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "|____| \\___/ |___/|___||_|_\\  \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "                              \n" + ColorCode.ANSI_RESET.escape());

    }

    /**
     * This method prints "You win" in the game frame
     */

    public void printWinner() {

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.ASCII_ART_START_UP.escape(), Box.ASCII_ART_START_LEFT.escape() + 1);
        System.out.print(ColorCode.ANSI_CYAN.escape() +
                " __  __   ______   __  __       __     __   ______   __   __    \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "/\\ \\_\\ \\ /\\  __ \\ /\\ \\/\\ \\     /\\ \\  _ \\ \\ /\\  __ \\ /\\ \"-.\\ \\   \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "\\ \\____ \\\\ \\ \\/\\ \\\\ \\ \\_\\ \\    \\ \\ \\/ \".\\ \\\\ \\ \\/\\ \\\\ \\ \\-.  \\  \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                " \\/\\_____\\\\ \\_____\\\\ \\_____\\    \\ \\__/\".~\\_\\\\ \\_____\\\\ \\_\\\\\"\\_\\ \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "  \\/_____/ \\/_____/ \\/_____/     \\/_/   \\/_/ \\/_____/ \\/_/ \\/_/ \n" + "\u001b[" + Box.ASCII_ART_START_LEFT.escape() + "C" +
                "                                                                \n" + ColorCode.ANSI_RESET.escape());

    }

    /**
     * This method erase part of the general frame
     *
     * @param thing represents the part of frame that will be erased
     */

    public void eraseThings(String thing) {

        switch (thing) {

            case "all":
                System.out.print(Escapes.CLEAR_ENTIRE_SCREEN.escape());
                break;

            case "game":
                System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape() - 1, Box.HORIZONTAL_DIM.escape() - 1);
                System.out.print(Escapes.CLEAR_SCREEN_FROM_HERE_TO_BEGINNING.escape());
                break;

            case "text":
                System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape(), 0);
                System.out.print(Escapes.CLEAR_SCREEN_FROM_HERE_TO_END.escape());
                break;

            case "playerBox":
                System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.PLAYERS_BOX_START.escape(), 0);
                for (int i = 0; i < (Box.TEXT_BOX_START.escape() - 1); i++) {
                    System.out.println(Escapes.CLEAR_LINE_FROM_CURSOR_TO_END.escape());
                    System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.PLAYERS_BOX_START.escape());
                }
                break;
            case "godBox":
                System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.GODS_BOX_START_LINE.escape(), Box.GODS_BOX_START.escape());
                for (int i = 0; i < (Box.PLAYER_BOX_START_LINE.escape() - 1); i++) {
                    System.out.println(Escapes.CLEAR_LINE_FROM_CURSOR_TO_END.escape());
                    System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.GODS_BOX_START.escape());
                }
        }


    }

    //Board methods

    public void printBoard() {

        /*

        //print Board frame

        //print left border and horizontal lines
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.BOARD_START_UP.escape(), Box.BOARD_START_LEFT.escape());
        System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + Unicode.BOX_DRAWINGS_LIGHT_DOWN_AND_RIGHT.escape());
        for (int i = 1; i < Box.SQUARE_VERTICAL_DIM.escape() * Box.SQUARE_DIMENSION.escape(); i++) {
            if(i % (Box.SQUARE_VERTICAL_DIM.escape() + 1) == 0){
                System.out.printf(Escapes.RESTORE_CURSOR_POSITION.escape() + Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);
                System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + Unicode.BOX_RAWINGS_LIGHT_VERTICAL_AND_RIGHT.escape());
                for (int j = 1; j < Box.SQUARE_HORIZONTAL_DIM.escape() * Box.SQUARE_DIMENSION.escape() + Box.SQUARE_DIMENSION.escape(); j++) {
                    if(j % (Box.SQUARE_HORIZONTAL_DIM.escape() + 1) == 0)
                        System.out.print(Unicode.BOX_DRAWINGS_LIGHT_VERTICAL_AND_HORIZONTAL.escape());
                    else
                        System.out.print(Unicode.BOX_DRAWINGS_LIGHT_HORIZONTAL.escape());
                }
                System.out.print(Unicode.BOX_DRAWINGS_LIGHT_VERTICAL_AND_LEFT.escape());
            }
            else {
                System.out.printf(Escapes.RESTORE_CURSOR_POSITION.escape() + Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);
                System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape());
            }
        }

        //print bottom border
        System.out.printf(Escapes.RESTORE_CURSOR_POSITION.escape() + Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);
        System.out.print(Unicode.BOX_DRAWINGS_LIGHT_UP_AND_RIGHT.escape());
        for (int i = 1; i < Box.SQUARE_HORIZONTAL_DIM.escape() * Box.SQUARE_DIMENSION.escape() + Box.SQUARE_DIMENSION.escape() - 1; i++) {
            if (i % (Box.SQUARE_HORIZONTAL_DIM.escape() + 1) == 0)
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), 1);
            else
                System.out.print(Unicode.BOX_DRAWINGS_LIGHT_HORIZONTAL.escape());
        }

        //print top border and vertical internal lines
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.BOARD_START_UP.escape(), Box.BOARD_START_LEFT.escape() + 1);
        for (int i = 1; i < Box.SQUARE_HORIZONTAL_DIM.escape() * Box.SQUARE_DIMENSION.escape() + Box.SQUARE_DIMENSION.escape(); i++) {
            if(i % (Box.SQUARE_HORIZONTAL_DIM.escape() + 1) == 0){
                System.out.println(Unicode.BOX_DRAWINGS_LIGHT_DOWN_AND_HORIZONTAL.escape() + Escapes.SAVE_CURSOR_POSITION.escape());
                for (int j = 1; j <= Box.SQUARE_VERTICAL_DIM.escape() * Box.SQUARE_DIMENSION.escape(); j++) {
                    System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.BOARD_START_LEFT.escape() + i - 1);
                    if(j % (Box.SQUARE_VERTICAL_DIM.escape() + 1) == 0)
                        System.out.println(Unicode.BOX_DRAWINGS_LIGHT_VERTICAL_AND_HORIZONTAL.escape());
                    else
                        System.out.println(Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape());
                }
                System.out.print(Unicode.BOX_DRAWINGS_LIGHT_UP_AND_HORIZONTAL.escape() + Escapes.RESTORE_CURSOR_POSITION.escape());
            }
            else
                System.out.print(Unicode.BOX_DRAWINGS_LIGHT_HORIZONTAL.escape());
        }

        //print right border
        System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + Unicode.BOX_DRAWINGS_LIGHT_DOWN_AND_LEFT.escape());
        for (int i = 1; i < Box.SQUARE_VERTICAL_DIM.escape() * Box.SQUARE_DIMENSION.escape(); i++) {
            if(i % (Box.SQUARE_VERTICAL_DIM.escape() + 1) == 0) {
                System.out.print(Escapes.RESTORE_CURSOR_POSITION.escape());
                System.out.printf(Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);
                System.out.print(Escapes.SAVE_CURSOR_POSITION.escape());
            }
            else{
                System.out.print(Escapes.RESTORE_CURSOR_POSITION.escape());
                System.out.printf(Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);
                System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape());
            }
        }
        System.out.print(Unicode.BOX_DRAWINGS_LIGHT_UP_AND_LEFT.escape());
        */


        //print 1,2,3,4,5 vertical board reference
        for (int i = Board.DIMENSION, j = 0; i > 0; i--, j += Box.SQUARE_DIMENSION.escape()) {
            System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.BOARD_START_UP.escape() + j + 2, Box.BOARD_START_LEFT.escape() - 3);
            System.out.printf("%d", i - 1);
        }

        //This cycle prints 0,0 position in the left-bottom corner

        for (int x = 0, i = 0, j = 0; x < Board.DIMENSION; x++, i += Box.SQUARE_HORIZONTAL_DIM.escape(), j++) {
            System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.BOARD_START_UP.escape(), Box.BOARD_START_LEFT.escape() + i - 1);
            for (int y = Board.DIMENSION - 1; y > -1; y--) {
                drawSquare(x, y);
                System.out.print("\n");
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.BOARD_START_LEFT.escape() + i - 2);
            }
            //print 1,2,3,4,5 horizontal board reference
            System.out.print(ColorCode.ANSI_RESET.escape());
            System.out.print("   " + j);
        }



    }

    private void setBackgroundColor(Square square){

        if (square.getDome())
            System.out.print(ColorCode.LEVEL_DOME_BLUE_BACKGROUND.escape());
        else {
            switch (square.getLevel()) {
                case 0:
                    System.out.print(ColorCode.LEVEL_0_GREEN_BACKGROUND.escape());
                    break;
                case 1:
                    System.out.print(ColorCode.LEVEL_1_SAND_BACKGROUND.escape());
                    break;
                case 2:
                    System.out.print(ColorCode.LEVEL_2_GRAY_BACKGROUND.escape());
                    break;
                case 3:
                    System.out.print(ColorCode.LEVEL_3_WHITE_BACKGROUND.escape());
                    break;
            }
        }

    }


    public void drawSquare(int x, int y) {

        Square square = gameBoard.getSquareByCoordinates(x, y);

        //change color of single square based on level of square

        System.out.print(ColorCode.ANSI_BLACK.escape());

        setBackgroundColor(square);

        System.out.print(ColorCode.WHITE.escape());
        System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + Unicode.BOX_DRAWINGS_LIGHT_DOWN_AND_RIGHT.escape());
        for (int i = 0; i < Box.SQUARE_HORIZONTAL_DIM.escape() - 2; i++) {
            System.out.print(Unicode.BOX_DRAWINGS_LIGHT_HORIZONTAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_LIGHT_DOWN_AND_LEFT.escape() + Escapes.RESTORE_CURSOR_POSITION.escape());
        setBackgroundColor(square);
        System.out.printf(Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);


        if (square.getWorker() != null) {
            if(square.getWorker().getGender().equals("male")) {
                System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + ColorCode.WHITE.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape() + Color.getColorCodeByColor(square.getWorker().getColor()).escape() + " " + Unicode.WORKER_MALE_ICON.escape() + " " );
                setBackgroundColor(square);
                System.out.print(Unicode.SQUARE_HORIZONTAL_DIM_MIN5.escape() + ColorCode.WHITE.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape() + Escapes.RESTORE_CURSOR_POSITION.escape());
                System.out.printf(Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);
            }
            if(square.getWorker().getGender().equals("female")) {
                System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + ColorCode.WHITE.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape() + Color.getColorCodeByColor(square.getWorker().getColor()).escape() + " " + Unicode.WORKER_FEMALE_ICON.escape() + " " );
                setBackgroundColor(square);
                System.out.print(Unicode.SQUARE_HORIZONTAL_DIM_MIN5.escape() + ColorCode.WHITE.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape() + Escapes.RESTORE_CURSOR_POSITION.escape());
                System.out.printf(Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);
            }
                System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + ColorCode.WHITE.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape());
                setBackgroundColor(square);
                System.out.print(Unicode.SQUARE_HORIZONTAL_DIM_MIN2.escape() + ColorCode.WHITE.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape() + Escapes.RESTORE_CURSOR_POSITION.escape());
                System.out.printf(Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);

        } else {
            for (int i = 0; i < 2; i++) {
                System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + ColorCode.WHITE.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape());
                setBackgroundColor(square);
                System.out.print(Unicode.SQUARE_HORIZONTAL_DIM_MIN2.escape() + ColorCode.WHITE.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape() + Escapes.RESTORE_CURSOR_POSITION.escape());
                System.out.printf(Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);
            }

        }

            System.out.print(ColorCode.WHITE.escape());
            System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape());
            setBackgroundColor(square);
            System.out.print(Unicode.SQUARE_HORIZONTAL_DIM_MIN3.escape() + square.getLevel());
            System.out.print(ColorCode.WHITE.escape() + Unicode.BOX_DRAWINGS_LIGHT_VERTICAL.escape() + Escapes.RESTORE_CURSOR_POSITION.escape());
            System.out.printf(Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);

            System.out.print(Unicode.BOX_DRAWINGS_LIGHT_UP_AND_RIGHT.escape());
            for (int i = 0; i < Box.SQUARE_HORIZONTAL_DIM.escape() - 2; i++) {
                System.out.print(Unicode.BOX_DRAWINGS_LIGHT_HORIZONTAL.escape());
            }
            System.out.print(Unicode.BOX_DRAWINGS_LIGHT_UP_AND_LEFT.escape());

            System.out.print(ColorCode.ANSI_RESET.escape());

    }

    //Worker methods

    //Text methods

    /**
     * This method is used to get input from keyboard without any control
     *
     * @return the string from input
     */

    public String input() {

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.INPUT_BOX_START.escape() + 1, 2);
        System.out.print(">");
        return InputCli.readLine();

    }

    //TODO : javadoc

    public String inputWithTimeout() {
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.INPUT_BOX_START.escape() + 1, 2);
        System.out.print(">");
        String input = "";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> result = executor.submit(InputCli::readLine);

        try {
            input = result.get(2, TimeUnit.MINUTES);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            if(!disconnected){
                new Thread(this::disconnectionForInputExpiredTimeout).start();
            }
            Thread.currentThread().interrupt();
            result.cancel(true);
        }

        return input;
    }

    public void inputWithTimeoutStartMatch(){
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.INPUT_BOX_START.escape() + 1, 2);
        System.out.print(">");
        String input = "";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> result = executor.submit(InputCli::readLine);

        try {
            result.get(2, TimeUnit.MINUTES);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            sendStartGameRequest();
        }

    }



    public void printInStartTextBox(String text) {

        char[] information = text.toCharArray();

        eraseThings("text");
        printStartTemplate();

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape() + 1, 2);

        //this cycle allow to avoid that text exceed the frame length

        for (int i = 2, j = 0; j < information.length; i++, j++) {
            System.out.print(information[j]);
            if (i == Box.HORIZONTAL_DIM.escape() - 2) {
                System.out.print("-\n");
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), 1);
                i = 1;
            }
        }

    }

    //TODO : javadoc

    public void printGodInGodBox(){
        eraseThings("godBox");

        printInGodTextBox("name",godToVisualize.getName());
        printInGodTextBox("description",godToVisualize.getDescription());
    }

    public void printInFinalTextBox(String text) {

        char[] information = text.toCharArray();

        eraseThings("text");
        printFinalTemplate();

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape() + 1, 2);

        //this cycle allow to avoid that text exceed the frame length

        for (int i = 2, j = 0; j < information.length; i++, j++) {
            System.out.print(information[j]);
            if (i == Box.HORIZONTAL_DIM.escape() - 2) {
                System.out.print("-\n");
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), 1);
                i = 1;
            }
        }

    }

    //TODO : javadoc

    public void changeGodToVisualize(){
        if(godToVisualize.getName().equals(myPlayer.getGod().getName())){
            godToVisualize = players.get(0).getGod();
        }else {
            if (players.size() == 1)
                godToVisualize = myPlayer.getGod();
            else {

                if (godToVisualize.getName().equals(players.get(0).getGod().getName()))
                    godToVisualize = players.get(1).getGod();
                else
                    godToVisualize = myPlayer.getGod();
            }
        }

        printGodInGodBox();
    }

    /**
     * This method prints in God text box
     * @param name represents what is intended to print, of god or its descriptions
     * @param text represents the content to be displayed
     */

    public void printInGodTextBox(String name, String text){

        int size = text.length();
        char[] information = text.toCharArray();

        printGameTemplate();

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());

        if(name.equals("name")){
            size = ((Box.HORIZONTAL_DIM.escape() - Box.GODS_BOX_START.escape()) - size)/2;
            System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.GODS_BOX_START_LINE.escape() + 3, Box.GODS_BOX_START.escape() + size + 1);
            System.out.print(text);
        }
        else{
            System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.GODS_BOX_START_LINE.escape() + 7, Box.GODS_BOX_START.escape() + 2);
            for (int i = Box.GODS_BOX_START.escape() + 2, j = 0; j < information.length; i++, j++) {
                System.out.print(information[j]);
                if (i == Box.HORIZONTAL_DIM.escape() - 3) {
                    System.out.print("-\n");
                    System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.GODS_BOX_START.escape() + 1);
                    i = Box.GODS_BOX_START.escape() + 1;
                }
            }
        }

    }

    public void appendInStartTextBox(String text) {
        char[] information = text.toCharArray();


        printStartTemplate();

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape() + 1, 2);

        //this cycle allow to avoid that text exceed the frame length

        System.out.print("\n");
        System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), 1);

        for (int i = 2, j = 0; j < information.length; i++, j++) {
            System.out.print(information[j]);
            if (i == Box.HORIZONTAL_DIM.escape() - 2) {
                System.out.print("-\n");
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), 1);
                i = 1;
            }
        }
    }

    /**
     * This method prints text in text frame
     *
     * @param text represents the text that will be printed
     */

    public void printInGameTextBox(String text) {

        char[] information = text.toCharArray();

        eraseThings("text");
        printGameTemplate();

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape() + 1, 2);

        //this cycle allow to avoid that text exceed the frame length

        for (int i = 2, j = 0; j < information.length; i++, j++) {
            System.out.print(information[j]);
            if (i == Box.HORIZONTAL_DIM.escape() - 2) {
                System.out.print("-\n");
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), 1);
                i = 1;
            }
        }


    }

    public void appendInGameTextBox(String text) {

        char[] information = text.toCharArray();

        printGameTemplate();

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape() + 1, 2);

        //this cycle allow to avoid that text exceed the frame length

        System.out.print("\n");
        System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), 1);

        for (int i = 2, j = 0; j < information.length; i++, j++) {
            System.out.print(information[j]);
            if (i == Box.HORIZONTAL_DIM.escape() - 2) {
                System.out.print("-\n");
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), 1);
                i = 1;
            }
        }
    }

    //support methods

    //TODO : javadoc

    //TODO : javadoc

    public void moveAfterChose(){
        printInGameTextBox("You have to move!");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!Thread.interrupted())
            inputThread = inputExecutor.submit(() -> {
            int[] startCoordinates = selectWorker();

            if(startCoordinates != null) {

                String input;
                int[] coordinates;

                do {
                    printInGameTextBox("Where do you want to move? (#,#) or type \"n\" if you want to change god to visualize in God Power box...");
                    input = inputWithTimeout();
                    while (!InputValidator.validateCOORDINATES(input) && !input.equals("n") && !Thread.interrupted()) {
                        printInGameTextBox("Invalid coordinates or input! Please try again (type #,#) or \"n\"...");
                        input = inputWithTimeout();
                    }

                    if (input.equals("n") && !Thread.interrupted()) changeGodToVisualize();
                } while (!Thread.interrupted() && input.equals("n"));


                if (InputValidator.validateCOORDINATES(input)) {
                    coordinates = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
                    sendMoveRequest(gameBoard.getSquareByCoordinates(startCoordinates[0], startCoordinates[1]).getWorker().getGender(), coordinates[0], coordinates[1]);
                }
            }
            });
    }

    //TODO : javadoc

    public void buildAfterChose(){
        printInGameTextBox("You have to build!");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!Thread.interrupted())
            inputThread = inputExecutor.submit(() -> {
            int[] startCoordinates = selectWorker();

            if(startCoordinates != null) {

                String inputCoordinates = null;
                int[] coordinates;

                do {
                    printInGameTextBox("Where do you want to build? (#,#) or type \"n\" if you want to change god to visualize in God Power box...");
                    inputCoordinates = inputWithTimeout();
                    while (!InputValidator.validateCOORDINATES(inputCoordinates) && !inputCoordinates.equals("n") && !Thread.interrupted()) {
                        printInGameTextBox("Invalid coordinates or input! Please try again (type #,#) or \"n\"...");
                        inputCoordinates = inputWithTimeout();
                    }

                    if (inputCoordinates.equals("n") && !Thread.interrupted()) changeGodToVisualize();
                } while (!Thread.interrupted() && inputCoordinates.equals("n"));

                int level;
                String inputLevel = "";
                if(InputValidator.validateCOORDINATES(inputCoordinates)) {
                    printInGameTextBox("What type of building do you want to build? (1 -> first block, 2 -> second block, 3 -> third block, 4 -> dome)");
                    inputLevel = inputWithTimeout();
                    while (!InputValidator.validateLEVEL(inputLevel) && !Thread.interrupted()) {
                        printInGameTextBox("Invalid building! Please try again (#)...");
                        inputLevel = inputWithTimeout();
                    }
                }

                if (!Thread.currentThread().isInterrupted()) {
                    coordinates = Arrays.stream(inputCoordinates.split(",")).mapToInt(Integer::parseInt).toArray();
                    level = Integer.parseInt(inputLevel);
                    sendBuildRequest(gameBoard.getSquareByCoordinates(startCoordinates[0], startCoordinates[1]).getWorker().getGender(), coordinates[0], coordinates[1], level);
                }
            }
            });
    }

    //TODO : javadoc

    private int[] selectWorker() {

        int[] coordinates = new int[2];

        if(workerForThisTurnCoordinates[0] == -1 && workerForThisTurnCoordinates[1] == -1) {
            String input;

            if(myPlayer.getWorkers().size() == 1){
                Square s = myPlayer.getWorkers().get(0).getCurrentPosition();
                coordinates[0] = s.getX();
                coordinates[1] = s.getY();
                appendInGameTextBox("You have only one worker in game, you will play your turn with him!");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                do {
                    printInGameTextBox("Select one of your workers: (m,f) or type \"n\" if you want to change god to visualize in God Power box...");
                    input = inputWithTimeout();
                    while (!input.equals("m") && !input.equals("f") && !input.equals("n") && !Thread.currentThread().isInterrupted()) {
                        printInGameTextBox("Invalid input! Please try again (m,f) or \"n\"...");
                        input = inputWithTimeout();
                    }
                    if(input.equals("n") && !Thread.currentThread().isInterrupted()) changeGodToVisualize();
                }while (!Thread.currentThread().isInterrupted() && input.equals("n"));

                if (input.equals("m") && !Thread.currentThread().isInterrupted()) {
                    Square s = myPlayer.getWorkerByGender("male").getCurrentPosition();
                    coordinates[0] = s.getX();
                    coordinates[1] = s.getY();
                }
                if (input.equals("f") && !Thread.currentThread().isInterrupted()) {
                    Square s = myPlayer.getWorkerByGender("female").getCurrentPosition();
                    coordinates[0] = s.getX();
                    coordinates[1] = s.getY();
                }
            }
        }else{
            coordinates = workerForThisTurnCoordinates;
        }

        if(Thread.currentThread().isInterrupted()) return null;

        return coordinates;
    }

    //TODO : javadoc

    private void abortInputProcessing() {
        if (inputThread != null && !inputThread.isCancelled()) {
            inputThread.cancel(true);
            inputThread = null;
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
