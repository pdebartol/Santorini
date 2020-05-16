package it.polimi.ingsw.view.client.viewComponents;

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

    //TODO : javadoc

    public void placeWorker(Worker worker) {
        this.worker = worker;
    }

    //TODO : javadoc

    public Worker removeWorker(){
        Worker workerRemoved = this.worker;
        this.worker = null;
        return workerRemoved;
    }

    //TODO : javadoc

    public void setLevel(int level) {
        this.level = level;
    }

}
