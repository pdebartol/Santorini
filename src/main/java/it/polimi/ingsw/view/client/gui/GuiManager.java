package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.View;
import it.polimi.ingsw.view.client.viewComponents.Board;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class GuiManager extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Parent root =  loadFXML("loginConnection").load();
        stage.setTitle("Santorini");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }


    /*
    public static void startGui() {
        launch();
    }
     */

    public static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GuiManager.class.getResource("/gui/fxml/"+fxml + ".fxml"));
        return fxmlLoader;
    }


}

