package it.polimi.ingsw.view.client.viewComponents;

import it.polimi.ingsw.model.enums.Color;

/**
 * This class represents a single worker
 * @author aledimaio
 */

public class Worker {

    private final Color color;
    private final String gender;

    public Worker(Color color, String gender) {
        this.color = color;
        this.gender = gender;
    }

    public Color getColor() {
        return color;
    }

    public String getGender(){
        return gender;
    }

}
