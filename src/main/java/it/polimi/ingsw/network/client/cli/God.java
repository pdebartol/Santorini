package it.polimi.ingsw.network.client.cli;

/**
 * This class represents a single God
 * @author aledimaio
 */

public class God {

    private final String name;
    private final String description;

    public God(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
