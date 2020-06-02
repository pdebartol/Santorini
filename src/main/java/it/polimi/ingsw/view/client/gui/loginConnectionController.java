package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.InputValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    ImageView  connect;

    @FXML
    AnchorPane ap;



    /**
     * This method is called when the connect button is pressed.
     * It starts a new thread which handles the connection with the server.
     */

    @FXML
    public void onReleaseConnection(MouseEvent mouseEvent) {
        String ip = getIpAddress();
        String port = getPort();
        if(!InputValidator.validateIP(ip)){
            notifyInvalidIp();
        }
        else if(!InputValidator.validatePORT(port)){
            notifyInvalidPort();
        }
        else {
            GuiManager.executor.submit( new Thread(() -> {
                Gui gui = new Gui(getIpAddress(), Integer.parseInt(getPort()), (Stage) ap.getScene().getWindow(), ap.getScene());
                gui.start();
            }));
        }
        Image updateButton = GuiManager.loadImage("Buttons/btn_connect.png");
        connect.setImage(updateButton);
        connect.setDisable(false);

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

    public void onPressedConnection(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_connect_pressed.png");
        connect.setImage(updateButton);
        connect.setDisable(true);
    }



}



