package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.view.client.viewComponents.God;

import java.util.ArrayList;

/**
 * This class represents a player
 * @author aledimaio
 */

public class Player {

    private final String username;
    private final String color;
    private God god = null;
    private ArrayList<Worker> workers;

    public Player(String username, String color) {
        this.username = username;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public String getColor() {
        return color;
    }

    public God getGod() {
        return god;
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public void setGod(God god) {
        this.god = god;
    }

    public void setWorkers(ArrayList<Worker> workers) {
        this.workers = workers;
    }

}
