package it.polimi.ingsw.view.client.cli;

/**
 * This class represents the board game
 * @author aledimaio
 */

public class Board {

    private final int DIMENSION = 5;
    private Square[][] table;

    public Board(){

        this.table = new Square[DIMENSION][DIMENSION];
        for(int x = 0; x < DIMENSION ; x++){
            for(int y = 0; y < DIMENSION ; y++)
                this.table[x][y] = new Square(x, y);
        }

    }

    public int getDIMENSION() {
        return DIMENSION;
    }

    public Square getSquare(int x, int y){
        return table[x][y];
    }

    /**
     * printBoard() print the board game
     */

    public void printBoard(){

        //print 1,2,3,4,5 vertical board reference
        for (int i = 0, j = 0; i < DIMENSION; i++, j += Box.SQUARE_DIMENSION.escape()) {
            System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.BOARD_START_UP.escape() + j, Box.BOARD_START_LEFT.escape() - 2);
            System.out.printf("%d", i + 1);
        }

        for (int x = 0, i = 0 ; x < DIMENSION ; x++ , i += Box.SQUARE_DIMENSION.escape()) {
            System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.BOARD_START_UP.escape(), Box.BOARD_START_LEFT.escape() + i - 1);
            for (int y = 0; y < DIMENSION ; y++) {
                table[x][y].drawSquare();
                System.out.print("\n");
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.BOARD_START_LEFT.escape() + i - 2);
            }
            //print 1,2,3,4,5 horizontal board reference
            System.out.print(Color.ANSI_RESET.escape());
            System.out.print(x + 1);
        }

    }

}
