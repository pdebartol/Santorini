package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Worker;

public interface Power {
     boolean checkMove(Worker w, Square s);
     boolean checkBuild(Worker w, Square s);
     boolean checkWin();
     void updateMove(Worker w, Square s);
     void updateBuild(Worker w, Square s);
     Integer getMaxMoves();
     Integer getMaxBuilds();
     Board getBoard();
}
