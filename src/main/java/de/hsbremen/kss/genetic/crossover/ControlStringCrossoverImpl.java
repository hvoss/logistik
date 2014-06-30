package de.hsbremen.kss.genetic.crossover;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.construction.NearestNeighbor;
import de.hsbremen.kss.model.Action;
import de.hsbremen.kss.model.OrderAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public final class ControlStringCrossoverImpl implements Crossover {

    private final RandomUtils randomUtils;

    public ControlStringCrossoverImpl(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    @Override
    public Plan crossover(final Plan firstPlan, final Plan secondPlan) {
        final Plan newPlan = new Plan(NearestNeighbor.class);
        Tour newTour;
        final Collection<Order> ordersToIgnore = new ArrayList<Order>();

        final List<Tour> parent1Tours = firstPlan.getTours();
        final List<Tour> parent2Tours = secondPlan.getTours();

        int parentSize;
        List<Tour> firstCrossoverPartner, secondCrossoverPartner;

        if (parent1Tours.size() > parent2Tours.size()) {
            parentSize = parent2Tours.size();
            firstCrossoverPartner = parent2Tours;
            secondCrossoverPartner = parent1Tours;
        } else {
            parentSize = parent1Tours.size();
            firstCrossoverPartner = parent1Tours;
            secondCrossoverPartner = parent2Tours;
        }
        for (int i = 0; i < parentSize; i++) {
            newTour = new Tour(firstCrossoverPartner.get(i).getVehicle());
            newTour.leafSourceDepot();
            ordersToIgnore.addAll(newTour.getOrders());
            crossover(newTour, firstCrossoverPartner.get(i), secondCrossoverPartner.get(i), ordersToIgnore);
            newTour.gotoDestinationDepot();
            newPlan.addTour(newTour);
        }

        newPlan.lock();

        return newPlan;
    }

    private Tour crossover(final Tour newTour, final Tour firstTour, final Tour secondTour, final Collection<Order> orderToIgnore) {
        final List<OrderAction> firstActions = new ArrayList<>(firstTour.getOrderActions());
        final List<OrderAction> secondActions = new ArrayList<>(secondTour.getOrderActions());

        while (!firstActions.isEmpty() || !secondActions.isEmpty()) {

            Action action;

            if (firstActions.isEmpty()) {
                action = secondActions.remove(0);
            } else if (secondActions.isEmpty()) {
                action = firstActions.remove(0);
            } else if (this.randomUtils.randomBoolean()) {
                action = firstActions.remove(0);
                secondActions.remove(action);
            } else {
                action = secondActions.remove(0);
                firstActions.remove(action);
            }

            if (action instanceof OrderAction) {
                final OrderAction orderAction = (OrderAction) action;
                if (orderToIgnore.contains(orderAction.getOrder())) {
                    continue;
                }
            }

            newTour.addOtherAction(action);
        }

        return newTour;
    }
}
