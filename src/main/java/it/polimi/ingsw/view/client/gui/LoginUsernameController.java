package it.polimi.ingsw.view.client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.event.ActionEvent;

public class LoginUsernameController {

    @FXML
    private TextField usernameField;
    @FXML
    private ImageView playImageViewButton;
    @FXML
    private Label textLabel;

    private Gui gui;

    @FXML
    public void sendUsername(MouseEvent mouseEvent) {
        gui.sendLoginRequest(getUsername());
    }


    public void setGui(Gui gui) {
        this.gui = gui;
    }

    private String getUsername(){
        return usernameField.getText();
    }
}
