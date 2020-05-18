package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.client.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Gui extends View {

    private LoginUsernameController loginUserController;


    private Stage loginUserStage;
    private Scene loginUserScene;

    private Stage stage;
    private Scene scene;

    public Gui (String ip, int port,Stage stage, Scene login){
        super(ip,port);
        this.stage = stage;
        this.scene = login;
        initLoginUsername();
    }


    private void initLoginUsername() {
        try {
            FXMLLoader loader = GuiManager.loadFXML("loginUsername");
            Parent root = loader.load();
            loginUserStage = new Stage();
            loginUserStage.setTitle("Login");
            loginUserScene = new Scene(root);
            loginUserController = loader.getController();
        } catch (IOException e) {
            System.out.println("Could not initialize loginUsername Scene");
        }
    }


    public void showLoginUsername(){
        loginUserController.setGui(this);
        stage.setScene(loginUserScene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void setMyIp(){

    }

    @Override
    public void setMyPort() {
    }

    @Override
    public void setUsername(boolean rejectedBefore) {
        System.out.println("Test");
        showLoginUsername();
    }

    @Override
    public void startMatch() {

    }

    @Override
    public void selectGods() {

    }

    @Override
    public void showLoginDone() {

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
    public void disconnectionForInputExpiredTimeout() {

    }
}

