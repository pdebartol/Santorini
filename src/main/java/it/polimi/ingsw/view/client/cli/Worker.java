package it.polimi.ingsw.view.client.cli;

/**
 * This class represents a single worker
 * @author aledimaio
 */

public class Worker {

    private final String icon = Unicode.WORKER_ICON.escape();
    private final ColorCode colorCode;
    private final String gender;

    public Worker(ColorCode colorCode, String gender) {
        this.colorCode = colorCode;
        this.gender = gender;
    }

    public String getIcon() {
        return icon;
    }

    public ColorCode getColorCode() {
        return colorCode;
    }

}
