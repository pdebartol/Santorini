package it.polimi.ingsw;

import it.polimi.ingsw.network.server.EchoServer;
import it.polimi.ingsw.view.client.TestLoginClass;

public class Main {

    //test
    public static void main(String[] args){
        if(args[0].equals("client")){
            clientMode(args);
        }else
            if(args[0].equals("server")){
                serverMode(args);
            }
    }

    public static void serverMode(String[] args)
    {
        int p;

        if(args.length == 2) p = Integer.parseInt(args[1]);
        else p = 1234;

        EchoServer echoServer = new EchoServer(p);
        echoServer.start();
    }

    public static void clientMode(String[] args){
        //Only for testing
        new TestLoginClass().start();

        //TODO : start the user choice interface

    }

}
