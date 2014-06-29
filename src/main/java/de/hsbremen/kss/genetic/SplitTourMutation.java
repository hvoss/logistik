package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Product;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.construction.SweepConstruction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.ConstructionUtils;
import de.hsbremen.kss.util.RandomUtils;

public class SplitTourMutation implements Mutation {

    private final RandomUtils randomUtils;

    private final ConstructionUtils constructionUtils;

    public SplitTourMutation(final RandomUtils randomUtils) {
        super();
        this.randomUtils = randomUtils;
        this.constructionUtils = new ConstructionUtils(randomUtils);
    }

    @Override
    public Plan mutate(final Configuration configuration, final Plan plan) {
        final Set<Vehicle> unusedVehicles = Vehicle.filterByProducts(configuration.getVehicles(), configuration.usedProducts());
        unusedVehicles.removeAll(plan.usedVehicles());

        // Sind alle Fahrzeuge schon verplant?
        if (!unusedVehicles.isEmpty()) {
            final Set<Product> products = Vehicle.extractProducts(unusedVehicles);
            final List<Tour> tours = plan.filterToursByProducts(products);
            final Tour tourToSplit = this.randomUtils.randomElement(tours);
            final List<Order> ordersFirstTour = new ArrayList<>(tourToSplit.getOrders());
            final List<Order> ordersSecondTour = new ArrayList<>();
            if (ordersFirstTour.size() >= 2) {
                final int num = this.randomUtils.nextInt(1, ordersFirstTour.size());
                for (int i = 0; i < num; i++) {
                    ordersSecondTour.add(this.randomUtils.removeRandomElement(ordersFirstTour));
                }

                final Tour firstTour = this.constructionUtils.createTourFromOrders(tourToSplit.getVehicle(), ordersFirstTour);
                final Set<Vehicle> useableVehicles = Vehicle.filterByProduct(unusedVehicles, tourToSplit.getVehicle().getProduct());
                final Vehicle secondVehicle = this.randomUtils.randomElement(useableVehicles);
                final Tour secondTour = this.constructionUtils.createTourFromOrders(secondVehicle, ordersSecondTour);

                final List<Tour> otherTours = new ArrayList<>(plan.getTours());
                otherTours.remove(tourToSplit);

                final Plan newPlan = new Plan(SweepConstruction.class);

                newPlan.addTour(firstTour);
                newPlan.addTour(secondTour);
                newPlan.addTours(otherTours);
                newPlan.lock();

                return newPlan;
            }
        }

        return plan;
    }
}
