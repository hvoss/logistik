package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.FastMath;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.OrderStation;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public class SimpleRandomConstruction implements Construction {

    private final RandomUtils randomUtils;

    public SimpleRandomConstruction(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    @Override
    public Plan constructPlan(final Configuration configuration) {

        final List<Vehicle> vehicles = new ArrayList<>(configuration.getVehicles());
        final List<Order> orders = new ArrayList<>(configuration.getOrders());

        final int ordersVehciles = (int) FastMath.ceil((double) orders.size() / (double) vehicles.size());

        final Plan plan = new Plan(SimpleRandomConstruction.class);

        while (!vehicles.isEmpty()) {
            final Vehicle vehicle = this.randomUtils.removeRandomElement(vehicles);
            final Tour tour = plan.newTour(vehicle);

            final ArrayList<OrderStation> stations = new ArrayList<>();
            for (int i = 0; i < ordersVehciles && !orders.isEmpty(); i++) {
                final Order order = this.randomUtils.removeRandomElement(orders);
                stations.add(order.getSource());
            }

            tour.leafSourceDepot();
            while (!stations.isEmpty()) {
                final OrderStation station = this.randomUtils.removeRandomElement(stations);
                final Order order = station.getOrder();
                if (station.isSource()) {
                    tour.addSourceOrder(order);
                    stations.add(order.getDestination());
                } else if (station.isDestination()) {
                    tour.addDestinationOrder(order);
                } else {
                    throw new IllegalStateException();
                }
            }
            tour.gotoDestinationDepot();
            plan.addTour(tour);
        }

        return plan;
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

}
