package it.polimi.ingsw.view.client.cli;

/**
 * This class represents the board game
 * @author aledimaio
 */

public class Board {

    private static final int DIMENSION = 5;
    private Square[][] table;

    public Board(){

        this.table = new Square[DIMENSION][DIMENSION];
        for(int x = 0; x < DIMENSION ; x++){
            for(int y = 0; y < DIMENSION ; y++)
                this.table[x][y] = new Square(x, y);
        }

    }

    public Square getSquare(int x, int y){
        return table[x][y];
    }

    /**
     * printBoard() print the board game
     */

    public void printBoard(){

        //print 1,2,3,4,5 vertical board reference
        for (int i = DIMENSION, j = 0; i > 0; i--, j += Box.SQUARE_DIMENSION.escape()) {
            System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.BOARD_START_UP.escape() + j, Box.BOARD_START_LEFT.escape() - 2);
            System.out.printf("%d", i - 1 );
        }

        //This cycle prints 0,0 position in the left-bottom corner

        for (int x = DIMENSION - 1, i = 0 , j = 0; x > -1 ; x-- , i += Box.SQUARE_DIMENSION.escape(), j++) {
            System.out.printf(Escapes.MOVE_CURSOR_INPUT_REQUIRED.escape(), Box.BOARD_START_UP.escape(), Box.BOARD_START_LEFT.escape() + i - 1);
            for (int y = DIMENSION - 1; y > - 1 ; y--) {
                table[x][y].drawSquare();
                System.out.print("\n");
                System.out.printf(Escapes.CURSOR_RIGHT_INPUT_REQUIRED.escape(), Box.BOARD_START_LEFT.escape() + i - 2);
            }
            //print 1,2,3,4,5 horizontal board reference
            System.out.print(ColorCode.ANSI_RESET.escape());
            System.out.print(j);
        }

    }

}
