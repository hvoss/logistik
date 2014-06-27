package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.util.FastMath;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.OrderStation;
import de.hsbremen.kss.configuration.Product;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public class SimpleRandomConstruction implements Construction {

    private final RandomUtils randomUtils;

    public SimpleRandomConstruction(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    @Override
    public Plan constructPlan(final Configuration configuration) {

        final List<Vehicle> vehicles = new ArrayList<>(configuration.getVehicles());
        final List<Order> orders = new ArrayList<>(configuration.getOrders());

        final Plan plan = new Plan(SimpleRandomConstruction.class);

        
        for (Product product : configuration.getProducts()) {
        	List<Order> filtersOrders = new ArrayList<>(Order.filterOrdersByProductType(orders, product));
        	List<Vehicle> filtersVehicles = new ArrayList<>(Vehicle.filterByProduct(vehicles, product));
			
        final int ordersVehciles = (int) FastMath.ceil((double) filtersOrders.size() / (double) filtersVehicles.size());
        
        while (!filtersVehicles.isEmpty() && !filtersOrders.isEmpty()) {
            final ArrayList<OrderStation> stations = new ArrayList<>();
            final Vehicle vehicle = this.randomUtils.removeRandomElement(filtersVehicles);
            
            for (int i = 0; i < ordersVehciles && !filtersOrders.isEmpty(); i++) {
                final Order order = this.randomUtils.removeRandomElement(filtersOrders);
                stations.add(order.getSource());
            }

            if (!stations.isEmpty()) {
                final Tour tour =  new Tour(vehicle);
                tour.leafSourceDepot();
                while (!stations.isEmpty()) {
                    final OrderStation station = this.randomUtils.removeRandomElement(stations);
                    final Order order = station.getOrder();
                    if (station.isSource()) {
                        tour.addSourceOrder(order);
                        stations.add(order.getDestination());
                    } else if (station.isDestination()) {
                        tour.addDestinationOrder(order);
                    } else {
                        throw new IllegalStateException();
                    }
                }
                tour.gotoDestinationDepot();
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
