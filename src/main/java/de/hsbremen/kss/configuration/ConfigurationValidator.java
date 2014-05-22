package de.hsbremen.kss.configuration;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationValidator {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationValidator.class);

    public boolean validate(final Configuration configuration) {

        boolean valid = true;

        final Set<Order> orders = configuration.getOrders();
        final Set<Vehicle> vehicles = configuration.getVehicles();

        for (final Order order : orders) {
            boolean transportPossible = false;
            for (final Vehicle vehicle : vehicles) {
                final Station sourceDepot = vehicle.getSourceDepot();
                final Station sourceStation = order.getSourceStation();
                final Station destinationStation = order.getDestinationStation();
                final Station destinationDepot = vehicle.getDestinationDepot();

                double time = vehicle.getTimeWindow().getStart();

                if (!vehicle.getProduct().equals(order.getProduct())) {
                    continue;
                }

                time += vehicle.calculateTavelingTime(sourceDepot, sourceStation);
                if (time > order.getSource().getTimeWindow().getEnd()) {
                    continue;
                }

                if (time < order.getSource().getTimeWindow().getStart()) {
                    time = order.getSource().getTimeWindow().getStart();
                }

                time += vehicle.calculateTavelingTime(sourceStation, destinationStation);

                if (time > order.getDestination().getTimeWindow().getEnd()) {
                    continue;
                }

                if (time < order.getDestination().getTimeWindow().getStart()) {
                    time = order.getDestination().getTimeWindow().getStart();
                }

                time += vehicle.calculateTavelingTime(destinationStation, destinationDepot);

                if (time > vehicle.getTimeWindow().getEnd()) {
                    continue;
                }

                transportPossible = true;
                break;
            }
            if (!transportPossible) {
                ConfigurationValidator.LOG.warn("transport of order: " + order + " is not possible");
                valid = false;
            }
        }

        return valid;
    }
}
