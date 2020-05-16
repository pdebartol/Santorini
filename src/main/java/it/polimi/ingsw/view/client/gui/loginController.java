package it.polimi.ingsw.view.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class loginController {

    @FXML
    public Label text;
    @FXML
    public TextField port;
    @FXML
    public TextField ipAddress;

    @FXML
    public void connection(ActionEvent actionEvent) throws IOException {

        //TODO connection management

        //Gui.setRoot("gameScene");

    }
}
