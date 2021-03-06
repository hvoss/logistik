package de.hsbremen.kss.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.model.Action;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

/**
 * canvas to display a map.
 * 
 * @author henrik
 * 
 */
public final class MapCanvas extends Canvas {

    /** Serial version UID. */
    private static final long serialVersionUID = 1453468239882525848L;

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(MapCanvas.class);

    /** border of the canvas */
    private static final int BORDER = 10;

    /** size of the circles to display stations */
    private static final int STATION_CIRCLE_SIZE = 4;

    /** space between station circle and the name of the station */
    private static final int SPACE_BETWEEN_CIRCLE_TEXT = 5;

    /** angle of the arrowhead */
    private static final double ARROWHEAD_ANGLE = FastMath.PI / 12;

    /** length of the arrowhead */
    private static final int ARROWHEAD_LENGTH = 30;

    /** plane to transform from 3D to 2D and the other way around. */
    private static final Plane PLANE = new Plane(Vector3D.PLUS_K);

    /** object to rotate clockwise */
    private static final Rotation POSITIVE_ROTATION = new Rotation(Vector3D.PLUS_K, MapCanvas.ARROWHEAD_ANGLE);

    /** object to rotate anti clockwise */
    private static final Rotation NEGATIVE_ROTATION = new Rotation(Vector3D.PLUS_K, -MapCanvas.ARROWHEAD_ANGLE);

    /** width of the model. */
    private final Map map;

    /** stations to display */
    private final Collection<Station> stations;

    /** background image */
    private final Image image;

    /** scale factor to transform model coordinates into graphical coordinates */
    private double scale;

    /** plan to display */
    private Plan plan;

    private static final List<Color> COLORS = Arrays.asList(Color.BLACK, Color.RED, Color.BLUE, Color.GRAY, Color.MAGENTA, Color.YELLOW, Color.PINK,
            Color.GREEN);

    /**
     * ctor.
     * 
     * @param width
     *            width of the model.
     * @param height
     *            height of the model.
     * @param stations
     *            stations to display
     * @param bestPlan
     *            plan to display
     */
    public MapCanvas(final Map map, final Collection<Station> stations) {
        Validate.notNull(map, "map is null");
        Validate.notNull(stations, "stations is null");
        Validate.noNullElements(stations, "stations contains null elements");

        this.stations = stations;
        this.map = map;

        if (map.image != null) {
            final InputStream is = MapCanvas.class.getResourceAsStream(map.image);

            Image tmpImage = null;
            try {
                tmpImage = ImageIO.read(is);
            } catch (final IOException e) {
                MapCanvas.LOG.warn("could not load graphical map", e);
            }

            this.image = tmpImage;
        } else {
            this.image = null;
        }
    }

    @Override
    public void paint(final Graphics g) {
        updateScale();

        final Dimension d = getSize();
        final Image offscreen = createImage(d.width, d.height);
        final Graphics offgc = offscreen.getGraphics();
        // clear the exposed area
        offgc.setColor(getBackground());
        offgc.fillRect(0, 0, d.width, d.height);
        offgc.setColor(getForeground());

        drawBackgroundAndStations(offgc);
        drawPlan(offgc);

        g.drawImage(offscreen, 0, 0, this);
    }

    /**
     * updates the scale factor.
     */
    private void updateScale() {
        final int screenWidth = getSize().width;
        final int screenHeight = getSize().height;

        final double scaleWidth = (double) (screenWidth - 2 * MapCanvas.BORDER) / (double) this.map.width();
        final double scaleHeight = (double) (screenHeight - 2 * MapCanvas.BORDER) / (double) this.map.height();

        this.scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
    }

    /**
     * draws the background and stations.
     * 
     * @param graphics
     */
    private void drawBackgroundAndStations(final Graphics g) {
        if (this.image != null) {
            g.drawImage(this.image, MapCanvas.BORDER, MapCanvas.BORDER, (int) (this.map.width() * this.scale - MapCanvas.BORDER),
                    (int) (this.map.height()

                    * this.scale - MapCanvas.BORDER), getParent());
        }

        for (final Station station : this.stations) {
            drawStation(g, station);
        }
    }

    /**
     * transforms model X-coordinate into graphical X-coordinate.
     * 
     * @param x
     *            model coordinate to transform
     * @return the graphical X-coordinate
     */
    private int transformX(final double x) {
        return (int) (x * this.scale) - (int) (Math.min(0, this.map.minX) * this.scale) + MapCanvas.BORDER;
    }

    /**
     * transforms model Y-coordinate into graphical Y-coordinate.
     * 
     * @param y
     *            model coordinate to transform
     * @return the graphical Y-coordinate
     */
    private int transformY(final double y) {
        return (int) (this.map.height() * this.scale) - (int) (y * this.scale) + (int) (Math.min(0, this.map.minY) * this.scale) + MapCanvas.BORDER;
    }

    /**
     * draws a circle and the name of the station on the map.
     * 
     * @param g
     * 
     * @param station
     *            station to draw
     */
    private void drawStation(final Graphics g, final Station station) {
        final Vector2D coords = station.getCoordinates();
        final int x = transformX(coords.getX());
        final int y = transformY(coords.getY());

        g.drawOval(x - MapCanvas.STATION_CIRCLE_SIZE / 2, y - MapCanvas.STATION_CIRCLE_SIZE / 2, MapCanvas.STATION_CIRCLE_SIZE,
                MapCanvas.STATION_CIRCLE_SIZE);
        g.drawString(station.getName(), x + MapCanvas.SPACE_BETWEEN_CIRCLE_TEXT, y + MapCanvas.STATION_CIRCLE_SIZE);
    }

    /**
     * draws a arrow from a source point to a destination point.
     * 
     * @param g
     * 
     * @param source
     *            source of the arrow.
     * @param destination
     *            destination if the arrow
     */
    private void drawArrow(final Graphics g, final Color color, final Vector2D source, final Vector2D destination) {
        final Vector2D arrowVector = source.subtract(destination).normalize().scalarMultiply(MapCanvas.ARROWHEAD_LENGTH);

        final Vector3D arrowVector3d = convert(arrowVector);

        final Vector3D a1 = MapCanvas.POSITIVE_ROTATION.applyTo(arrowVector3d);
        final Vector3D a2 = MapCanvas.NEGATIVE_ROTATION.applyTo(arrowVector3d);

        drawLine(g, color, source, destination);
        drawLine(g, color, destination, destination.add(convert(a1)));
        drawLine(g, color, destination, destination.add(convert(a2)));
    }

    /**
     * converts a 3D vector into a 2D vector.
     * 
     * @param d3Vector
     *            3D vector to convert
     * @return the converted 2D vector
     */
    private static Vector2D convert(final Vector3D d3Vector) {
        return MapCanvas.PLANE.toSubSpace(d3Vector);
    }

    /**
     * converts a 2D vector into a 3D vector.
     * 
     * @param d2Vector
     *            2D vector to convert
     * @return the converted 3D vector
     */
    private static Vector3D convert(final Vector2D d2Vector) {
        return MapCanvas.PLANE.getPointAt(d2Vector, 0);
    }

    /**
     * draws a line between two points (vectors)
     * 
     * @param g
     * 
     * @param source
     *            source point of the line
     * @param destination
     *            end point of the line
     */
    private void drawLine(final Graphics g, final Color color, final Vector2D source, final Vector2D destination) {
        final int sX = transformX(source.getX());
        final int sY = transformY(source.getY());
        final int dX = transformX(destination.getX());
        final int dY = transformY(destination.getY());

        final Color oldColor = g.getColor();
        g.setColor(color);
        g.drawLine(sX, sY, dX, dY);
        g.setColor(oldColor);
    }

    /**
     * draws the plan
     * 
     * @param g
     */
    public void drawPlan(final Graphics g) {
        if (this.plan != null) {
            final List<Tour> tours = this.plan.getTours();
            for (int i = 0; i < tours.size(); i++) {
                final Tour tour = tours.get(i);
                final Color color = MapCanvas.COLORS.get(i % MapCanvas.COLORS.size());
                Station lastStation = null;
                for (final Action action : tour.getActions()) {
                    final Station station = action.getStation();

                    if (lastStation != null && !station.equals(lastStation)) {
                        drawArrow(g, color, lastStation.getCoordinates(), station.getCoordinates());
                    }

                    lastStation = station;
                }
            }
        }
    }

    public void setPlan(final Plan plan) {
        this.plan = plan;
        repaint();
    }
}
