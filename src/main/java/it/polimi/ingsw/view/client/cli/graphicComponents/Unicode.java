package it.polimi.ingsw.view.client.cli.graphicComponents;

public enum Unicode {
    WORKER_ICON("\u3020"),
    BOX_DRAWINGS_HEAVY_HORIZONTAL("━"),
    BOX_DRAWINGS_HEAVY_VERTICAL("┃"),
    BOX_DRAWINGS_HEAVY_DOWN_AND_RIGHT("┏"),
    BOX_DRAWINGS_HEAVY_UP_AND_RIGHT("┗"),
    BOX_DRAWINGS_HEAVY_UP_AND_LEFT("┛"),
    BOX_DRAWINGS_HEAVY_DOWN_AND_LEFT("┓"),
    BOX_RAWINGS_HEAVY_VERTICAL_AND_RIGHT("┣"),
    BOX_DRAWINGS_HEAVY_VERTICAL_AND_LEFT("┫"),
    BOX_DRAWINGS_HEAVY_VERTICAL_AND_HORIZONTAL("╋"),
    BOX_DRAWINGS_HEAVY_UP_AND_HORIZONTAL("┻"),
    BOX_DRAWINGS_HEAVY_DOWN_AND_HORIZONTAL("┳"),
    BOX_DRAWINGS_LIGHT_HORIZONTAL("─"),
    BOX_DRAWINGS_LIGHT_VERTICAL("│"),
    BOX_DRAWINGS_LIGHT_DOWN_AND_RIGHT("┌"),
    BOX_DRAWINGS_LIGHT_UP_AND_RIGHT("└"),
    BOX_DRAWINGS_LIGHT_UP_AND_LEFT("┘"),
    BOX_DRAWINGS_LIGHT_DOWN_AND_LEFT("┐"),
    BOX_RAWINGS_LIGHT_VERTICAL_AND_RIGHT("├"),
    BOX_DRAWINGS_LIGHT_VERTICAL_AND_LEFT("┤"),
    BOX_DRAWINGS_LIGHT_VERTICAL_AND_HORIZONTAL("┼"),
    BOX_DRAWINGS_LIGHT_UP_AND_HORIZONTAL("┴"),
    BOX_DRAWINGS_LIGHT_DOWN_AND_HORIZONTAL("┬");

    private String escape;

    Unicode (String escape){
        this.escape = escape;
    }

    /**
     * This method allow to get directly the code by returning it as a String
     */

    public String escape(){
        return escape;
    }

}
