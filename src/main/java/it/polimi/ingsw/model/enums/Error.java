package it.polimi.ingsw.model.enums;


/**
 * Error is the enum representing the error codes showing up when the player moves or builds.
 * @author pierobartolo
 */

public enum Error {
    ISNT_WORKER_CHOSEN("isntWorkerChosen"),
    BLOCK_MOVE_UP("Athena blocked upward movements"),
    BUILDS_EXCEEDED("buildsExceeded"),
    BUILD_BEFORE_MOVE("buildBeforeMove"),
    CANT_DOME_UNDERFOOT("you can't build a dome under yourself"),
    CANT_MOVE_UP("you can't move up because you build before you moved"),
    EXTRA_BUILD_NOT_PERIMETER("the additional build can't be on a perimeter space"),
    EXTRA_BUILD_NOT_SAME_SPACE("the additional build can't be on the same space"),
    EXTRA_BUILD_ONLY_SAME_SPACE("the additional build must be built on top of your first block"),
    EXTRA_MOVE_NOT_BACK("your worker can't moves back to the space it started on"),
    INVALID_LEVEL_BUILD("invalid block"),
    INVALID_LEVEL_MOVE("the space where you want to move is too high"),
    IS_DOME("there is a dome"),
    MOVES_EXCEEDED("movesExceeded"),
    MOVE_AFTER_BUILD("moveAfterBuild"),
    NOT_ADJACENT("the space you selected is not adjacent"),
    NOT_FREE("the space you selected is occupied"),
    SAME_DIRECTION_NOT_FREE("you can't push the worker on this space because the space in the same direction is occupied"),
    EXTRA_BUILD_NOT_DOME("the additional build block can't be a dome"),
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
