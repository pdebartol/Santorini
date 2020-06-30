package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.viewComponents.God;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * This Class is the controller for the god selection connection scene.
 * @author pierobartolo
 */

public class GodSelectionController {

    @FXML
    public ImageView confirmationImageViewButton;
    @FXML
    public ImageView godSelectionButton;
    @FXML
    public Label progressionOfGods;
    @FXML
    public Button nextButton;
    @FXML
    public Button prevButton;
    @FXML
    public Label descriptionLabel;
    @FXML
    public ImageView imageGod;
    @FXML
    public Label instructionLabel;

    private Gui gui;


    /**
     * This method is called when the previous god button is pressed, it updated the gui
     * @param actionEvent none
     */

    @FXML
    public void prevGodImage(ActionEvent actionEvent) {
        God god = gui.getPreviousGod();
        int godId = god.getId();
        imageGod.setImage(GuiManager.loadGod(godId));
        descriptionLabel.setText(god.getDescription());
    }

    /**
     * This method is called when the next god button is pressed, it updates the gui
     * @param actionEvent none
     */

    @FXML
    public void nextGodImage(ActionEvent actionEvent) {
        God god = gui.getNextGod();
        int godId = god.getId();
        imageGod.setImage(GuiManager.loadGod(godId));
        descriptionLabel.setText(god.getDescription());
    }

    /**
     * This method is called when the confirm button is pressed to change its image.
     * @param mouseEvent none
     */

    @FXML
    public void changeImageOfConfirmationButton(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_confirm_pressed.png");
        confirmationImageViewButton.setImage(updateButton);
    }

    /**
     * This method is called when the user confirms his choice and makes it final
     * @param mouseEvent none
     */

    @FXML
    public void nextPhaseBlueButton(MouseEvent mouseEvent) {

        if(gui.getIsChallenger()){
            gui.sendCreateGodsRequest((ArrayList<Integer>) gui.getSelectedGodsIds());

        }
        else{
            gui.sendChooseGodRequest(gui.getUserSelectedGodId());

        }
        Image updateButton = GuiManager.loadImage("Buttons/btn_confirm.png");
        confirmationImageViewButton.setImage(updateButton);
        confirmationImageViewButton.setDisable(true);

    }

    /**
     * This method is called when the challenger confirms is choice of the god
     * @param mouseEvent none
     */

    @FXML
    public void confirmGodSelection(MouseEvent mouseEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure " + gui.getCurrentGod().getName()  + " is the god you want?",ButtonType.NO, ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
           gui.addCurrentGod();
        }
    }

    /**
     * This method is called when the scene starts, and it initializes the first god
     * @param starterGod the first god in the deck
     */

    public void initializeGods(God starterGod){
        int godId = starterGod.getId();
        imageGod.setImage(GuiManager.loadGod(godId));
        descriptionLabel.setText(starterGod.getDescription());
    }

    public void setGodProgression(String text){
        progressionOfGods.setText(text);
    }

    public void hideConfirmGodButton(){
        godSelectionButton.setDisable(true);
        godSelectionButton.setVisible(false);
    }

    public void hideFinalConfirmButton(){
        confirmationImageViewButton.setDisable(true);
        confirmationImageViewButton.setVisible(false);
    }

    public void showConfirmGodButton(){
        godSelectionButton.setDisable(false);
        godSelectionButton.setVisible(true);
    }

    public void showFinalConfirmButton(){
        confirmationImageViewButton.setDisable(false);
        confirmationImageViewButton.setVisible(true);
    }

    public void setInstructionLabel(String Text){
        instructionLabel.setText(Text);
    }

    public void setGui(Gui gui){
        this.gui = gui;
    }

}
