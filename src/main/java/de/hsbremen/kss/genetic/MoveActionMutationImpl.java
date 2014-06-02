package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.construction.SweepConstruction;
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
    public Plan mutate(final Plan plan) {
        final Plan newPlan = new Plan(SweepConstruction.class);

        final List<Tour> tours = new ArrayList<>(plan.getTours());
        final Tour tourToMutate = this.randomUtils.removeRandomElement(tours);

        final List<OrderAction> actions = tourToMutate.getOrderActions();
        final OrderAction actionToMove = this.randomUtils.removeRandomElement(actions);
        this.randomUtils.insertAtRandomPosition(actions, actionToMove);

        final Tour newTour = newPlan.newTour(tourToMutate.getVehicle());

        newTour.addOtherActions(actions);

        newPlan.addTour(newTour);

        for (final Tour tour : tours) {
            newPlan.addTour(tour);
        }

        return newPlan;
    }

}
