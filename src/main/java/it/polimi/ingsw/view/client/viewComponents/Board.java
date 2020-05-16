package it.polimi.ingsw.view.client.viewComponents;

public class Board {

    public static final int DIMENSION = 5;
    private Square[][] table;

    public Board(){

        this.table = new Square[DIMENSION][DIMENSION];
        for(int x = 0; x < DIMENSION ; x++){
            for(int y = 0; y < DIMENSION ; y++)
                this.table[x][y] = new Square(x, y);
        }

    }

    //TODO : javadoc

    public Square getSquareByCoordinates(int x, int y){
        return table[x][y];
    }

}
