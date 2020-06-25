package it.polimi.ingsw.view.client.viewComponents;

/**
 * This class represents a Santorini Board. This class is part of the copy of the model on the client
 * (scheme to temporarily save game data).
 * @author marcoDige
 */

public class Board {

    public static final int DIMENSION = 5;
    private final Square[][] table;

    public Board(){

        this.table = new Square[DIMENSION][DIMENSION];
        for(int x = 0; x < DIMENSION ; x++){
            for(int y = 0; y < DIMENSION ; y++)
                this.table[x][y] = new Square(x, y);
        }

    }

    /**
     * This method allows to get a Square object from his coordinates
     * @param x is the X coordinate
     * @param y is the Y coordinate
     * @return
     */

    public Square getSquareByCoordinates(int x, int y){
        return table[x][y];
    }

}
