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

    public Configuration generateConfiguration(final int diameter, final int numStation) {
        final List<Station> stations = generateStations(diameter, numStation);

        final int loadableOrders = 4;
        final int numVehicles = numStation / loadableOrders;
        final Product product = new Product(1, "Product #1");

        final List<Vehicle> vehicles = new ArrayList<>(numVehicles);
        final Station depot = new Station(0, "Depot", new Vector2D(0, 0));

        for (int i = 1; i <= numVehicles; i++) {
            final Vehicle vehicle = new Vehicle("" + i, "Vehicle #" + i, depot, depot, 80d, TimeWindow.INFINITY_TIMEWINDOW, product, loadableOrders);
            vehicles.add(vehicle);

        }

        final List<Order> orders = new ArrayList<>(numStation);
        for (int i = 0; i < numStation; i++) {
            final Station station = stations.get(i);

            final OrderStation source = new OrderStation(depot, TimeWindow.INFINITY_TIMEWINDOW, 0d);
            final OrderStation destination = new OrderStation(station, TimeWindow.INFINITY_TIMEWINDOW, 0d);

            final String name = depot.getName() + " => " + station.getName();

            final Order order = new Order(i, name, source, destination, product, 1);
            orders.add(order);

        }
        stations.add(depot);

        return new Configuration(new HashSet<>(orders), new HashSet<>(stations), new HashSet<>(vehicles), new HashSet<>(Arrays.asList(product)),
                new HashSet<ComplexOrder>());

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
