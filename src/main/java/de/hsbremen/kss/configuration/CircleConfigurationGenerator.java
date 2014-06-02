package de.hsbremen.kss.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import de.hsbremen.kss.util.RandomUtils;

public class CircleConfigurationGenerator {

    RandomUtils randomUtils;

    public CircleConfigurationGenerator(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    public Configuration generateConfiguration(final int diameter, final int numStation, final int rounds) {
        final List<Station> stations = new ArrayList<>(numStation);
        for (int i = 1; i <= numStation; i++) {
            final double phi = (2 * FastMath.PI * (i - 1)) / numStation;
            final double x = diameter * FastMath.cos(phi);
            final double y = diameter * FastMath.sin(phi);
            final Vector2D coordinates = new Vector2D(x, y);
            final Station station = new Station(i, "Station #" + i, coordinates);
            stations.add(station);
        }

        return null;
    }
}
