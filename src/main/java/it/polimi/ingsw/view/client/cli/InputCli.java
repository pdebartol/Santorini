package it.polimi.ingsw.view.client.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class contains utilities for asynchronous input from CLI.
 * @author marcoDige
 */

public class InputCli {

    //attributes

    public static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    //methods

    /**
     * This method allows you to acquire command-line input asynchronously. asynchronicity allows you to stop the
     * thread at any time in the event that a blocking message arrives in the background
     * (disconnection or login of a third player).
     * @return input from command-line
     */

    public static String readLine(){
        String input = "";
        try{
            while(in.ready())
                in.readLine();
            while(!in.ready())
                Thread.sleep(200);
            input = in.readLine();
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
        }
        return input;
    }


}
