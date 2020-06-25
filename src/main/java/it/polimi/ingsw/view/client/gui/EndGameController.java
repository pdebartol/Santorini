package it.polimi.ingsw.view.client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class EndGameController {

    @FXML
    public Button creditsButton;
    @FXML
    public Button newGameButton;
    @FXML
    public ImageView endGameFrame;
    @FXML
    public ImageView trumpetsRight;
    @FXML
    public ImageView trumpetsLeft;
    @FXML
    public ImageView playerGodImageView;
    @FXML
    public Label winLoseLabel;
    @FXML
    public ImageView cloudLeft;
    @FXML
    public ImageView cloudRight;
    @FXML
    public Label informationLabel;

    private Gui gui;


    public void newGame(MouseEvent mouseEvent){
        gui.newGame();
    }

    public void setGui(Gui gui){
        this.gui = gui;
    }

    public void setGodImage(int godId){
        playerGodImageView.setImage(GuiManager.loadGodEndGame(godId));
    }

    /**
     * This method is called when the client's user wins the match.
     */

    public void setWin(){
        winLoseLabel.setText("You Won!");
        informationLabel.setText("");
        cloudRight.setImage(GuiManager.loadImage("Layouts/Endgame/endgame_victorycloudright.png"));
        cloudLeft.setImage(GuiManager.loadImage("Layouts/Endgame/endgame_victorycloudleft.png"));
        trumpetsLeft.setImage(GuiManager.loadImage("Layouts/Endgame/endgame_victorytrumpets_left.png"));
        trumpetsRight.setImage(GuiManager.loadImage("Layouts/Endgame/endgame_victorytrumpets_right.png"));
        endGameFrame.setImage(GuiManager.loadImage("Layouts/Endgame/endgame_victorywin.png"));


    }

    /**
     * This method is called when the client's user loses the match.
     */

    public void setLose(){
        winLoseLabel.setText("You Lost!");
        informationLabel.setText("");
        cloudRight.setImage(GuiManager.loadImage("Layouts/Endgame/endgame_defeatcloudright.png"));
        cloudLeft.setImage(GuiManager.loadImage("Layouts/Endgame/endgame_defeatcloudleft.png"));
        trumpetsLeft.setImage(GuiManager.loadImage("Layouts/Endgame/endgame_defeattrumpetleft.png"));
        trumpetsRight.setImage(GuiManager.loadImage("Layouts/Endgame/endgame_defeattrumpetright.png"));
        endGameFrame.setImage(GuiManager.loadImage("Layouts/Endgame/endgame_windowdefeat.png"));
    }


    /**
     * This method is called when the credits button is pressed.
     * @param mouseEvent
     */

    public void credits(MouseEvent mouseEvent){
        gui.alertUser("Credits", "Software Engineering Project\nPiersilvio De Bartolomeis\nMarco Di Gennaro\nAlessandro Di Maio", Alert.AlertType.INFORMATION);
    }
}
