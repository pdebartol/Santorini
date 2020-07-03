package it.polimi.ingsw.model.enums;

import it.polimi.ingsw.view.client.cli.graphicComponents.ColorCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Color is the enum representing the workers' possible colors.
 * @author pierobartolo AND marcoDige
 */

public enum Color {
    ORANGE("O"),
    GREY("G"),
    AZURE("A");

    private final String label;

     Color(String label){
        this.label = label;
    }

    /**
     * This method allows to obtain the Color value from his label
     * @param label is the Color to obtain's label
     * @return a Color
     */

    public static Color valueOfLabel(String label) {
        for (Color e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }

    /**
     * This method allows to obtain the Color's label
     * @param c is the label to obtain's Color
     * @return a label
     */

    public static String labelOfEnum(Color c){
        for (Color col : values()) {
            if (col.equals(c)) {
                return col.label;
            }
        }
        return null;
    }

    /**
     * This method allows to obtain a list which contains all colors into this Enum
     * @return
     */

    public static List<Color> getColorList(){
        return new ArrayList<>(Arrays.asList(values()));
    }

    /**
     * This method allows to obtain the ColorCode associated with the Color
     * @param c is the ColorCode to obtain's Color
     * @return a ColorCode
     */

    public static ColorCode getColorCodeByColor(Color c){
        ColorCode cc;

         switch (Objects.requireNonNull(Color.labelOfEnum(c))){
             case "O" :
                 cc = ColorCode.ORANGE;
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
