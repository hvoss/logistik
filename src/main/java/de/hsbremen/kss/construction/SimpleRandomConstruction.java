package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.FastMath;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Product;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.ConstructionUtils;
import de.hsbremen.kss.util.RandomUtils;

public class SimpleRandomConstruction implements Construction {

    private final RandomUtils randomUtils;

    private final ConstructionUtils constructionUtils;

    public SimpleRandomConstruction(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
        this.constructionUtils = new ConstructionUtils(randomUtils);
    }

    @Override
    public Plan constructPlan(final Configuration configuration) {

        final List<Vehicle> vehicles = new ArrayList<>(configuration.getVehicles());
        final List<Order> orders = new ArrayList<>(configuration.getOrders());

        final Plan plan = new Plan(SimpleRandomConstruction.class);

        for (final Product product : configuration.getProducts()) {
            final List<Order> filtersOrders = new ArrayList<>(Order.filterOrdersByProductType(orders, product));
            final List<Vehicle> filtersVehicles = new ArrayList<>(Vehicle.filterByProduct(vehicles, product));

            final int ordersVehciles = (int) FastMath.ceil((double) filtersOrders.size() / (double) filtersVehicles.size());

            while (!filtersVehicles.isEmpty() && !filtersOrders.isEmpty()) {
                final Vehicle vehicle = this.randomUtils.removeRandomElement(filtersVehicles);

                final int num = FastMath.min(ordersVehciles, filtersOrders.size());
                if (num > 0) {
                    final List<Order> tourOrders = this.randomUtils.removeRandomElements(filtersOrders, num);
                    final Tour tour = this.constructionUtils.createTourFromOrders(vehicle, tourOrders);
                    plan.addTour(tour);
                }
            }
        }
        plan.lock();
        return plan;
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

}
