package it.polimi.ingsw.view.client;
import java.util.regex.Pattern;

/**
 * This class contains utilities to validate user input entered through the interface through the interface.
 * It uses regular expressions to achieve its purpose.
 * @author marcoDige
 */

public class InputValidator {

    //attributes

    /**
     * This REGEXP is used to validate the IP address entered by the user
     */

    private final static String IP_REGEXP = "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\." +
            "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\." +
            "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\." +
            "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])";

    /**
     * This REGEXP is used to validate the port number entered by the user
     */

    private final static String PORT_REGEXP = "(^(1)0[2-9]\\d$)|(^(1)[2-9](\\d{2}$))|(^[2-9](\\d{3}$))|(^[1-6](\\d{4})$)";

    /**
     * This REGEXP is used to validate username entered by the user
     */

    private final static String USERNAME_REGEXP = "^[A-Za-z0-9_-]{3,10}$";

    /**
     * This REGEXP is used to validate the coordinates entered by the user
     */

    private final static String COORDINATES_REGEXP = "^[0-4],[0-4]$";

    /**
     * This REGEXP is used to validate the building level entered by the user
     */

    private final static String LEVEL_REGEXP = "[1-4]";

    //methods

    /**
     * This method check that input matches to IP_REGEXP
     * @param input is the input of the user
     * @return true : input matches to IP_REGEXP
     *         false : input doesn't matches to IP_REGEXP
     */

    public static boolean validateIP(String input){
        return Pattern.matches(IP_REGEXP,input);
    }

    /**
     * This method check that input matches to PORT_REGEXP
     * @param input is the input of the user
     * @return true : input matches to PORT_REGEXP
     *         false : input doesn't matches to PORT_REGEXP
     */

    public static boolean validatePORT(String input){
        return Pattern.matches(PORT_REGEXP,input);
    }

    /**
     * This method check that input matches to USERNAME_REGEXP
     * @param input is the input of the user
     * @return true : input matches to USERNAME_REGEXP
     *         false : input doesn't matches to USERNAME_REGEXP
     */

    public static boolean validateUSERNAME(String input){return Pattern.matches(USERNAME_REGEXP,input);}

    /**
     * This method check that input matches to COORDINATES_REGEXP
     * @param input is the input of the user
     * @return true : input matches to COORDINATES_REGEXP
     *         false : input doesn't matches to COORDINATES_REGEXP
     */

    public static boolean validateCOORDINATES(String input){
        return Pattern.matches(COORDINATES_REGEXP,input);
    }

    /**
     * This method check that input matches to LEVEL_REGEXP
     * @param input is the input of the user
     * @return true : input matches to LEVEL_REGEXP
     *         false : input doesn't matches to LEVEL_REGEXP
     */

    public static boolean validateLEVEL(String input) {return Pattern.matches(LEVEL_REGEXP,input);}
}
