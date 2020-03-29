package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class GodsFactoryTest {
    Board gameBoard;



    @Test
    public void first_test(){
         ArrayList<Integer> first_test = new ArrayList<>();
         first_test.add(1);
         first_test.add(5);
         first_test.add(2);
         Board board = new Board();
         GodsFactory x = new GodsFactory(board);
        for (God elem : x.getGods(first_test)) {
            System.out.println(elem.getPower());
        }

        }

}