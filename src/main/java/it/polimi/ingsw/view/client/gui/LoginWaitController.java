package it.polimi.ingsw.view.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    public Label thirdPlayerUsername2;
    @FXML
    public ImageView thirdPlayerImageView;
    @FXML
    public ImageView secondPlayerImageView;
    @FXML
    public ImageView myPlayerImageView;



    @FXML
    private Button waitButton;

    @FXML
    private Button startButton;

    @FXML
    private Label informationBox;

    @FXML
    private Label playersNameBox;



    /**
     * This method is called when the creator of the match starts the game.
     * @param actionEvent none
     */

    @FXML
    public void starGameNow(ActionEvent actionEvent) {
        gui.sendStartGameRequest();
    }


    /**
     * This method is called when the creator of the match chooses to wait for another player.
     * @param actionEvent
     */

    @FXML
    public void wait(ActionEvent actionEvent) {
        setInformationBox("Wait for a third player..");
        this.hideStartButton();
        this.hideWaitButton();
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }


    public void hideStartButton(){
        startButton.setDisable(true);
        startButton.setVisible(false);
    }

    public void showStartButton(){
        startButton.setDisable(false);
        startButton.setVisible(true);
    }

    public void hideWaitButton(){
        waitButton.setDisable(true);
        waitButton.setVisible(false);
    }
    public void showWaitButton(){
        waitButton.setDisable(false);
        waitButton.setVisible(true);
    }

    public void setInformationBox(String text){
        informationBox.setText(text);
    }

    public String getInformationBox(){
        return informationBox.getText();
    }

    public String getPlayersBox(){
        return playersNameBox.getText();
    }

    public void setPlayersNameBox(String text){
        playersNameBox.setText(text);
    }

    public void changeWaitButtonImageView(MouseEvent mouseEvent) {
    }

    public void changeStartButtonImageView(MouseEvent mouseEvent) {
    }
}
