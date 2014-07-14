package de.hsbremen.kss.genetic.crossover;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Product;
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
    public Plan crossover(final Configuration configuration, final Plan firstPlan, final Plan secondPlan) {
        final Plan newPlan = new Plan(ControlStringCrossoverImpl.class, firstPlan, secondPlan);

        final Map<Product, List<Tour>> firstToursByProduct = firstPlan.toursByProduct();
        final Map<Product, List<Tour>> secondToursByProduct = secondPlan.toursByProduct();
        final Collection<Order> ordersToIgnore = new HashSet<Order>();

        for (final Product product : configuration.getProducts()) {
            final List<Tour> firstTours = firstToursByProduct.get(product);
            final List<Tour> secondTours = secondToursByProduct.get(product);

            while (!firstTours.isEmpty() || !secondTours.isEmpty()) {
                final Tour firstTour = this.randomUtils.removeRandomElement(firstTours);
                final Tour secondTour = this.randomUtils.removeRandomElement(secondTours);
                Tour primaryTour;
                Tour secondaryTour;
                Tour newTour;

                if (firstTour == null) {
                    primaryTour = secondTour;
                    secondaryTour = firstTour;
                } else if (secondTour == null) {
                    primaryTour = firstTour;
                    secondaryTour = secondTour;
                } else if (this.randomUtils.randomBoolean()) {
                    primaryTour = firstTour;
                    secondaryTour = secondTour;
                } else {
                    primaryTour = secondTour;
                    secondaryTour = firstTour;
                }

                newTour = new Tour(primaryTour.getVehicle());
                newTour.leafSourceDepot();
                crossover(newTour, primaryTour, secondaryTour, ordersToIgnore);
                ordersToIgnore.addAll(newTour.getOrders());
                newTour.gotoDestinationDepot();

                if (!newTour.getOrders().isEmpty()) {
                    newPlan.addTour(newTour);
                }
            }
        }

        newPlan.lock();

        return newPlan;
    }

    private Tour crossover(final Tour newTour, final Tour firstTour, final Tour secondTour, final Collection<Order> orderToIgnore) {
        final List<OrderAction> firstActions;
        final List<OrderAction> secondActions;

        if (firstTour != null) {
            firstActions = new ArrayList<>(firstTour.getOrderActions());
        } else {
            firstActions = Collections.emptyList();
        }

        if (secondTour != null) {
            secondActions = new ArrayList<>(secondTour.getOrderActions());
        } else {
            secondActions = Collections.emptyList();
        }

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
