package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.client.View;
import it.polimi.ingsw.view.client.viewComponents.God;
import it.polimi.ingsw.view.client.viewComponents.Player;
import it.polimi.ingsw.view.client.viewComponents.Worker;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;

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
     * Scene and Controller for the end game scene
     */

    private Scene endGameScene;
    private EndGameController endGameSceneController;

    /**
     * GUI's primary stage where all the scenes are set
     */

    private final Stage primaryStage;

    /**
     * Initial connection scene, useful when there are network related problems
     */

    private final Scene initialScene;

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

    /**
     * Keeps track of the challenger during setup
     */

    private boolean isChallenger = false;

    /**
     * Keeps track of the state of the game
     */

    private boolean endGame = false;

    /**
     * Timer that disconnects the user for inactivity
     */

    public Timeline timer;


    public Gui (String ip, int port,Stage stage, Scene scene){
        super(ip,port);
        this.primaryStage = stage;
        this.initialScene = scene;
        createTimer();
        initLoginUsername();
        initPlayerOrderSelection();
        initGameScene();
        initEndGame();
    }


    /**
     * This method loads the FXML of the login scene and initializes its controller
     */

    private void initLoginUsername() {
                    try {
                        FXMLLoader loader = GuiManager.loadFXML("loginUsername");
                        Parent root = loader.load();
                        loginUserScene = new Scene(root);
                        LoginUsernameController loginUserController = loader.getController();
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
                        loginWaitController.setMyPlayerUsername(myPlayer.getUsername());
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
            FXMLLoader loader = GuiManager.loadFXML("gameScene_02");
            Parent root = loader.load();
            gameScene = new Scene(root);
            gameSceneController = loader.getController();
            gameSceneController.setGui(this);
            gameSceneController.hideGod();
            gameSceneController.hideMoveButton();
            gameSceneController.hideBuildButton();
            gameSceneController.hideEndButton();
            gameSceneController.hideImageViews();
        } catch (IOException e) {
            System.out.println("Could not initialize Game Scene");
        }
    }


    /**
     * This method loads the FXML of the end game scene and initializes its controller
     */

    private void initEndGame() {
        try {
            FXMLLoader loader = GuiManager.loadFXML("endGame");
            Parent root = loader.load();
            endGameScene = new Scene(root);
            endGameSceneController = loader.getController();
            endGameSceneController.setGui(this);
        } catch (IOException e) {
            System.out.println("Could not initialize loginWait Scene");
        }
    }


    /**
     * This method throws an alert to the user and it is called when something goes wrong
     * @param title alert's title
     * @param text alert's text
     */

    private void alertUser(String title, String text, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    @Override
    public void setMyIp(){

    }

    @Override
    public void newGame() {

        Platform.runLater(
                () -> {
                    alertUser("New Game", "You are starting a new game!", Alert.AlertType.INFORMATION);
                    primaryStage.setScene(initialScene);
                });
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
                    () -> alertUser("Username Error","Username already in use!", Alert.AlertType.WARNING));
        }

    }

    @Override
    public void startMatch() {
        String infoMessage;
        if (players.size() == 1) {
            infoMessage = "You are currently 2 players in the game, press start to start the game now or wait to wait for a third player!";
            Platform.runLater(
                    () -> {
                        loginWaitController.showStartButton();
                        loginWaitController.showWaitButton();
                        loginWaitController.setInformationBox(infoMessage);
                    });

        } else {
            //TODO two minutes timeout to strt
            infoMessage = "You are currently 3 players in the game, press start now!";
            Platform.runLater(
                    () -> {
                        loginWaitController.hideWaitButton();
                        loginWaitController.showStartButton();
                        loginWaitController.setInformationBox(infoMessage);
                    });

        }
    }

    @Override
    public void selectGods() {
        restartTimer();
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
            restartTimer();
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
        restartTimer();
        Platform.runLater(
                () -> {
                    playerOrderController.setInstructionLabel("Insert an existing username...");
                    ArrayList<Player> tempPlayers = (ArrayList<Player>) players.clone();
                    tempPlayers.add(myPlayer);
                    playerOrderController.setPlayers(tempPlayers);
                    primaryStage.setScene(playerOrderScene);
                    primaryStage.show();
                });

    }

    @Override
    public void setWorkerOnBoard(String gender, boolean rejectedBefore) {
        restartTimer();
        if(!rejectedBefore){
            Platform.runLater(
                    () -> {
                        gameSceneController.setInstructionLabel("Setup your " + gender + " worker!");
                        gameSceneController.setupWorker(gender);
                    });
        }
        else{
            Platform.runLater(
                    () -> {
                        gameSceneController.restoreImage();
                        alertUser("Worker Error","Invalid worker position!", Alert.AlertType.WARNING);
                    });
        }
    }

    @Override
    public void turn(String firstOperation) {
        restartTimer();
        Platform.runLater(
                () -> nextOperation(firstOperation));
    }

    @Override
    public void move() {
        Platform.runLater(
                () -> {
                    gameSceneController.showMoveButton();
                    gameSceneController.setInstructionLabel("You have to move!" );
                });

    }

    @Override
    public void build() {
        Platform.runLater(
                () -> {
                    gameSceneController.showBuildButton();
                    gameSceneController.setInstructionLabel("You have to build!" );
                });

    }

    @Override
    public void moveOrBuild() {
        Platform.runLater(
                () -> {
                    gameSceneController.showMoveButton();
                    gameSceneController.showBuildButton();
                    gameSceneController.setInstructionLabel("Please move one of your workers or select one of your workers and build something!" );
                });
    }

    @Override
    public void buildOrEnd() {
        Platform.runLater(
                () -> {
                    gameSceneController.showBuildButton();
                    gameSceneController.showEndButton();
                    gameSceneController.setInstructionLabel("You can build or end your turn!" );
                });
    }

    @Override
    public void showLoginDone() {
        String infoMessage = "Hi " + myPlayer.getUsername() + ", you're in!\n"
                 + " You're the creator of this match, so you will decide when to start the game.\n"
                    + "You can either start it when another player logs in or wait for a third player.\n"
                    + "The moment the third player logs in you can start the game, which will still start automatically after 2 minutes "
                    + "from the login of the third player.";


        Platform.runLater(
                () -> {
                    initLoginWait();
                    loginWaitController.setInformationBox(infoMessage);
                    primaryStage.setScene(loginWaitScene);
                    primaryStage.show();
                });

    }

    @Override
    public void showNewUserLogged(String username, Color color) {
        String infoMessage = loginWaitController.getInformationBox() + "\n" + username + " is a new player!";
        Platform.runLater(
                () -> {
                    if(players.size() == 1) loginWaitController.setSecondPlayer(username);
                    else loginWaitController.setThirdPlayerUsername(username);
                    loginWaitController.setInformationBox(infoMessage);
                });

    }

    @Override
    public void showWaitMessage(String waitFor, String author) {
        pauseTimer();
        switch (waitFor) {
            case "startMatch":
                 final String infoMatch = (author + " is the creator! Waiting for him to start the game...");
                 Platform.runLater(
                        () -> {
                            loginWaitController.setInformationBox(infoMatch);
                            if(players.size() == 1) loginWaitController.setSecondPlayer(author);
                            else{
                                loginWaitController.setSecondPlayer(author);
                                loginWaitController.setThirdPlayerUsername(players.get(1).getUsername());
                            }
                        });
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
                        () -> godSelectionController.setInstructionLabel(author + " is choosing the starter player..."));
                break;
            case "setupMaleWorkerOnBoard":
                Platform.runLater(
                        () -> gameSceneController.setInstructionLabel(author + " is placing his male worker on the board..."));
                break;
            case "setupFemaleWorkerOnBoard":
                Platform.runLater(
                        () -> gameSceneController.setInstructionLabel(author + " is placing his female worker on the board..."));
                break;
            case "hisTurn":
                Platform.runLater(
                        () -> gameSceneController.setInstructionLabel(author + " is playing his turn.."));
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
        pauseTimer();

        String infoMessage = "\nYou will receive the last god left after choosing the other players.";

        Platform.runLater(
                () -> godSelectionController.setInstructionLabel(infoMessage));


    }


    @Override
    public void showGodsChallengerSelected(String username, ArrayList<Integer> ids) {



        String infoMessage =(username) + (" has chosen the following gods : ") + (getGodById(ids.get(0)).getName());
        for(int i = 1; i < ids.size(); i++){
            infoMessage = infoMessage +   (", ") + (getGodById(ids.get(i)).getName());
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
        pauseTimer();
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
                    ArrayList<Player> tempPlayers = (ArrayList<Player>) players.clone();
                    tempPlayers.add(0, myPlayer);
                    gameSceneController.setPlayers(tempPlayers);
                    primaryStage.setScene(gameScene);
                    primaryStage.show();
                    if(username.equals(myPlayer.getUsername()))
                        gameSceneController.setInstructionLabel(username + " You will be the starter player!");
                    else
                     gameSceneController.setInstructionLabel(username + " will be the starter player!");


                });
        }


    @Override
    public void showBoard() {
        Platform.runLater(
                () -> gameSceneController.updateBoard(gameBoard));

    }

    @Override
    public void showTurnEnded(String username) {

    }

    @Override
    public void showMyTurnEnded() {
        Platform.runLater(
                () -> {
                    gameSceneController.hideBuildButton();
                    gameSceneController.hideMoveButton();
                    gameSceneController.hideEndButton();
                    gameSceneController.state = "wait";
                    gameSceneController.updateBoard(gameBoard);
                });
    }

    @Override
    public void showTurnErrors(List<String> errors) {


        StringBuilder errorMessage = new StringBuilder("Invalid action! errors encountered : ");

        for (String error : errors) {
            switch (error) {
                case "BMU":
                    errorMessage.append(", Athena blocked upward movements");
                    break;
                case "CDU":
                    errorMessage.append(", you can't build a dome under yourself");
                    break;
                case "CMU":
                    errorMessage.append(", you can't move up because you build before you moved");
                    break;
                case "EBNP":
                    errorMessage.append(", the additional build can't be on a perimeter space");
                    break;
                case "EBNSS":
                    errorMessage.append(", the additional build can't be on the same space");
                    break;
                case "EBOSS":
                    errorMessage.append(", the additional build must be built on top of your first block");
                    break;
                case "EMNB":
                    errorMessage.append(", your worker can't moves back to the space it started on");
                    break;
                case "ILB":
                    errorMessage.append(", you can't build this block in the space you selected");
                    break;
                case "ILM":
                    errorMessage.append(", the space where you want to move is too high");
                    break;
                case "ID":
                    errorMessage.append(", there is a dome");
                    break;
                case "NA":
                    errorMessage.append(", the space you selected is not adjacent");
                    break;
                case "NF":
                    errorMessage.append(", the space you selected is occupied");
                    break;
                case "SDNF":
                    errorMessage.append(", you can't push the worker on this space because the space in the same direction is occupied");
                    break;
                case "EBND":
                    errorMessage.append(", the additional build block can't be a dome");
                    break;
                case "IWW":
                    errorMessage.append(", you must continue the shift with the first worker used");
            }
        }
        System.out.println("Errors: " + errors);

        errorMessage.append(".");
        errorMessage.delete(37,38);

        String finalErrorMessage = errorMessage.toString();
        Platform.runLater(() -> {
            alertUser("Match Information", finalErrorMessage, Alert.AlertType.WARNING);
            gameSceneController.restoreImage();
        });
    }


    @Override
    public void serverNotFound() {
        Platform.runLater(
                () -> alertUser("Server Error", "Server not found!", Alert.AlertType.ERROR));
    }

    @Override
    public void showAnotherClientDisconnection() {
        Platform.runLater(
                () -> {
                    alertUser("Server Error", "Match ended! Another client disconnected from the match!", Alert.AlertType.ERROR);
                    primaryStage.setScene(initialScene);
                });
    }

    @Override
    public void showDisconnectionForLobbyNoLongerAvailable() {
        Platform.runLater(
                () -> {
                    alertUser("Server Error", "Lobby is already full! You will need to find another match!", Alert.AlertType.ERROR);
                    primaryStage.setScene(initialScene);
                });
    }

    @Override
    public void showServerDisconnection() {
        if(!endGame)
            Platform.runLater(
                    () -> {
                       alertUser("Server Error", "The server disconnected!", Alert.AlertType.ERROR);
                        primaryStage.setScene(initialScene);
                    });
    }

    @Override
    public void showDisconnectionForInputExpiredTimeout() {
        Platform.runLater(
                () -> {
                    alertUser("Server Error", "You were disconnected because the timeout expired!", Alert.AlertType.ERROR);
                    primaryStage.setScene(initialScene);
                });
    }

    @Override
    public void showPlayerLose(String username) {
        ArrayList<Player> tempPlayers = (ArrayList<Player>) players.clone();
        tempPlayers.add(myPlayer);
        Platform.runLater(
                () -> alertUser("Match Information", username+ " has lost!", Alert.AlertType.INFORMATION));
    }

    @Override
    public void showYouLose(String reason, String winner) {
        endGame = true;
        Platform.runLater(
                () -> {
                    endGameSceneController.setGodImage(myPlayer.getGod().getId());
                    endGameSceneController.setLose(reason+". "+ winner  + " has won!");
                    primaryStage.setScene(endGameScene);
                    primaryStage.show();
                });
    }

    @Override
    public void showYouWin(String reason) {
        endGame = true;
        Platform.runLater(
                () -> {

                    endGameSceneController.setGodImage(myPlayer.getGod().getId());
                    endGameSceneController.setWin(reason);
                    primaryStage.setScene(endGameScene);
                    primaryStage.show();

                });

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

    /**
     * This method is used to filter only the gods that are available to choose (both for challenger and non-challenger)
     * @param ids of the gods that the challenger selected
     * @return a list containing the filtered gods
     */
    private List<God> getVisibleGods(List<Integer> ids){
        if(ids.size() > 0 && isChallenger)
            return gods.stream().filter(God -> !ids.contains(God.getId())).collect(Collectors.toList());
        else if(ids.size() > 0 )
            return gods.stream().filter(God -> ids.contains(God.getId())).collect(Collectors.toList());
        else
            return gods;
    }

    public God getPlayerGod(Player p){
        return getGodById(p.getGod().getId());
    }

    public Color getMyColor(){
        return myPlayer.getWorkers().get(0).getColor();
    }

    public String getWorkerGender(int x, int y){
        return gameBoard.getSquareByCoordinates(x,y).getWorker().getGender();
    }

    public Worker getSelectedWorker(){
        if(workerForThisTurnCoordinates[0] != -1  && workerForThisTurnCoordinates[1] != -1)
            return gameBoard.getSquareByCoordinates(workerForThisTurnCoordinates[0],workerForThisTurnCoordinates[1]).getWorker();
        return null;
    }

    public int[] getMyWorkerPosition(String gender){
        int[] coordinates = new int[2];
        if(myPlayer.getWorkerByGender(gender) != null){
            coordinates[0] = myPlayer.getWorkerByGender(gender).getCurrentPosition().getX();
            coordinates[1] = myPlayer.getWorkerByGender(gender).getCurrentPosition().getY();
        }
        else{
            coordinates[0] = -1;
            coordinates[1] = -1;
        }
        return coordinates;
    }



    public boolean isMyWorker(int x, int y){
        Worker maleWorker = myPlayer.getWorkerByGender("male");
        Worker femaleWorker = myPlayer.getWorkerByGender("female");
        if(maleWorker != null && maleWorker.getCurrentPosition().getX() == x && maleWorker.getCurrentPosition().getY() == y)
            return true;
        return femaleWorker != null && femaleWorker.getCurrentPosition().getX() == x && femaleWorker.getCurrentPosition().getY() == y;
    }

    public void createTimer(){
        timer = new Timeline(new KeyFrame(
                Duration.millis(180000),
                ae -> clientHandler.disconnectionForTimeout()));
    }


    void restartTimer() { timer.stop(); timer.playFromStart(); }

    void pauseTimer() { timer.pause(); }

    boolean isMyPlayer(Player p){
        return myPlayer.getUsername().equals(p.getUsername());
    }
}

