package de.hsbremen.kss.gui;

public class Map {

    public static final Map GERMANY = new Map(0, 632, 0, 876, "/deutschland.png");

    public final int minX;

    public final int maxX;

    public final int minY;

    public final int maxY;

    public final String image;

    public Map(final int minX, final int maxX, final int minY, final int maxY, final String image) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.image = image;
    }

    public static Map circle(final int diameter) {
        return new Map(-diameter, diameter, -diameter, diameter, null);
    }

    public int height() {
        return this.maxY - this.minY;
    }

    public int width() {
        return this.maxX - this.minX;
    }

}
