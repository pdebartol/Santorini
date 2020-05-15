package it.polimi.ingsw.model.enums;

import it.polimi.ingsw.view.client.cli.graphicComponents.ColorCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public static String labelOfEnum(Color c){
        for (Color col : values()) {
            if (col.equals(c)) {
                return col.label;
            }
        }
        return null;
    }

    public static List<Color> getColorList(){
        return new ArrayList<>(Arrays.asList(values()));
    }

    public static ColorCode getColorCodeByColor(Color c){
        ColorCode cc;

         switch (Objects.requireNonNull(Color.labelOfEnum(c))){
             case "W" :
                 cc = ColorCode.WHITE;
                 break;
             case "G" :
                 cc = ColorCode.GREY;
                 break;
             case "A":
                 cc =  ColorCode.AZURE;
                 break;
             default:
                 throw new IllegalStateException("Unexpected value: " + Objects.requireNonNull(Color.labelOfEnum(c)));
         }

         return cc;

    }
}
