package it.polimi.ingsw.view.client.cli;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class represents visual elements on the cli, it contain methods to draw texts and "graphics" elements
 * @author aledimaio
 */

public class Elements {

    private Player player = null;
    private ArrayList<Player> otherPlayer = null;
    private Board board;

    public Elements() {
        this.board = new Board();
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Player> getOtherPlayer() {
        return otherPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * The printTemplate method print the background visual elements, the frame of game
     */

    public void printTemplate(){

        //draw the top line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_RIGHT.escape());
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() - 1 ; i++) {
            if(i == Box.TEXT_START.escape()) {
                System.out.print("Game");
                i += 4;
            }
            System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_LEFT.escape());

        //draw the left line

        System.out.println(Escapes.CURSOR_HOME_0x0.escape());
        for (int i = 1; i < Box.VERTICAL_DIM.escape() ; i++) {
            if(i == Box.TEXT_BOX_START.escape() - 1 || i == Box.INPUT_BOX_START.escape() - 1)
                System.out.println(Unicode.BOX_RAWINGS_HEAVY_VERTICAL_AND_RIGHT.escape());
            else
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_UP_AND_RIGHT.escape());

        //draw the bottom line

        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() - 1 ; i++) {
            System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_UP_AND_LEFT.escape());

        //draw the right line

        System.out.println(Escapes.CURSOR_HOME_0x0.escape());
        //System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), 0, 1);
        for (int i = 1; i < Box.VERTICAL_DIM.escape() ; i++) {
            System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.HORIZONTAL_DIM.escape() - 1);
            if(i == Box.TEXT_BOX_START.escape() - 1 || i == Box.INPUT_BOX_START.escape() - 1)
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL_AND_LEFT.escape());
            else
                System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
        }

        //draw the text line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape(), 2);
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() ; i++) {
            if (i == Box.TEXT_START.escape()) {
                System.out.print("Text");
                i += 4;
            }
            else
                System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }

        //draw the input line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.INPUT_BOX_START.escape(), 2);
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() ; i++) {
            if (i == Box.TEXT_START.escape()) {
                System.out.print("Input");
                i += 5;
            }
            else
                System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        }


    }

    /**
     * This method is used to get input from keyboard without any control
     * @return the string from input
     */

    public String Input(){

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.INPUT_BOX_START.escape() + 1, 2);
        System.out.print(">");
        Scanner input = new Scanner(System.in);
        return input.nextLine();

    }

    /**
     * This method prints text in text frame
     * @param text represents the text that will be printed
     */

    public void printInTextBox(String text){

        char[] information = text.toCharArray();

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape() + 1, 2);

        //this cycle allow to avoid that text exceed the frame length

        for (int i = 2, j = 0; j < information.length; i++, j++) {
            System.out.print(information[j]);
            if (i == Box.HORIZONTAL_DIM.escape() - 2){
                System.out.print("-\n");
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), 1);
                i = 1;
            }
        }


    }

    /**
     * This method prints "Santorini" in the game frame
     */

    public void printSantorini(){

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.ASCIIART_START_UP.escape(), Box.ASCIIART_START_LEFT.escape() + 1);
        System.out.print(Color.ANSI_BLUE.escape() +
                "  _____  ____  ____   ______   ___   ____   ____  ____   ____ \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                " / ___/ /    ||    \\ |      | /   \\ |    \\ |    ||    \\ |    |\n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "(   \\_ |  o  ||  _  ||      ||     ||  D  ) |  | |  _  | |  | \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                " \\__  ||     ||  |  ||_|  |_||  O  ||    /  |  | |  |  | |  | \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                " /  \\ ||  _  ||  |  |  |  |  |     ||    \\  |  | |  |  | |  | \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                " \\    ||  |  ||  |  |  |  |  |     ||  .  \\ |  | |  |  | |  | \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "  \\___||__|__||__|__|  |__|   \\___/ |__|\\_||____||__|__||____|\n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "                                                              \n" + Color.ANSI_RESET.escape());

    }

    /**
     * This method prints "Loser" in the game frame
     */

    public void printLoser(){

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.ASCIIART_START_UP.escape(), Box.ASCIIART_START_LEFT.escape() + 1);
        System.out.print(Color.ANSI_RED.escape() +
                " (        )   (         (     \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                " )\\ )  ( /(   )\\ )      )\\ )  \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "(()/(  )\\()) (()/( (   (()/(  \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                " /(_))((_)\\   /(_)))\\   /(_)) \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "(_))    ((_) (_)) ((_) (_))   \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "| |    / _ \\ / __|| __|| _ \\  \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "| |__ | (_) |\\__ \\| _| |   /  \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "|____| \\___/ |___/|___||_|_\\  \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "                              \n" + Color.ANSI_RESET.escape());

    }

    /**
     * This method prints "You win" in the game frame
     */

    public void printYouWon(){

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.ASCIIART_START_UP.escape(), Box.ASCIIART_START_LEFT.escape() + 1);
        System.out.print(Color.ANSI_CYAN.escape() +
                " __  __   ______   __  __       __     __   ______   __   __    \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "/\\ \\_\\ \\ /\\  __ \\ /\\ \\/\\ \\     /\\ \\  _ \\ \\ /\\  __ \\ /\\ \"-.\\ \\   \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "\\ \\____ \\\\ \\ \\/\\ \\\\ \\ \\_\\ \\    \\ \\ \\/ \".\\ \\\\ \\ \\/\\ \\\\ \\ \\-.  \\  \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                " \\/\\_____\\\\ \\_____\\\\ \\_____\\    \\ \\__/\".~\\_\\\\ \\_____\\\\ \\_\\\\\"\\_\\ \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "  \\/_____/ \\/_____/ \\/_____/     \\/_/   \\/_/ \\/_____/ \\/_/ \\/_/ \n" + "\u001b[" + Box.ASCIIART_START_LEFT.escape() + "C" +
                "                                                                \n" + Color.ANSI_RESET.escape());

    }

    /**
     * This method prints Gods on screen
     * @param i indicates if will be prints the God of player or Gods of other players
     */

    public void printDivinities(int i){

        if (i == 1) {
            printInTextBox(player.getGod().getName() + "\n\u001b[1C" + player.getGod().getDescription() + "enter for continue");
            Input();
        }
        else{
            for (int j = 0; j < otherPlayer.size() ; j++) {
                printInTextBox(otherPlayer.get(j).getGod().getName() + "\n\u001b[1C" + otherPlayer.get(j).getGod().getDescription()
                + "\n\u001b[1C" + (j + 1) + " of " + otherPlayer.size() + " enter for continue");
                Input();
            }
        }

    }

    /**
     * This method erase part of the general frame
     * @param thing represents the part of frame that will be erased
     */

    public void eraseThings(String thing){

        switch (thing){

            case "all":
                System.out.print(Escapes.CLEAR_ENTIRE_SCREEN.escape());
                printTemplate();
            break;

            case "game":
                System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape() - 1, Box.HORIZONTAL_DIM.escape() - 1);
                System.out.print(Escapes.CLEAR_SCREEN_FROM_HERE_TO_BEGINNING.escape());
                printTemplate();
            break;

            case "text":
                System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape(), 0);
                System.out.print(Escapes.CLEAR_SCREEN_FROM_HERE_TO_END.escape());
                printTemplate();

        }


    }

}
