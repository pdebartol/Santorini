package it.polimi.ingsw.model;

/**
 * Square class represents a single box of the entire board/table
 * it is useful to have because of the possibility to have much more control over events of the game
 * such as the build of a dome, check if a single square is free or if is it possible build on that square
 * @author aledimaio
 */

public class Square {

    //attributes

    /**
     * Square class have attributes to represents its position in the board/table, the worker on it the level and if
     * there is a dome or not
     */

    private int level;
    private boolean dome;
    private int xPosition;
    private int yPosition;
    private Worker worker;

    //constructors

    /**
     * The constructor initialize the Square on the table
     * @param xPosition
     * @param yPosition
     */

    public Square(int xPosition, int yPosition){
        this.level = 0;
        this.dome = false;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    //methods

    /**
     * The method above allow to put a worker on the Square
     * @param worker represent the selected worker of the current player
     */

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void setDome(boolean d) {
        dome = d;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public Worker getWorker() {
        return worker;
    }

    public int getLevel() {
        return level;
    }

    public boolean getDome() {
        return dome;
    }

    /**
     * The method remove the worker from current square
     */

    public void removeWorker() {}

    /**
     * the method check if in the current square there are a worker or a dome
     */

    public boolean isFree() {
        return false;
    }

    /**
     * the method check if in the current square there is a complete tower, that is there are 3 level set attribute and
     * there is a dome
     */

    public boolean isCompleteTower() {
        return false;
    }

    /**
     *The method above is a setter for Level attribute
     */

    public boolean buildLevel() {
        return false;
    }

}
