package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

public class RandomConstruction implements Construction {

    @Override
    public Plan constructPlan(final Configuration configuration) {
        final Plan plan = new Plan(RandomConstruction.class);
        final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);

        final List<Order> orders = new ArrayList<>(configuration.getOrders());
        Collections.shuffle(orders); // TODO fix start value
        final Tour tour = new Tour(vehicle);

        for (final Order order : orders) {
            tour.addOrderAndStation(order, order.getSource());
        }

        plan.addTour(tour);

        return plan;
    }

}
