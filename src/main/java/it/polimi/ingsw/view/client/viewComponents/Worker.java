package it.polimi.ingsw.view.client.viewComponents;

import it.polimi.ingsw.model.enums.Color;

/**
 * This class represents a Santorini worker. This class is part of the copy of the model on the client
 * (scheme to temporarily save game data).
 * @author marcoDige
 */

public class Worker {

    private final Color color;
    private final String gender;
    private Square currentPosition;

    public Worker(Color color, String gender) {
        this.color = color;
        this.gender = gender;
        currentPosition = null;

    }

    public void setCurrentPosition(Square s){
        this.currentPosition = s;
    }

    public Color getColor() {
        return color;
    }

    public String getGender(){
        return gender;
    }

    public Square getCurrentPosition(){
        return currentPosition;
    }

}
