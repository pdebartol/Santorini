package it.polimi.ingsw.model;



public abstract class PowerDecorator implements Power {

    //attributes

    protected Power decoratedPower;

    //methods

    public PowerDecorator(Power p){
        super();
        this.decoratedPower = p;
    }

    @Override
    public boolean checkMove(Worker w, Square s) {
        return false;
    }

    @Override
    public boolean checkBuild(Worker w, Square s) {
        return false;
    }

    @Override
    public boolean checkWin() {
        return false;
    }

    @Override
    public void updateMove(Worker w, Square s) {

    }

    @Override
    public void updateBuild(Worker w, Square s) {

    }

    @Override
    public boolean checkTurn(int mode){
        return false;
    }

    @Override
    public Board getBoard() {
        return null;
    }
}
