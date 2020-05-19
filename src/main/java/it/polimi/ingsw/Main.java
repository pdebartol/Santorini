package it.polimi.ingsw;

import it.polimi.ingsw.network.server.EchoServer;
import it.polimi.ingsw.view.client.cli.Cli;
import it.polimi.ingsw.view.client.gui.Gui;
import it.polimi.ingsw.view.client.gui.GuiManager;
import javafx.application.Application;

//TODO : javadoc

public class Main {

    public static void main(String[] args){
        if(args[0].equals("-client")){
            clientMode(args);
        }else
            if(args[0].equals("-server")){
                serverMode(args);
            }
    }

    //TODO : javadoc

    public static void serverMode(String[] args)
    {
        int p;

        if(args.length == 2) p = Integer.parseInt(args[1]);
        else p = 1234;

        EchoServer echoServer = new EchoServer(p);
        echoServer.start();
    }

    //TODO : javadoc

    public static void clientMode(String[] args){

        if(args.length == 2 && args[1].equals("-cli"))
            new Cli();
        else {
            //new  GuiManager().startGui();
            Application.launch(GuiManager.class);
        }
    }

}
