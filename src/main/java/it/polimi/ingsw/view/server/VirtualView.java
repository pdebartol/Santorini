package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ActionListener;

import java.util.ArrayList;

public class VirtualView {

    //attributes

    ActionListener listener;

    //methods

    void setListener(ActionListener l){
        this.listener = l;
    }

    void loginRequest(String username, String color){

    }

    void startGameRequest(String username){

    }

    void chooseStartingPlayerRequest(String username, String playerChosen){

    }


    void createGodsRequest(String username, ArrayList<Integer> ids){

    }

    void choseGodRequest(String username, String godName){

    }

    void setupOnBoardRequest(String username, int workerId, int x, int y){

    }

    void moveRequest(String username, int workerId, int x, int y){

    }

    void buildRequest(String username, int workerId, int x, int y){

    }

    void endOfTurn(String username){

    }

}
