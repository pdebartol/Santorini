package it.polimi.ingsw.model;

/**
 * Worker class represents the pawn each player can have in game (max 2)
 * it is linked to Square class in order to provide the possibility to know where worker is
 * @author aledimaio
 */

public class Worker {

    //attributes

    /**
     * Due to its connection to Square in the worker class there is additional private attribute "currentSquare"
     * that attribute is necessary in order to know where worker actually is on the board
     */

    private Color color;
    private String gender;
    private Square lastSquareMove;
    private Square lastSquareBuilt;
    private Square currentSquare;

    //constructors

    //methods

    public void setColor(Color c) {
        color = c;
    }

    public void setGender(String g) {
        gender = g;
    }

    public void setLastSquareMove(Square s) {
        lastSquareMove = s;
    }

    public void setLastSquareBuilt(Square s) {
        lastSquareBuilt = s;
    }

    public void setCurrentSquare(Square s) {
        currentSquare = s;
    }

    public Square getCurrentSquare() {
        return currentSquare;
    }

    public Color getColor() {
        return color;
    }

    public String getGender() {
        return gender;
    }

    public Square getLastSquareMove() {
        return lastSquareMove;
    }

    public Square getLastSquareBuilt() {
        return lastSquareBuilt;
    }

    public void updateWorkerPosition(Square s) {

    }

}
