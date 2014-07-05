package de.hsbremen.kss.genetic.mutation;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.model.OrderAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public class MoveActionMutationImpl implements Mutation {

    private final RandomUtils randomUtils;

    public MoveActionMutationImpl(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    @Override
    public Plan mutate(final Configuration configuration, final Plan plan) {
        final Plan newPlan = new Plan(MoveActionMutationImpl.class, plan);

        final List<Tour> tours = new ArrayList<>(plan.getTours());
        final Tour tourToMutate = this.randomUtils.removeRandomElement(tours);

        final List<OrderAction> actions = tourToMutate.getOrderActions();

        if (actions.size() <= 2) {
            return plan;
        }

        final OrderAction actionToMove = this.randomUtils.removeRandomElement(actions);
        if (actionToMove.isSource()) {
            final int idx = indexOfDestination(actions, actionToMove.getOrder());
            if (idx >= 0) {
                this.randomUtils.insertBeforeAtRandomPosition(actions, actionToMove, idx);
            } else {
                throw new IllegalStateException();
            }
        } else if (actionToMove.isDestination()) {
            final int idx = indexOfSource(actions, actionToMove.getOrder());
            if (idx >= 0) {
                this.randomUtils.insertAfterAtRandomPosition(actions, actionToMove, idx);
            } else {
                throw new IllegalStateException();
            }
        } else {
            throw new IllegalStateException();
        }

        final Tour newTour = new Tour(tourToMutate.getVehicle());
        newTour.leafSourceDepot();
        newTour.addOtherActions(actions);
        newTour.gotoDestinationDepot();

        newPlan.addTour(newTour);

        newPlan.addTours(tours);

        newPlan.lock();

        return newPlan;
    }

    public static int indexOfDestination(final List<OrderAction> actions, final Order order) {
        for (int i = 0; i < actions.size(); i++) {
            final OrderAction action = actions.get(i);
            if (action.isDestination() && action.getOrder().equals(order)) {
                return i;
            }
        }

        return -1;
    }

    public static int indexOfSource(final List<OrderAction> actions, final Order order) {
        for (int i = 0; i < actions.size(); i++) {
            final OrderAction action = actions.get(i);
            if (action.isSource() && action.getOrder().equals(order)) {
                return i;
            }
        }

        return -1;
    }

}
