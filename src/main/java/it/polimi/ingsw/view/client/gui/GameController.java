package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.view.client.viewComponents.*;
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
import java.util.ArrayList;

/**
 * This class implements the GameScene Controller
 *
 * @author pierobartolo & aledimaio
 */

public class GameController {

    @FXML
    public Label playerName;
    @FXML
    public Label informationBox;
    @FXML
    public ImageView moveButton;
    @FXML
    public ImageView buildButton;
    @FXML
    public Label godDescription;
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
    @FXML
    public ImageView godContainer;
    @FXML
    public ImageView playerFlag;
    @FXML
    public ImageView nextButton;
    @FXML
    public AnchorPane blocksContainer;

    private Gui gui;

    private boolean dNdActiveMove;
    private boolean dNdActiveBuild;

    private ImageView builderWorker;

    private String workerGender;

    private ImageView source;
    private ImageView source_pointer;

    private ImageView destination;
    private ImageView destination_pointer;

    //constructors

    public GameController(){
        dNdActiveMove = false;
        dNdActiveBuild = false;
        builderWorker = null;
    }

    /**
     * In-Game Players
     */

    private ArrayList<Player> players;

    /**
     * Current player in the player's information box
     */

    private int currentPlayerId = 0;


    /**
     * Current state of the match
     */

    String state = "worker";


    public void setGui(Gui gui) {
        this.gui = gui;
    }

    /**
     * This method change the image of the button when it is pressed
     * @param mouseEvent represents the left mouse button held down
     */

    public void onPressedBuildButton(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_build_pressed.png");
        buildButton.setImage(updateButton);
        buildButton.setDisable(true);
    }

    /**
     * This method turns on the build-phase of the match (when the build button is pressed)
     * @param mouseEvent none
     */

    public void doActionBuild(MouseEvent mouseEvent) {
        dNdActiveBuild = true;
        state = "build";
        showImageViews();
    }

    /**
     * This method turns on the move-phase of the match (when the move button is pressed)
     * @param mouseEvent none
     */

    public void doActionMove(MouseEvent mouseEvent) {
        dNdActiveMove = true;
        state = "move";
        updateBoard(gui.gameBoard);
        //Yellow
    }

    /**
     * This method ends the turn (when the endTurn button is pressed)
     * @param mouseEvent represents the click action
     */

    public void doActionEndTurn(MouseEvent mouseEvent) {
        gui.sendEndOfTurnRequest();
    }

    /**
     * This method change the image of the button when it is pressed
     * @param mouseEvent none
     */

    public void onPressedMoveButton(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_move_pressed.png");
        moveButton.setImage(updateButton);
        moveButton.setDisable(true);
    }

    /**
     * This method is triggered when a drag & drop action does not end successfully.
     */

    public void restoreImage() {
        source_pointer.setImage(source.getImage());
        destination_pointer.setImage(destination.getImage());
    }

    /**
     * This method represents the "On Drag Done" method of JavaFX for the builders images.
     * @param dragEvent is the object that handles the drag & drop features
     */

    public void dragDoneMethod(DragEvent dragEvent) {

        if (dragEvent.getTransferMode() == TransferMode.MOVE) {
            if(((ImageView) dragEvent.getSource()).getId().equals("worker"))
                ((ImageView) dragEvent.getSource()).setImage(null);
        }

    }

    /**
     * This method represents the "On Drag Done" method of JavaFX for workers.
     * @param dragEvent is the object that handles the drag & drop features
     */

    public void removeWorkerDragged(DragEvent dragEvent) {

        if (dragEvent.getTransferMode() == TransferMode.MOVE) {
            if(((ImageView) dragEvent.getSource()).getId().equals("worker"))
                ((ImageView) dragEvent.getSource()).setImage(null);
        }

    }

    /**
     * This method represents the "On Drag Dropped" method of JavaFX. It is triggered when a drop action is completed.
     * @param dragEvent handle the "drop" action
     */

    @FXML
    public void acceptElement(DragEvent dragEvent) {

        switch (state) {

            case "worker":
                int x = GridPane.getRowIndex(((ImageView) dragEvent.getSource()).getParent());
                int y = GridPane.getColumnIndex(((ImageView) dragEvent.getSource()).getParent());
                gui.sendSetWorkerOnBoardRequest(workerGender, x, y);
                break;

            case "move":
                int selectedX = GridPane.getRowIndex(source_pointer.getParent());
                int selectedY = GridPane.getColumnIndex(source_pointer.getParent());
                int newX = GridPane.getRowIndex(((ImageView) dragEvent.getSource()).getParent());
                int newY = GridPane.getColumnIndex(((ImageView) dragEvent.getSource()).getParent());

                gui.sendMoveRequest(gui.getWorkerGender(selectedX, selectedY), newX, newY);
                dNdActiveMove = false;
                hideMoveButton();
                break;

            case "build":

                int level = 0;
                switch (source_pointer.getId()) {
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

                int xToBuild = GridPane.getRowIndex(((ImageView) dragEvent.getSource()).getParent());
                int yToBuild = GridPane.getColumnIndex(((ImageView) dragEvent.getSource()).getParent());
                if(gui.getSelectedWorker() != null) {
                    gui.sendBuildRequest(gui.getSelectedWorker().getGender(), xToBuild, yToBuild, level);

                }else {
                    int startX = GridPane.getRowIndex(builderWorker.getParent());
                    int startY = GridPane.getColumnIndex(builderWorker.getParent());
                    gui.sendBuildRequest(gui.getWorkerGender(startX,startY), xToBuild, yToBuild, level);
                }

                hideBuildButton();
                hideImageViews();
                dNdActiveBuild = false;
                break;
        }

        Dragboard db = dragEvent.getDragboard();
        boolean success = false;

        ImageView test = ((ImageView) dragEvent.getSource());

        destination = new ImageView(((ImageView) dragEvent.getSource()).getImage());
        destination_pointer = test;

        if (db.hasImage()) {
            test.setImage(db.getImage());
            success = true;
        }

        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    /**
     * This method represents the "On Drag Over" method of JavaFX for workers.
     * @param dragEvent is the object that handles the drag & drop features
     */

    @FXML
    public void highlightSquareOverMethod(DragEvent dragEvent) {

        if (state.equals("worker") || dNdActiveMove || (dNdActiveBuild && ((ImageView) dragEvent.getSource()).getId().equals("workerImageView"))) {
            if ((dragEvent.getGestureSource() != dragEvent.getSource()) && dragEvent.getDragboard().hasImage())
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            dragEvent.consume();
        }

    }

    /**
     * This method checks if a given worker is my worker
     * @param mouseEvent get the worker
     * @return the positions of my worker
     */

    private boolean isMyWorker(MouseEvent mouseEvent) {

        int x = GridPane.getRowIndex(((ImageView) mouseEvent.getSource()).getParent());
        int y = GridPane.getColumnIndex(((ImageView) mouseEvent.getSource()).getParent());
        int[] workerPosition = gui.getMyWorkerPosition(gui.getWorkerGender(x, y));

        return workerPosition[0] == x && workerPosition[1] == y;

    }

    /**
     * This method checks if a drag & drop action could start
     * @param mouseEvent is the object represents the mouse action
     * @return a boolean response to the answer "can I drag?"
     */

    private boolean canIDrag(MouseEvent mouseEvent) {

        switch (state) {
            case "worker":
                return ((ImageView) mouseEvent.getSource()).getId().equals("worker");
            case "move":
                int x = GridPane.getRowIndex(((ImageView) mouseEvent.getSource()).getParent());
                int y = GridPane.getColumnIndex(((ImageView) mouseEvent.getSource()).getParent());

                if (dNdActiveMove && ((ImageView) mouseEvent.getSource()).getImage() != null && isMyWorker(mouseEvent)) {
                    if (gui.getSelectedWorker() != null)
                        return gui.getSelectedWorker().getGender().equals(gui.getWorkerGender(x, y));
                    return true;
                }else
                    return false;
            case "build":
                return dNdActiveBuild && !(((ImageView) mouseEvent.getSource()).getId().equals("workerImageView")) && (gui.getSelectedWorker() != null || builderWorker != null);
        }

        return false;
    }

    /**
     * This method represents the "On Drag Detected" method of JavaFX.
     * @param mouseEvent handle the drag action
     */

    @FXML
    public void startChangingPosition(MouseEvent mouseEvent) {

        if (canIDrag(mouseEvent)) {

            ImageView test = ((ImageView) mouseEvent.getSource());


            source = new ImageView(((ImageView) mouseEvent.getSource()).getImage());
            source_pointer = test;

            Dragboard db = test.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(test.getImage());
            db.setContent(content);
            //db.setDragView(test.getImage());
            mouseEvent.consume();

        }

    }

    /**
     * This method shows the current player's information (when the showPlayer button is pressed)
     */

    public void showPlayerInformation() {
            getCurrentGod();
            godImage.setVisible(true);
            godDescription.setVisible(true);
            godContainer.setVisible(true);
            playerFlag.setVisible(true);
            playerName.setVisible(true);
            nextButton.setVisible(true);
            nextButton.setDisable(false);
    }

    /**
     * This method shows the  next player's information (when the showPlayer button is pressed)
     */

    public void nextPlayer(MouseEvent actionEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_blue.png");
        nextButton.setImage(updateButton);
        if (currentPlayerId >= players.size() - 1)
            currentPlayerId = 0;
        else
            currentPlayerId++;

        getCurrentGod();
    }

    /**
     * This method gets the god of the current player inside the infoBox
     */

    private void getCurrentGod() {
        God tempGod = gui.getPlayerGod(players.get(currentPlayerId));
        godDescription.setText(tempGod.getDescription());
        godImage.setImage(GuiManager.loadGod(tempGod.getId()));
        playerName.setText(players.get(currentPlayerId).getUsername());
        if(gui.isMyPlayer(players.get(currentPlayerId)))
            playerFlag.setEffect(createDropShadow(javafx.scene.paint.Color.LIGHTGOLDENRODYELLOW));
        else
            playerFlag.setEffect(null);
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
        playerName.setText(players.get(currentPlayerId).getUsername());
    }

    /**
     * This method setups the worker's image
     * @param gender of the worker
     */

    public void setupWorker(String gender) {
        worker.setImage(getWorkerImage(gui.getMyColor(), gender));
        workerGender = gender;
    }

    public void setInstructionLabel(String text) {
        informationBox.setText(text);
    }

    /**
     * This method displays the updated board
     * @param board_view is the board that have to be displayed
     */

    public void updateBoard(Board board_view) {

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                updateSquare(board_view.getSquareByCoordinates(x, y));
            }
        }

        if(builderWorker != null){
            builderWorker.setEffect(null);
            builderWorker = null;
        }

    }

    /**
     * This method displays a single updated square
     * @param square is the square that have to be displayed
     */

    public void updateSquare(Square square) {

        int x = square.getX();
        int y = square.getY();
        Image imageTemp;
        AnchorPane anchorPane = (AnchorPane) getNodeFromGridPane(boardGridPane, y, x);

        if (anchorPane != null) {

            if (square.getWorker() != null) {
                Image workerImage = getWorkerImage(square.getWorker().getColor(), square.getWorker().getGender());
                ImageView worker =   ((ImageView) anchorPane.getChildren().get(1));
                worker.setImage(workerImage);
                if((state.equals("move") || state.equals("build") || state.equals("start")) && gui.isMyWorker(x,y)){
                    Worker selectedWorker = gui.getSelectedWorker();
                    if(selectedWorker == null || (selectedWorker.getCurrentPosition().getX() == x && selectedWorker.getCurrentPosition().getY() == y))
                        worker.setEffect(createDropShadow(javafx.scene.paint.Color.BLUE));
                    else
                        worker.setEffect(null);

                }
                else
                    worker.setEffect(null);
            } else {
                ((ImageView) anchorPane.getChildren().get(1)).setImage(null);
            }

            if (square.getDome()) {
                switch (square.getLevel()) {

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
     * @param col      represents the x
     * @param row      represents the y
     * @return the node or null if the node does not exists
     */

    public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {

        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * This method hides the move button in the main game scene
     */

    public void hideMoveButton() {
        Image updateButton = GuiManager.loadImage("Buttons/btn_move_inactive.png");
        moveButton.setImage(updateButton);
        moveButton.setDisable(true);
    }


    /**
     * This method shows the move button in the main game scene
     */

    public void showMoveButton() {
        moveButton.setVisible(true);
        moveButton.setDisable(false);
        Image updateButton = GuiManager.loadImage("Buttons/btn_move.png");
        moveButton.setImage(updateButton);
    }


    /**
     * This method shows the build button in the main game scene
     */

    public void showBuildButton() {
        buildButton.setVisible(true);
        buildButton.setDisable(false);
        Image updateButton = GuiManager.loadImage("Buttons/btn_build.png");
        buildButton.setImage(updateButton);
    }

    /**
     * This method hides the build button in the main game scene
     */

    public void hideBuildButton() {
        Image updateButton = GuiManager.loadImage("Buttons/btn_build_inactive.png");
        buildButton.setImage(updateButton);
        buildButton.setDisable(true);
    }

    /**
     * This method shows the end button in the main game scene
     */

    public void showEndButton() {
        endTurnButton.setVisible(true);
        endTurnButton.setDisable(false);
        Image updateButton = GuiManager.loadImage("Buttons/btn_end.png");
        endTurnButton.setImage(updateButton);
    }

    /**
     * This method hides the end button in the main game scene
     */

    public void hideEndButton() {
        Image updateButton = GuiManager.loadImage("Buttons/btn_end_inactive.png");
        endTurnButton.setImage(updateButton);
        endTurnButton.setDisable(true);
    }

    /**
     * This method gets the image of the worker from resources
     * @param color of the worker
     * @param gender of the worker
     * @return an Image object containing the worker specified
     */

    private Image getWorkerImage(Color color, String gender) {
        // default
        Image workerImage = GuiManager.loadImage("Buildings_+_pawns/" + gender + "_azure_worker.png");

        switch (color) {
            case AZURE:
                workerImage = GuiManager.loadImage("Buildings_+_pawns/" + gender + "_azure_worker.png");
                break;
            case ORANGE:
                workerImage = GuiManager.loadImage("Buildings_+_pawns/" + gender + "_white_worker.png");
                break;
            case GREY:
                workerImage = GuiManager.loadImage("Buildings_+_pawns/" + gender + "_gray_worker.png");
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
        if (state.equals("build") && ((ImageView) mouseEvent.getSource()).getImage() != null && gui.getSelectedWorker() == null) {

            //de-activate red circle around worker if another one was selected
            if (builderWorker != null)
                builderWorker.setEffect(null);


            if (((((ImageView) mouseEvent.getSource()).getId().equals("workerImageView")) && isMyWorker(mouseEvent))) {

                builderWorker = ((ImageView) mouseEvent.getSource());
                DropShadow borderGlow = new DropShadow();

                borderGlow.setOffsetY(0f);
                borderGlow.setOffsetX(0f);
                borderGlow.setColor(javafx.scene.paint.Color.RED);
                borderGlow.setWidth(70);
                borderGlow.setHeight(70);

                //set red circle around worker selected
                builderWorker.setEffect(borderGlow);
            }

        }

    }

    /**
     * This method hides the construction blocks
     */

    public void hideImageViews() {
        firstLevelImageView.setVisible(false);
        secondLevelImageView.setVisible(false);
        thirdLevelImageView.setVisible(false);
        domeImageView.setVisible(false);
        blocksContainer.setVisible(false);
    }

    /**
     * This method shows the construction blocks
     */

    public void showImageViews() {
        firstLevelImageView.setVisible(true);
        secondLevelImageView.setVisible(true);
        thirdLevelImageView.setVisible(true);
        domeImageView.setVisible(true);
        blocksContainer.setVisible(true);
    }

    /**
     * This method change the image of the button when it is pressed
     * @param mouseEvent none
     */

    public void onPressedEndTurnButton(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_end_pressed.png");
        endTurnButton.setImage(updateButton);
        endTurnButton.setDisable(true);
    }

    /**
     * This method change the image of the button when it is pressed
     * @param mouseEvent none
     */

    public void onPressNextButton(MouseEvent mouseEvent) {
        Image updateButton = GuiManager.loadImage("Buttons/btn_blue_pressed.png");
        nextButton.setImage(updateButton);
    }

    /**
     * This method creates a border glow effect that we use to highlight elements on the board
     * @param c shadow's color
     * @return  dropshadow effect
     */

    private DropShadow createDropShadow(javafx.scene.paint.Color c){
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(c);
        borderGlow.setWidth(70);
        borderGlow.setHeight(70);

        return borderGlow;
    }


    public void setState(String state){
        this.state = state;
    }

}

