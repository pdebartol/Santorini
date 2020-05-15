package it.polimi.ingsw.view.client.viewComponents;
import it.polimi.ingsw.model.enums.Color;
import java.util.ArrayList;

public class Player {

    private final String username;
    private final ArrayList<Worker> workers;
    private boolean loser;
    private God god;

    public Player(String username, Color color) {
        this.username = username;
        this.workers = new ArrayList<>();
        this.workers.add(new Worker(color, "male"));
        this.workers.add(new Worker(color, "female"));
        this.loser = false;
        this.god = null;
    }



    public void setGod(God god) {
        this.god = god;
    }

    public String getUsername() {
        return username;
    }

    public Worker getWorkerByGender(String gender){
        for(Worker w : workers) {
            if (w.getGender().equals(gender)) return w;
        }
        return null;
    }

    public boolean getLoser(){
        return loser;
    }

    public God getGod(){return god;}

    public void lose(){
        loser = true;
    }
}
