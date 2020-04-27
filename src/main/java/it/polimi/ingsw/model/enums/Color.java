package it.polimi.ingsw.model.enums;

/**
 * Color is the enum representing the workers' possible colors.
 * @author pierobartolo
 */

public enum Color {
    WHITE("W"),
    GREY("G"),
    AZURE("A");

    private final String label;

     Color(String label){
        this.label = label;
    }

    public static Color valueOfLabel(String label) {
        for (Color e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
