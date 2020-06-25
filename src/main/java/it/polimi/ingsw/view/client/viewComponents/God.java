package it.polimi.ingsw.view.client.viewComponents;

/**
 * This class represents a Santorini God. This class is part of the copy of the model on the client
 * (scheme to temporarily save game data).
 * @author marcoDige
 */

public class God {

    private final int id;
    private final String name;
    private final String description;

    public God(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId(){return id;}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
