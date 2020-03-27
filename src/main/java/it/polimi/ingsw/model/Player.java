package it.polimi.ingsw.model;

public class Player {

    //attributes

    private String username;
    private Worker[] workers;
    private God god;

    //constructors

    public Player(String username, Worker[] workers) {
        this.username = username;
        this.workers = workers;
    }

    //methods

    public void setUsername(String u){
        this.username = u;
    }

    public void setGod(God g){
        this.god = g;
    }

    public String getUsername(){
        return username;
    }

    public Worker[] getWorkers(){
        return workers;
    }

    public God getGod() {
        return god;
    }

    public void addWorker(Worker w){

    }

    public void removeWorker(Worker w){

    }

    public boolean canMove(){
        return false;
    }

    public boolean move(Worker w, Square s){
        return false;
    }

    public boolean build(Worker w, Square s){
        return false;
    }
}
