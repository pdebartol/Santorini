package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * Power is the interface responsible for implementing the god's power.
 * @author pierobartolo
 */

public interface Power {

     //methods

     /**
      * This method checks if moving the worker to a given square is a legit move
      * @param w is the worker that wants to move
      * @param x is the x square coordinate where the worker wants to move
      * @param y is the y square coordinate where the worker wants to move
      * @return an ArrayList that is empty if the move is legal, otherwise it contains the errors that prevent the worker from moving.
      */

     ArrayList<Error> checkMove(Worker w, int x, int y);

     /**
      * This method checks if a worker can build in a given square.
      * @param w is the worker that wants to build
      * @param x is the x square coordinate where the worker wants to build
      * @param y is the y square coordinate where the worker wants to build
      * @param l is the level the worker wants to build
      * @return an ArrayList that is empty if the build is legal, otherwise it contains the errors that prevent the worker from building.
      */

     ArrayList<Error> checkBuild(Worker w, int x, int y, int l);

     /**
      * This method checks if the player has won
      * @return true or false to indicate if the player has won
      */

     boolean checkWin(Worker w);

     /**
      * This method is called only if the checkMove was successful, it updates the model
      * saving the player's move.
      * @param w is the worker that moves
      * @param x is the x square coordinate where the worker moves
      * @param y is the y square coordinate where the worker moves
      */

     void updateMove(Worker w, int x, int y);

     /**
      * This method is called only if the checkBuild was successful, it updates the model
      * saving the player's build
      * @param w is the worker that builds
      * @param x is the x square coordinate where the worker builds
      * @param y is the y square coordinate where the worker builds
      * @param l is the level the worker builds
      */

     void updateBuild(Worker w, int x, int y, int l);


     /**
      * This method return the current board
      * @return the current instance of Board
      */

     Board getBoard();

     /**
      * This method is called when a player finish his turn. It checks that player has done standard moves and
      * reset all counters or flag for the turn in model.
      * @param workers is the arrayList of worker that player uses
      * @return true -> The turn is correctly finish
      *         false -> The turn cannot finish now
      */

     boolean endOfTurn(ArrayList<Worker> workers);
}
