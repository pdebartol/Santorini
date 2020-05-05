package it.polimi.ingsw.network.client.cli;

/**
 * this class represents escape codes that allow control over the terminal interface
 * @author aledimaio
 */

public enum Escapes {

    CLEAR_ENTIRE_SCREEN("\033[2J"),
    CURSOR_RIGHT_INPUT_REQUIRED("\033[%dC"),
    CURSOR_HOME_0x0("\u001b[H"),
    SCREEN_640x480("\u001b[=18h"),
    MOVE_CURSOR_INPUT_REQUIRED("\033[%d;%dH"),
    SAVE_CURSOR_POSITION("\u001b[s"),
    RESTORE_CURSOR_POSITION("\u001b[u"),
    CURSOR_DOWN_INPUT_REQUIRED("\u001b[%dB"),
    CLEAR_SCREEN_FROM_HERE_TO_BEGINNING("\u001b[1J"),
    CLEAR_SCREEN_FROM_HERE_TO_END("\u001b[0J");

    private String escape;

    Escapes (String escape){
        this.escape = escape;
    }

    /**
     * This method allow to get directly the code by returning it as a String
     */

    public String escape(){
        return escape;
    }

}
