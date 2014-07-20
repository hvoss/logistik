package de.hsbremen.kss.validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Action;
import de.hsbremen.kss.model.FromDepotAction;
import de.hsbremen.kss.model.OrderLoadAction;
import de.hsbremen.kss.model.OrderUnloadAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.ToDepotAction;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.TimeUtils;

/**
 * A simple validator.
 * 
 * checks only the source stations.
 * 
 * @author henrik
 * 
 */
public final class FullValidator implements Validator {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(FullValidator.class);
    private boolean enableLogging = true;

    @Override
    public boolean validate(final Configuration configuration, final Plan plan) {
        try {
            final Set<Order> notVisitedSourceOrders = new HashSet<>(configuration.getOrders());
            final Set<Order> notVisitedDestinationOrders = new HashSet<>(configuration.getOrders());

            final Set<Order> visitedSourceOrders = new HashSet<>();
            final Set<Order> visitedDestinationOrders = new HashSet<>();

            boolean allRight = true;

            for (final Tour tour : plan.getTours()) {
                final Vehicle vehicle = tour.getVehicle();
                double time = vehicle.getTimeWindow().getStart();
                final List<Action> actions = tour.getActions();
                int amount = 0;

                final Action firstAction = tour.firstAction();
                final Action lastAction = tour.lastAction();

                if (!(firstAction instanceof FromDepotAction)) {
                    log("Tour #" + tour.getId() + ": " + firstAction.getClass().getSimpleName() + " is the first action, but it should be a "
                            + FromDepotAction.class.getSimpleName() + " action");
                    allRight = false;
                }

                if (!(lastAction instanceof ToDepotAction)) {
                    log("Tour #" + tour.getId() + ": " + lastAction.getClass().getSimpleName() + " is the last action, but it should be a "
                            + ToDepotAction.class.getSimpleName() + " action");
                    allRight = false;
                }

                if (tour.freeTime() < 0) {
                    final double duration = Precision.round(tour.actualDuration(), 2);
                    final double maxTimespan = Precision.round(vehicle.getTimeWindow().timespan(), 2);
                    log("Tour #" + tour.getId() + " took " + duration + " hours. But it must take a maximum of " + maxTimespan + " hours");
                    allRight = false;
                }

                Station lastStation = vehicle.getSourceDepot();

                for (int i = 0; i < actions.size(); i++) {
                    final Action action = actions.get(i);
                    final Station station = action.getStation();

                    time += vehicle.calculateTavelingTime(lastStation, station);

                    if (!action.timewindow().between(time)) {
                        log("Tour #" + tour.getId() + ": action " + action + " not performed in time window: " + action.timewindow() + " ; time: "
                                + TimeUtils.convertToClockString(time));
                    }
                    time += action.duration();

                    if (action instanceof FromDepotAction) {
                        final FromDepotAction fromDepotAction = (FromDepotAction) action;
                        if (i > 0) {
                            log("Tour #" + tour.getId() + ": " + fromDepotAction + " is not the first action, it is action no: " + i);
                            allRight = false;
                        }

                    }

                    if (action instanceof ToDepotAction) {
                        final ToDepotAction toDepotAction = (ToDepotAction) action;
                        if (i - 1 == actions.size()) {
                            log("Tour #" + tour.getId() + ": " + toDepotAction + " is not the last action, it is action no: " + i);
                            allRight = false;
                        }

                    }

                    if (action instanceof OrderLoadAction) {
                        final OrderLoadAction orderLoadAction = (OrderLoadAction) action;
                        final Order order = orderLoadAction.getOrder();
                        if (!visitedSourceOrders.add(orderLoadAction.getOrder())) {
                            log("Tour #" + tour.getId() + ": " + orderLoadAction + " performed multiple times");
                            allRight = false;
                        }

                        if (!vehicle.canTransport(order)) {
                            log("Tour #" + tour.getId() + ": vehicle " + vehicle + " can't transport " + order.getProduct()
                                    + ". It can transport only " + vehicle.getProduct() + "!");
                            allRight = false;
                        }

                        amount += orderLoadAction.getOrder().getAmount();
                        if (amount > vehicle.getCapacity()) {
                            log("Tour #" + tour.getId() + ": " + "vehicle " + vehicle + " overloaded (amount: " + amount + ", max: "
                                    + vehicle.getCapacity() + ") on " + orderLoadAction);
                            allRight = false;
                        }

                    }

                    if (action instanceof OrderUnloadAction) {
                        final OrderUnloadAction orderUnloadAction = (OrderUnloadAction) action;

                        amount -= orderUnloadAction.getOrder().getAmount();

                        if (!visitedSourceOrders.contains(orderUnloadAction.getOrder())) {
                            log("Tour #" + tour.getId() + ": " + orderUnloadAction + " performed before " + OrderLoadAction.class.getSimpleName());
                            allRight = false;
                        }

                        if (!visitedDestinationOrders.add(orderUnloadAction.getOrder())) {
                            log("Tour #" + tour.getId() + ": " + orderUnloadAction + " performed multiple times");
                            allRight = false;
                        }

                    }

                    lastStation = station;
                }
            }

            notVisitedSourceOrders.removeAll(visitedSourceOrders);
            notVisitedDestinationOrders.removeAll(visitedDestinationOrders);

            for (final Order order : notVisitedSourceOrders) {
                log("source order \"" + order + "\" not visited");
                allRight = false;
            }

            for (final Order order : notVisitedDestinationOrders) {
                final Tour tour = plan.associatedTour(order);
                log("destination order \"" + order + "\" not visited on tour #" + tour.getId());
                allRight = false;
            }

            return allRight;
        } catch (final Exception ex) {
            if (this.enableLogging) {
                FullValidator.LOG.error("", ex);
            }
            return false;
        }
    }

    private void log(final String text) {
        if (this.enableLogging) {
            FullValidator.LOG.warn(text);
        }
    }

    @Override
    public void enableLogging(final boolean enable) {
        this.enableLogging = enable;
    }
}
