package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.viewComponents.Board;
import it.polimi.ingsw.view.client.viewComponents.God;
import it.polimi.ingsw.view.client.viewComponents.Player;
import it.polimi.ingsw.view.client.viewComponents.Square;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;



public class GameController {

    @FXML
    public ImageView imageToDrag;
    @FXML
    public Button showInformationButton;
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
    public ImageView redButton;
    @FXML
    public Label godDescription;
    @FXML
    public Label godName;
    @FXML
    public ImageView godImage;
    @FXML
    public ImageView worker;
    @FXML
    public GridPane boardGridPane;
    @FXML
    public ImageView endTurnButton;

    private boolean dNdActive = false;

    String workerGender;

    private Gui gui;

    ImageView source;
    ImageView source_pointer;

    ImageView destination;
    ImageView destination_pointer;

    private ArrayList<Player> players;

    private int currentPlayerId = 0;

    boolean showGod = false;


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

    /**
     * On Drag Done Method
     * @param dragEvent
     */

    public void removeWorkerDragged(DragEvent dragEvent) {

        if(dragEvent.getTransferMode() == TransferMode.MOVE)
            ((ImageView)dragEvent.getSource()).setImage(null);

    }

    /**
     * On Drag Dropped method
     * @param dragEvent
     */

    public void acceptElement(DragEvent dragEvent) {
        
        gui.sendSetWorkerOnBoardRequest(workerGender,boardGridPane.getRowIndex( ((ImageView) dragEvent.getSource()).getParent()),boardGridPane.getColumnIndex( ((ImageView) dragEvent.getSource()).getParent()));
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
        dragEvent.consume();


        //TODO if the answer from server is negative restoreImage();

    }

    public void loadingScreen(){
        //loading screen
    }

    public void consumeDrop(DragEvent dragEvent){

    }

    public void restoreImage(){

        source_pointer.setImage(source.getImage());
        destination_pointer.setImage(destination.getImage());

    }

    public void highlightSquare(DragEvent dragEvent) {

        //((ImageView)dragEvent.getSource()).setImage();

    }

    public void notHihglightSquare(DragEvent dragEvent) {


    }

    /**
     * On Drag Over method
     * @param dragEvent
     * @throws IOException
     */

    public void highlightSquareOverMethod(DragEvent dragEvent) throws IOException {

        /* ((ImageView)dragEvent.getGestureSource()) != source_pointer should check that the place for drop is not the same of source of drag
        if(((ImageView)dragEvent.getGestureSource()) != source_pointer && dragEvent.getDragboard().hasImage())
            dragEvent.acceptTransferModes(TransferMode.MOVE);
        dragEvent.consume();
         */

        if(dNdActive) {
            if (dragEvent.getDragboard().hasImage())
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            dragEvent.consume();
        }

    }

    public void startChangingPosition(MouseEvent mouseEvent) {

        //TODO if dNdActive is true -> start dNd

        if(dNdActive) {
            ImageView test = ((ImageView) mouseEvent.getSource());

            source = new ImageView(((ImageView) mouseEvent.getSource()).getImage());
            source_pointer = test;

            Dragboard db = test.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(test.getImage());
            db.setContent(content);
            mouseEvent.consume();
        }

    }

    public void dragDoneMethod(DragEvent dragEvent) {

        if(dragEvent.getTransferMode() == TransferMode.MOVE)
            ((ImageView)dragEvent.getSource()).setImage(null);

    }

    /**
     * When showInformationButton is clicked, informations about selected player is displayed
     * @param actionEvent
     */

    public void showOtherPlayerInformation(ActionEvent actionEvent) {
        if(!showGod){
            God tempGod = gui.getPlayerGod(players.get(currentPlayerId));
            godName.setText(tempGod.getName());
            godDescription.setText(tempGod.getDescription());
            godImage.setImage(GuiManager.loadGod(tempGod.getId()));
            godName.setVisible(true);
            godImage.setVisible(true);
            godDescription.setVisible(true);
            showInformationButton.setText("Hide Informations");
            showGod = true;
        }

        else{
            showInformationButton.setText("Show Informations");
            hideGod();
            showGod = false;
        }


    }

    public void nextPlayer(ActionEvent actionEvent) {
        if(currentPlayerId >= players.size()-1 )
            currentPlayerId = 0;
        else
            currentPlayerId++;

        God tempGod = gui.getPlayerGod(players.get(currentPlayerId));
        godName.setText(tempGod.getName());
        godDescription.setText(tempGod.getDescription());
        godImage.setImage(GuiManager.loadGod(tempGod.getId()));

        numberOfPlayer.setText(currentPlayerId+1 + " of " + players.size());
        playerName.setText(players.get(currentPlayerId).getUsername());
    }

    public void showPrevPlayer(ActionEvent actionEvent) {
        if(currentPlayerId == 0)
            currentPlayerId = players.size()-1;
        else
            currentPlayerId--;


        God tempGod = gui.getPlayerGod(players.get(currentPlayerId));
        godName.setText(tempGod.getName());
        godDescription.setText(tempGod.getDescription());
        godImage.setImage(GuiManager.loadGod(tempGod.getId()));

        numberOfPlayer.setText(currentPlayerId+1 + " of " + players.size());
        playerName.setText(players.get(currentPlayerId).getUsername());
    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
        numberOfPlayer.setText(currentPlayerId+1 + " of " + players.size());
        playerName.setText(players.get(currentPlayerId).getUsername());
    }

    public void setupWorker(String gender){

        Image workerImage;

        switch(gui.getMyColor()){
            case AZURE:
                 workerImage = GuiManager.loadImage("Buildings_+_pawns/"+gender+"_azure_worker.png");
                 worker.setImage(workerImage);
                break;
            case ORANGE:
                workerImage = GuiManager.loadImage("Buildings_+_pawns/"+gender+"_white_worker.png");
                worker.setImage(workerImage);
                break;
            case GREY:
                workerImage = GuiManager.loadImage("Buildings_+_pawns/"+gender+"_gray_worker.png");
                worker.setImage(workerImage);
                break;
        }
        workerGender = gender;
        showInformationButton.setText("Show Informations");
    }

    public void setInstructionLabel(String text){
        informationBox.setText(text);
    }

    public void hideGod(){
        godDescription.setVisible(false);
        godImage.setVisible(false);
        godName.setVisible(false);
    }

    public void updateBoard(Board board_view){


        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                updateSquare(board_view.getSquareByCoordinates(x,y));
            }
        }

    }

    public void updateSquare(Square square){

        int x = square.getX();
        int y = square.getY();
        Image imageTemp;
        AnchorPane anchorPane = (AnchorPane) getNodeFromGridPane(boardGridPane, y, x);
        //Node node = getNodeFromGridPane(board, x, y);
        //Node node = board.getChildren().get(y*5+x);
        //AnchorPane anchorPane = (AnchorPane) boardGridPane.getChildren().get((y*5+x));

        if (anchorPane != null){

            if(square.getWorker() != null){
                //Image workerImage = GuiManager.loadImage("Buildings_+_pawns/"+square.getWorker().getGender()+"_"+square.getWorker().getColor().toString()+"_worker.png");
                Image workerImage = GuiManager.loadImage("Buildings_+_pawns/"+square.getWorker().getGender()+"_azure_worker.png");
                 ((ImageView) anchorPane.getChildren().get(0)).setImage(workerImage);
            }
            else {
                ((ImageView) anchorPane.getChildren().get(0)).setImage(null);
            }

            if(square.getDome()){
                switch (square.getLevel()){

                    case 0:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/dome.png");
                        //TODO set buildingImage to background or to another ImageView in the same anchorPane
                        break;
                    case 1:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/first_level_dome.png");
                        break;
                    case 2:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/second_level_dome.png");
                        break;
                    case 3:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/complete_tower.png");
                        break;

                }

            } else {
                switch (square.getLevel()) {

                    case 0:
                        //TODO remove background or the other ImageView
                        break;
                    case 1:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/first_level.png");
                        break;
                    case 2:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/second_level.png");
                        break;
                    case 3:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/third_level.png");
                        break;

                }
            }



        }

    }

    public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {

        for (Node node : gridPane.getChildren()) {
            if (gridPane.getColumnIndex(node) == col &&  gridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /*
    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }
    */


    public void setGui(Gui gui){
        this.gui = gui;
    }

    public void activateWorkers(){
        dNdActive = true;
    }
    public void deactivateWorkers(){
        dNdActive = false;
    }

    public void hideBlueButton(){
        blueButton.setVisible(false);
        blueButton.setDisable(true);
    }

    public void showBlueButton(){
        blueButton.setVisible(false);
        blueButton.setDisable(true);
    }
    public void showRedButton(){
        redButton.setVisible(false);
        redButton.setDisable(true);
    }

    public void hideRedButton(){
        redButton.setVisible(false);
        redButton.setDisable(true);
    }

    /**
     * This method is triggered when a square is clicked
     */

    @FXML
    public void buildAction(MouseEvent mouseEvent) {
    }
}
