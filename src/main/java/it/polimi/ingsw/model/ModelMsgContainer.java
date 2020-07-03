package it.polimi.ingsw.model;

import it.polimi.ingsw.msgUtilities.server.AnswerMsgWriter;
import it.polimi.ingsw.msgUtilities.server.UpdateMsgWriter;
import org.w3c.dom.Document;

/**
 * This class is used to write and keep changes to the model during a specific action.
 * It contains two types of messages: update and answer.
 * During the action the model classes can access the two messages which are reset at each action start in the controller.
 * @author marcoDige
 */

public class ModelMsgContainer {

    //attributes

    private final UpdateMsgWriter updateMsgWriter;
    private final AnswerMsgWriter answerMsgWriter;
    private Document updateMsg, answerMsg;

    //constructors

    /**
     * This constructor set 2 writers and 2 documents based on username and action mode.
     * The answer is set by default to accepted because these messages will be sent only and exclusively if the total of
     * the action will be considered valid.
     * @param username is the player who is doing the action's username
     * @param mode is the action mode
     */

    public ModelMsgContainer(String username, String mode){
        this.updateMsgWriter = new UpdateMsgWriter();
        this.answerMsgWriter = new AnswerMsgWriter();
        updateMsg = updateMsgWriter.setStandardUpdateValues(username,mode);
        answerMsg = answerMsgWriter.setStandardAnswerValues(username,mode,"accepted");
    }

    //methods

    public Document getAnswerMsg() {
        return answerMsg;
    }

    public Document getUpdateMsg(){
        return updateMsg;
    }

    /**
     * This method allows to update messages in the container after a move.
     * @param index is the progressive number of the update (Writer needs it to not overwrite previous updates in the xml)
     * @param startX is the starting x coordinate
     * @param startY is the starting y coordinate
     * @param x is the new x coordinate
     * @param y is the new y coordinate
     */

    public void updateMove(int index, int startX, int startY, int x, int y){
        answerMsg = answerMsgWriter.moveUpdate(index,startX,startY,x,y);
        updateMsg = updateMsgWriter.moveUpdate(index,startX,startY,x,y);
    }

    /**
     * This method allows to update messages in the container after a build.
     * @param index is the progressive number of the update (Writer needs it to not overwrite previous updates in the xml)
     * @param startX is the worker x coordinate
     * @param startY is the worker y coordinate
     * @param x is the x coordinate where worker has built
     * @param y is the y coordinate where worker has built
     * @param level is the new level in x,y square
     */

    public void updateBuild(int index, int startX, int startY, int x, int y, int level){
        answerMsg = answerMsgWriter.buildUpdate(index,startX,startY,x,y,level);
        updateMsg = updateMsgWriter.buildUpdate(index,startX,startY,x,y,level);
    }

    /**
     * This method allows to update the nextStep tag in the answer message.
     * This tag is fundamental for the advancement of the turn logic.
     * @param nextStep is the next step in turn that the player who is playing the turn has to do.
     *        "move" : the player has to move
     *        "build" : the player has to build
     *        "move/build" : the player can both move or build
     *        "build/end" : the player can both build or end his turn
     */

    public void nextStepIndication(String nextStep){
        answerMsg = answerMsgWriter.nextStepTurnIndication(nextStep);
    }

    /**
     * This method allows to update messages in the container after a special end of turn (Remove and Build).
     * @param index is the progressive number of the update (Writer needs it to not overwrite previous updates in the xml)
     * @param startX is the worker x coordinate
     * @param startY is the worker y coordinate
     * @param level is the new level in startX,startY square
     */

    public void endOfTurnRemoveAndBuildUpdate(int index, int startX,int startY,int level){
        answerMsg = answerMsgWriter.endOfTurnRemoveAndBuildUpdate(index, startX,startY,level);
        updateMsg = updateMsgWriter.endOfTurnRemoveAndBuildUpdate(index, startX,startY,level);
    }
}
