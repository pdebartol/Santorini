package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.client.View;
import it.polimi.ingsw.view.client.viewComponents.God;
import it.polimi.ingsw.view.client.viewComponents.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


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
     * Scene and Controller for the God selection scene
     */

    private Scene godSelectionScene;
    private GodSelectionController godSelectionController;

    /**
     * GUI's primary stage where all the scenes are set
     */

    private Stage primaryStage;

    /**
     * Initial connection scene, useful when there are network related problems
     */

    private Scene initialScene;

    private ListIterator<God> godsIterator;

    /**
     * Keeps track of the current god during godSelectionScene
     */

    int currentGodId = 0;

    /**
     * Keeps track of the selected Gods from the challenger
     */

    ArrayList<Integer> selectedGodIds = new ArrayList<>();

    public Gui (String ip, int port,Stage stage, Scene scene){
        super(ip,port);
        this.primaryStage = stage;
        this.initialScene = scene;
        godsIterator = gods.listIterator(0);
        initLoginUsername();
        initLoginWait();
        initGodSelection();
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
     * This method loads the FXML of the god selection scene and initializes its controller
     */

    private void initGodSelection(){
        try {
            FXMLLoader loader = GuiManager.loadFXML("godSelection");
            Parent root = loader.load();
            godSelectionScene = new Scene(root);
            godSelectionController = loader.getController();
            godSelectionController.setGui(this);
            godSelectionController.initializeGods(gods.get(0));
            godSelectionController.setGodProgression(1 + " of " + gods.size());
        } catch (IOException e) {
            System.out.println("Could not initialize GodSelection Scene");
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


    // Useless

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
            infoMessage = "You are currently 2 players in the game, enter \"s\" to start the game now or \"w\" to wait for a third player!";
            Platform.runLater(
                    () -> {
                        loginWaitController.showStartButton();
                        loginWaitController.showWaitButton();
                        loginWaitController.setInformationBox(infoMessage);
                    });

        } else {
            infoMessage = "You are currently 3 players in the game, press enter to start the game now! The match will automatically start in 2 minutes";
            Platform.runLater(
                    () -> {
                        loginWaitController.showStartButton();
                        loginWaitController.setInformationBox(infoMessage);
                    });

        }



    }

    @Override
    public void selectGods() {
        String infoMessage = "You are the challenger! Now you have to chose " + (players.size() + 1) + " Gods for this match! Wait...";

        Platform.runLater(
                () -> {
                    primaryStage.setScene(godSelectionScene);
                    primaryStage.show();
                    godSelectionController.setInstructionLabel(infoMessage);
                });
    }

    @Override
    public void selectGod(List<Integer> ids) {

    }

    @Override
    public void selectStartingPlayer() {

    }

    @Override
    public void setWorkerOnBoard(String gender, boolean rejectedBefore) {

    }

    @Override
    public void showLoginDone() {
        String infoMessage = "Hi " + myPlayer.getUsername() + ", you're in!\n";

        if (players.size() == 0){
            infoMessage += " You're the creator of this match, so you will decide "+
                    "when to start the game. You can either start it when another player logs in or wait for a third player.\n"
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
        String infoMessage = loginWaitController.getInformationBox() + "\n" + username + " is a new player!";
        Platform.runLater(
                () -> {
                    loginWaitController.setPlayersNameBox(loginWaitController.getPlayersBox()+", "+ username);
                    loginWaitController.setInformationBox(infoMessage);
                });
    }

    @Override
    public void showWaitMessage(String waitFor, String author) {
        switch (waitFor) {
            case "startMatch":
                 final String infoMatch = ("Waiting for " + author + "(creator)'s start game command...");
                 Platform.runLater(
                        () -> loginWaitController.setInformationBox(infoMatch));
                break;
            case "createGods":
                final String infoGods = author + " is the challenger, he is choosing " + (players.size() + 1) + " divinities for this game...";
                Platform.runLater(
                        () -> {
                            loginWaitController.hideWaitButton();
                            loginWaitController.hideStartButton();
                            loginWaitController.setInformationBox(infoGods);
                        });
                break;
        }


    }

    @Override
    public void showMatchStarted() {
        String infoMessage = "The match has been started...";

        Platform.runLater(
                () -> loginWaitController.setInformationBox(infoMessage));


    }

    @Override
    public void showGodsChoiceDone(ArrayList<Integer> ids) {
        System.out.println(ids);
    }

    @Override
    public void showGodsChallengerSelected(String username, ArrayList<Integer> ids) {

    }

    @Override
    public void showMyGodSelected() {

    }

    @Override
    public void showGodSelected(String username) {

    }

    @Override
    public void showStartingPlayer(String username) {

    }

    @Override
    public void showBoard() {

    }

    @Override
    public void serverNotFound() {
        System.out.print("server not found");
        Platform.runLater(
                () -> alertUser("Server Error", "Server not found!"));
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


    /**
     * This method is called during the godSelection scene when the button next button is pressed
     * @return the next God in the list
     */

    public God getNextGod() {
        if(currentGodId >= gods.size()-1) currentGodId = 0;
        else currentGodId++;

        godSelectionController.setGodProgression(currentGodId + 1 + " of " + gods.size());
        return gods.get(currentGodId);
    }

    /**
     * This method is called during the godSelection scene when the button previous button is pressed
     * @return the previous God in the list
     */


    public God getPreviousGod() {
        if(currentGodId == 0) currentGodId = gods.size()-1;
        else currentGodId--;

        godSelectionController.setGodProgression(currentGodId + 1 + " of " + gods.size());

        return gods.get(currentGodId);

    }

    /**
     * This method returns the current god's choice
     * @return current god
     */

    public God getCurrentGod(){
        return gods.get(currentGodId);
    }


    /**
     * This method adds the current God to the list of selected Gods
     */

    public void addCurrentGod(){
        selectedGodIds.add(currentGodId);
        gods.remove(currentGodId);

        if (currentGodId >= gods.size()-1) {
            currentGodId = 0;
        } else {
            currentGodId++;
        }

        godSelectionController.initializeGods(gods.get(currentGodId));
        godSelectionController.setGodProgression(currentGodId + 1 + " of " + gods.size());

        if(players.size()+1 -selectedGodIds.size() > 0)
            godSelectionController.setInstructionLabel("You have to choose " + (players.size() + 1 - selectedGodIds.size()) + " more gods. Press enter to continue...");
        else{
            sendCreateGodsRequest(selectedGodIds);
            //TODO Wait for others player to choose gods
        }
    }

}

