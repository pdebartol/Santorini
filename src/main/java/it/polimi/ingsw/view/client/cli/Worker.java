package it.polimi.ingsw.view.client.cli;

/**
 * This class represents a single worker
 * @author aledimaio
 */

public class Worker {

    private final String icon = Unicode.WORKER_ICON.escape();
    private final Color color;

    public Worker(Color color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public Color getColor() {
        return color;
    }

}
