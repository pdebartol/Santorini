package it.polimi.ingsw.view.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginWaitController {

    private Gui gui;

    @FXML
    private Button waitButton;

    @FXML
    private Button startButton;

    @FXML
    private Label informationBox;

    @FXML
    private Label playersNameBox;

    public void starGameNow(ActionEvent actionEvent) {
    }

    public void wait(ActionEvent actionEvent) {
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

    public void setPlayersNameBox(String text){
        playersNameBox.setText(text);
    }
}
