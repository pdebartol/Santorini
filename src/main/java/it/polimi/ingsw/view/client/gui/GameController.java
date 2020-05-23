package it.polimi.ingsw.view.client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
    public Button shwoInformationButton;
    @FXML
    public Label playerName;
    @FXML
    public Button nextButton;
    @FXML
    public Label numberOfPlayer;
    @FXML
    public Button prevButton;
    @FXML
    public Label informationBox;
    @FXML
    public ImageView blueButton;
    @FXML
    public Label godDescription;
    @FXML
    public Label godName;
    @FXML
    public ImageView godImage;
    @FXML
    public ImageView worker;

    String workerGender;

    private Gui gui;

    ImageView source;
    ImageView source_pointer;

    ImageView destination;
    ImageView destination_pointer;

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

        ((ImageView)dragEvent.getSource()).setImage(null);

    }

    public void acceptElement(DragEvent dragEvent) {
        gui.sendSetWorkerOnBoardRequest(workerGender,board.getRowIndex( ((ImageView) dragEvent.getSource()).getParent()),board.getColumnIndex( ((ImageView) dragEvent.getSource()).getParent()));
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;

        ImageView test = ((ImageView)dragEvent.getSource());

        destination = new ImageView(((ImageView)dragEvent.getSource()).getImage());
        destination_pointer = test;
        if(db.hasImage()){
            test.setImage(db.getImage());
            success = true;
        }

        dragEvent.setDropCompleted(success);


        //TODO if the answer from server is negative restoreImage();

        
    }

    public void loadingScreen(){
        //loading screen
    }

    public void consumeDrop(DragEvent dragEvent){

    }

    public void restoreImage(){




    }

    public void highlightSquare(DragEvent dragEvent) {

        //((ImageView)dragEvent.getSource()).setImage();

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

        ImageView test = ((ImageView)mouseEvent.getSource());

        source = new ImageView(((ImageView)mouseEvent.getSource()).getImage());
        source_pointer = test;

        Dragboard db = test.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(test.getImage());
        db.setContent(content);

    }

    public void dragDoneMethod(DragEvent dragEvent) {

        ((ImageView)dragEvent.getSource()).setImage(null);

    }

    /**
     * When showInformationButton is clicked, informations about selected player is displayed
     * @param actionEvent
     */

    public void showOtherPlayerInformation(ActionEvent actionEvent) {
    }

    public void nextPlayer(ActionEvent actionEvent) {
    }

    public void showPrevPlayer(ActionEvent actionEvent) {
    }

    public void setupWorker(String gender){
        Image workerImage = GuiManager.loadImage("Buildings_+_pawns/"+gender+"_azure_worker.png");
        worker.setImage(workerImage);
        workerGender = gender;
    }

    public void setInstructionLabel(String text){
        informationBox.setText(text);
    }

    public void setGui(Gui gui){
        this.gui = gui;
    }
}
