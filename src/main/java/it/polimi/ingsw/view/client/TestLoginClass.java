package it.polimi.ingsw.view.client;

import it.polimi.ingsw.msgUtilities.client.RequestMsgWriter;
import it.polimi.ingsw.network.MsgSender;
import it.polimi.ingsw.network.client.EchoClient;

import java.util.List;
import java.util.Scanner;

//TEST
public class TestLoginClass {

    EchoClient echoClient;
    String myUsername;
    List<String> otherUsername;

    public void start() {
        echoClient = new EchoClient(ipAddressInput(),portInput());
        echoClient.start();
    }

    public String ipAddressInput(){
        System.out.println("Ip address : ");
        return new Scanner(System.in).nextLine();
    }

    public int portInput(){
        System.out.println("Port : ");
        return Integer.parseInt(new Scanner(System.in).nextLine());
    }

    public void login(boolean rejectedBefore){
        if(!rejectedBefore) System.out.println("You have to log in!\n");
        else System.out.println("Your name already exists, enter a new one!\n");
        System.out.println("Username :");
        echoClient.sendMsg(new RequestMsgWriter().loginRequest(new Scanner(System.in).nextLine()));
    }

    public void logged(List<String> usernames, String yourUsername){
        myUsername = yourUsername;
        otherUsername.addAll(usernames);
        System.out.println("Hi " + yourUsername + ", you're in!\n");
        if(usernames.isEmpty()) System.out.println("Wow, you're the first component of this game.\n");
        else {
            System.out.println("The other players in this game are : \n");
            for (String user : usernames)
                System.out.println(user + "\n");
        }
    }

    public void newUser(String username){
        otherUsername.add(username);
        System.out.println("A new user logged in, his username is : " + username + "\n");
    }

    public void startMatch(){
        System.out.println("There is a new player with you now, do you want to start now a 2 player game (2) or you want to wait a third player (3)?");
        if (Integer.parseInt(new Scanner(System.in).nextLine()) == 2) echoClient.sendMsg(new RequestMsgWriter().startGameRequest(myUsername));
        else System.out.println("Waiting for the third player...\n");
    }

    public void matchStarted(){
        System.out.println("The game has started\n");
    }
}
