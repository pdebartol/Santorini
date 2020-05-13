package it.polimi.ingsw.view.client.cli;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents the game flow
 * @author aledimaio
 */

public class Cli {

    private Elements elements;

    public Cli(){
        this.elements = new Elements();
    }

    public Elements getElements() {
        return elements;
    }

    /**
     * This method setup the terminal for the cli
     */

    public void cliSetup(){

        System.out.print(Escapes.CLEAR_ENTIRE_SCREEN.escape());
        elements.printTemplate();

    }

    /**
     * This method represents the game setup
     */

    public void gameSetup(){

        elements.printSantorini();
        elements.printInTextBox("Press enter button to start");
        elements.Input();
        elements.eraseThings("all");

        login();

    }

    /**
     * This game represents an ordinary turn
     */

    public void gameTurn(){

        boolean notOverYet = true;

            while(notOverYet) {

                elements.eraseThings("all");
                elements.getBoard().printBoard();
                elements.printInTextBox("Insert command (type \"show commands\" for help)");

                switch (elements.Input()) {

                    case "move":
                        elements.eraseThings("text");
                        move();
                        notOverYet = !endTurn();
                        break;

                    case "build":
                        elements.eraseThings("text");
                        build();
                        notOverYet = !endTurn();
                        break;

                    case "show my divinity":
                        elements.printDivinities(1);
                        break;

                    case "show other divinities":
                        elements.printDivinities(0);
                        break;

                    case "show commands":
                        elements.eraseThings("text");
                        elements.printInTextBox("move - build - show my divinity - show other divinities || Press enter to continue");
                        elements.Input();
                        break;

                    case "quit":
                        elements.eraseThings("text");
                        notOverYet = false;

                    default:
                        elements.eraseThings("text");
                        elements.printInTextBox("Wrong command! Retype it! - Press enter to continue");
                        elements.Input();

                }
            }

    }

    /**
     * This method represents the login process
     */

    private void login(){

        int players;

        //boolean value used to check condition in cycle

        boolean check = false;
        String username = null;

        //TODO change type to Color from String

        String color = null;
        boolean challenger = true;

        do {
            if(!check)
                elements.printInTextBox("Username already taken! Please, insert another username:");
            else
                elements.printInTextBox("Insert your username:");
            check = true;
            username = elements.Input();
        }while (!checkUsername());

        elements.eraseThings("text");

        /*
        do {
            elements.printInTextBox("Insert the color of your worker:");
            color = elements.Input();
        }while (!checkColor());
         */

        //TODO get color chosen randomly by server

        elements.printInTextBox("Server randomly assigned to you the color: " + color + ". Press Enter to continue");
        elements.Input();

        elements.setPlayer(new Player(username, color));

        elements.eraseThings("text");
        players = numberOfPlayers();

        //TODO start game method if this is the first player

        /*
        if(isFirstPlayer){

            while(true) {
                elements.eraseThings("text");
                elements.printInTextBox("You are the first player! There is another Player waiting, do you want to start now? Enter \"y\" if you want or simply wait for the third player! ");
                if (elements.Input() == "y")
                    startGame();
                else
            }

        }
         */

    }

    private void challenger(){

        ArrayList<God> gods = new ArrayList<>();
        ArrayList<God> chosenGods = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        boolean check = false;
        int i = 0;

        //TODO get god from server and put them in the ArrayList
        //TODO get other players' username and put them in the ArrayList (included the active player)

        elements.printInTextBox("You are the challenger! Now you have to chose the divinities for all players, to chose one" +
                "enter \"this\" when Name of God and his description is displayed. Now press enter to continue:");
        elements.Input();

        elements.eraseThings("text");

        elements.printInTextBox("\"this\" to select God, \"next\" to go to next God's card, \"prev\" to go to previously God's card" +
                "\"show command\" to show commands avaiable. Press enter to continue:");
        elements.Input();

        while (chosenGods.size() < 3) {

            switch (elements.Input()) {

                case "next":
                    elements.printInTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                            gods.get(i).getDescription());
                    i++;
                    if (i > gods.size()) i = 0;
                    break;

                case "prev":
                    i--;
                    if (i < 0) i = gods.size();
                    elements.printInTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                            gods.get(i).getDescription());
                    break;

                case "this":
                    elements.eraseThings("text");
                    elements.printInTextBox("Confirm selection of " + gods.get(i) + "? Type \"y\" for yes or anything else for no");
                    if (elements.Input() == "y")
                        chosenGods.add(gods.get(i));
                    else {
                        elements.eraseThings("text");
                        elements.printInTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                                gods.get(i).getDescription());
                    }
                    break;

                case "show commands":
                    elements.printInTextBox("\"this\" to select God, \"next\" to go to next God's card, \"prev\" to go to previously God's card" +
                            "\"show command\" to show commands avaiable. Press enter to continue:");
                    elements.Input();
                    elements.printInTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() +  "\n" +
                            gods.get(i).getDescription());
                    break;
            }
        }

        elements.eraseThings("text");

        elements.printInTextBox("Now other players are choosing their god, wait until they end!");

        //TODO set the last god left as the god of the challenger

        elements.eraseThings("text");
        elements.printInTextBox("Now you have to chose the order of players by choosing the first one:");

        while (check) {

            i = 0;

            switch (elements.Input()) {

                case "next":
                    elements.eraseThings("text");
                    elements.printInTextBox(players.get(i).getUsername());
                    i++;
                    if (i > gods.size()) i = 0;
                    break;

                case "prev":
                    i--;
                    if (i < 0) i = gods.size();
                    elements.eraseThings("text");
                    elements.printInTextBox(players.get(i).getUsername());
                    break;

                case "this":
                    elements.eraseThings("text");
                    elements.printInTextBox("Confirm selection of " + players.get(i).getUsername() + "? Type \"y\" for yes or anything else for no");
                    if (elements.Input().equals("y")) {
                        //TODO send the selected player to server
                        check = true;
                        elements.eraseThings("text");
                    }
                    else {
                        elements.eraseThings("text");
                        elements.printInTextBox(players.get(i).getUsername());
                    }
                    break;

                case "show commands":
                    elements.eraseThings("text");
                    elements.printInTextBox("\"this\" to select Player, \"next\" to go to next Player's username, \"prev\" to go to previously Player's username" +
                            "\"show command\" to show commands available. Press enter to continue:");
                    elements.Input();
                    elements.printInTextBox(players.get(i).getUsername());
                    break;
            }
        }


    }

    private void notChallenger(){

        int i = 0;
        ArrayList<God> gods = new ArrayList<>();

        //TODO get remaining gods from server that have been previously chosen by challenger

        while (elements.getPlayer().getGod() != null) {

            switch (elements.Input()) {

                case "next":
                    elements.eraseThings("text");
                    elements.printInTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                            gods.get(i).getDescription());
                    i++;
                    if (i > gods.size()) i = 0;
                    break;

                case "prev":
                    i--;
                    if (i < 0) i = gods.size();
                    elements.eraseThings("text");
                    elements.printInTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                            gods.get(i).getDescription());
                    break;

                case "this":
                    elements.eraseThings("text");
                    elements.printInTextBox("Confirm selection of " + gods.get(i) + "? Type \"y\" for yes or anything else for no");
                    if (elements.Input() == "y")
                        elements.getPlayer().setGod(new God(gods.get(i).getName(), gods.get(i).getDescription()));
                    else {
                        elements.eraseThings("text");
                        elements.printInTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                                gods.get(i).getDescription());
                    }
                    break;

                case "show commands":
                    elements.eraseThings("text");
                    elements.printInTextBox("\"this\" to select God, \"next\" to go to next God's card, \"prev\" to go to previously God's card" +
                            "\"show command\" to show commands avaiable. Press enter to continue:");
                    elements.Input();
                    elements.eraseThings("text");
                    elements.printInTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() +  "\n" +
                            gods.get(i).getDescription());
                    break;
            }
        }

    }

    /**
     * This method represents the move process in the game
     */

    private void move(){

        int[] numbers;
        int[] workerPosition;

        workerPosition = selectWorker();

        elements.printInTextBox("Select the square where you want to move your worker: (type #,#)");

        numbers = Arrays.stream(elements.Input().split(",")).mapToInt(Integer::parseInt).toArray();

        //TODO check if input is valid (check via server?)

        //update board after server update
        //TODO add Apollo case management

        elements.getBoard().getSquare(numbers[0], numbers[1]).setWorker(elements.getBoard().getSquare(workerPosition[0], workerPosition[1]).getWorker());
        elements.getBoard().getSquare(workerPosition[0], workerPosition[1]).setWorker(null);

        elements.eraseThings("text");

    }

    private int[] selectWorker(){

        int[] numbers;

        do {
            elements.eraseThings("text");
            elements.printInTextBox("Select the worker: (type #,#)");

            numbers = Arrays.stream(elements.Input().split(",")).mapToInt(Integer::parseInt).toArray();

        }while(elements.getBoard().getSquare(numbers[0], numbers[1]).getWorker() == null);

        //TODO change color of square of selected worker

        return numbers;

    }

    /**
     * This method represents the build process in the game
     */

    private void build(){

        int[] numbers;

        elements.printInTextBox("Select the square where you want your worker to build: (type #,#)");

        numbers = Arrays.stream(elements.Input().split(",")).mapToInt(Integer::parseInt).toArray();

        //TODO not necessary to provide the level wanted because it always increment by one, but is this game logic in view?

        //TODO check if input is valid

        //TODO update board

        elements.eraseThings("text");

    }

    private boolean endTurn(){
        //TODO check if turn is over
        return false;
    }

    private boolean checkUsername(){
        //TODO check if username is already taken
        return true;
    }

    private boolean checkColor(){
        //TODO check if color is already taken
        return true;
    }

    private int numberOfPlayers(){
        int players = 1;
        return players;
    }



}
