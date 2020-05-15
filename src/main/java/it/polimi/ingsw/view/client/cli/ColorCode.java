package it.polimi.ingsw.view.client.cli;

/**
 * This class represents escape codes that can set style and color of the text
 * @author aledimaio
 */

public enum ColorCode {

    ANSI_BLACK("\u001B[30m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_MAGENTA("\u001B[35m"),
    ANSI_CYAN("\u001B[36m"),
    ANSI_WHITE("\u001B[37m"),
    ANSI_RESET("\u001b[0m"),
    LEVEL_0_GREEN_BACKGROUND("\u001b[48;5;112m"),
    LEVEL_1_SAND_BACKGROUND("\u001b[48;5;112m"),
    LEVEL_2_GRAY_BACKGROUND("\u001b[48;5;180m"),
    LEVEL_3_WHITE_BACKGROUND("\u001b[48;5;253m"),
    LEVEL_DOME_BLUE_BACKGROUND("\u001b[48;5;32m");

    private String escape;

    ColorCode(String escape){
        this.escape = escape;
    }

    /**
     * This method allow to get directly the code by returning it as a String
     */

    public String escape(){
        return escape;
    }
}
