package de.hsbremen.kss.validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Action;
import de.hsbremen.kss.model.FromDepotAction;
import de.hsbremen.kss.model.OrderLoadAction;
import de.hsbremen.kss.model.OrderUnloadAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.ToDepotAction;
import de.hsbremen.kss.model.Tour;

/**
 * A simple validator.
 * 
 * checks only the source stations.
 * 
 * @author henrik
 * 
 */
public final class SimpleValidator implements Validator {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(SimpleValidator.class);

    @Override
    public boolean validate(final Configuration configuration, final Plan plan) {
        final Set<Order> notVisitedSourceOrders = new HashSet<>(configuration.getOrders());
        final Set<Order> notVisitedDestinationOrders = new HashSet<>(configuration.getOrders());

        final Set<Order> visitedSourceOrders = new HashSet<>();
        final Set<Order> visitedDestinationOrders = new HashSet<>();

        boolean allRight = true;

        for (final Tour tour : plan.getTours()) {
            final Vehicle vehicle = tour.getVehicle();
            final List<Action> actions = tour.getActions();
            int weight = 0;

            for (int i = 0; i < actions.size(); i++) {
                final Action action = actions.get(i);
                if (action instanceof FromDepotAction) {
                    final FromDepotAction fromDepotAction = (FromDepotAction) action;
                    if (i > 0) {
                        SimpleValidator.LOG.warn(fromDepotAction + " is not the first action, it is action no: " + i);
                        allRight = false;
                    }

                } else if (action instanceof ToDepotAction) {
                    final ToDepotAction toDepotAction = (ToDepotAction) action;
                    if (i - 1 == actions.size()) {
                        SimpleValidator.LOG.warn(toDepotAction + " is not the last action, it is action no: " + i);
                        allRight = false;
                    }

                } else if (action instanceof OrderLoadAction) {
                    final OrderLoadAction orderLoadAction = (OrderLoadAction) action;
                    if (!visitedSourceOrders.add(orderLoadAction.getOrder())) {
                        SimpleValidator.LOG.warn(orderLoadAction + " performed multiple times");
                        allRight = false;
                    }

                    weight += orderLoadAction.getOrder().weightOfProducts();
                    if (weight > vehicle.maxCapacityWeight()) {
                        SimpleValidator.LOG.warn("vehicle " + vehicle + " overloaded (weight: " + weight + ", max: " + vehicle.maxCapacityWeight()
                                + ") on " + orderLoadAction);
                        allRight = false;
                    }

                } else if (action instanceof OrderUnloadAction) {
                    final OrderUnloadAction orderUnloadAction = (OrderUnloadAction) action;

                    weight -= orderUnloadAction.getOrder().weightOfProducts();

                    if (!visitedSourceOrders.contains(orderUnloadAction.getOrder())) {
                        SimpleValidator.LOG.warn(orderUnloadAction + " performed before " + OrderLoadAction.class.getSimpleName());
                        allRight = false;
                    }

                    if (!visitedDestinationOrders.add(orderUnloadAction.getOrder())) {
                        SimpleValidator.LOG.warn(orderUnloadAction + " performed multiple times");
                        allRight = false;
                    }

                } else {
                    throw new IllegalStateException("unknown action type: " + action.getClass());
                }
            }
        }

        notVisitedSourceOrders.removeAll(visitedSourceOrders);
        notVisitedDestinationOrders.removeAll(visitedDestinationOrders);

        for (final Order order : notVisitedSourceOrders) {
            SimpleValidator.LOG.warn("source order \"" + order + "\" not visited");
            allRight = false;
        }

        for (final Order order : notVisitedDestinationOrders) {
            SimpleValidator.LOG.warn("destination order \"" + order + "\" not visited");
            allRight = false;
        }

        return allRight;
    }
}
