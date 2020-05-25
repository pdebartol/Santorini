package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.client.viewComponents.Board;
import it.polimi.ingsw.view.client.viewComponents.God;
import it.polimi.ingsw.view.client.viewComponents.Player;
import it.polimi.ingsw.view.client.viewComponents.Square;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;

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


    @FXML
    public void testMethod(MouseEvent mouseEvent) {

    }

    public void changeImageViewRedButton(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_coral_pressed_build.png");
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

    public void acceptElement(DragEvent dragEvent) {

        switch(state){
            case "worker":
                gui.sendSetWorkerOnBoardRequest(workerGender,boardGridPane.getColumnIndex( ((ImageView) dragEvent.getSource()).getParent()),boardGridPane.getRowIndex( ((ImageView) dragEvent.getSource()).getParent()));
                break;
            case "move":
                gui.setSelectedWorker(boardGridPane.getColumnIndex( source_pointer.getParent()), boardGridPane.getRowIndex( source_pointer.getParent()));
                gui.sendMoveRequest(gui.getWorkerGender(boardGridPane.getColumnIndex(source_pointer.getParent()),boardGridPane.getRowIndex( source_pointer.getParent())),boardGridPane.getColumnIndex( ((ImageView) dragEvent.getSource()).getParent()), boardGridPane.getRowIndex( ((ImageView) dragEvent.getSource()).getParent()));
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
                if(gui.getSelectedWorker() == null && builderWorker != null){
                    gui.setSelectedWorker(boardGridPane.getColumnIndex(builderWorker.getParent()), boardGridPane.getRowIndex(builderWorker.getParent()));
                    builderWorker.setEffect(null);
                    System.out.print("Sono dentro l'acceptWorker\n");
                }
                System.out.println("BUILD : " + boardGridPane.getColumnIndex( ((ImageView) dragEvent.getSource()).getParent())  +" "+ boardGridPane.getRowIndex( ((ImageView) dragEvent.getSource()).getParent()));
                gui.sendBuildRequest(gui.getSelectedWorker().getGender(),boardGridPane.getColumnIndex( ((ImageView) dragEvent.getSource()).getParent()), boardGridPane.getRowIndex( ((ImageView) dragEvent.getSource()).getParent()), level);
                if(builderWorker != null){
                    builderWorker.setEffect(null);
                    builderWorker = null;
                }

                redButton.setDisable(true);
                hideImageViews();
                dNdActiveBuild = false;
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


        //TODO if the answer from server is negative restoreImage();

    }

    /**
     * On Drag Over method
     */

    public void highlightSquareOverMethod(DragEvent dragEvent) throws IOException {

        /* ((ImageView)dragEvent.getGestureSource()) != source_pointer should check that the place for drop is not the same of source of drag
        if(((ImageView)dragEvent.getGestureSource()) != source_pointer && dragEvent.getDragboard().hasImage())
            dragEvent.acceptTransferModes(TransferMode.MOVE);
        dragEvent.consume();
         */

        if(state.equals("worker") || dNdActiveMove || (dNdActiveBuild && ((ImageView) dragEvent.getSource()).getId().equals("workerImageView"))) {
            if ((dragEvent.getGestureSource() != dragEvent.getSource()) && dragEvent.getDragboard().hasImage())
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            dragEvent.consume();
        }

    }

    /**
     * On Drag Detected method
     */

    public void startChangingPosition(MouseEvent mouseEvent) {

        boolean myWorker = false;

        if(state.equals("move") && dNdActiveMove && (((ImageView) mouseEvent.getSource()).getImage() != null)) {
            int i[] = gui.getMyWorkerPosition(gui.getWorkerGender(boardGridPane.getColumnIndex(((ImageView) mouseEvent.getSource()).getParent()), boardGridPane.getRowIndex(((ImageView) mouseEvent.getSource()).getParent())));
            myWorker = (dNdActiveMove && (((AnchorPane)((ImageView) mouseEvent.getSource()).getParent()).getChildren().get(1).getId().equals("workerImageView")) && (i[0] == boardGridPane.getColumnIndex(((ImageView) mouseEvent.getSource()).getParent()) && (i[1] == boardGridPane.getRowIndex(((ImageView) mouseEvent.getSource()).getParent()))));
        }
        if((state.equals("worker") && ((ImageView) mouseEvent.getSource()).getId().equals("worker")) || (myWorker) || (dNdActiveBuild && !(((ImageView) mouseEvent.getSource()).getId().equals("workerImageView")))) {
            ImageView test = ((ImageView) mouseEvent.getSource());


            source = new ImageView(((ImageView) mouseEvent.getSource()).getImage());
            source_pointer = test;

            Dragboard db = test.startDragAndDrop(TransferMode.MOVE);
            /*
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setTransform(new Scale(0.0002,0.0002));
            db.setDragView(test.snapshot(parameters, null));
             */
            ClipboardContent content = new ClipboardContent();
            content.putImage(test.getImage());
            db.setContent(content);
            mouseEvent.consume();
        }

    }

    public void highlightSquare(DragEvent dragEvent) { }

    public void notHihglightSquare(DragEvent dragEvent) { }

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

    /**
     * This method is called when the next player button is pressed, it updates the gui
     * @param actionEvent none
     */

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
     * This method display the updated board by updating each square
     * @param board_view
     */

    public void updateBoard(Board board_view){

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                updateSquare(board_view.getSquareByCoordinates(x, y));
            }
        }

    }

    /**
     * This method display a single updated square
     * @param square
     */

    public void updateSquare(Square square){

        int x = square.getX();
        int y = square.getY();
        Image imageTemp;
        AnchorPane anchorPane = (AnchorPane) getNodeFromGridPane(boardGridPane, x, y);
        //Node node = getNodeFromGridPane(board, x, y);
        //Node node = board.getChildren().get(y*5+x);
        //AnchorPane anchorPane = (AnchorPane) boardGridPane.getChildren().get((y*5+x));

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
     * This method return a node of GridPane
     * @param gridPane represents a GridPane
     * @param col represents x position
     * @param row represents y position
     * @return the selected node
     */

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


    public void hideBlueButton(){
        blueButton.setVisible(false);
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
        Image updateButton = GuiManager.loadImage("Buttons/btn_coral_build.png");
        redButton.setImage(updateButton);
    }

    public void hideRedButton(){
        redButton.setVisible(false);
        redButton.setDisable(true);
    }

    public void showEndButton(){
        endTurnButton.setVisible(true);
        endTurnButton.setDisable(false);
        Image updateButton = GuiManager.loadImage("Buttons/btn_small_gray.png");
        endTurnButton.setImage(updateButton);
    }

    public void hideEndButton(){
        endTurnButton.setVisible(false);
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
     * This method is triggered when a square is clicked and allow to build before move
     */

    @FXML
    public void buildAction(MouseEvent mouseEvent) {

        /*
        builderWorker = ((ImageView) mouseEvent.getSource());
        gui.setSelectedWorker(boardGridPane.getColumnIndex(builderWorker.getParent()), boardGridPane.getRowIndex(builderWorker.getParent()));
        int depth = 70;
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(javafx.scene.paint.Color.RED);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        builderWorker.setEffect(borderGlow);
*/


        if(state.equals("build") && (((ImageView) mouseEvent.getSource()).getImage() != null) && (gui.getSelectedWorker() == null)){

            System.out.print("Dopo il primo if\n");
            if(builderWorker != null)
                builderWorker.setEffect(null);

            int[] i = gui.getMyWorkerPosition(gui.getWorkerGender(boardGridPane.getRowIndex(((ImageView) mouseEvent.getSource()).getParent()), boardGridPane.getColumnIndex(((ImageView) mouseEvent.getSource()).getParent())));
            boolean myWorker = ((i[0] == boardGridPane.getRowIndex(((ImageView) mouseEvent.getSource()).getParent()) && (i[1] == boardGridPane.getColumnIndex(((ImageView) mouseEvent.getSource()).getParent()))));

            System.out.print("Prima del secondo if");
            if(myWorker){
                builderWorker = ((ImageView) mouseEvent.getSource());
                //gui.setSelectedWorker(boardGridPane.getColumnIndex(builderWorker.getParent()), boardGridPane.getRowIndex(builderWorker.getParent()));
                int depth = 70;
                borderGlow.setOffsetY(0f);
                borderGlow.setOffsetX(0f);
                borderGlow.setColor(javafx.scene.paint.Color.RED);
                borderGlow.setWidth(depth);
                borderGlow.setHeight(depth);
                builderWorker.setEffect(borderGlow);
            }

        }


/*
        boolean myWorker = state.equals("build") && (((ImageView) mouseEvent.getSource()).getImage() != null);
        myWorker = myWorker &&

        if(state.equals("build") && (((ImageView) mouseEvent.getSource()).getImage() != null)){}

        boolean myWorker = false;

        if(state.equals("build")) {
            int i[] = gui.getMyWorkerPosition(gui.getWorkerGender(boardGridPane.getRowIndex(((ImageView) mouseEvent.getSource()).getParent()), boardGridPane.getColumnIndex(((ImageView) mouseEvent.getSource()).getParent())));
            myWorker = ((((ImageView) mouseEvent.getSource()).getId().equals("workerImageView")) && (i[0] == boardGridPane.getRowIndex(((ImageView) mouseEvent.getSource()).getParent()) && (i[1] == boardGridPane.getColumnIndex(((ImageView) mouseEvent.getSource()).getParent()))));
        }

        if(state.equals("build") && myWorker){

            builderWorker = ((ImageView) mouseEvent.getSource());

            int depth = 70;
            borderGlow.setOffsetY(0f);
            borderGlow.setOffsetX(0f);
            borderGlow.setColor(javafx.scene.paint.Color.RED);
            borderGlow.setWidth(depth);
            borderGlow.setHeight(depth);
            ((ImageView) mouseEvent.getSource()).setEffect(borderGlow);
        }
         */

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


}
