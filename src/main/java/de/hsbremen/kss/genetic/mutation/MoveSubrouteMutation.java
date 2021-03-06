package de.hsbremen.kss.genetic.mutation;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.configuration.Configuration;
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
    public Plan mutate(final Configuration configuration, final Plan plan) {
        final Plan mutatedPlan = new Plan(MoveSubrouteMutation.class, plan);

        final List<Tour> tours = new ArrayList<>(plan.getTours());
        if (tours.isEmpty()) {
            return plan;
        }

        final Tour tourToMutate = this.randomUtils.removeRandomElement(tours);

        final List<OrderAction> orderActions = tourToMutate.getOrderActions();

        if (orderActions.size() <= 2) {
            return plan;
        }

        final List<OrderAction> subRoute = this.randomUtils.removeRandomSublist(orderActions);
        this.randomUtils.insertAtRandomPosition(orderActions, subRoute);

        final Tour mutatedTour = new Tour(tourToMutate.getVehicle());
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
