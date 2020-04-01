package it.polimi.ingsw.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Worker represents a pawn that player uses to play.
 * color and gender can't be changed once worker has been created.
 * @author aledimaio & marcoDige
 */

public class Worker  {

    //attributes

    private PropertyChangeSupport support;


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


    //constructors

    public Worker(Color color, String gender){
        this.color = color;
        this.gender = gender;
        this.support = new PropertyChangeSupport(this);
    }

    //methods



    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setColor(Color c) {
        color = c;
    }

    public void setGender(String g) {
        gender = g;
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


    public void setLastSquareBuild(Square s) {
        if( s == null) throw new IllegalArgumentException("Null square as argument!");
        lastSquareBuild = s;
    }

    /**
     * This method allows to set false inGame attribute (remove the worker from the game)
     */

    public void removeFromGame(){
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
        if( s == null) throw new IllegalArgumentException("Null square as argument!");
        currentSquare = s;
        s.setWorker(this);
    }

    /**
     * This method allows to update the worker position on the board
     * @param s is the new square where worker is to be localized
     */

    public void updateWorkerPosition(Square s) {
        if( s == null) throw new IllegalArgumentException("Null square as argument!");
        lastSquareMove = currentSquare;
        currentSquare = s;
        s.setWorker(this);
    }

}
