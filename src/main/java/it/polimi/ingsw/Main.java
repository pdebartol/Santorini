package it.polimi.ingsw;

import it.polimi.ingsw.network.client.EchoClient;
import it.polimi.ingsw.network.server.EchoServer;
import it.polimi.ingsw.view.client.cli.Cli;
import it.polimi.ingsw.view.client.gui.Gui;
import javafx.application.Application;

import java.util.Scanner;

public class Main {

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
        EchoClient echoClient;
        if (args.length == 3)
            echoClient = new EchoClient(args[1], Integer.parseInt(args[2]));
        else
            echoClient = new EchoClient("localhost", 1234);

        echoClient.start();
        interfaceSelection();

    }

    public static void interfaceSelection(){
        Scanner input = new Scanner(System.in);
        System.out.print("Select \"cli\" or \"gui\": ");

        switch (input.nextLine()){
            case "cli":
                Cli cli = new Cli();
                break;
            case "gui":
                Application.launch(Gui.class);
                break;
            default: throw new IllegalArgumentException("Input non valido!!!");
        }

    }

}
