package it.polimi.ingsw.view.client;

import java.util.regex.Pattern;

//TODO : javadoc

public class InputValidator {

    //attributes

    private final static String IP_REGEXP = "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\." +
            "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\." +
            "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\." +
            "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])";
    private final static String PORT_REGEXP = "(^(1)0[2-9]\\d$)|(^(1)[2-9](\\d{2}$))|(^[2-9](\\d{3}$))|(^[1-6](\\d{4})$)";
    private final static String USERNAME_REGEXP = "^[A-Za-z0-9_-]{3,10}$";
    private final static String COORDINATES_REGEXP = "^[0-4],[0-4]$";
    private final static String LEVEL_REGEXP = "[1-4]";

    //methods

    //TODO : javadoc

    public static boolean validateIP(String input){
        return Pattern.matches(IP_REGEXP,input);
    }

    //TODO : javadoc

    public static boolean validatePORT(String input){
        return Pattern.matches(PORT_REGEXP,input);
    }

    //TODO : javadoc

    public static boolean validateUSERNAME(String input){return Pattern.matches(USERNAME_REGEXP,input);}

    //TODO : javadoc

    public static boolean validateCOORDINATES(String input){
        return Pattern.matches(COORDINATES_REGEXP,input);
    }

    //TODO : javadoc

    public static boolean validateLEVEL(String input) {return Pattern.matches(LEVEL_REGEXP,input);}
}
