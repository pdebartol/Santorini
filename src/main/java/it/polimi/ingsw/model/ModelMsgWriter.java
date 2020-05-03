package it.polimi.ingsw.model;

import it.polimi.ingsw.msgUtilities.server.MsgOutWriter;

/**
 * This class implements method that model uses to notify changes after a move/build or endOfTurn command.
 * @author marcoDige
 */

public class ModelMsgWriter {

    //attributes

    MsgOutWriter outWriter;

    //constructor

    public ModelMsgWriter(){
        outWriter = new MsgOutWriter();
    }

    //methods

}
