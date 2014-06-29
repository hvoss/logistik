package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.construction.SweepConstruction;
import de.hsbremen.kss.model.OrderAction;
import de.hsbremen.kss.model.OrderLoadAction;
import de.hsbremen.kss.model.OrderUnloadAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public class SwapOrderMutationImpl implements Mutation {

    private final RandomUtils randomUtils;

    public SwapOrderMutationImpl(final RandomUtils randomUtils) {
        super();
        this.randomUtils = randomUtils;
    }

    @Override
    public Plan mutate(final Configuration configuration, final Plan plan) {
        final List<Tour> tours = new ArrayList<>(plan.getTours());

        if (tours.size() >= 2) {
            final Plan newPlan = new Plan(SweepConstruction.class);

            final Tour firstTour = this.randomUtils.removeRandomElement(tours);
            final Tour secondTour = this.randomUtils.removeRandomElement(tours);

            newPlan.addTours(tours);

            final Order firstOrder = this.randomUtils.randomElement(firstTour.getOrders());
            final Order secondOrder = this.randomUtils.randomElement(secondTour.getOrders());

            createTour(firstTour, firstOrder, secondOrder, newPlan);
            createTour(secondTour, secondOrder, firstOrder, newPlan);

            newPlan.lock();

            return newPlan;
        }

        return plan;
    }

    private void createTour(final Tour oldTour, final Order firstOrder, final Order secondOrder, final Plan plan) {
        final Tour newTour = new Tour(oldTour.getVehicle());
        newTour.leafSourceDepot();
        for (final OrderAction action : oldTour.getOrderActions()) {
            if (action instanceof OrderLoadAction) {
                if (action.getOrder().equals(firstOrder)) {
                    newTour.addSourceOrder(secondOrder);
                } else {
                    newTour.addSourceOrder(action.getOrder());
                }
            } else if (action instanceof OrderUnloadAction) {
                if (action.getOrder().equals(firstOrder)) {
                    newTour.addDestinationOrder(secondOrder);
                } else {
                    newTour.addDestinationOrder(action.getOrder());
                }
            } else {
                throw new IllegalStateException();
            }
        }
        newTour.gotoDestinationDepot();
        plan.addTour(newTour);
    }
}
