package it.polimi.ingsw.view.client.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputCli {

    public static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static String readLine(){
        String input = "";
        try{
            while(!in.ready())
                Thread.sleep(200);
            input = in.readLine();
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
        }
        return input;
    }


}
