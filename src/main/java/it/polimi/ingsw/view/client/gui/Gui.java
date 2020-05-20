package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.client.View;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Gui extends View {

    private LoginUsernameController loginUserController;
    private LoginWaitController loginWaitController;


    private Scene loginUserScene;
    private Scene loginWaitScene;

    private Stage primaryStage;

    public Gui (String ip, int port,Stage stage){
        super(ip,port);
        this.primaryStage = stage;
        initLoginUsername();
        initLoginWait();

    }


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

    private void initLoginWait() {
                    try {
                        FXMLLoader loader = GuiManager.loadFXML("loginWait");
                        Parent root = loader.load();
                        loginWaitScene = new Scene(root);
                        loginWaitController = loader.getController();
                        loginWaitController.setGui(this);
                    } catch (IOException e) {
                        System.out.println("Could not initialize loginWait Scene");
                    }
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
                    () -> loginUserController.notifyAlreadyInUse());
        }

    }

    @Override
    public void startMatch() {

    }

    @Override
    public void selectGods() {

    }

    @Override
    public void showLoginDone() {
        Platform.runLater(
                () -> {
                    primaryStage.setScene(loginWaitScene);
                    primaryStage.show();
                });

    }

    @Override
    public void showNewUserLogged(String username, Color color) {

    }

    @Override
    public void showWaitMessage(String waitFor, String author) {

    }

    @Override
    public void showMatchStarted() {

    }

    @Override
    public void showBoard() {

    }

    @Override
    public void serverNotFound() {

    }

    @Override
    public void showAnotherClientDisconnection() {

    }

    @Override
    public void showDisconnectionForLobbyNoLongerAvailable() {

    }

    @Override
    public void showServerDisconnection() {

    }

    @Override
    public void showDisconnectionForInputExpiredTimeout() {

    }

    @Override
    public void disconnectionForInputExpiredTimeout() {

    }
}

