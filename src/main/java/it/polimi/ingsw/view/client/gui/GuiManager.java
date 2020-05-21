package it.polimi.ingsw.view.client.gui;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;


/**
 * This class creates the primary Stage and starts the GUI
 * @author pierobartolo & aledimaio
 */

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

    public static void startGui() {
        launch();
    }

    /**
     * Helper function for loading FXML files.
     * @param fxml the name of the fxml file
     * @return an instance of FXMLLoader
     * @throws IOException none
     */

    public static FXMLLoader loadFXML(String fxml) throws IOException {
        return  new FXMLLoader(GuiManager.class.getResource("/gui/fxml/"+fxml + ".fxml"));
    }

    public static Image  loadImage(String img){
        return new Image(GuiManager.class.getResource("/gui/graphics_resources/Sprite/"+img).toString());
    }


}

