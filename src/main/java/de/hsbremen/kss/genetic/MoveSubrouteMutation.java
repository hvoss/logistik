package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.construction.SweepConstruction;
import de.hsbremen.kss.model.OrderAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public class MoveSubrouteMutation implements Mutation {

    private final RandomUtils randomUtils;

    public MoveSubrouteMutation(final RandomUtils randomUtils) {
        super();
        this.randomUtils = randomUtils;
    }

    @Override
    public Plan mutate(final Plan plan) {
        final Plan mutatedPlan = new Plan(SweepConstruction.class);

        final List<Tour> tours = new ArrayList<>(plan.getTours());
        if (tours.isEmpty()) {
            return plan;
        }

        final Tour tourToMutate = this.randomUtils.removeRandomElement(tours);

        final List<OrderAction> orderActions = tourToMutate.getOrderActions();

        final List<OrderAction> subRoute = this.randomUtils.removeRandomSublist(orderActions);
        this.randomUtils.insertAtRandomPosition(orderActions, subRoute);

        final Tour mutatedTour = mutatedPlan.newTour(tourToMutate.getVehicle());
        mutatedTour.leafSourceDepot();
        mutatedTour.addOtherActions(orderActions);
        mutatedTour.gotoDestinationDepot();
        mutatedPlan.addTour(mutatedTour);

        for (final Tour tour : tours) {
            mutatedPlan.addTour(tour);
        }

        mutatedPlan.lock();

        return mutatedPlan;
    }
}
