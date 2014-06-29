package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.construction.SweepConstruction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public final class CombineTwoToursMutationImpl implements Mutation {

    private final RandomUtils randomUtils;

    public CombineTwoToursMutationImpl(final RandomUtils randomUtils) {
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

            final Tour newTour = new Tour(firstTour.getVehicle());

            newTour.leafSourceDepot();
            newTour.addOtherActions(firstTour.getOrderActions());
            newTour.addOtherActions(secondTour.getOrderActions());
            newTour.gotoDestinationDepot();

            newPlan.addTour(newTour);

            return newPlan;
        }

        return plan;
    }

}
