package de.hsbremen.kss.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
        final List<Station> stations = generateStations(diameter, numStation);

        final Product product = new Product(1, "Product #1");
        final Vehicle vehicle = new Vehicle("1", "Vehicle #1", stations.get(0), stations.get(0), 80d, TimeWindow.INFINITY_TIMEWINDOW, product,
                Integer.MAX_VALUE);

        final List<Order> orders = new ArrayList<>(numStation * rounds);
        Station lastStation = null;
        for (int i = 0; i < numStation * rounds; i++) {
            final int idx = i % numStation;
            final Station station = stations.get(idx);

            if (lastStation != null) {
                final OrderStation source = new OrderStation(lastStation, TimeWindow.INFINITY_TIMEWINDOW, 0d);
                final OrderStation destination = new OrderStation(station, TimeWindow.INFINITY_TIMEWINDOW, 0d);

                final String name = lastStation.getName() + " => " + station.getName();

                final Order order = new Order(i, name, source, destination, product, 1);
                orders.add(order);

            }

            lastStation = station;
        }

        return new Configuration(new HashSet<>(orders), new HashSet<>(stations), new HashSet<>(Arrays.asList(vehicle)), new HashSet<>(
                Arrays.asList(product)), new HashSet<ComplexOrder>());

    }

    private List<Station> generateStations(final int diameter, final int numStation) {
        final List<Station> stations = new ArrayList<>(numStation);
        for (int i = 1; i <= numStation; i++) {
            final double phi = (2 * FastMath.PI * (i - 1)) / numStation;
            final double x = diameter * FastMath.cos(phi);
            final double y = diameter * FastMath.sin(phi);
            final Vector2D coordinates = new Vector2D(x, y);
            final Station station = new Station(i, "Station #" + i, coordinates);
            stations.add(station);
        }
        return stations;
    }
}
