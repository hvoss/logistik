package de.hsbremen.kss.validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.util.Precision;
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
            int amount = 0;

            final Action firstAction = tour.firstAction();
            final Action lastAction = tour.lastAction();

            if (!(firstAction instanceof FromDepotAction)) {
                SimpleValidator.LOG.warn("Tour #" + tour.getId() + ": " + firstAction.getClass().getSimpleName()
                        + " is the first action, but it should be a " + FromDepotAction.class.getSimpleName() + " action");
                allRight = false;
            }

            if (!(lastAction instanceof ToDepotAction)) {
                SimpleValidator.LOG.warn("Tour #" + tour.getId() + ": " + lastAction.getClass().getSimpleName()
                        + " is the last action, but it should be a " + ToDepotAction.class.getSimpleName() + " action");
                allRight = false;
            }

            if (tour.freeTime() < 0) {
                final double duration = Precision.round(tour.actualDuration(), 2);
                final double maxTimespan = Precision.round(vehicle.getTimeWindow().timespan(), 2);
                SimpleValidator.LOG.warn("Tour #" + tour.getId() + " took " + duration + " hours. But it must take a maximum of " + maxTimespan
                        + " hours");
                allRight = false;
            }

            for (int i = 0; i < actions.size(); i++) {
                final Action action = actions.get(i);
                if (action instanceof FromDepotAction) {
                    final FromDepotAction fromDepotAction = (FromDepotAction) action;
                    if (i > 0) {
                        SimpleValidator.LOG
                                .warn("Tour #" + tour.getId() + ": " + fromDepotAction + " is not the first action, it is action no: " + i);
                        allRight = false;
                    }

                } else if (action instanceof ToDepotAction) {
                    final ToDepotAction toDepotAction = (ToDepotAction) action;
                    if (i - 1 == actions.size()) {
                        SimpleValidator.LOG.warn("Tour #" + tour.getId() + ": " + toDepotAction + " is not the last action, it is action no: " + i);
                        allRight = false;
                    }

                } else if (action instanceof OrderLoadAction) {
                    final OrderLoadAction orderLoadAction = (OrderLoadAction) action;
                    final Order order = orderLoadAction.getOrder();
                    if (!visitedSourceOrders.add(orderLoadAction.getOrder())) {
                        SimpleValidator.LOG.warn("Tour #" + tour.getId() + ": " + orderLoadAction + " performed multiple times");
                        allRight = false;
                    }

                    if (!vehicle.canTransport(order)) {
                        SimpleValidator.LOG.warn("Tour #" + tour.getId() + ": vehicle " + vehicle + " can't transport " + order.getProduct()
                                + ". It can transport only " + vehicle.getProduct() + "!");
                        allRight = false;
                    }

                    amount += orderLoadAction.getOrder().getAmount();
                    if (amount > vehicle.getCapacity()) {
                        SimpleValidator.LOG.warn("Tour #" + tour.getId() + ": " + "vehicle " + vehicle + " overloaded (amount: " + amount + ", max: "
                                + vehicle.getCapacity() + ") on " + orderLoadAction);
                        allRight = false;
                    }

                } else if (action instanceof OrderUnloadAction) {
                    final OrderUnloadAction orderUnloadAction = (OrderUnloadAction) action;

                    amount -= orderUnloadAction.getOrder().getAmount();

                    if (!visitedSourceOrders.contains(orderUnloadAction.getOrder())) {
                        SimpleValidator.LOG.warn("Tour #" + tour.getId() + ": " + orderUnloadAction + " performed before "
                                + OrderLoadAction.class.getSimpleName());
                        allRight = false;
                    }

                    if (!visitedDestinationOrders.add(orderUnloadAction.getOrder())) {
                        SimpleValidator.LOG.warn("Tour #" + tour.getId() + ": " + orderUnloadAction + " performed multiple times");
                        allRight = false;
                    }

                } else {
                    throw new IllegalStateException("Tour #" + tour.getId() + ": " + "unknown action type: " + action.getClass());
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
            final Tour tour = plan.associatedTour(order);
            SimpleValidator.LOG.warn("destination order \"" + order + "\" not visited on tour #" + tour.getId());
            allRight = false;
        }

        return allRight;
    }
}
