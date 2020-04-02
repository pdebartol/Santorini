package it.polimi.ingsw.model;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Player represents a player who is currently playing in the match.
 * Username can't be changed once player has been created.
 * @author marcoDige
 */

public class Player implements PropertyChangeListener {

    //attributes

    private final String username;
    /**
     * workers is an array of Worker objects, that represents the workers which Player use to play.
     */

    private ArrayList<Worker> workers;
    private God god;

    //constructors

    /**
     * Class constructors which constructs a Player with a specified username and a color for his workers.
     * @param username is the player username
     * @param color is the player's workers color
     */

    public Player(String username, Color color) {
        this.username = username;
        this.workers = new ArrayList<>();
        this.workers.add(new Worker(color,"male"));
        this.workers.add(new Worker(color, "female"));

        // Player observes workers
        Objects.requireNonNull(workers.get(0)).addPropertyChangeListener(this);
        Objects.requireNonNull(workers.get(1)).addPropertyChangeListener(this);
    }

    //methods

    public void setGod(God g){ this.god = g; }

    public String getUsername(){
        return username;
    }

    public ArrayList<Worker> getWorkers(){
        return workers;
    }

    public God getGod() {
        return god;
    }

    /**
     * This method checks if the player is blocked (if the player can't move, he loses)
     * @return true or false to indicate if at least one worker which player use can move in at least one square
     * compatible with rules or not
     */

    public boolean canMove(){
        for(int i = -1; i <= 1 ; i++)
            for(int j = -1; j <= 1; j++){
                    for (Worker worker : workers) {
                        int x = worker.getCurrentSquare().getXPosition() + i;
                        int y = worker.getCurrentSquare().getYPosition() + j;
                        if (x >= 0 && x <= 4 && y >= 0 && y <= 4)
                            if (god.getPower().checkMove(worker,x,y).size() == 0)
                                return true;
                    }
                }
        return false;
    }

    /**
     * This method checks if the player can Build in his turn (if the player can't build, he loses)
     * @return true or false to indicate if at least one worker which player use can build in at least one square
     * compatible with rules or not
     */

    public boolean canBuild(){
        for(int i = -1; i <= 1 ; i++)
            for(int j = -1; j <= 1; j++){
                    for (Worker worker : workers) {
                        int x = worker.getCurrentSquare().getXPosition() + i;
                        int y = worker.getCurrentSquare().getYPosition() + j;
                        if (x >= 0 && x <= 4 && y >= 0 && y <= 4)
                            if (god.getPower().checkBuild(worker,x,y).size() == 0)
                                return true;
                    }
                }
        return false;
    }

    /**
     * This method holds all the logic behind a move. if the move is possible, it updates the model status.
     * Else it send to view errors found during the check.
     * It also controls if after the move, the player has won.
     * @param w is the worker who wants to move
     * @param x is the x square coordinate where the worker wants to move
     * @param y is the y square coordinate where the worker wants to move
     * @return true or false to indicate if the move was done or not
     */

    public boolean move(Worker w, int x, int y){
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");
        if (x < 0 || x > 4 || y < 0 || y > 4) throw new IllegalArgumentException("Null worker as argument!");
        Power power = god.getPower();
        ArrayList<Error> errors = power.checkMove(w, x, y);
        if(errors.size() == 0){
            power.updateMove(w,x,y);
            // if(power.checkWin(w)) send to view a msg to signal winner
            return true;
        }else
            sendErrorMsg(errors);

        return false;
    }

    /**
     * This method holds all the logic behind a build move. If the build move is possible, it updates the model status.
     * Else it send to view errors found during the check.
     * @param w is the worker who wants to build
     * @param x is the x square coordinate where the worker wants to build
     * @param y is the y square coordinate where the worker wants to build
     * @return true or false to indicate if the build move was done or not
     */

    public boolean build(Worker w, int x, int y){
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");
        if (x < 0 || x > 4 || y < 0 || y > 4) throw new IllegalArgumentException("Null worker as argument!");
        Power power = god.getPower();
        ArrayList<Error> errors = power.checkBuild(w, x, y);
        if(errors.size() == 0){
            power.updateBuild(w,x,y);
            return true;
        }else
            sendErrorMsg(errors);

        return false;
    }

    /**
     * This method send to view errors found during the check if the move or build is invalid.
     * @param errors array of errors
     */

    public void sendErrorMsg(ArrayList<Error> errors){
        for(Error e: errors)
            switch (e){
                case NOT_FREE:
                    //send to view a msg for the error
                case NOT_ADJACENT:
                    //send to view a msg for the error
                case INVALID_LEVEL:
                    //send to view a msg for the error
                case IS_DOME:
                    //send to view a msg for the error
                case MOVE_AFTER_BUILD:
                    //send to view a msg for the error
                case BUILD_BEFORE_MOVE:
                    //send to view a msg for the error
                case BUILDS_EXCEEDED:
                    //send to view a msg for the error
                case MOVES_EXCEEDED:
                    //send to view a msg for the error
            }
    }

    /**
     * This method is called when a specific events occurs in the Worker Class.
     * It handles the events correctly. In this class it is used for remove a worker when it has been removed from Game.
     * @param evt the event occurred.
     */

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("worker_removal")){
            Worker w = (Worker) evt.getOldValue();
            workers.remove(w);
            w.removePropertyChangeListener(this);
        }
    }
}
