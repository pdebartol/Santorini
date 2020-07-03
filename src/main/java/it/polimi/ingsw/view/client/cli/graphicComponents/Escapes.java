package it.polimi.ingsw.view.client.cli.graphicComponents;

/**
 * this class represents escape codes that allow control over the terminal interface
 * @author aledimaio
 */

public enum Escapes {

    CLEAR_ENTIRE_SCREEN("\033[2J"),
    CURSOR_RIGHT_INPUT_REQUIRED("\033[%dC"),
    CURSOR_LEFT_INPUT_REQUIRED("\033[%dD"),
    CURSOR_UP_INPUT_REQUIRED("\033[%dA"),
    CURSOR_DOWN_INPUT_REQUIRED("\033[%dB"),
    CURSOR_HOME_0x0("\033[H"),
    SCREEN_640x480("\033[=18h"),
    MOVE_CURSOR_INPUT_REQUIRED("\033[%d;%dH"),
    SAVE_CURSOR_POSITION("\033[s"),
    RESTORE_CURSOR_POSITION("\033[u"),
    CLEAR_LINE_FROM_CURSOR_TO_END("\033[0K"),
    CLEAR_SCREEN_FROM_HERE_TO_BEGINNING("\033[1J"),
    CLEAR_SCREEN_FROM_HERE_TO_END("\033[0J");

    private final String escape;

    Escapes (String escape){
        this.escape = escape;
    }

    /**
     * This method allow to get directly the code by returning it as a String
     * @return the desired value as a String
     */

    public String escape(){
        return escape;
    }

}
