package it.polimi.ingsw.view.client.viewComponents;

/**
 * This class represents a single square (that makes up the board). This class is part of the copy of the model on the client
 * (scheme to temporarily save game data).
 * @author marcoDige
 */

public class Square {

    private final int x;
    private final int y;
    private Worker worker;
    private int level;
    private boolean dome;

    /**
     * When the constructor is invoked the square is initialized at level zero without any worker on it
     */

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
        this.level = 0;
        this.dome = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Worker getWorker() {
        return worker;
    }

    public int getLevel() {
        return level;
    }

    public boolean getDome(){return dome;}

    /**
     * This method allows to place the worker on this square
     * @param worker is the worker to place
     */

    public void placeWorker(Worker worker) {
        this.worker = worker;
        worker.setCurrentPosition(this);
    }

    /**
     * This method allows to free this square from the worker place on it.
     * @return the worker removed
     */

    public Worker removeWorker(){
        Worker workerRemoved = this.worker;
        this.worker = null;
        return workerRemoved;
    }

    /**
     * This method allows to build the level on this square.
     * @param level is the level to build
     */

    public void setLevel(int level) {
        if(level == 4) dome = true;
        else this.level = level;
    }

}
