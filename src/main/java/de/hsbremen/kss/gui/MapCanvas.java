package de.hsbremen.kss.gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import de.hsbremen.kss.configuration.Station;

public class MapCanvas extends Canvas {

    private static final int SPACE_BETWEEN_CIRCLE_TEXT = 5;

    private static final int BORDER = 5;

    private static final int CIRCLE_SIZE = 4;

    private final int width;

    private final int height;

    private final Collection<Station> stations;

    private Image image;

    public MapCanvas(final int width, final int height, final Collection<Station> stations) {
        super();
        this.width = width;
        this.height = height;
        this.stations = stations;

        final InputStream is = MapCanvas.class.getResourceAsStream("/deutschland.png");

        try {
            this.image = ImageIO.read(is);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void paint(final Graphics g) {
        final int screenWidth = getSize().width;
        final int screenHeight = getSize().height;

        final double scaleWidth = (double) (screenWidth - 2 * MapCanvas.BORDER) / (double) this.width;
        final double scaleHeight = (double) (screenHeight - 2 * MapCanvas.BORDER) / (double) this.height;

        final double scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;

        g.drawImage(this.image, MapCanvas.BORDER, MapCanvas.BORDER, (int) (this.width * scale - MapCanvas.BORDER),
                (int) (this.height * scale - MapCanvas.BORDER), getParent());

        for (final Station s : this.stations) {
            final Vector2D coords = s.getCoordinates();
            final int x = (int) (coords.getX() * scale) + MapCanvas.BORDER;
            final int y = screenHeight - (int) (coords.getY() * scale) - MapCanvas.BORDER;

            g.drawOval(x - MapCanvas.CIRCLE_SIZE / 2, y - MapCanvas.CIRCLE_SIZE / 2, MapCanvas.CIRCLE_SIZE, MapCanvas.CIRCLE_SIZE);
            g.drawString(s.getName(), x + MapCanvas.SPACE_BETWEEN_CIRCLE_TEXT, y + MapCanvas.CIRCLE_SIZE);

        }
    }
}
