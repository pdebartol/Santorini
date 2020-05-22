package it.polimi.ingsw.view.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PlayerOrderSelectionController {

    @FXML
    public Button prevButton;
    @FXML
    public Button nextButton;
    @FXML
    public Label numberOfPlayer;
    @FXML
    public Button selectFirstPlayer;
    @FXML
    public ImageView playImageViewButton;
    @FXML
    private Label informationLabel;

    private Gui gui;

    @FXML
    public void confirmFirstPlayerAndStartGame(MouseEvent mouseEvent) {
    }
    @FXML
    public void nextPlayer(ActionEvent actionEvent) {
    }
    @FXML
    public void prevPlayer(ActionEvent actionEvent) {
    }


    public void setInformationLabel(String text){
        informationLabel.setText(text);
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }
}
