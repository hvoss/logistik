package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Saving;
import de.hsbremen.kss.model.Tour;

/**
 * Realizes the sequential Savings-Algorithm Has a problem, when the depot is in
 * one of the orders
 * 
 * @author david
 * 
 */
public class SavingsContruction implements Construction {

    private static final Logger LOG = LoggerFactory.getLogger(SavingsContruction.class);

    @Override
    public Plan constructPlan(final Configuration configuration) {
        final Plan plan = new Plan(SavingsContruction.class);
        final List<Order> orderList = new ArrayList<Order>(configuration.getOrders());
        final List<Saving> savingList = new ArrayList<>();
        final List<Order> savingsOrderList = new ArrayList<>();
        final List<Order> processedOrders = new ArrayList<>();
        final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
        final Station depot = vehicle.getSourceDepot();
        final Tour tour = new Tour(vehicle);

        for (final Order sourceOrder : orderList) {
            processedOrders.add(sourceOrder);
            for (final Order destinationOrder : orderList) {
                if (!processedOrders.contains(destinationOrder)) {
                    savingList.add(new Saving(sourceOrder, destinationOrder, depot));
                }
            }
        }

        Collections.sort(savingList);

        SavingsContruction.LOG.info("Depot: " + depot.getName());

        for (final Saving saving : savingList) {
            SavingsContruction.LOG.info("Savings: " + saving.getSourceOrder().getSource().getName() + " => "
                    + saving.getDestinationOrder().getSource().getName() + ": " + saving.getSavingsValue());
        }

        SavingsContruction.LOG.info("Best Pair: " + savingList.get(0).getSourceOrder().getSource().getName() + " "
                + savingList.get(0).getDestinationOrder().getSource().getName());
        savingsOrderList.add(savingList.get(0).getSourceOrder());
        savingsOrderList.add(savingList.get(0).getDestinationOrder());
        savingList.remove(0);

        while (savingsOrderList.size() < orderList.size()) {
            final int indexNextPair = searchNextPair(savingList, savingsOrderList);
            SavingsContruction.LOG.info("Next Pair: " + savingList.get(indexNextPair).getSourceOrder().getSource().getName() + " "
                    + savingList.get(indexNextPair).getDestinationOrder().getSource().getName());

            if (!savingsOrderList.contains(savingList.get(indexNextPair).getSourceOrder())) {
                if (savingsOrderList.get(0).equals(savingList.get(indexNextPair).getDestinationOrder())) {
                    savingsOrderList.add(0, savingList.get(indexNextPair).getSourceOrder());
                } else {
                    savingsOrderList.add(savingList.get(indexNextPair).getSourceOrder());
                }
            } else {
                if (savingsOrderList.get(0).equals(savingList.get(indexNextPair).getSourceOrder())) {
                    savingsOrderList.add(0, savingList.get(indexNextPair).getDestinationOrder());
                } else {
                    savingsOrderList.add(savingList.get(indexNextPair).getDestinationOrder());
                }
            }
            savingList.remove(indexNextPair);
        }

        for (final Order order : savingsOrderList) {
            tour.addOrderAndStation(order, order.getSource());
        }

        plan.addTour(tour);

        return plan;
    }

    private int searchNextPair(final List<Saving> savingList, final List<Order> savingsOrderList) {
        int indexNextPair = -1;
        final Order firstElement = savingsOrderList.get(0);
        final Order lastElement = savingsOrderList.get(savingsOrderList.size() - 1);
        for (final Saving saving : savingList) {
            if (!(savingsOrderList.contains(saving.getSourceOrder()) && savingsOrderList.contains(saving.getDestinationOrder()))) {
                if (saving.getSourceOrder().equals(firstElement) || saving.getSourceOrder().equals(lastElement)
                        || saving.getDestinationOrder().equals(firstElement) || saving.getDestinationOrder().equals(lastElement)) {
                    indexNextPair = savingList.indexOf(saving);
                    break;
                }
            }
        }
        return indexNextPair;
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

}
