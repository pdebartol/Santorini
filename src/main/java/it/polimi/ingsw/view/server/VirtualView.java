package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ControllerActionListener;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.model.enums.Error;

import java.util.ArrayList;

public class VirtualView implements ViewActionListener{

    //attributes

    ControllerActionListener controllerListener;

    //methods

    //Request Methods

    public void setListener(ControllerActionListener l){
        this.controllerListener = l;
    }

    void loginRequest(String username, Color color){
        ArrayList<Error> errors = controllerListener.onNewPlayer(username, color);

        //TODO if !empty send errors back to client
    }

    void startGameRequest(String username){
        String challengerUsername = controllerListener.onStartGame();

        //TODO send challenger back to clients
    }

    void createGodsRequest(String username, ArrayList<Integer> ids){
        ArrayList<Error> errors = controllerListener.onChallengerChooseGods(username, ids);

        //TODO if !empty send errors back to client
    }

    void choseGodRequest(String username, int godId){
       ArrayList<Error> errors = controllerListener.onPlayerChooseGod(username, godId );
    }

    void chooseStartingPlayerRequest(String username, String playerChosen){

    }




    void setupOnBoardRequest(String username, int workerId, int x, int y){

    }

    void moveRequest(String username, int workerId, int x, int y){

    }

    void buildRequest(String username, int workerId, int x, int y){

    }

    void endOfTurn(String username){

    }

    // Answer Methods

}
