package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.client.View;
import it.polimi.ingsw.view.client.viewComponents.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;


/**
 * This class implements the GUI with JavaFX
 * @author pierobartolo
 */

public class Gui extends View {

    /**
     * Scene and Controller for the login scene
     */

    private Scene loginUserScene;
    private LoginUsernameController loginUserController;

    /**
     * Scene and Controller for the lobby scene
     */

    private Scene loginWaitScene;
    private LoginWaitController loginWaitController;

    /**
     * GUI's primary stage where all the scenes are set
     */

    private Stage primaryStage;

    /**
     * Initial connection scene, useful when there are network related problems
     */

    private Scene initialScene;

    public Gui (String ip, int port,Stage stage, Scene scene){
        super(ip,port);
        this.primaryStage = stage;
        this.initialScene = scene;
        initLoginUsername();
        initLoginWait();

    }


    /**
     * This method loads the FXML of the login scene and initializes its controller
     */

    private void initLoginUsername() {
                    try {
                        FXMLLoader loader = GuiManager.loadFXML("loginUsername");
                        Parent root = loader.load();
                        loginUserScene = new Scene(root);
                        loginUserController = loader.getController();
                        loginUserController.setGui(this);
                    } catch (IOException e) {
                        System.out.println("Could not initialize loginUsername Scene");
                    }
    }

    /**
     * This method loads the FXML of the lobby scene and initializes its controller
     */

    private void initLoginWait() {
                    try {
                        FXMLLoader loader = GuiManager.loadFXML("loginWait");
                        Parent root = loader.load();
                        loginWaitScene = new Scene(root);
                        loginWaitController = loader.getController();
                        loginWaitController.setGui(this);
                        loginWaitController.hideStartButton();
                        loginWaitController.hideWaitButton();
                    } catch (IOException e) {
                        System.out.println("Could not initialize loginWait Scene");
                    }
    }


    /**
     * This method throws an alert to the user and it is called when something goes wrong
     * @param title alert's title
     * @param text alert's text
     */

    private void alertUser(String title, String text){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    @Override
    public void setMyIp(){

    }

    @Override
    public void setMyPort() {
    }

    @Override
    public void setUsername(boolean rejectedBefore) {
        // the server is asking for the username for the first time
        if(!rejectedBefore){
            Platform.runLater(
                    () -> {
                        primaryStage.setScene(loginUserScene);
                        primaryStage.show();
                    });
        }
        // username was rejected
        else{
            Platform.runLater(
                    () -> alertUser("Username Error","Username already in use!"));
        }

    }

    @Override
    public void startMatch() {
        String infoMessage;
        if (players.size() == 1) {
            infoMessage = ("You are currently 2 players in the game, enter \"s\" to start the game now or \"w\" to wait for a third player!");

        } else {
            infoMessage = ("You are currently 3 players in the game, press enter to start the game now! The match will automatically start in 2 minutes");
        }

        Platform.runLater(
                () -> {
                    loginWaitController.showStartButton();
                    loginWaitController.showWaitButton();
                    loginWaitController.setInformationBox(infoMessage);
                });
    }

    @Override
    public void selectGods() {

    }

    @Override
    public void selectGod() {

    }

    @Override
    public void showLoginDone() {
        String infoMessage = "Hi " + myPlayer.getUsername() + ", you're in!";

        if (players.size() == 0){
            infoMessage += " You're the creator of this match, so you will decide "+
                    "when to start the game. You can either start it when another player logs in or wait for a third player. "
                    + "The moment the third player logs in you can start the game, which will still start automatically after 2 minutes "
                    + "from the login of the third player.";
        }
        else
            infoMessage += (" You're currently ") + ((players.size() + 1) + (" players in this game : You"));

        String playersInGame = myPlayer.getUsername();
        for (Player player : players)
                playersInGame += (", ") + (player.getUsername());

        String finalInfoMessage = infoMessage;
        String finalPlayersInGame = playersInGame;
        Platform.runLater(
                () -> {
                    loginWaitController.setInformationBox(finalInfoMessage);
                    loginWaitController.setPlayersNameBox(finalPlayersInGame);
                    primaryStage.setScene(loginWaitScene);
                    primaryStage.show();
                });

    }

    @Override
    public void showNewUserLogged(String username, Color color) {
        String infoMessage = loginWaitController.getInformationBox() + username + " is a new player!";
        Platform.runLater(
                () -> {
                    loginWaitController.setInformationBox(infoMessage);
                });
    }

    @Override
    public void showWaitMessage(String waitFor, String author) {
        String infoMessage = "";
        switch (waitFor) {
            case "startMatch":
                 infoMessage = ("Waiting for " + author + "(creator)'s start game command...");
                break;
            case "createGods":
                 infoMessage = (author + " is the challenger, he is choosing " + (players.size() + 1) + " divinities for this game...");
        }

        String finalInfoMessage = infoMessage;
        Platform.runLater(
                () -> {
                    loginWaitController.setInformationBox(finalInfoMessage);
                });
    }

    @Override
    public void showMatchStarted() {

    }

    @Override
    public void showGodsChoiceDone(ArrayList<Integer> ids) {

    }

    @Override
    public void showGodsChallengerSelected(String username, ArrayList<Integer> ids) {

    }

    @Override
    public void showBoard() {

    }

    @Override
    public void serverNotFound() {
        System.out.print("server not found");
        Platform.runLater(
                () -> {
                    alertUser("Server Error", "Server not found!");
                });
    }

    @Override
    public void showAnotherClientDisconnection() {
        Platform.runLater(
                () -> {
                    alertUser("Server Error", "Match ended! Another client disconnected from the match!");
                    primaryStage.setScene(initialScene);
                });
    }

    @Override
    public void showDisconnectionForLobbyNoLongerAvailable() {
        Platform.runLater(
                () -> {
                    alertUser("Server Error", "Lobby is already full! You will need to find another match!");
                    primaryStage.setScene(initialScene);
                });
    }

    @Override
    public void showServerDisconnection() {
        Platform.runLater(
                () -> {
                   alertUser("Server Error", "The server disconnected!");
                    primaryStage.setScene(initialScene);
                });
    }

    @Override
    public void showDisconnectionForInputExpiredTimeout() {

    }

    @Override
    public void disconnectionForInputExpiredTimeout() {

    }

}

