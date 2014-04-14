package de.hsbremen.kss.construction;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

public class RadialConstruction implements Construction {

    private static final Logger LOG = LoggerFactory.getLogger(RadialConstruction.class);

    @Override
    public Plan constructPlan(final Configuration configuration) {
        final Plan plan = new Plan(RadialConstruction.class);

        final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
        final Tour tour = new Tour(vehicle);
        final Station sourceDepot = vehicle.getSourceDepot();

        final TreeMap<Double, Order> sortedOrders = new TreeMap<>();

        for (final Order order : configuration.getOrders()) {
            final Station station = order.getSource();
            final double angle = sourceDepot.angle(station);
            sortedOrders.put(Double.valueOf(angle), order);
        }

        RadialConstruction.LOG.debug(sortedOrders.toString());

        for (final Map.Entry<Double, Order> order : sortedOrders.entrySet()) {
            tour.addOrderAndStation(order.getValue(), order.getValue().getSource());
        }

        plan.addTour(tour);

        return plan;
    }

}
