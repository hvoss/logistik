package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.SavingStation;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.ConstructionUtils;

/**
 * Realizes the sequential Savings-Algorithm Has a problem, when the depot is in
 * one of the orders
 *
 * @author david
 *
 */
public class SavingsContruction implements Construction {

    @Override
    public Plan constructPlan(final Configuration configuration) {
        return constructPlan(configuration, null);
    }

    public Plan constructPlan(final Configuration configuration, final Station startStation) {
        final Plan plan = new Plan(SavingsContruction.class);

        final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
        final Tour tour = new Tour(vehicle);
        final Station sourceDepot = vehicle.getSourceDepot();

        final Set<Order> visitedSourceOrders = new HashSet<>();
        final Set<Order> visitedDestinationOrders = new HashSet<>();

        Station actualStation;
        Station lastStation = null;

        if (startStation != null) {
            actualStation = startStation;
        } else {
            final List<SavingStation> savingsList = new ArrayList<>();
            final Set<Station> stations = Order.getAllSourceStations(configuration.getOrders());
            final Set<Station> processedStations = new HashSet<>();

            for (final Station station : stations) {
                processedStations.add(station);
                for (final Station otherStation : stations) {
                    if (!processedStations.contains(otherStation)) {
                        savingsList.add(new SavingStation(station, otherStation, sourceDepot));
                    }
                }
            }

            Collections.sort(savingsList);
            actualStation = savingsList.get(0).getSourceStation();
        }

        while (true) {
            final Set<Station> processableStations = ConstructionUtils.processableStations(configuration.getOrders(), visitedSourceOrders,
                    visitedDestinationOrders, tour);
            if (processableStations.isEmpty()) {
                break;
            }

            if (lastStation != null) {
                final List<SavingStation> savingsList = new ArrayList<>(processableStations.size());
                for (final Station station : processableStations) {
                    savingsList.add(new SavingStation(lastStation, station, sourceDepot));
                }

                Collections.sort(savingsList);
                actualStation = savingsList.get(0).getDestinationStation();
            }

            final Set<Order> newSourceOrders = new HashSet<>();
            final Set<Order> newDestinationOrders = new HashSet<>();
            newSourceOrders.removeAll(visitedSourceOrders);
            newDestinationOrders.removeAll(visitedDestinationOrders);

            // unload orders
            final Collection<Order> destinationStationReached = CollectionUtils.intersection(visitedSourceOrders, newDestinationOrders);
            visitedDestinationOrders.addAll(destinationStationReached);
            tour.addDestinationOrders(destinationStationReached);

            // Load new orders
            for (final Order newSourceOrder : newSourceOrders) {
                if (tour.freeAmount() >= newSourceOrder.getAmount()) {
                    tour.addSourceOrder(newSourceOrder);

                    // order where the source station reached
                    visitedSourceOrders.add(newSourceOrder);
                }
            }

            lastStation = actualStation;

        }

        plan.addTour(tour);

        plan.lock();
        return plan;
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

}
