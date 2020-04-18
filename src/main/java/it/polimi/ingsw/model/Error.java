package it.polimi.ingsw.model;


/**
 * Error is the enum representing the error codes showing up when the player moves or builds.
 * @author pierobartolo
 */

public enum Error {
    ISNT_WORKER_CHOSEN,
    BLOCK_MOVE_UP,
    BUILDS_EXCEEDED,
    BUILD_BEFORE_MOVE,
    CANT_DOME_UNDERFOOT,
    CANT_MOVE_UP,
    EXTRA_BUILD_NOT_PERIMETER,
    EXTRA_BUILD_NOT_SAME_SPACE,
    EXTRA_BUILD_ONLY_SAME_SPACE,
    EXTRA_MOVE_NOT_BACK,
    INVALID_LEVEL_BUILD,
    INVALID_LEVEL_MOVE,
    IS_DOME,
    MOVES_EXCEEDED,
    MOVE_AFTER_BUILD,
    NOT_ADJACENT,
    NOT_FREE,
    SAME_DIRECTION_NOT_FREE,
    EXTRA_BUILD_NOT_DOME

}
