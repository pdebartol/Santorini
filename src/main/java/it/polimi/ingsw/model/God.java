package it.polimi.ingsw.model;

/**
 * God is the class representing the god's card with his name, description and power.
 * @author pierobartolo
 */

public class God  {

    //attributes

    private final String name;
    private final String description;
    private  Power power;

    //constructors

    public God(String name, String description, Power power) {
        this.name = name;
        this.description = description;
        this.power = power;
    }

    //methods

    public void setPower(Power power) {
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Power getPower() {
        return power;
    }

}
