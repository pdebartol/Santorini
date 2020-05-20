package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.InputValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * This Class is the controller for the initial connection scene.
 * @author pierobartolo
 */

public class loginConnectionController {

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
        String ip = getIpAddress();
        String port = getPort();
        if(!InputValidator.validateIP(ip)){
            notifyInvalidIp();
        }
        else if(!InputValidator.validatePORT(port)){
            notifyInvalidPort();
        }
        else {
            new Thread(() -> {
                Gui gui = new Gui(getIpAddress(), Integer.parseInt(getPort()), (Stage) connectButton.getScene().getWindow(), connectButton.getScene());
                gui.start();
            }) {{
                start();
            }};
        }

    }

    private String getIpAddress() {
        return ipAddress.getText();
    }

    private String getPort() {
        return port.getText();
    }


    /**
     * Notifies the user (with Alert) that the IP is not valid.
     */

    void notifyInvalidIp() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("IP Error");
        alert.setHeaderText("Invalid IP Address!");
        alert.showAndWait();
    }

    /**
     * Notifies the user (with Alert) that the Port is not valid.
     */

    void notifyInvalidPort() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Port Error");
        alert.setHeaderText("Invalid Port Number!");
        alert.showAndWait();
    }

}



