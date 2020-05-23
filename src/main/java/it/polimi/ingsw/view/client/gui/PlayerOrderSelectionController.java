package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.viewComponents.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

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
    @FXML
    private Label playerName;

    private Gui gui;

    private ArrayList<Player> players;

    private int currentPlayerId = 0;


    @FXML
    public void confirmFirstPlayerAndStartGame(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,  players.get(currentPlayerId).getUsername() + " will start the match. Are you sure?", ButtonType.NO, ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            gui.sendChooseStartingPlayerRequest(players.get(currentPlayerId).getUsername());
        }
    }

    @FXML
    public void nextPlayer(ActionEvent actionEvent) {
        if(currentPlayerId >= players.size()-1 )
            currentPlayerId = 0;
        else
            currentPlayerId++;

        numberOfPlayer.setText(currentPlayerId+1 + " of " + players.size());
        playerName.setText(players.get(currentPlayerId).getUsername());
    }
    @FXML
    public void prevPlayer(ActionEvent actionEvent) {
        if(currentPlayerId == 0)
            currentPlayerId = players.size()-1;
        else
            currentPlayerId--;

        numberOfPlayer.setText(currentPlayerId+1 + " of " + players.size());
        playerName.setText(players.get(currentPlayerId).getUsername());
    }


    public void setInstructionLabel(String text){
        informationLabel.setText(text);
    }


    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
        numberOfPlayer.setText(currentPlayerId+1 + " of " + players.size());
        playerName.setText(players.get(currentPlayerId).getUsername());
    }
}
