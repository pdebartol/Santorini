package it.polimi.ingsw.view.client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class LoginUsernameController {

    @FXML
    private TextField usernameField;
    @FXML
    private ImageView playImageViewButton;
    @FXML
    private Label textLabel;

    private Gui gui;


    /**
     * This method is called when the user presses the PLAY button and connects with his username
     */
    @FXML
    public void sendUsername(MouseEvent mouseEvent) {
        gui.sendLoginRequest(getUsername());
    }


    /**
     * Notifies the user that the chosen username is already in use.
     */

    void notifyAlreadyInUse() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Username Error");
        alert.setHeaderText("Username already in use!");
        alert.showAndWait();
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    private String getUsername(){
        return usernameField.getText();
    }

    }
