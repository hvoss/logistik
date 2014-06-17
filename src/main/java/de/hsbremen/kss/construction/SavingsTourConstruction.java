package de.hsbremen.kss.construction;

import java.util.ArrayList;
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
import de.hsbremen.kss.model.Saving;
import de.hsbremen.kss.model.SavingOrder;
import de.hsbremen.kss.model.Tour;

/**
 * Realizes the Savings-Algorithm with orders (one element is an order instead
 * of a location)
 * 
 * @author david
 * 
 */
public class SavingsTourConstruction implements Construction {

    @Override
    public Plan constructPlan(final Configuration configuration) {
        return constructPlan(configuration, null);
    }

    public Plan constructPlan(final Configuration configuration, final Order startOrder) {
        final Plan plan = new Plan(SavingsTourConstruction.class);

        final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
        final Station depot = vehicle.getSourceDepot();
        final Tour tour = plan.newTour(vehicle);

        final Set<Order> configurationOrders = new HashSet<>(configuration.getOrders());
        final Set<Order> visitedOrders = new HashSet<>(configurationOrders.size());

        Order lastOrder = null;
        Order actualOrder;

        // define the actual order with savings over the two stations of an
        // order
        if (startOrder == null) {
            final List<SavingOrder> savingsOrderList = new ArrayList<SavingOrder>(configurationOrders.size());

            for (final Order order : configurationOrders) {
                savingsOrderList.add(new SavingOrder(order, depot));
            }
            Collections.sort(savingsOrderList);
            actualOrder = savingsOrderList.get(0).getOrder();
        } else {
            actualOrder = startOrder;
        }

        while (!configurationOrders.isEmpty()) {

            final List<Saving> savingsList = new ArrayList<>();

            // calculate savings value and define actual order
            if (lastOrder != null) {
                for (final Order order : configurationOrders) {
                    savingsList.add(new Saving(lastOrder, order, depot));
                }

                Collections.sort(savingsList);
                actualOrder = savingsList.get(0).getDestinationOrder();
            }

            final Station actualStation = actualOrder.getSourceStation();
            final Set<Order> newSourceOrders = new HashSet<>(actualStation.getSourceOrders());
            newSourceOrders.removeAll(visitedOrders);
            final Set<Order> loadedSourceOrders = new HashSet<>();

            // load new orders for the actual station
            for (final Order newSourceorder : newSourceOrders) {
                if (tour.freeAmount() >= newSourceorder.getAmount()) {
                    tour.addSourceOrder(newSourceorder);
                    loadedSourceOrders.add(newSourceorder);
                }
            }

            // unload orders
            for (final Order order : loadedSourceOrders) {
                tour.addDestinationOrder(order);
                lastOrder = order;
            }

            visitedOrders.addAll(loadedSourceOrders);
            configurationOrders.removeAll(loadedSourceOrders);
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
