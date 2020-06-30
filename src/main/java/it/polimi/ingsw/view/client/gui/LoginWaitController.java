package it.polimi.ingsw.view.client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * This Class is the controller for the wait after login scene.
 * @author pierobartolo
 */

public class LoginWaitController {

    private Gui gui;

    @FXML
    public Label myPlayerUsername;
    @FXML
    public Label secondPlayerUsername;
    @FXML
    public Label thirdPlayerUsername;
    @FXML
    public ImageView thirdPlayerImageView;
    @FXML
    public ImageView secondPlayerImageView;
    @FXML
    public ImageView myPlayerImageView;
    @FXML
    public ImageView start;
    @FXML
    public ImageView wait;
    @FXML
    private Label informationBox;

    /**
     * This method is called when the creator of the match starts the game.
     * @param mouseEvent none
     */

    @FXML
    public void start(MouseEvent mouseEvent) {
        gui.sendStartGameRequest();
        gui.pauseStartMatchTimer();
    }

    /**
     * This method is called when the creator of the match chooses to wait for another player.
     * @param mouseEvent none
     */

    @FXML
    public void wait(MouseEvent mouseEvent) {
        setInformationBox("Wait for a third player..");
        this.hideStartButton();
        this.hideWaitButton();
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void hideStartButton(){
        start.setDisable(true);
        start.setVisible(false);
    }

    public void showStartButton(){
        start.setDisable(false);
        start.setVisible(true);
    }

    public void hideWaitButton(){
        wait.setDisable(true);
        wait.setVisible(false);
    }
    public void showWaitButton(){
        wait.setDisable(false);
        wait.setVisible(true);
    }

    public void setInformationBox(String text){
        informationBox.setText(text);
    }

    public String getInformationBox(){
        return informationBox.getText();
    }

    public void setSecondPlayer(String text){
        secondPlayerUsername.setText(text);
    }

    public void setThirdPlayerUsername(String text){
        thirdPlayerUsername.setText(text);
    }

    public void setMyPlayerUsername(String text){
        myPlayerUsername.setText(text);
    }

    public void onPressedStart(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_start_pressed.png");
        start.setImage(updateButton);

    }

    public void onPressedWait(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_wait_pressed.png");
        wait.setImage(updateButton);
    }

}
