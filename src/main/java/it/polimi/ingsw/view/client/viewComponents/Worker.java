package it.polimi.ingsw.view.client.viewComponents;

import it.polimi.ingsw.model.enums.Color;

//TODO : javadoc

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
