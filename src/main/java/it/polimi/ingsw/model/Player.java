package it.polimi.ingsw.model;

public class Player {
    private String username;
    private Worker[] workers;
    private God god;

    public void setUsername(String u){

    }

    public void setGod(God g){

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
