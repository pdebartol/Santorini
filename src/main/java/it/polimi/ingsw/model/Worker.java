package it.polimi.ingsw.model;

/**
 * Worker represents a pawn that player uses to play.
 * @author aledimaio
 */

public class Worker {

    //attributes

    /**
     * currentSquare represents the square where the worker is currently localized
     * lastSquareMove represents the square where the worker was localized before being moved
     * lastSquareBuild represents the square where the worker last built
     */

    private Color color;
    private String gender;

    /**
     * lastSquareMove represents the square where the worker was localized before being moved
     */

    private Square lastSquareMove;

    /**
     * lastSquareBuild represents the square where the worker last built
     */

    private Square lastSquareBuild;

    /**
     * currentSquare represents the square where the worker is currently localized
     */

    private Square currentSquare;

    /**
     * inGame indicates if the worker is on the board or not
     */
    private boolean inGame;

    //constructors

    public Worker(Color color, String gender){
        this.color = color;
        this.gender = gender;
    }

    //methods

    public void setColor(Color c) {
        color = c;
    }

    public void setGender(String g) {
        gender = g;
    }

    public void setLastSquareBuild(Square s) {
        lastSquareBuild = s;
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

    public Square getLastSquareBuild() {
        return lastSquareBuild;
    }

    public boolean getInGame(){
        return inGame;
    }

    /**
     * This method allows to set false inGame attribute (remove the worker from the game)
     */

    public void removeFromGame(){
        inGame = false;
        currentSquare = null;
        lastSquareMove = null;
        lastSquareBuild = null;
    }

    /**
     * This method allows to position, during the startup step, the Worker on board
     * @param s is the square where worker must start
     */
    public void setWorkerOnBoard(Square s){
        inGame = true;
        currentSquare = s;
        s.setWorker(this);
    }

    /**
     * This method allows to update the worker position on the board
     * @param s is the new square where worker is to be localized
     */

    public void updateWorkerPosition(Square s) {
        lastSquareMove = currentSquare;
        currentSquare = s;
    }

}
