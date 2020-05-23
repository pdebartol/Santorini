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
import java.util.stream.Collectors;


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
     * Scene and Controller for the PlayerOrderSelection scene
     */

    private Scene playerOrderScene;
    private PlayerOrderSelectionController playerOrderController;

    /**
     * Scene and Controller for the Game scene
     */

    private Scene gameScene;
    private GameController gameSceneController;


    /**
     * GUI's primary stage where all the scenes are set
     */

    private Stage primaryStage;

    /**
     * Initial connection scene, useful when there are network related problems
     */

    private Scene initialScene;


    /**
     * Keeps track of the current god id during godSelectionScene
     */

    private int currentGodId = 0;

    /**
     * Keeps track of the god ids selected from the challenger
     */

    private List<Integer> challengerSelectedGodsIds = new ArrayList<>();

    /**
     * Keeps track of the selected God from the user
     */

    private int userSelectedGodId;



    private int currentPlayer;


    private boolean isChallenger = false;

    public Gui (String ip, int port,Stage stage, Scene scene){
        super(ip,port);
        this.primaryStage = stage;
        this.initialScene = scene;
        initLoginUsername();
        initLoginWait();
        initPlayerOrderSelection();
        initGameScene();
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

    private void initGodSelection(List<God> visibleGods){
        try {
            FXMLLoader loader = GuiManager.loadFXML("godSelection");
            Parent root = loader.load();
            godSelectionScene = new Scene(root);
            godSelectionController = loader.getController();
            godSelectionController.setGui(this);
            godSelectionController.initializeGods(visibleGods.get(0));
            godSelectionController.setGodProgression(1 + " of " + visibleGods.size());
            godSelectionController.hideFinalConfirmButton();
        } catch (IOException e) {
            System.out.println("Could not initialize GodSelection Scene");
        }
    }

    /**
     * This method loads the FXML of the playerOrderSelection scene and initializes its controller
     */

    private void initPlayerOrderSelection(){
        try {
            FXMLLoader loader = GuiManager.loadFXML("playerOrderSelection");
            Parent root = loader.load();
            playerOrderScene = new Scene(root);
            playerOrderController = loader.getController();
            playerOrderController.setGui(this);
        } catch (IOException e) {
            System.out.println("Could not initialize PlayerOrderSelection Scene");
        }
    }

    /**
     * This method loads the FXML of the game scene and initializes its controller
     */

    private void initGameScene(){
        try {
            FXMLLoader loader = GuiManager.loadFXML("gameScene");
            Parent root = loader.load();
            gameScene = new Scene(root);
            gameSceneController = loader.getController();
            gameSceneController.setGui(this);
        } catch (IOException e) {
            System.out.println("Could not initialize Game Scene");
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
                        loginWaitController.hideWaitButton();
                        loginWaitController.setInformationBox(infoMessage);
                    });

        }
    }

    @Override
    public void selectGods() {
        String infoMessage = "You are the challenger! Now you have to chose " + (players.size() + 1) + " Gods for this match!";
        isChallenger = true;
        Platform.runLater(
                () -> {
                    initGodSelection(getVisibleGods(challengerSelectedGodsIds));
                    primaryStage.setScene(godSelectionScene);
                    primaryStage.show();
                    godSelectionController.setInstructionLabel(infoMessage);
                });
    }

    @Override
    public void selectGod(List<Integer> ids) {
        if(ids.size() == 1){
            Platform.runLater(
                    () -> godSelectionController.setInstructionLabel("The last god left is " + getGodById(ids.get(0)).getName() + " and he will be your god for this game."));
            sendChooseGodRequest(ids.get(0));
        }
        else{
            Platform.runLater(
                    () -> {
                        List<God> visibleGods = getVisibleGods(ids);
                        challengerSelectedGodsIds = ids;
                        godSelectionController.showConfirmGodButton();
                        godSelectionController.initializeGods(visibleGods.get(0));
                        godSelectionController.setGodProgression(1 + " of " + visibleGods.size());
                        godSelectionController.setInstructionLabel("Choose one of the following gods!");
                    });
        }


        }


    @Override
    public void selectStartingPlayer() {
        Platform.runLater(
                () -> {
                    playerOrderController.setInstructionLabel("Insert an existing username...");
                    players.add(myPlayer);
                    playerOrderController.setPlayers(players);
                    primaryStage.setScene(playerOrderScene);
                    primaryStage.show();
                });

    }

    @Override
    public void setWorkerOnBoard(String gender, boolean rejectedBefore) {

    }

    @Override
    public void turn(String firstOperation) {

    }

    @Override
    public void move() {

    }

    @Override
    public void build() {

    }

    @Override
    public void moveOrBuild() {

    }

    @Override
    public void buildOrEnd() {

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
            case "choseStartingPlayer":
                Platform.runLater(
                        () -> godSelectionController.setInstructionLabel(author + " is choosing a the starter player..."));
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


        String infoMessage = "\n You will receive the last god left after choosing the other players.";

        Platform.runLater(
                () -> godSelectionController.setInstructionLabel(infoMessage));


    }


    @Override
    public void showGodsChallengerSelected(String username, ArrayList<Integer> ids) {



        String infoMessage =(username) + (" has chosen the following gods : ") + (getGodById(ids.get(0)).getName());
        for(int i = 1; i < ids.size(); i++){
            infoMessage +=  (", ") + (getGodById(ids.get(i)).getName());
        }

        String finalInfoMessage = infoMessage;
        Platform.runLater(
                () -> {
                    List<God> visibleGods = getVisibleGods(ids);
                    initGodSelection(visibleGods);
                    challengerSelectedGodsIds = ids;
                    godSelectionController.initializeGods(visibleGods.get(0));
                    godSelectionController.setGodProgression(1 + " of " + visibleGods.size());
                    godSelectionController.setInstructionLabel(finalInfoMessage);
                    godSelectionController.hideConfirmGodButton();
                    primaryStage.setScene(godSelectionScene);
                    primaryStage.show();
                });


    }

    @Override
    public void showMyGodSelected() {
        Platform.runLater(
                () -> {
                    if(!isChallenger)
                        godSelectionController.setInstructionLabel("Good choice! Your God is " + myPlayer.getGod().getName() + ".");
                    else
                        godSelectionController.setInstructionLabel("Your God is " + myPlayer.getGod().getName() + ".");

                });

    }

    @Override
    public void showGodSelected(String username) {
        Platform.runLater(
                () -> godSelectionController.setInstructionLabel(username + " has chosen " + getPlayerByUsername(username).getGod().getName() + "."));

    }

    @Override
    public void showStartingPlayer(String username) {
        Platform.runLater(
                () -> {
                    primaryStage.setScene(gameScene);
                    primaryStage.show();
                    if(username.equals(myPlayer.getUsername()))
                        gameSceneController.setInstructionLabel(username + " You will be the starter player!");
                    else
                        gameSceneController.setInstructionLabel(username + " will be the starter player!");
                    gameSceneController.setupWorker();

                });


        }


    @Override
    public void showBoard() {

    }

    @Override
    public void showTurnEnded(String username) {

    }

    @Override
    public void showMyTurnEnded() {

    }

    @Override
    public void showTurnErrors(List<String> errors) {

    }

    @Override
    public void serverNotFound() {
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
        List<God> visibleGods = getVisibleGods(challengerSelectedGodsIds);
        if(currentGodId >= visibleGods.size()-1) currentGodId = 0;
        else currentGodId++;

        godSelectionController.setGodProgression(currentGodId + 1 + " of " + visibleGods.size());
        return visibleGods.get(currentGodId);
    }

    /**
     * This method is called during the godSelection scene when the button previous button is pressed
     * @return the previous God in the list
     */


    public God getPreviousGod() {
        List<God> visibleGods = getVisibleGods(challengerSelectedGodsIds);
        if(currentGodId == 0) currentGodId = visibleGods.size()-1;
        else currentGodId--;

        godSelectionController.setGodProgression(currentGodId + 1 + " of " + visibleGods.size());

        return visibleGods.get(currentGodId);

    }

    /**
     * This method returns the current god's choice
     * @return current god
     */

    public God getCurrentGod(){
        return getVisibleGods(challengerSelectedGodsIds).get(currentGodId);
    }


    /**
     * This method adds the current God to the list of selected Gods
     */

    public void addCurrentGod(){
        if(isChallenger) challengerSelectedGodsIds.add(getVisibleGods(challengerSelectedGodsIds).get(currentGodId).getId());
        else{
            userSelectedGodId = getVisibleGods(challengerSelectedGodsIds).get(currentGodId).getId();
            challengerSelectedGodsIds.remove((Object) userSelectedGodId);
        }

        if (currentGodId >= getVisibleGods(challengerSelectedGodsIds).size()-1) {
            currentGodId = 0;
        } else {
            currentGodId++;
        }
        List<God> visibleGods = getVisibleGods(challengerSelectedGodsIds);

        godSelectionController.initializeGods(visibleGods.get(currentGodId));
        godSelectionController.setGodProgression(currentGodId + 1 + " of " + visibleGods.size());

        if(players.size()+1 - challengerSelectedGodsIds.size() > 0 && isChallenger)
                godSelectionController.setInstructionLabel("You have to choose " + (players.size() + 1 - challengerSelectedGodsIds.size()) + " more gods. Press enter to continue...");
        else{
                godSelectionController.setInstructionLabel("Gods chosen! You're ready for the next phase!");
                godSelectionController.hideConfirmGodButton();
                godSelectionController.showFinalConfirmButton();
            }


    }

    public List<Integer> getSelectedGodsIds(){
        return challengerSelectedGodsIds;
    }

    public boolean getIsChallenger(){
        return isChallenger;
    }

    public int getUserSelectedGodId(){
        return userSelectedGodId;
    }

    private List<God> getVisibleGods(List<Integer> ids){
        if(ids.size() > 0 && isChallenger)
            return gods.stream().filter(God -> !ids.contains(God.getId())).collect(Collectors.toList());
        else if(ids.size() > 0 )
            return gods.stream().filter(God -> ids.contains(God.getId())).collect(Collectors.toList());
        else
            return gods;
    }


}

