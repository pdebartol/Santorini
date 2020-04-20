package it.polimi.ingsw.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Worker represents a pawn that the player uses to play.
 * Color and gender can't be changed once the worker has been created.
 * This class is listened from Player class (when a worker is deleted, Player has to delete this worker from his worker list).
 * @author aledimaio & marcoDige
 */

public class Worker  {

    //attributes

    private PropertyChangeSupport support;
    private final Color color;
    private final String gender;

    /**
     * lastSquareMove represents the square where the worker was before being moved.
     */

    private Square lastSquareMove;

    /**
     * lastSquareBuild represents the square where the worker last built.
     */

    private Square lastSquareBuild;

    /**
     * currentSquare represents the square where the worker is currently localized.
     */

    private Square currentSquare;

    /**
     * isMoving indicates if this worker was chosen for the turn.
     */

    private boolean isMoving;

    //constructors

    public Worker(Color color, String gender){
        this.color = color;
        this.gender = gender;
        this.support = new PropertyChangeSupport(this);
        this.isMoving = false;
    }

    //methods

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

    public boolean getIsMoving() {
        return isMoving;
    }


    public void setLastSquareBuild(Square s) {
        if( s == null) throw new IllegalArgumentException("Null square as argument!");
        lastSquareBuild = s;
    }

    public void isMovingOn(){
        isMoving = true;
    }

    public void isMovingOff(){
        isMoving = false;
    }

    /**
     * This method allows to set false inGame attribute (remove the worker from the game)
     */

    public void removeFromGame(){
        if(currentSquare != null) currentSquare.removeWorker();
        currentSquare = null;
        lastSquareMove = null;
        lastSquareBuild = null;
        support.firePropertyChange("worker_removal", this,null); // notifies player of the removal
    }

    /**
     * This method allows to position, during the startup step, the Worker on board
     * @param s is the square where worker must start
     */
    public void setWorkerOnBoard(Square s){
        if(s == null) throw new IllegalArgumentException("Null square as argument!");
        if(!s.isFree()) throw  new IllegalStateException("Can't set worker on an occupied square");
        currentSquare = s;
        lastSquareMove = s;
        s.setWorker(this);
    }

    /**
     * This method allows to update the worker position on the board
     * @param s is the new square where worker is to be localized
     */

    public void updateWorkerPosition(Square s) {
        if(s == null) throw new IllegalArgumentException("Null square as argument!");
        if(!s.isFree()) throw new IllegalStateException("Can't set worker on an occupied square");
        lastSquareMove = currentSquare;
        currentSquare = s;
        s.setWorker(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

}
