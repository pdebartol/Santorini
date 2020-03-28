package it.polimi.ingsw.model;



public interface Power {

     //methods

     boolean checkMove(Worker w, Square s);

     boolean checkBuild(Worker w, Square s);

     boolean checkWin();

     void updateMove(Worker w, Square s);

     void updateBuild(Worker w, Square s);

     boolean checkTurn(boolean mode);


     Board getBoard();
}
