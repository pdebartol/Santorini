package it.polimi.ingsw.view.client;

import it.polimi.ingsw.msgUtilities.client.RequestMsgWriter;
import it.polimi.ingsw.network.client.EchoClient;
import it.polimi.ingsw.view.client.cli.InputCli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TEST
public class TestLoginClass {

    EchoClient echoClient;
    String ipAddress;
    int port;
    String myUsername;
    List<String> otherUsernames;

    public TestLoginClass(){
        ipAddressInput();
        portInput();
        start();
    }

    public void start() {
        otherUsernames = new ArrayList<>();
        echoClient = new EchoClient(ipAddress,port,this);
        echoClient.start();
    }

    public void ipAddressInput(){
        System.out.println("Ip address : ");
        String ip = InputCli.readLine();
        while(!InputValidator.validateIP(ip)){
            System.out.println("Not valid IP! ");
            System.out.println("IP address : ");
            ip = InputCli.readLine();
        }
        ipAddress = ip;
    }

    public void portInput(){
        System.out.println("Port : ");
        String p = InputCli.readLine();
        while(!InputValidator.validatePORT(p)){
            System.out.println("Not valid port number!");
            System.out.println("Port : ");
            p = InputCli.readLine();
        }
        port = Integer.parseInt(p);
    }

    public void lobbyNoLongerAvailable(){
        System.out.println("Too long, in the meantime the match has begun! You have been entered in another lobby!");
    }

    public void disconnection(boolean clientDisc){
        if (clientDisc) System.out.println("The match finished because a client has disconnected! \n");
        else System.out.println("The server has disconnected\n");
        System.out.println("Do you want to look for another game (s) or do you want to go out (any key) ?");
        if(InputCli.readLine().equals("s")) start();
        else System.exit(0);
    }

    public void disconnectionForLobby(){
        System.out.println("Disconnection...\n");
        System.out.println("Do you want to look for another game (s) or do you want to go out (any key) ?");
        if(InputCli.readLine().equals("s")) start();
        else System.exit(0);
    }

    public void login(boolean rejectedBefore){
        if(!rejectedBefore) System.out.println("You have to log in!\n");
        else System.out.println("Your name already exists, enter a new one!\n");
        System.out.println("Username :");
        String username = InputCli.readLine();
        while (!InputValidator.validateUSERNAME(username)){
            System.out.println("Username :");
            username = InputCli.readLine();
        }
        echoClient.sendMsg(new RequestMsgWriter().loginRequest(username));
    }

    public void logged(List<String> usernames, String yourUsername){
        myUsername = yourUsername;
        otherUsernames.addAll(usernames);
        System.out.println("Hi " + yourUsername + ", you're in!\n");
        if(otherUsernames.isEmpty()) System.out.println("Wow, you're the first component of this game. You have to wait other players to start the match.");
        else {
            System.out.println("The other players in this game are :");
            for (String user : otherUsernames)
                System.out.println(user);
        }
    }

    public void newUser(String username){
        otherUsernames.add(username);
        System.out.println("A new user logged in, his username is : " + username + "\n");
    }

    public void startMatch(){
        System.out.println("There is a new player with you now, from now you can start the game whenever " +
                "you want (the match will start in 2 or 3 player mode depending on the players in the game at the " +
                "time of the start, writing \"start\"");
        try {
            if (InputCli.in.readLine().equals("start")) echoClient.sendMsg(new RequestMsgWriter().startGameRequest(myUsername));
            else System.out.println("Waiting for the third player...\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitStartingMatch(String creator){
        System.out.println("\n" + creator + " is the match creator, so he can start the match now or wait for another player...");
    }

    public void matchStarted(){
        System.out.println("The game has started\n");
    }
}
