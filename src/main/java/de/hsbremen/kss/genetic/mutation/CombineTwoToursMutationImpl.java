package de.hsbremen.kss.genetic.mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Product;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.ConstructionUtils;
import de.hsbremen.kss.util.RandomUtils;

public final class CombineTwoToursMutationImpl implements Mutation {

    private final RandomUtils randomUtils;

    public CombineTwoToursMutationImpl(final RandomUtils randomUtils) {
        super();
        this.randomUtils = randomUtils;
    }

    @Override
    public Plan mutate(final Configuration configuration, final Plan plan) {
        final Map<Product, List<Tour>> toursByProduct = plan.toursByProduct();

        final Map<Product, List<Tour>> filteredTours = ConstructionUtils.filter(toursByProduct, 2);

        if (!filteredTours.isEmpty()) {
            final List<Tour> tours = filteredTours.get(this.randomUtils.randomElement(filteredTours.keySet()));
            final Plan newPlan = new Plan(CombineTwoToursMutationImpl.class);

            final Tour firstTour = this.randomUtils.removeRandomElement(tours);
            final Tour secondTour = this.randomUtils.removeRandomElement(tours);

            final List<Tour> allTours = new ArrayList<>(plan.getTours());
            allTours.remove(firstTour);
            allTours.remove(secondTour);

            newPlan.addTours(allTours);

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
