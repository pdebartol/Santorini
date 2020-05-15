package it.polimi.ingsw.view.client.viewComponents;
import it.polimi.ingsw.model.enums.Color;
import java.util.ArrayList;

public class Player {

    private final String username;
    private final ArrayList<Worker> workers;
    private boolean loser;

    public Player(String username, Color color) {
        this.username = username;
        this.workers = new ArrayList<>();
        this.workers.add(new Worker(color, "male"));
        this.workers.add(new Worker(color, "female"));
        this.loser = false;
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

    public void lose(){
        loser = true;
    }
}
