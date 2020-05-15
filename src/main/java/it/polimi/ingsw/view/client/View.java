package it.polimi.ingsw.view.client;
import it.polimi.ingsw.view.client.viewComponents.*;

import java.util.ArrayList;

public abstract class View {

    //attributes
    protected Player myPlayer;
    protected ArrayList<Player> players;
    protected Board gameBoard;

    //constructors
    public View(){
        myPlayer = null;
        players = new ArrayList<>();
        gameBoard = new Board();
    }

    //methods

    //TODO : Methods to update data and to show output


}
