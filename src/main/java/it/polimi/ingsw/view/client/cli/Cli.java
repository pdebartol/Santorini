package it.polimi.ingsw.view.client.cli;

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
        String username;
        String color;

        do {
            elements.printInTextBox("Insert your username:");
            username = elements.Input();
        }while (!checkUsername());

        elements.eraseThings("text");

        do {
            elements.printInTextBox("Insert the color of your worker:");
            color = elements.Input();
        }while (!checkColor());

        //TODO create player

        elements.eraseThings("text");
        players = numberOfPlayers();

        if (players == 1) {
            elements.printInTextBox("You are the first player!");
            firstPlayer();
        }
        else{
            elements.printInTextBox("You are not the first player! There are already " + players + " players");
            notFirstPlayer();
        }


    }

    private void firstPlayer(){

    }

    private void notFirstPlayer(){

    }

    /**
     * This method represents the move process in the game
     */

    private void move(){

        int[] numbers;

        elements.printInTextBox("Select the square where you want to move your worker: (type #,#)");

        numbers = Arrays.stream(elements.Input().split(",")).mapToInt(Integer::parseInt).toArray();

        //TODO check if input is valid

        //TODO update board

        elements.eraseThings("text");

    }

    /**
     * This method represents the build process in the game
     */

    private void build(){

        int[] numbers;

        elements.printInTextBox("Select the square where you want your worker to build: (type #,#)");

        numbers = Arrays.stream(elements.Input().split(",")).mapToInt(Integer::parseInt).toArray();

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

    private void selectWorker(){
        //TODO send the position of selected workerd or send the entire move or build process (worker selection plus move)?
    }

}
