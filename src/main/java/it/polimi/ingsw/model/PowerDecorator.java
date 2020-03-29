package it.polimi.ingsw.model;

/**
 * PowerDecorator is an abstract class necessary as a basis for the decoration of gods' powers.
 * All methods call the corresponding decoratedPower method.
 * @author marcoDige
 */

public abstract class PowerDecorator implements Power {

    //attributes

    /**
     * Power decoratedPower represents the already power decorated.
     */
    protected Power decoratedPower;

    //methods

    public PowerDecorator(Power p){
        super();
        this.decoratedPower = p;
    }

    @Override
    public boolean checkMove(Worker w, Square s) {
        return decoratedPower.checkMove(w,s);
    }

    @Override
    public boolean checkBuild(Worker w, Square s) {
        return decoratedPower.checkBuild(w,s);
    }

    @Override
    public boolean checkWin() {
        return decoratedPower.checkWin();
    }

    @Override
    public void updateMove(Worker w, Square s) {
        decoratedPower.updateMove(w,s);
    }

    @Override
    public void updateBuild(Worker w, Square s) {
        decoratedPower.updateBuild(w,s);
    }

    @Override
    public boolean checkTurn(int mode){
        return decoratedPower.checkTurn(mode);
    }

    @Override
    public Board getBoard() {
        return decoratedPower.getBoard();
    }
}
