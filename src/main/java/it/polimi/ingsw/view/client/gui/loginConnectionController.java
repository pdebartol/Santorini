package it.polimi.ingsw.view.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * This Class is the controller for the initial connection scene.
 * @author pierobartolo
 */

public class loginConnectionController {

    @FXML
    public Label text;
    @FXML
    public TextField port;
    @FXML
    public TextField ipAddress;
    @FXML
    private Button connectButton;



    /**
     * This method is called when the connect button is pressed.
     * It starts a new thread which handles the connection with the server.
     */

    @FXML
    public void connection(ActionEvent actionEvent) {
            new Thread(() ->{
                Gui gui = new Gui(getIpAddress(), Integer.parseInt(getPort()), (Stage) connectButton.getScene().getWindow());
                gui.start();
            }){{start();}};
    }

    private String getIpAddress() {
        return ipAddress.getText();
    }

    private String getPort() {
        return port.getText();
    }
}



