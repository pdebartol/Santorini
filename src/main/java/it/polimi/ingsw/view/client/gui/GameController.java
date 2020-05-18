package it.polimi.ingsw.view.client.gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class GameController {

    @FXML
    public GridPane board;
    @FXML
    public ImageView targetSquare = new ImageView();
    @FXML
    public ImageView imageToDrag;

    @FXML
    public void testMethod(MouseEvent mouseEvent) {


    }

    public void changeImageViewRedButton(MouseEvent mouseEvent) {
    }

    public void doActionRedButton(MouseEvent mouseEvent) {
    }

    public void changeImageViewBlueButton(MouseEvent mouseEvent) {
    }

    public void doActionBlueButton(MouseEvent mouseEvent) {
    }

    public void removeWorkerDragged(DragEvent dragEvent) {
    }

    public void acceptElement(DragEvent dragEvent) {

        Dragboard db = dragEvent.getDragboard();
        boolean success = false;

        if(db.hasImage()){
            targetSquare.setImage(imageToDrag.getImage());
            success = true;
        }

        dragEvent.setDropCompleted(success);
    }

    public void highlightSquare(DragEvent dragEvent) {
    }

    public void notHihglightSquare(DragEvent dragEvent) {
    }

    public void highlightSquareOverMethod(DragEvent dragEvent) throws IOException {

        Dragboard db = dragEvent.getDragboard();

        if(db.hasImage()){
            dragEvent.acceptTransferModes(TransferMode.MOVE);
        }

        dragEvent.consume();
    }

    public void startChangingPosition(MouseEvent mouseEvent) {

        Dragboard db = imageToDrag.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(imageToDrag.getImage());
        db.setContent(content);

    }

    public void dragDoneMethod(DragEvent dragEvent) {

        imageToDrag.setImage(null);

    }
}
