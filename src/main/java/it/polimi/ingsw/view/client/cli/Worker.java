package it.polimi.ingsw.view.client.cli;

/**
 * This class represents a single worker
 * @author aledimaio
 */

public class Worker {

    private final String icon = Unicode.WORKER_ICON.escape();
    private final Color color;
    private final String gender;

    public Worker(Color color, String gender) {
        this.color = color;
        this.gender = gender;
    }

    public String getIcon() {
        return icon;
    }

    public Color getColor() {
        return color;
    }

}
