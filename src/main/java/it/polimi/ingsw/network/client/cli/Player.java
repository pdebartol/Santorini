package it.polimi.ingsw.network.client.cli;

import java.util.ArrayList;

/**
 * This class represents a player
 * @author aledimaio
 */

public class Player {

    private final String username;
    private final Color color;
    private ArrayList<Worker> workers;

    public Player(String username, Color color) {
        this.username = username;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public Color getColor() {
        return color;
    }

}
