package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class GodFactoryTest {

    @Test
    public void first_test(){
         ArrayList<Integer> first_test = new ArrayList<>();
         first_test.add(1);
         first_test.add(3);
         first_test.add(2);
         GodFactory x = new GodFactory(first_test);
        for (God elem : x.getGods()) {
            System.out.println(elem.getPower());
        }

        }

}