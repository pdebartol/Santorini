package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.InputValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * This Class is the controller for the login scene.
 * @author pierobartolo
 */

public class LoginUsernameController {

    @FXML
    private TextField usernameField;

    private Gui gui;


    /**
     * This method is called when the user presses the PLAY button and connects with his username
     */

    @FXML
    public void sendUsername(MouseEvent mouseEvent) {
        String user = getUsername();
        if(!InputValidator.validateUSERNAME(user)){
            notifyInvalidUsername();
        }
        else{
            gui.sendLoginRequest(getUsername());
        }
    }

    /**
     * Notifies the user (with Alert) that the username is not valid.
     */

    void notifyInvalidUsername() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Username Error");
        alert.setHeaderText("Invalid username format!");
        alert.showAndWait();
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    private String getUsername(){
        return usernameField.getText();
    }

    }
