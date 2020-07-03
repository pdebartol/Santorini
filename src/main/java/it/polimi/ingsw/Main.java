package it.polimi.ingsw;

import it.polimi.ingsw.network.server.EchoServer;
import it.polimi.ingsw.view.client.cli.Cli;
import it.polimi.ingsw.view.client.gui.GuiManager;

/**
 * This is the main class.
 * The jar will start in either client or server mode, depending on the command line arguments given.
 * Double click on the jar will start the gui.
 */

public class Main {

    public static void main(String[] args){
        if(args.length == 0)
            clientMode(args);
        if(args[0].equals("-client")){
            clientMode(args);
        }else
            if(args[0].equals("-server")){
                serverMode(args);
            }
    }

    /**
     * This method starts the game in server mode. If the port is not specified it will use default port (1234).
     * @param args server's port
     */

    public static void serverMode(String[] args)
    {
        int p;

        if(args.length == 2) p = Integer.parseInt(args[1]);
        else p = 1234;

        EchoServer echoServer = new EchoServer(p);
        echoServer.start();
    }

    /**
     * This method starts the game in client mode. If no argument is given it will start the gui.
     * @param args client mode ("-cli" or "-gui")
     */

    public static void clientMode(String[] args){

        if(args.length == 2 && args[1].equals("-cli"))
            new Cli();
        if(args.length == 0 || args.length == 2 && args[1].equals("-gui"))
            new  GuiManager().startGui();

    }

}
