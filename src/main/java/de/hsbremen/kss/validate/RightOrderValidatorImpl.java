package de.hsbremen.kss.validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.model.Action;
import de.hsbremen.kss.model.FromDepotAction;
import de.hsbremen.kss.model.OrderLoadAction;
import de.hsbremen.kss.model.OrderUnloadAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.ToDepotAction;
import de.hsbremen.kss.model.Tour;

public class RightOrderValidatorImpl implements Validator {

    private static Logger LOG = LoggerFactory.getLogger(RightOrderValidatorImpl.class);

    private boolean enableLogging;

    @Override
    public boolean validate(final Configuration configuration, final Plan plan) {
        if (plan.getValid() != null) {
            return plan.getValid();
        }

        try {
            final Set<Order> notVisitedSourceOrders = new HashSet<>(configuration.getOrders());
            final Set<Order> notVisitedDestinationOrders = new HashSet<>(configuration.getOrders());

            final Set<Order> visitedSourceOrders = new HashSet<>();
            final Set<Order> visitedDestinationOrders = new HashSet<>();

            boolean allRight = true;

            for (final Tour tour : plan.getTours()) {
                final List<Action> actions = tour.getActions();

                for (int i = 0; i < actions.size(); i++) {
                    final Action action = actions.get(i);

                    if (action instanceof FromDepotAction) {
                        final FromDepotAction fromDepotAction = (FromDepotAction) action;
                        if (i > 0) {
                            log(plan, "Tour #" + tour.getId() + ": " + fromDepotAction + " is not the first action, it is action no: " + i);
                            allRight = false;
                        }
                    }

                    if (action instanceof ToDepotAction) {
                        final ToDepotAction toDepotAction = (ToDepotAction) action;
                        if (i - 1 == actions.size()) {
                            log(plan, "Tour #" + tour.getId() + ": " + toDepotAction + " is not the last action, it is action no: " + i);
                            allRight = false;
                        }
                    }

                    if (action instanceof OrderLoadAction) {
                        final OrderLoadAction orderLoadAction = (OrderLoadAction) action;
                        if (!visitedSourceOrders.add(orderLoadAction.getOrder())) {
                            log(plan, "Tour #" + tour.getId() + ": " + orderLoadAction + " performed multiple times");
                            allRight = false;
                        }

                        if (!orderLoadAction.getOrder().getProduct().equals(tour.getVehicle().getProduct())) {
                            log(plan, "Tour #" + tour.getId() + ": " + orderLoadAction + " wrong Product!");
                            allRight = false;
                        }

                    }

                    if (action instanceof OrderUnloadAction) {
                        final OrderUnloadAction orderUnloadAction = (OrderUnloadAction) action;

                        if (!visitedSourceOrders.contains(orderUnloadAction.getOrder())) {
                            log(plan,
                                    "Tour #" + tour.getId() + ": " + orderUnloadAction + " performed before " + OrderLoadAction.class.getSimpleName());
                            allRight = false;
                        }

                        if (!visitedDestinationOrders.add(orderUnloadAction.getOrder())) {
                            log(plan, "Tour #" + tour.getId() + ": " + orderUnloadAction + " performed multiple times");
                            allRight = false;
                        }

                    }
                }
            }

            notVisitedSourceOrders.removeAll(visitedSourceOrders);
            notVisitedDestinationOrders.removeAll(visitedDestinationOrders);

            for (final Order order : notVisitedSourceOrders) {
                log(plan, "source order \"" + order + "\" not visited");
                allRight = false;
            }

            for (final Order order : notVisitedDestinationOrders) {
                final Tour tour = plan.associatedTour(order);
                log(plan, "destination order \"" + order + "\" not visited on tour #" + tour.getId());
                allRight = false;
            }

            plan.setValid(allRight);

            return allRight;
        } catch (final Exception ex) {
            if (this.enableLogging) {
                RightOrderValidatorImpl.LOG.error("", ex);
            }
            return false;
        }
    }

    private void log(final Plan plan, final String text) {
        if (this.enableLogging && !plan.maybeInvalid()) {
            RightOrderValidatorImpl.LOG.warn(text);
        }
    }

    @Override
    public void enableLogging(final boolean enable) {
        this.enableLogging = enable;
    }

}
