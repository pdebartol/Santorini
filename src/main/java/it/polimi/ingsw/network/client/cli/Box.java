package it.polimi.ingsw.network.client.cli;

public enum Box {
    VERTICAL_DIM (34),
    HORIZONTAL_DIM (100),
    TEXT_BOX_START (27),
    INPUT_BOX_START (32),
    TEXT_START (2),
    BOARD_START_LEFT(35),
    BOARD_START_UP(3),
    SQUARE_DIMENSION(4),
    ASCIIART_START_LEFT(16),
    ASCIIART_START_UP(11);

    private int escape;

    Box(int escape) {
        this.escape = escape;
    }

    public int escape(){
        return escape;
    }

}
