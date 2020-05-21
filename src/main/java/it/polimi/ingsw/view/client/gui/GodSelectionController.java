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
    public Label nameLabel;
    @FXML
    public ImageView imageGod;
    @FXML
    public Label instructionLabel;

    private Gui gui;



    private Image loadGod(int id){
        Image god;

        if(id < 10){
            god = GuiManager.loadImage("godCards/0"+ (id) + ".png");
        }
        else{
            god = GuiManager.loadImage("godCards/"+ (id) + ".png");
        }

        return god;
    }


    /**
     * This method is called when the previous god button is pressed, it updated the gui
     * @param actionEvent none
     */

    @FXML
    public void prevGodImage(ActionEvent actionEvent) {
        God god = gui.getPreviousGod();
        int godId = god.getId();
        imageGod.setImage(loadGod(godId));
        nameLabel.setText(god.getName());
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
        imageGod.setImage(loadGod(godId));
        nameLabel.setText(god.getName());
        descriptionLabel.setText(god.getDescription());
    }

    @FXML
    public void changeImageOfSelectionButton(MouseEvent mouseEvent) {
    }

    @FXML
    public void changeImageOfConfirmationButton(MouseEvent mouseEvent) {
    }

    @FXML
    public void nextPhaseBlueButton(MouseEvent mouseEvent) {
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

    public void setInstructionLabel(String Text){
        instructionLabel.setText(Text);
    }

    public void setGui(Gui gui){
        this.gui = gui;
    }


    /**
     * This method is called when the scene starts, and it initializes the first god
     * @param starterGod the first god in the deck
     */

    public void initializeGods(God starterGod){
        int godId = starterGod.getId();
        imageGod.setImage(loadGod(godId));
        nameLabel.setText(starterGod.getName());
        descriptionLabel.setText(starterGod.getDescription());
    }


    public void setGodProgression(String text){
        progressionOfGods.setText(text);
    }
}
