package it.polimi.ingsw.view.client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginUsernameController {

    @FXML
    public TextField usernameField;

    private Gui gui;

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
