package it.polimi.ingsw.view.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController {

    @FXML
    public Label text;
    @FXML
    public TextField port;
    @FXML
    public TextField ipAddress;
    @FXML
    private Button connectButton;


    @FXML
    public void connection(ActionEvent actionEvent) throws IOException {
        Gui gui = new Gui(getIpAddress(), Integer.parseInt(getPort()),(Stage) connectButton.getScene().getWindow(),connectButton.getScene());
        gui.start(); }

    private String getIpAddress() {
        return ipAddress.getText();
    }

    private String getPort() {
        return port.getText();
    }
}
