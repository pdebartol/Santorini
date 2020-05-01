package it.polimi.ingsw.model.enums;

public enum State {
    LOGIN("login"),
    GODS_SETUP("gods_setup"),
    CHOOSE_GOD("choose_god"),
    CHOOSE_STARTER("choose_starter"),
    SET_WORKERS("set_workers"),
    IN_GAME("in_game");

    private final String label;

    State(String label){
        this.label = label;
    }

    public static State valueOfLabel(String label) {
        for (State sta : values()) {
            if (sta.label.equals(label)) {
                return sta;
            }
        }
        return null;
    }


}
