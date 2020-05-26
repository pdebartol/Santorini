package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.client.viewComponents.Board;
import it.polimi.ingsw.view.client.viewComponents.God;
import it.polimi.ingsw.view.client.viewComponents.Player;
import it.polimi.ingsw.view.client.viewComponents.Square;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;


/**
 * This class implements the GameScene Controller
 * @author pierobartolo & aledimaio
 */

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
    @FXML
    public ImageView firstLevelImageView;
    @FXML
    public ImageView secondLevelImageView;
    @FXML
    public ImageView thirdLevelImageView;
    @FXML
    public ImageView domeImageView;

    DropShadow borderGlow= new DropShadow();

    private boolean dNdActiveMove = false;
    private boolean dNdActiveBuild = false;

    ImageView builderWorker = null;

    String workerGender;

    private Gui gui;

    ImageView source;
    ImageView source_pointer;

    ImageView destination;
    ImageView destination_pointer;

    /**
     * In-Game Players
     */

    private ArrayList<Player> players;

    /**
     * Current player in the player's information box
     */

    private int currentPlayerId = 0;

    /**
     * When it is true the gui shows the player's god card
     */

    boolean showGod = false;

    /**
     * Current state of the match.
     * (worker-> place workers)
     * (move)
     * (build)
     */

    String state = "worker";


    public void setGui(Gui gui){
        this.gui = gui;
    }

    public void changeImageViewRedButton(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_blue_pressed.png");
        redButton.setImage(updateButton);
        redButton.setDisable(true);
    }

    /**
     * build button
     * @param mouseEvent
     */

    public void doActionRedButton(MouseEvent mouseEvent) {
        dNdActiveBuild = true;
        state ="build";
        showImageViews();
    }

    public void changeImageViewBlueButton(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_blue_pressed_move.png");
        blueButton.setImage(updateButton);
        blueButton.setDisable(true);
    }

    public void doActionBlueButton(MouseEvent mouseEvent) {
        dNdActiveMove = true;
        state = "move";
    }

    public void restoreImage(){
        source_pointer.setImage(source.getImage());
        destination_pointer.setImage(destination.getImage());
    }

    /**
     * On Drag Done method for levels
     * @param dragEvent
     */

    public void dragDoneMethod(DragEvent dragEvent) {

        if(dragEvent.getTransferMode() == TransferMode.MOVE){
            if(!((ImageView) dragEvent.getSource()).getId().equals("worker")){}
            else
                ((ImageView)dragEvent.getSource()).setImage(null);
        }

    }

    /**
     * On Drag Done Method
     * @param dragEvent
     */

    public void removeWorkerDragged(DragEvent dragEvent) {

        if(dragEvent.getTransferMode() == TransferMode.MOVE){
            if(!((ImageView) dragEvent.getSource()).getId().equals("worker")){}
            else
                ((ImageView)dragEvent.getSource()).setImage(null);
        }

    }

    /**
     * On Drag Dropped method
     * @param dragEvent
     */

    @FXML
    public void acceptElement(DragEvent dragEvent) {

        switch(state){

            case "worker":
                int x = GridPane.getRowIndex(((ImageView) dragEvent.getSource()).getParent());
                int y = GridPane.getColumnIndex(((ImageView) dragEvent.getSource()).getParent());
                gui.sendSetWorkerOnBoardRequest(workerGender,x,y);
                break;

            case "move":
                int selectedX = GridPane.getRowIndex(source_pointer.getParent());
                int selectedY = GridPane.getColumnIndex(source_pointer.getParent());
                if(gui.getSelectedWorker() == null)
                    gui.setSelectedWorker(selectedX,selectedY);

                int newX = GridPane.getRowIndex( ((ImageView) dragEvent.getSource()).getParent());
                int newY = GridPane.getColumnIndex( ((ImageView) dragEvent.getSource()).getParent());

                System.out.println("Move : " + newX  +" "+ newY);

                gui.sendMoveRequest(gui.getWorkerGender(selectedX,selectedY),newX, newY);
                dNdActiveMove = false;
                blueButton.setDisable(true);
                break;

            case "build":

                int level = 0;
                switch (source_pointer.getId()){
                    case "firstLevelImageView":
                        level = 1;
                        break;
                    case "secondLevelImageView":
                        level = 2;
                        break;
                    case "thirdLevelImageView":
                        level = 3;
                        break;
                    case "domeImageView":
                        level = 4;
                        break;
                }

                //if a worker is selected by clicking on it (build before move case) now the worker for the build action is set
                //this is do because of the possibility to change selected worker for the build action (only in build before move case)
                if(builderWorker != null){
                    int X = GridPane.getRowIndex(builderWorker.getParent());
                    int Y = GridPane.getColumnIndex(builderWorker.getParent());
                    gui.setSelectedWorker(X, Y);
                }


                if(gui.getSelectedWorker() != null) {
                    int xToBuild = GridPane.getRowIndex(((ImageView) dragEvent.getSource()).getParent());
                    int yToBuild = GridPane.getColumnIndex(((ImageView) dragEvent.getSource()).getParent());
                    System.out.println("selected worker:  " + gui.getSelectedWorker().getCurrentPosition().getX() + " " + gui.getSelectedWorker().getCurrentPosition().getY());
                    System.out.println("BUILD : " + xToBuild + " " + yToBuild);
                    gui.sendBuildRequest(gui.getSelectedWorker().getGender(), xToBuild, yToBuild, level);

                    //remove red circle around worker selected for build before move
                    if (builderWorker != null) {
                        builderWorker.setEffect(null);
                        builderWorker = null;
                    }

                    redButton.setDisable(true);
                    hideImageViews();
                    dNdActiveBuild = false;
                }
                break;
        }

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


    }

    /**
     * On Drag Over method
     */

    @FXML
    public void highlightSquareOverMethod(DragEvent dragEvent) throws IOException {

        if(state.equals("worker") || dNdActiveMove || (dNdActiveBuild && ((ImageView) dragEvent.getSource()).getId().equals("workerImageView"))) {
            if ((dragEvent.getGestureSource() != dragEvent.getSource()) && dragEvent.getDragboard().hasImage())
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            dragEvent.consume();
        }

    }


    private boolean isMyWorker(MouseEvent mouseEvent){

        int x = GridPane.getRowIndex(((ImageView) mouseEvent.getSource()).getParent());
        int y = GridPane.getColumnIndex(((ImageView) mouseEvent.getSource()).getParent());
        int[] workerPosition = gui.getMyWorkerPosition(gui.getWorkerGender(x,y));

        return workerPosition[0] == x && workerPosition[1] == y;

    }

    private boolean canIDrag(MouseEvent mouseEvent){

        switch (state){

            case "worker":
                if ( ((ImageView) mouseEvent.getSource()).getId().equals("worker") )
                    return true;
                break;

            case "move":
                int x = GridPane.getRowIndex(((ImageView) mouseEvent.getSource()).getParent());
                int y = GridPane.getColumnIndex(((ImageView) mouseEvent.getSource()).getParent());

                if( dNdActiveMove && ((ImageView) mouseEvent.getSource()).getImage() != null && isMyWorker(mouseEvent)){
                    if (gui.getSelectedWorker() != null)
                        return gui.getSelectedWorker().getGender().equals(gui.getWorkerGender(x, y));
                    return true;
                }
                break;

            case "build":
                if( dNdActiveBuild &&  !(((ImageView) mouseEvent.getSource()).getId().equals("workerImageView")) && (gui.getSelectedWorker() != null || builderWorker != null) )
                    return true;
                break;

            default:
                return false;
        }

        return false;
    }

    /**
     * On Drag Detected method
     */

    @FXML
    public void startChangingPosition(MouseEvent mouseEvent) {

        if( canIDrag(mouseEvent) ) {

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

    //actually useless

    @FXML
    public void highlightSquare(DragEvent dragEvent) { }

    //actually useless

    @FXML
    public void notHighlightSquare(DragEvent dragEvent) { }

    /**
     * When showInformationButton is clicked, informations about selected player is displayed
     * @param actionEvent
     */

    @FXML
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

    /**
     * This method is called when the next player button is pressed, it updates the gui
     * @param actionEvent none
     */

    @FXML
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

    /**
     * This method is called when the prev player button is pressed, it updates the gui
     * @param actionEvent none
     */

    @FXML
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
        worker.setImage(getWorkerImage(gui.getMyColor(),gender));
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

    /**
     * This method displays the updated board
     * @param board_view is the board that have to be displayed
     */

    public void updateBoard(Board board_view){

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                updateSquare(board_view.getSquareByCoordinates(x,y));
            }
        }

    }

    /**
     * This method displays a single updated square
     * @param square is the square that have to be displayed
     */

    public void updateSquare(Square square){

        int x = square.getX();
        int y = square.getY();
        Image imageTemp;
        AnchorPane anchorPane = (AnchorPane) getNodeFromGridPane(boardGridPane, y, x);

        if (anchorPane != null){

            if(square.getWorker() != null){
                Image workerImage = getWorkerImage( square.getWorker().getColor(), square.getWorker().getGender());
                ((ImageView) anchorPane.getChildren().get(1)).setImage(workerImage);
            }
            else {
                ((ImageView) anchorPane.getChildren().get(1)).setImage(null);
            }

            if(square.getDome()){
                switch (square.getLevel()){

                    case 0:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/dome.png");
                        ((ImageView) anchorPane.getChildren().get(0)).setImage(imageTemp);
                        break;
                    case 1:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/first_level_dome.png");
                        ((ImageView) anchorPane.getChildren().get(0)).setImage(imageTemp);
                        break;
                    case 2:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/second_level_dome.png");
                        ((ImageView) anchorPane.getChildren().get(0)).setImage(imageTemp);
                        break;
                    case 3:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/complete_tower.png");
                        ((ImageView) anchorPane.getChildren().get(0)).setImage(imageTemp);
                        break;

                }

            } else {
                switch (square.getLevel()) {

                    case 0:
                        ((ImageView) anchorPane.getChildren().get(0)).setImage(null);
                        break;
                    case 1:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/first_level.png");
                        ((ImageView) anchorPane.getChildren().get(0)).setImage(imageTemp);
                        break;
                    case 2:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/second_level.png");
                        ((ImageView) anchorPane.getChildren().get(0)).setImage(imageTemp);
                        break;
                    case 3:
                        imageTemp = GuiManager.loadImage("Buildings_+_pawns/third_level.png");
                        ((ImageView) anchorPane.getChildren().get(0)).setImage(imageTemp);
                        break;

                }
            }

        }

    }

    /**
     * This method return a node of GridPane by giving to it the coordinates of the node
     * @param gridPane represents the GridPane
     * @param col represents the x
     * @param row represents the y
     * @return the node or null if the node does not exists
     */

    public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {

        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col &&  GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public void hideBlueButton(){
        Image updateButton = GuiManager.loadImage("Buttons/btn_small_gray.png");
        blueButton.setImage(updateButton);
        blueButton.setDisable(true);
    }

    public void showBlueButton(){
        blueButton.setVisible(true);
        blueButton.setDisable(false);
        Image updateButton = GuiManager.loadImage("Buttons/btn_blue_move.png");
        blueButton.setImage(updateButton);
    }
    public void showRedButton(){
        redButton.setVisible(true);
        redButton.setDisable(false);
        Image updateButton = GuiManager.loadImage("Buttons/btn_blue.png");
        redButton.setImage(updateButton);
    }

    public void hideRedButton(){
        Image updateButton = GuiManager.loadImage("Buttons/btn_small_gray.png");
        redButton.setImage(updateButton);
        redButton.setDisable(true);
    }

    public void showEndButton(){
        endTurnButton.setVisible(true);
        endTurnButton.setDisable(false);
        Image updateButton = GuiManager.loadImage("Buttons/btn_blue.png");
        endTurnButton.setImage(updateButton);
    }

    public void hideEndButton(){
        Image updateButton = GuiManager.loadImage("Buttons/btn_small_gray.png");
        endTurnButton.setImage(updateButton);
        endTurnButton.setDisable(true);
    }

    private Image getWorkerImage(Color color, String gender ){
        // default
        Image workerImage = GuiManager.loadImage("Buildings_+_pawns/"+gender+"_azure_worker.png");

        switch(color){
            case AZURE:
                workerImage = GuiManager.loadImage("Buildings_+_pawns/"+gender+"_azure_worker.png");
                break;
            case ORANGE:
                workerImage = GuiManager.loadImage("Buildings_+_pawns/"+gender+"_white_worker.png");
                break;
            case GREY:
                workerImage = GuiManager.loadImage("Buildings_+_pawns/"+gender+"_gray_worker.png");
                break;

        }
        return workerImage;

    }

    /**
     * This method is triggered when a square is clicked and it is used to select a worker for build action, if not previously selected
     */

    @FXML
    public void buildAction(MouseEvent mouseEvent) {

        // first check
        if(state.equals("build") && (((ImageView) mouseEvent.getSource()).getImage() != null && gui.getSelectedWorker() == null) ){

            //de-activate red circle around worker if another one was selected
            if(builderWorker != null)
                builderWorker.setEffect(null);


            boolean myWorker = ( (((ImageView) mouseEvent.getSource()).getId().equals("workerImageView")) && isMyWorker(mouseEvent));

            if(myWorker){

                builderWorker = ((ImageView) mouseEvent.getSource());

                int depth = 70;
                borderGlow.setOffsetY(0f);
                borderGlow.setOffsetX(0f);
                borderGlow.setColor(javafx.scene.paint.Color.RED);
                borderGlow.setWidth(depth);
                borderGlow.setHeight(depth);

                //set red circle around worker selected
                builderWorker.setEffect(borderGlow);
            }

        }

    }


    public void hideImageViews(){
        firstLevelImageView.setVisible(false);
        secondLevelImageView.setVisible(false);
        thirdLevelImageView.setVisible(false);
        domeImageView.setVisible(false);
    }

    public void showImageViews(){
        firstLevelImageView.setVisible(true);
        secondLevelImageView.setVisible(true);
        thirdLevelImageView.setVisible(true);
        domeImageView.setVisible(true);
    }


    public void changeImageViewEndButton(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_blue_pressed.png");
        redButton.setImage(updateButton);
        redButton.setDisable(true);
    }

    public void doActionEndButton(MouseEvent mouseEvent) {
        gui.sendEndOfTurnRequest();
    }
}
