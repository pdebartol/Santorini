package it.polimi.ingsw.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

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
        god = null;

        // Player observes workers
        Objects.requireNonNull(workers.get(0)).addPropertyChangeListener(this);
        Objects.requireNonNull(workers.get(1)).addPropertyChangeListener(this);
    }

    //methods

    /**
     * This is a particular setter. It makes God immutable, once set god, player can't change it.
     * @param g is the god player want to use
     */

    public void setGod(God g){
        if(god == null)
            this.god = g;
    }

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
                        if (god.getPower().checkMove(worker, x, y).isEmpty())
                            return true;
                }
            }
        //TODO: notify to view this player can't move (he loses)
        return false;
    }

    /**
     * This method checks if the player can Build in his turn (if the player can't build, he loses)
     * @param w is the worker player used for move (he have to use the same worker for build)
     * @return true or false to indicate if worker which player use can build in at least one square compatible
     * with rules or not
     */

    public boolean canBuild(Worker w){
        if (w == null) throw new IllegalArgumentException("Null worker as argument!");
        for(int i = -1; i <= 1 ; i++)
            for(int j = -1; j <= 1; j++){
                int x = w.getCurrentSquare().getXPosition() + i;
                int y = w.getCurrentSquare().getYPosition() + j;
                if (x >= 0 && x <= 4 && y >= 0 && y <= 4)
                    for(int k = 1; k < 5; k++)
                        if (god.getPower().checkBuild(w, x, y, k).isEmpty())
                            return true;
            }
        //TODO: notify to view this player can't build (he loses)
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

    public List<Error> move(Worker w, int x, int y){
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");
        if (x < 0 || x > 4 || y < 0 || y > 4) throw new IllegalArgumentException("Invalid coordinates!");
        Power power = god.getPower();

        //make error list immutable out of model
        List<Error> errorList = power.checkMove(w, x, y);
        List<Error> errors = Collections.unmodifiableList(errorList);
        if(errors.isEmpty()){
            power.updateMove(w,x,y);
            //TODO: notify possible win to view
            //TODO: notify changes to view
        }else
            sendErrorMsg(errors);

        return errors;
    }

    /**
     * This method holds all the logic behind a build move. If the build move is possible, it updates the model status.
     * Else it send to view errors found during the check.
     * @param w is the worker who wants to build
     * @param x is the x square coordinate where the worker wants to build
     * @param y is the y square coordinate where the worker wants to build
     * @return true or false to indicate if the build move was done or not
     */

    public List<Error> build(Worker w, int x, int y, int l){
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");
        if (x < 0 || x > 4 || y < 0 || y > 4) throw new IllegalArgumentException("Invalid coordinates!");
        if(l < 1 || l > 4 ) throw new IllegalArgumentException("Invalid level!");
        Power power = god.getPower();

        //make error list immutable out of model
        List<Error> errorList = power.checkBuild(w, x, y, l);
        List<Error> errors = Collections.unmodifiableList(errorList);
        if(errors.isEmpty()){
            power.updateBuild(w, x, y, l);
            //TODO: notify changes to view
        }else
            sendErrorMsg(errors);

        return errors;
    }

    /**
     * This method send to view errors found during the check if the move or build is invalid.
     * @param errors array of errors
     */

    public void sendErrorMsg(List<Error> errors){
        for(Error e: errors)
            switch (e){
                case NOT_FREE:
                    //TODO: notify to view this error
                case NOT_ADJACENT:
                    //TODO: notify to view this error
                case INVALID_LEVEL_MOVE:
                    //TODO: notify to view this error
                case INVALID_LEVEL_BUILD:
                    //TODO: notify to view this error
                case IS_DOME:
                    //TODO: notify to view this error
                case MOVE_AFTER_BUILD:
                    //TODO: notify to view this error
                case BUILD_BEFORE_MOVE:
                    //TODO: notify to view this error
                case BUILDS_EXCEEDED:
                    //TODO: notify to view this error
                case MOVES_EXCEEDED:
                    //TODO: notify to view this error
                case BLOCK_MOVE_UP:
                    //TODO: notify to view this error
                case CANT_DOME_UNDERFOOT:
                    //TODO: notify to view this error
                case SAME_DIRECTION_NOT_FREE:
                    //TODO: notify to view this error
                case EXTRA_BUILD_NOT_PERIMETER:
                    //TODO: notify to view this error
                case EXTRA_MOVE_NOT_BACK:
                    //TODO: notify to view this error
                case EXTRA_BUILD_ONLY_SAME_SPACE:
                    //TODO: notify to view this error
                case CANT_MOVE_UP:
                    //TODO: notify to view this error
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
            //TODO: notify to view that a worker was removed
            //TODO: notify to view if this player has no worker (he loses)
        }
    }

    /**
     * This method is called at the end of each turn.
     * It resets moves and builds counters and activates "End of Turn" powers.
     * @param w an arraylist containing the player's workers.
     * @return True if the player can end the turn, false otherwise.
     */

    public boolean endTurn( ArrayList<Worker> w){
        if(w == null) throw new IllegalArgumentException("Null worker as argument!");
        if(w.size() == 0) throw new IllegalArgumentException("No workers passed");

        Power power = god.getPower();
        return power.endOfTurn(workers);
    }


}
