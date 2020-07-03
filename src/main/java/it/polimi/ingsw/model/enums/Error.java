package it.polimi.ingsw.model.enums;


/**
 * Error is the enum representing the error codes showing up when the player moves or builds.
 * @author pierobartolo AND marcoDige
 */

public enum Error {
    SWAP_MY_WORKER("SMW"),
    ISNT_WORKER_CHOSEN("IWC"),
    BLOCK_MOVE_UP("BMU"),
    BUILDS_EXCEEDED("BE"),
    BUILD_BEFORE_MOVE("BBM"),
    CANT_DOME_UNDERFOOT("CDU"),
    CANT_MOVE_UP("CMU"),
    EXTRA_BUILD_NOT_PERIMETER("EBNP"),
    EXTRA_BUILD_NOT_SAME_SPACE("EBNSS"),
    EXTRA_BUILD_ONLY_SAME_SPACE("EBOSS"),
    EXTRA_MOVE_NOT_BACK("EMNB"),
    INVALID_LEVEL_BUILD("ILB"),
    INVALID_LEVEL_MOVE("ILM"),
    IS_DOME("ID"),
    MOVES_EXCEEDED("ME"),
    MOVE_AFTER_BUILD("MAB"),
    NOT_ADJACENT("NA"),
    NOT_FREE("NF"),
    SAME_DIRECTION_NOT_FREE("SDNF"),
    EXTRA_BUILD_NOT_DOME("EBND"),
    LOGIN_USERNAME_NOT_AVAILABLE("LUNA"),
    LOGIN_COLOR_NOT_AVAILABLE("LCNA"),
    LOGIN_TOO_MANY_PLAYERS("LTMP"),
    SETUP_IS_NOT_CHALLENGER("SINC"),
    SETUP_WORKER_ON_OCCUPIED_SQUARE("SWOOS"),
    SETUP_WORKER_ALREADY_SET("SWAS"),
    INGAME_NOT_YOUR_TURN("INYT"),
    INGAME_CANNOT_END_TURN("ICET"),
    INGAME_WRONG_WORKER("IWW");



    private final String label;

    Error(String label){
        this.label = label;
    }

    /**
     * This method allows to obtain the Error value from his label
     * @param label is the Error to obtain's label
     * @return an Error
     */

    public static Error valueOfLabel(String label) {
        for (Error err : values()) {
            if (err.label.equals(label)) {
                return err;
            }
        }
        return null;
    }

    /**
     * This method allows to obtain the Error's label
     * @param e is the label to obtain's Error
     * @return a label
     */

    public static String labelOfEnum(Error e){
        for (Error err : values()) {
            if (err.equals(e)) {
                return err.label;
            }
        }
        return null;
    }
}
