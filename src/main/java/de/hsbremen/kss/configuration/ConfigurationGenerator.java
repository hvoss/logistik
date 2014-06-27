package de.hsbremen.kss.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import de.hsbremen.kss.util.RandomUtils;

public class ConfigurationGenerator {

    private final RandomUtils randomUtils;

    public ConfigurationGenerator(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    public Configuration generateConfiguration(final Collection<Station> stations, final Collection<Product> products,
            final Collection<Vehicle> vehicles, final int numberOfOrder) {
        final Set<Order> orders = new HashSet<>();

        for (int i = 0; i < numberOfOrder; i++) {
        	final Station sourceStation = this.randomUtils.randomElement(stations);
            final Station destinationStation = this.randomUtils.randomElement(stations, sourceStation);
            final Product product = this.randomUtils.randomElement(products);
            final String name = sourceStation.getName() + " => " + destinationStation.getName();

            final double startStart = this.randomUtils.nextInt(8, 12);
            final double endStart = this.randomUtils.nextInt(14, 20);
            
            final double startEnd = this.randomUtils.nextInt(8, 12);
            final double endEnd = this.randomUtils.nextInt(36, 40);

            TimeWindow startTimeWindow = new TimeWindow(startStart, endStart);
            TimeWindow endTimeWindow = new TimeWindow(startEnd, endEnd);

            startTimeWindow = TimeWindow.INFINITY_TIMEWINDOW;
            endTimeWindow = TimeWindow.INFINITY_TIMEWINDOW;
            
            final Double serviceTime = 0.25;

            final OrderStation source = new OrderStation(sourceStation, startTimeWindow, serviceTime);
            final OrderStation destination = new OrderStation(destinationStation, endTimeWindow, serviceTime);

            final Integer amount = 1;

            final Order order = new Order(i, name, source, destination, product, amount);
            orders.add(order);
        }
        return new Configuration(orders, new HashSet<>(stations), new HashSet<>(vehicles), new HashSet<>(products), new HashSet<ComplexOrder>());
    }

    public List<Station> generateStations(final int minX, final int maxX, final int minY, final int maxY, final int num) {
        final List<Station> stations = new ArrayList<>(num);

        for (int i = 1; i <= num; i++) {
            final int x = this.randomUtils.nextInt(minX, maxX + 1);
            final int y = this.randomUtils.nextInt(minY, maxY + 1);

            final Vector2D coordinates = new Vector2D(x, y);
            final Station station = new Station(i, "Station #" + i, coordinates);
            stations.add(station);
        }

        return stations;
    }
}
