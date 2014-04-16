package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

public class NearestNeighbor implements Construction {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(NearestNeighbor.class);

    @Override
    public Plan constructPlan(final Configuration configuration) {
        final Plan plan = new Plan(NearestNeighbor.class);
        final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
        final List<Order> orders = new ArrayList<>(configuration.getOrders());
        final List<Station> stations = new ArrayList<>(configuration.getStations());

        Station startStation = vehicle.getSourceDepot();
        stations.remove(startStation);
        final Tour tour = new Tour(vehicle);

        while (!stations.isEmpty()) {
            // Order nearestOrder =
            // startStation.findNearestSourceStation(orders);
            Station nearestStation = startStation.findNearestStation(stations);
            final List<Station> tempStations = new ArrayList<>(stations);
            List<Order> tempOrders = new ArrayList<>(orders);

            for (final Order order : tempOrders) {
                if (order.getDestination() == nearestStation && !tour.getStations().contains(order.getSource())) {
                    tempStations.remove(order.getDestination());
                    nearestStation = startStation.findNearestStation(tempStations);
                    tempOrders = new ArrayList<>(orders);
                }
            }

            if (nearestStation != null) {
                tour.addStation(nearestStation);
                startStation = nearestStation;
                stations.remove(startStation);
                // orders.remove(nearestOrder);
            } else {
                break;
            }
        }

        tour.addOrders(configuration.getOrders());

        plan.addTour(tour);

        return plan;
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

}
