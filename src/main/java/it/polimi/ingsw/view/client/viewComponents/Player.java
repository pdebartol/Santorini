package it.polimi.ingsw.view.client.viewComponents;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.client.viewComponents.Worker;
import java.util.ArrayList;

/**
 * This class represents a player
 * @author aledimaio
 */

public class Player {

    private final String username;
    private ArrayList<Worker> workers;

    public Player(String username, Color color) {
        this.username = username;
        this.workers = new ArrayList<>();
        this.workers.add(new Worker(color, "male"));
        this.workers.add(new Worker(color, "female"));
    }

    public String getUsername() {
        return username;
    }

}
