package it.polimi.ingsw.model.enums;


/**
 * Error is the enum representing the error codes showing up when the player moves or builds.
 * @author pierobartolo
 */

public enum Error {
    ISNT_WORKER_CHOSEN("isntWorkerChosen"),
    BLOCK_MOVE_UP("blockMoveUp"),
    BUILDS_EXCEEDED("buildsExceeded"),
    BUILD_BEFORE_MOVE("buildBeforeMove"),
    CANT_DOME_UNDERFOOT("cantDomeUnderfoot"),
    CANT_MOVE_UP("cantMoveUp"),
    EXTRA_BUILD_NOT_PERIMETER("extraBuildNotPerimeter"),
    EXTRA_BUILD_NOT_SAME_SPACE("extraBuildNotSameSpace"),
    EXTRA_BUILD_ONLY_SAME_SPACE("extraBuildOnlySameSpace"),
    EXTRA_MOVE_NOT_BACK("extraMoveNotBack"),
    INVALID_LEVEL_BUILD("invalidLevelBuild"),
    INVALID_LEVEL_MOVE("invalidLevelMove"),
    IS_DOME("isDome"),
    MOVES_EXCEEDED("movesExceeded"),
    MOVE_AFTER_BUILD("moveAfterBuild"),
    NOT_ADJACENT("notAdjacent"),
    NOT_FREE("notFree"),
    SAME_DIRECTION_NOT_FREE("sameDirectionNotFree"),
    EXTRA_BUILD_NOT_DOME("extra"),
    LOGIN_USERNAME_NOT_AVAILABLE("loginUsernameNotAvailable"),
    LOGIN_COLOR_NOT_AVAILABLE("loginColorNotAvailable"),
    LOGIN_TOO_MANY_PLAYERS("LoginTooManyPlayers"),
    SETUP_IS_NOT_CHALLENGER("SetupIsNotChallenger"),
    SETUP_WORKER_ON_OCCUPIED_SQUARE("SetupWorkerOnOccupiedSquare"),
    SETUP_WORKER_ALREADY_SET("SetupWorkerAlreadySet"),
    INGAME_NOT_YOUR_TURN("InGameNotYourTurn"),
    INGAME_CANNOT_END_TURN("InGameCannotEndTurn"),
    INGAME_WRONG_WORKER("InGameWrongWorker");



    private final String label;

    Error(String label){
        this.label = label;
    }

    public static Error valueOfLabel(String label) {
        for (Error err : values()) {
            if (err.label.equals(label)) {
                return err;
            }
        }
        return null;
    }

    public static String labelOfEnum(Error e){
        for (Error err : values()) {
            if (err.equals(e)) {
                return err.label;
            }
        }
        return null;
    }
}
