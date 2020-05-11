package it.polimi.ingsw.model;

import it.polimi.ingsw.msgUtilities.server.AnswerMsgWriter;
import it.polimi.ingsw.msgUtilities.server.UpdateMsgWriter;
import org.w3c.dom.Document;

public class ModelMsgContainer {

    //attributes

    private final UpdateMsgWriter updateMsgWriter;
    private final AnswerMsgWriter answerMsgWriter;
    private Document updateMsg, answerMsg;

    //constructors

    public ModelMsgContainer(String username, String mode){
        this.updateMsgWriter = new UpdateMsgWriter();
        this.answerMsgWriter = new AnswerMsgWriter();
        updateMsgWriter.setStandardUpdateValues(username,mode);
        answerMsgWriter.setStandardAnswerValues(username,mode,"accepted");
    }

    //methods

    public Document getAnswerMsg() {
        return answerMsg;
    }

    public Document getUpdateMsg(){
        return updateMsg;
    }

    public void updateMove(int startX, int startY, int x, int y){
        answerMsg = answerMsgWriter.moveUpdate(startX,startY,x,y);
        updateMsg = updateMsgWriter.moveUpdate(startX,startY,x,y);
    }

    public void updateBuild(int startX, int startY, int x, int y, int level){
        answerMsg = answerMsgWriter.buildUpdate(startX,startY,x,y,level);
        updateMsg = updateMsgWriter.buildUpdate(startX,startY,x,y,level);
    }

    public void nextStepIndication(String nextStep){
        answerMsg = answerMsgWriter.nextStepTurnIndication(nextStep);
    }

    public void endOfTurnRemoveAndBuildUpdate(int startX,int startY,int level){
        answerMsg = answerMsgWriter.endOfTurnRemoveAndBuildUpdate(startX,startY,level);
        updateMsg = updateMsgWriter.endOfTurnRemoveAndBuildUpdate(startX,startY,level);
    }
}
