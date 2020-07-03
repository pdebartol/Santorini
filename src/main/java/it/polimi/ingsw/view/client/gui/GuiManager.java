package it.polimi.ingsw.view.client.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class creates the primary Stage and starts the GUI
 * @author pierobartolo and aledimaio
 */

public class GuiManager extends Application {

    public static ExecutorService executor;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root =  loadFXML("loginConnection").load();
        stage.setResizable(false);
        stage.setTitle("AM10 Santorini");
        stage.getIcons().add(new Image("/gui/graphics_resources/Sprite/cm_boardgame.png"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        executor = Executors.newCachedThreadPool();
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
            executor.shutdown();
        });
    }

    public static void startGui() {
        launch();
    }

    /**
     * Helper function for loading FXML files.
     * @param fxml the name of the fxml file
     * @return an instance of FXMLLoader
     */

    public static FXMLLoader loadFXML(String fxml) {
        return  new FXMLLoader(GuiManager.class.getResource("/gui/fxml/"+fxml + ".fxml"));
    }

    public static Image  loadImage(String img){
        return new Image(GuiManager.class.getResource("/gui/graphics_resources/Sprite/"+img).toString());
    }

    /**
     * This method loads the image of a god Card
     * @param id of the god
     * @return an Image of the god
     */

    public static Image loadGod(int id){
        Image god;

        if(id < 10){
            god = GuiManager.loadImage("godCards/0"+ (id) + ".png");
        }
        else{
            god = GuiManager.loadImage("godCards/"+ (id) + ".png");
        }

        return god;
    }

    /**
     * This method loads the image of a god for the end of the game
     * @param id of the god
     * @return an Image of the god
     */

    public static Image loadGodEndGame(int id){
        Image god;

        god = GuiManager.loadImage("Cards/Small/"+ (id) + ".png");

        return god;
    }

}

