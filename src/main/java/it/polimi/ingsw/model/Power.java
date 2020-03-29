package it.polimi.ingsw.model;

/**
 * Power is the interface responsible for implementing the god's power.
 * @author pierobartolo
 */

public interface Power {

     //methods


     /**
      * This method checks if moving the worker to a given square is a legit move
      * @param w is the worker that wants to move
      * @param s is the square where the worker wants to move
      * @return true or false to indicate if the move is legit or not
      */
     boolean checkMove(Worker w, Square s);

     /**
      * This method checks if a worker can build in a given square.
      * @param w is the worker that wants to build
      * @param s is the square where the worker wants to build
      * @return true or false to indicate if the build is legit or not
      */
     boolean checkBuild(Worker w, Square s);

     /**
      * This method checks if the player has won
      * @return true or false to indicate if the player has won
      */
     boolean checkWin();

     /**
      * This method is called only if the checkMove was successful, it updates the model
      * saving the player's move.
      * @param w is the worker that moves
      * @param s is the square where the worker moves
      */
     void updateMove(Worker w, Square s);

     /**
      * This method is called only if the checkBuild was successful, it updates the model
      * saving the player's build
      * @param w is the worker that builds
      * @param s is the square where the worker moves
      */

     void updateBuild(Worker w, Square s);

     /**
      * This method check if the player can move or build at any given time
      * @param mode 0 --> check if the player can move
      *             1 --> check if the player can build
      * @return true or false to indicate if  player can move or build
      */

     boolean checkTurn(int mode);


     /**
      * This method return the current board
      * @return the current instance of Board
      */
     Board getBoard();
}
