package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * Player represents a player who is currently playing in the match.
 * Username can't be changed once player has been created.
 * @author marcoDige
 */
public class Player {

    //attributes

    private final String username;
    private ArrayList<Worker> workers;
    private God god;

    //constructors

    /**
     * Class constructors which constructs a Player with a specified username and workers.
     * @param username the player username
     * @param workers workers that player use to play
     */
    public Player(String username, ArrayList<Worker> workers) {
        this.username = username;
        this.workers = workers;
    }

    //methods

    public void setGod(God g){
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

    public void removeWorker(Worker w){
        workers.remove(w);
    }

    /**
     * This method allows to control if the player is blocked (if the player can't move, he loses).
     * @return true or false to indicate if at least one worker which player use can move in at least one square
     * compatible with rules or not
     */
    public boolean canMove(){
        for(int i = -1; i >= -1 && i <= 1; i++)
            for(int j = -1; j >= -1 && j <= 1; i++)
                if(!(i == 0 && j == 0)) {
                    for (int k = 0; k < workers.size(); k++) {
                        int x = workers.get(k).getCurrentSquare().getXPosition() + i;
                        int y = workers.get(k).getCurrentSquare().getYPosition() + j;
                        if (x >= 0 && x <= 4 && y >= 0 && x <= 4)
                            if (god.getPower().checkMove(workers.get(k), god.getPower().getBoard().getTable()[x][y]))
                                return true;
                    }
                }
        return false;
    }

    /**
     * This method holds all the logic behind a move. if the move is possible, it updates the model status. It also
     * control if after the move, the player has won.
     * @param w is the worker who wants to move
     * @param s is the square where the worker wants to move
     * @return true or false to indicate if the move was done or not
     */
    public boolean move(Worker w, Square s){
        Power power = god.getPower();
        if(power.checkTurn(0) && power.checkMove(w,s)){
            power.updateMove(w,s);
            //TODO: notify if Player win after control with winChek()
            return true;
        }
        return false;
    }

    /**
     * This method holds all the logic behind a build move. If the build move is possible, it updates the model status.
     * @param w is the worker who wants to build
     * @param s is the square where the worker wants to build
     * @return true or false to indicate if the build move was done or not
     */
    public boolean build(Worker w, Square s){
        Power power = god.getPower();
        if(power.checkTurn(0) && power.checkBuild(w,s)){
            power.updateBuild(w,s);
            return true;
        }
        return false;
    }
}
