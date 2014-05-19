package de.hsbremen.kss.configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.hsbremen.kss.util.RandomUtils;

public class ConfigurationGenerator {

    private final RandomUtils randomUtils;

    public ConfigurationGenerator(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    public Configuration generateConfiguration(final Collection<Station> stations, final Collection<Product> products,
            final Collection<Vehicle> vehicles, final int numberOfOrder) {
        final Set<Order> orders = new HashSet<>();
        final Set<Station> stationCopy = Station.copy(stations);

        for (int i = 0; i < numberOfOrder; i++) {
            final Station sourceStation = this.randomUtils.randomElement(stationCopy);
            final Station destinationStation = this.randomUtils.randomElement(stationCopy, sourceStation);
            final Product product = this.randomUtils.randomElement(products);
            final String name = sourceStation.getName() + " => " + destinationStation.getName();

            final TimeWindow timeWindow = new TimeWindow(8d, 10d);
            final Double serviceTime = 0.25;

            final OrderStation source = new OrderStation(sourceStation, timeWindow, serviceTime);
            final OrderStation destination = new OrderStation(destinationStation, timeWindow, serviceTime);

            final Integer amount = 50;

            final Order order = new Order(i, name, source, destination, product, amount);
            orders.add(order);
        }
        return new Configuration(orders, stationCopy, new HashSet<>(vehicles), new HashSet<>(products), new HashSet<ComplexOrder>());
    }
}
