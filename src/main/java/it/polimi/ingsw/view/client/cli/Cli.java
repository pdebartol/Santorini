package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.msgUtilities.client.RequestMsgWriter;
import it.polimi.ingsw.view.client.InputValidator;
import it.polimi.ingsw.view.client.View;
import it.polimi.ingsw.view.client.cli.graphicComponents.Box;
import it.polimi.ingsw.view.client.cli.graphicComponents.ColorCode;
import it.polimi.ingsw.view.client.cli.graphicComponents.Escapes;
import it.polimi.ingsw.view.client.cli.graphicComponents.Unicode;
import it.polimi.ingsw.view.client.viewComponents.*;
import it.polimi.ingsw.view.client.viewComponents.Square;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * This class represents the game flow
 * @author aledimaio
 */

public class Cli extends View {

    private final String icon = Unicode.WORKER_ICON.escape();

    public Cli(){
        super();
        cliSetup();
    }

    /**
     * This method setup the terminal for the cli
     */

    public void cliSetup(){
        System.out.print(Escapes.CLEAR_ENTIRE_SCREEN.escape());
        printStartTemplate();
        gameSetup();
    }
    
    //View Override methods

    // TODO : javadoc

    @Override
    public void setMyIp() {
        printInStartTextBox("Insert the server IP address!");
        String ip = input();
        while(!InputValidator.validateIP(ip)){
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
        while(!InputValidator.validatePORT(port)){
            printInStartTextBox("Invalid port! Please, try again.");
            port = input();
        }

        myPort = Integer.parseInt(port);
    }

    //TODO : javadoc

    @Override
    public void setUsername(boolean rejectedBefore) {
        printInStartTextBox("Insert your username (must be at least 3 characters long and no more than 15, valid characters: A-Z, a-z, 1-9, _)");
        String username = input();
        while(!InputValidator.validateUSERNAME(username)){
            printInStartTextBox("Invalid username! It must be at least 3 characters long and no more than 15, valid characters: A-Z, a-z, 1-9, _, try again!");
            username = input();
        }
        sendLoginRequest(username);
    }

    //TODO : javadoc

    @Override
    public void startMatch() {
        String input;
        if(players.size() == 1) {
            appendInStartTextBox("You are currently 2 players in the game, enter \"s\" to start the game now or \"w\" to wait for a third player!");
            do{
                input = input();
            }while(!input.equals("s") && !input.equals("w"));

            if(input.equals("s")) sendStartGameRequest();
            else printInStartTextBox("Wait for a third player...");
        }else {
            appendInStartTextBox("You are currently 3 players in the game, press enter to start the game now! The match will automatically start in 3 minutes");
            inputWithTimeout(3, TimeUnit.MINUTES);
            sendStartGameRequest();
        }
    }

    //TODO : javadoc

    @Override
    public void selectGods() {

    }

    //TODO : javadoc

    @Override
    public void showLoginDone() {
        StringBuilder message;
        message = new StringBuilder("Hi " + myPlayer.getUsername() + ", you're in!");
        if(players.size() == 0) message.append(" You're the creator of this match, so you will decide "
                + "when to start the game. You can either start it when another player logs in or wait for a third player. "
                + "The moment the third player logs in you can start the game, which will still start automatically after 2 minutes "
                + "from the login of the third player.");
        else
            message.append(" You're currently ").append(players.size() + 1).append(" players in this game : You");
            for(Player player : players)
                message.append(", ").append(player.getUsername());
        printInStartTextBox(message.toString());
    }

    //TODO : javadoc

    @Override
    public void showNewUserLogged(String username, Color color) {
        printInStartTextBox(username + " is a new player!");
    }

    //TODO : javadoc

    @Override
    public void showWaitMessage(String waitFor, String author) {
        appendInStartTextBox("Waiting for creator's start game command...");
    }

    //TODO : javadoc

    @Override
    public void showMatchStarted() {
        System.out.print(Escapes.CLEAR_ENTIRE_SCREEN.escape());
        printGameTemplate();
        printInStartTextBox("the match has been started...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO : javadoc

    @Override
    public void showBoard() {

    }

    //TODO : javadoc

    @Override
    public void serverNotFound() {
        printInStartTextBox("Server not found!");
        try {
            Thread.sleep(3000);
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
        printInStartTextBox("A client has disconnected from the game, the match has been deleted! Do you want to try to search a new game? (s/n)");
        String input;
        do{
            input = input();
        }while(!input.equals("s") && !input.equals("n"));

        if(input.equals("s")){
            newGame();
            start();
        }
        else System.exit(0);
    }

    //TODO : javadoc

    @Override
    public void showDisconnectionForLobbyNoLongerAvailable() {
        printInStartTextBox("too long, the lobby you were entered into has already started! Do you want to try to search a new game? (s/n)");
        String input;
        do{
            input = input();
        }while(!input.equals("s") && !input.equals("n"));

        if(input.equals("s")){
            newGame();
            start();
        }
        else System.exit(0);
    }

    //TODO : javadoc

    @Override
    public void showServerDisconnection() {
        printInStartTextBox("The server has disconnected! Do you want to try to reconnect? (s/n)");
        String input;
        do{
            input = input();
        }while(!input.equals("s") && !input.equals("n"));

        if(input.equals("s")){
            newGame();
            gameSetup();
        }
        else System.exit(0);
    }

    //TODO : javadoc

    @Override
    public void disconnectionForInputExpiredTimeout() {
        printInStartTextBox("The timeout to do your action has expired, " +
                "you were kicked out of the game! Do you want to try to search a new game? (s/n)");
        String input;
        do{
            input = input();
        }while(!input.equals("s") && !input.equals("n"));

        if(input.equals("s")){
            newGame();
            start();
        }
        else System.exit(0);
    }

    //Frame methods

    /**
     * The printTemplate method print the background visual elements, the frame of game
     */

    public void printStartTemplate(){

        //draw the top line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_RIGHT.escape());
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() - 1 ; i++) {
            if(i == Box.TEXT_START.escape()) {
                System.out.print("Login");
                i += 5;
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


        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.CREDITS_START_FROM_UP.escape(), Box.CREDITS_START_LEFT.escape());
        System.out.println(ColorCode.ANSI_CYAN.escape() + "          Software engineering project, AM10 group, credits to:");
        System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.CREDITS_START_LEFT.escape());
        System.out.print( ColorCode.ANSI_CYAN.escape() + "    Piersilvio De Bartolomeis, Marco Di Gennaro, Alessandro Di Maio" + ColorCode.ANSI_RESET.escape());

        printSantorini();




    }

    public void printGameTemplate(){

        //draw the top line

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_RIGHT.escape());
        for (int i = 1; i < Box.HORIZONTAL_DIM.escape() - 1 ; i++) {
            if(i == Box.TEXT_START.escape()) {
                System.out.print("Game Board");
                i += 10;
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

        //draw the players vertical line

        /*
        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.PLAYERS_BOX_START.escape());
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_HORIZONTAL.escape());
        System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.TEXT_START.escape());
        System.out.println("Players");
        for (int i = 1; i < (Box.TEXT_BOX_START.escape() - 1) ; i++) {
            System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.PLAYERS_BOX_START.escape());
            System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
        }
        System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.PLAYERS_BOX_START.escape());
        System.out.println(Unicode.BOX_DRAWINGS_HEAVY_UP_AND_HORIZONTAL.escape());
         */

        //draw player box

        System.out.print(Escapes.CURSOR_HOME_0x0.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.PLAYER_BOX_START_LINE.escape(), Box.PLAYERS_BOX_START.escape());
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_DOWN_AND_RIGHT.escape());
        for (int i = Box.PLAYERS_BOX_START.escape(); i < (Box.HORIZONTAL_DIM.escape() - 1) ; i++)
            System.out.print(Unicode.BOX_DRAWINGS_HEAVY_HORIZONTAL.escape());
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL_AND_LEFT.escape());
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), (Box.PLAYER_BOX_START_LINE.escape() + 1), Box.PLAYERS_BOX_START.escape());
        for (int i = (Box.PLAYER_BOX_START_LINE.escape() + 1); i < (Box.TEXT_BOX_START.escape() - 1); i++){
            System.out.println(Unicode.BOX_DRAWINGS_HEAVY_VERTICAL.escape());
            System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.PLAYERS_BOX_START.escape());
        }
        System.out.print(Unicode.BOX_DRAWINGS_HEAVY_UP_AND_HORIZONTAL.escape());

        //print player name in players' box

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.PLAYER_BOX_START_LINE.escape() + 1, Box.PLAYERS_BOX_START.escape() + 1);
        System.out.println(myPlayer.getUsername());
        for (int i = 0; i < players.size(); i++) {
            System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.PLAYERS_BOX_START.escape() + 1);
            System.out.println(players.get(i).getUsername());
        }


    }

    /**
     * This method prints "Santorini" in the game frame
     */

    public void printSantorini(){

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.ASCII_ART_START_UP.escape(), Box.ASCII_ART_START_LEFT.escape() + 1);
        System.out.print(ColorCode.ANSI_BLUE.escape() +
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

    public void printLoser(){

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.ASCII_ART_START_UP.escape(), Box.ASCII_ART_START_LEFT.escape() + 1);
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

    public void printWinner(){

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
     * @param thing represents the part of frame that will be erased
     */

    public void eraseThings(String thing){

        switch (thing){

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
        }


    }
    
    //Board methods

    public void printBoard(){

        //print 1,2,3,4,5 vertical board reference
        for (int i = Board.DIMENSION, j = 0; i > 0; i--, j += Box.SQUARE_DIMENSION.escape()) {
            System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.BOARD_START_UP.escape() + j, Box.BOARD_START_LEFT.escape() - 2);
            System.out.printf("%d", i - 1 );
        }

        //This cycle prints 0,0 position in the left-bottom corner

        for (int x = Board.DIMENSION - 1, i = 0 , j = 0; x > -1 ; x-- , i += Box.SQUARE_HORIZONTAL_DIM.escape(), j++) {
            System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.BOARD_START_UP.escape(), Box.BOARD_START_LEFT.escape() + i - 1);
            for (int y = Board.DIMENSION - 1; y > - 1 ; y--) {
                drawSquare(x,y);
                System.out.print("\n");
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.BOARD_START_LEFT.escape() + i - 2);
            }
            //print 1,2,3,4,5 horizontal board reference
            System.out.print(ColorCode.ANSI_RESET.escape());
            System.out.print(j);
        }

    }

    public void drawSquare(int x, int y){

        Square square = gameBoard.getSquareByCoordinates(x,y);

        //change color of single square based on level of square

        if(square.getDome())
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

        if (square.getWorker() != null) {
            System.out.println(Escapes.SAVE_CURSOR_POSITION.escape() + Color.getColorCodeByColor(square.getWorker().getColor()).escape() + icon + Unicode.SQUARE_HORIZONTAL_DIM_MIN1.escape()
                    + Escapes.RESTORE_CURSOR_POSITION.escape());
            for (int i = 1; i < Box.SQUARE_DIMENSION.escape() - 1 ; i++) {
                System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + Unicode.SQUARE_HORIZONTAL_DIM.escape()
                        + Escapes.RESTORE_CURSOR_POSITION.escape());
                System.out.printf(Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);
            }
            System.out.print(Unicode.SQUARE_HORIZONTAL_DIM_MIN1.escape() + ColorCode.ANSI_BLACK.escape() + square.getLevel() + ColorCode.ANSI_RESET.escape());
        }
        else {
            for (int i = 0; i < Box.SQUARE_DIMENSION.escape() - 1 ; i++) {
                System.out.print(Escapes.SAVE_CURSOR_POSITION.escape() + Unicode.SQUARE_HORIZONTAL_DIM.escape()
                        + Escapes.RESTORE_CURSOR_POSITION.escape());
                System.out.printf(Escapes.CURSOR_DOWN_INPUT_REQUIRED.escape(), 1);
            }
            System.out.print(Unicode.SQUARE_HORIZONTAL_DIM_MIN1.escape() + ColorCode.ANSI_BLACK.escape() + square.getLevel() + ColorCode.ANSI_RESET.escape());
        }
    }
    
    //Worker methods
    
    
    
    //Text methods

    /**
     * This method is used to get input from keyboard without any control
     * @return the string from input
     */

    public String input(){

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.INPUT_BOX_START.escape() + 1, 2);
        System.out.print(">");
        return InputCli.readLine();

    }

    //TODO : javadoc

    public String inputWithTimeout(int timeout, TimeUnit unit) {
        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.INPUT_BOX_START.escape() + 1, 2);
        System.out.print(">");
        String input = "";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> result = executor.submit(InputCli::readLine);

        try {
            input = result.get(timeout, unit);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            return "expiredTimeout";
        }

        return input;
    }


    public void printInStartTextBox(String text){

        char[] information = text.toCharArray();

        eraseThings("text");
        printStartTemplate();

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


    public void appendInStartTextBox(String text){
        char[] information = text.toCharArray();


        printStartTemplate();

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.TEXT_BOX_START.escape() + 1, 2);

        //this cycle allow to avoid that text exceed the frame length

        System.out.print("\n");
        System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), 1);
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
     * This method prints text in text frame
     * @param text represents the text that will be printed
     */

    public void printInGameTextBox(String text){

        char[] information = text.toCharArray();

        eraseThings("text");
        printGameTemplate();

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

    public void printInPlayerBox(String text){

        char[] information = text.toCharArray();

        eraseThings("player");
        printGameTemplate();

        System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), 1, Box.PLAYERS_BOX_START.escape() + 1);

        for (int i = 2, j = 0; j < information.length; i++, j++) {
            System.out.print(information[j]);
            if (i == Box.HORIZONTAL_DIM.escape() - 2){
                System.out.print("-\n");
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.PLAYERS_BOX_START.escape());
                i = 1;
            }
        }

    }
    
    //Gods methods

    /**
     * This method prints Gods on screen
     * @param i indicates if will be prints the God of player or Gods of other players
     */

    public void printStartDivinities(int i){

        if (i == 1) {
            printInStartTextBox(myPlayer.getGod().getName() + "\n\u001b[1C" + myPlayer.getGod().getDescription() + "enter for continue");
            input();
        }
        else{
            for (int j = 0; j < players.size() ; j++) {
                printInStartTextBox(players.get(j).getGod().getName() + "\n\u001b[1C" + players.get(j).getGod().getDescription()
                        + "\n\u001b[1C" + (j + 1) + " of " + players.size() + " enter for continue");
                input();
            }
        }

    }

    public void printGameDivinities(int i){

        if (i == 1) {
            printInGameTextBox(myPlayer.getGod().getName() + "\n\u001b[1C" + myPlayer.getGod().getDescription() + "enter for continue");
            input();
        }
        else{
            printInGameTextBox(myPlayer.getGod().getName() + "\n\u001b[1C" + myPlayer.getGod().getDescription() + "enter for continue");
            for (int j = 0; j < players.size() ; j++) {
                printInGameTextBox(players.get(j).getGod().getName() + "\n\u001b[1C" + players.get(j).getGod().getDescription()
                        + "\n\u001b[1C" + (j + 1) + " of " + players.size() + " enter for continue");
                input();
            }
        }

    }
    
    /**
     * This method represents the game setup
     */

    public void gameSetup(){

        printInStartTextBox("Press enter button to start");
        input();

        //Connection setup
        setMyIp();
        setMyPort();

        //start connection
        start();
    }

    /**
     * This game represents an ordinary turn
     */

    public void gameTurn(){

        boolean notOverYet = true;

            while(notOverYet) {

                eraseThings("all");
                printBoard();
                printInGameTextBox("Insert command (type \"show commands\" for help)");

                switch (input()) {

                    case "move":
                        eraseThings("text");
                        move();
                        notOverYet = !endTurn();
                        break;

                    case "build":
                        eraseThings("text");
                        build();
                        notOverYet = !endTurn();
                        break;

                    case "show my divinity":
                        printGameDivinities(1);
                        break;

                    case "show other divinities":
                        printGameDivinities(0);
                        break;

                    case "show commands":
                        eraseThings("text");
                        printInGameTextBox("move - build - show my divinity - show other divinities || Press enter to continue");
                        input();
                        break;

                    case "quit":
                        eraseThings("text");
                        notOverYet = false;

                    default:
                        eraseThings("text");
                        printInGameTextBox("Wrong command! Retype it! - Press enter to continue");
                        input();

                }
            }

    }

    private void challenger(List<God> gods){
        ArrayList<God> chosenGods = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        boolean check = false;
        int i = 0;

        //TODO get god from server and put them in the ArrayList
        //TODO get other players' username and put them in the ArrayList (included the active player)

        printInStartTextBox("You are the challenger! Now you have to chose the divinities for all players, to chose one" +
                "enter \"this\" when Name of God and his description is displayed. Now press enter to continue:");
        input();

        eraseThings("text");

        printInStartTextBox("\"this\" to select God, \"next\" to go to next God's card, \"prev\" to go to previously God's card" +
                "\"show command\" to show commands avaiable. Press enter to continue:");
        input();

        while (chosenGods.size() < 3) {

            switch (input()) {

                case "next":
                    printInStartTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                            gods.get(i).getDescription());
                    i++;
                    if (i > gods.size()) i = 0;
                    break;

                case "prev":
                    i--;
                    if (i < 0) i = gods.size();
                    printInStartTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                            gods.get(i).getDescription());
                    break;

                case "this":
                    eraseThings("text");
                    printInStartTextBox("Confirm selection of " + gods.get(i) + "? Type \"y\" for yes or anything else for no");
                    if (input() == "y")
                        chosenGods.add(gods.get(i));
                    else {
                        eraseThings("text");
                        printInStartTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                                gods.get(i).getDescription());
                    }
                    break;

                case "show commands":
                    printInStartTextBox("\"this\" to select God, \"next\" to go to next God's card, \"prev\" to go to previously God's card" +
                            "\"show command\" to show commands avaiable. Press enter to continue:");
                    input();
                    printInStartTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() +  "\n" +
                            gods.get(i).getDescription());
                    break;
            }
        }

        eraseThings("text");

        printInStartTextBox("Now other players are choosing their god, wait until they end!");

        //TODO set the last god left as the god of the challenger

        eraseThings("text");
        printInStartTextBox("Now you have to chose the order of players by choosing the first one:");

        while (check) {

            i = 0;

            switch (input()) {

                case "next":
                    eraseThings("text");
                    printInStartTextBox(players.get(i).getUsername());
                    i++;
                    if (i > gods.size()) i = 0;
                    break;

                case "prev":
                    i--;
                    if (i < 0) i = gods.size();
                    eraseThings("text");
                    printInStartTextBox(players.get(i).getUsername());
                    break;

                case "this":
                    eraseThings("text");
                    printInStartTextBox("Confirm selection of " + players.get(i).getUsername() + "? Type \"y\" for yes or anything else for no");
                    if (input().equals("y")) {
                        //TODO send the selected player to server
                        check = true;
                        eraseThings("text");
                    }
                    else {
                        eraseThings("text");
                        printInStartTextBox(players.get(i).getUsername());
                    }
                    break;

                case "show commands":
                    eraseThings("text");
                    printInStartTextBox("\"this\" to select Player, \"next\" to go to next Player's username, \"prev\" to go to previously Player's username" +
                            "\"show command\" to show commands available. Press enter to continue:");
                    input();
                    printInStartTextBox(players.get(i).getUsername());
                    break;
            }
        }


    }

    private void notChallenger(){

        int i = 0;
        ArrayList<God> gods = new ArrayList<>();

        //TODO get remaining gods from server that have been previously chosen by challenger

        while (myPlayer.getGod() != null) {

            switch (input()) {

                case "next":
                    printInStartTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                            gods.get(i).getDescription());
                    i++;
                    if (i > gods.size()) i = 0;
                    break;

                case "prev":
                    i--;
                    if (i < 0) i = gods.size();
                    printInStartTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                            gods.get(i).getDescription());
                    break;

                case "this":
                    printInStartTextBox("Confirm selection of " + gods.get(i) + "? Type \"y\" for yes or anything else for no");
                    if (input() == "y")
                        myPlayer.setGod(new God(gods.get(i).getId(),gods.get(i).getName(), gods.get(i).getDescription()));
                    else {
                        printInStartTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() + "\n" +
                                gods.get(i).getDescription());
                    }
                    break;

                case "show commands":
                    printInStartTextBox("\"this\" to select God, \"next\" to go to next God's card, \"prev\" to go to previously God's card" +
                            "\"show command\" to show commands avaiable. Press enter to continue:");
                    input();
                    printInStartTextBox(gods.get(i).getName() + " " + (i + 1) + " of " + gods.size() +  "\n" +
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

        printInGameTextBox("Select the square where you want to move your worker: (type #,#)");

        numbers = Arrays.stream(input().split(",")).mapToInt(Integer::parseInt).toArray();

        //TODO check if input is valid (check via server?)

        //update board after server update
        //TODO add Apollo case management

        gameBoard.getSquareByCoordinates(numbers[0], numbers[1]).placeWorker(gameBoard.getSquareByCoordinates(workerPosition[0], workerPosition[1]).getWorker());
        gameBoard.getSquareByCoordinates(workerPosition[0], workerPosition[1]).placeWorker(null);

        eraseThings("text");

    }

    private int[] selectWorker(){

        int[] numbers;

        do {
            eraseThings("text");
            printInGameTextBox("Select the worker: (type #,#)");

            numbers = Arrays.stream(input().split(",")).mapToInt(Integer::parseInt).toArray();

        }while(gameBoard.getSquareByCoordinates(numbers[0], numbers[1]).getWorker() == null);

        //TODO change color of square of selected worker

        return numbers;

    }

    /**
     * This method represents the build process in the game
     */

    private void build(){

        int[] numbers;

        printInGameTextBox("Select the square where you want your worker to build: (type #,#)");

        numbers = Arrays.stream(input().split(",")).mapToInt(Integer::parseInt).toArray();

        //TODO not necessary to provide the level wanted because it always increment by one, but is this game logic in view?

        //TODO check if input is valid

        //TODO update board

        eraseThings("text");

    }

    private boolean endTurn(){
        //TODO check if turn is over
        return false;
    }

}
