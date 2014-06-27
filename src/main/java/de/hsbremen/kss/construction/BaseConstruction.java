package de.hsbremen.kss.construction;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.OrderStation;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.simpleconstruction.SimpleConstruction;

/**
 * a base construction implementation. follows the template pattern. the base
 * logic finds processable stations and orders. the specific implementation
 * chooses out of the stations and orders.
 * 
 * @author henrik
 * 
 */
public abstract class BaseConstruction implements Construction {

    /** construction methods to find simple routes */
    private final SimpleConstruction simpleConstruction;

    private static final Logger LOG = LoggerFactory.getLogger(BaseConstruction.class);

    /**
     * ctor.
     * 
     * @param simpleConstruction
     *            construction methods to find simple routes
     */
    public BaseConstruction(final SimpleConstruction simpleConstruction) {
        this.simpleConstruction = simpleConstruction;
    }

    @Override
    public final Plan constructPlan(final Configuration configuration) {
        final Plan plan = new Plan(getClass());

        final Set<Vehicle> vehicles = new HashSet<>(configuration.getVehicles());

        final Set<Order> availableOrders = new HashSet<>(configuration.getOrders());

        while (!availableOrders.isEmpty()) {
            final Vehicle vehicle = nextVehicle(vehicles);
            final Tour tour =  new Tour(vehicle);
            vehicles.remove(vehicle);
            tour.leafSourceDepot();

            while (true) {
                final Set<Station> processableStations = findPossibleNextStations(tour, availableOrders);

                if (processableStations.isEmpty()) {
                    if (tour.notDeliveredOrders().isEmpty()) {
                        break;
                    } else {
                        BaseConstruction.LOG.error("undeliverd orders:" + tour.notDeliveredOrders());
                        break;
                    }
                }
                final Station rElement = nextStation(tour, processableStations);

                // unload orders
                final Collection<Order> destinationStationReached = Order.filterOrdersByDestinationStation(tour.notDeliveredOrders(), rElement);
                tour.addDestinationOrders(destinationStationReached);
                for (final Order order : destinationStationReached) {
                    unloadOrder(order);
                }

                // Load new orders
                final Set<Order> availableSourceOrders = Order.filterOrdersBySourceStation(vehicle,availableOrders, rElement);
                final HashSet<Order> loadableOrders = new HashSet<>();

                for (final Order order : availableSourceOrders) {
                    if (orderIncludePossible(tour, order)) {
                        loadableOrders.add(order);
                    }
                }

                final List<Order> orderSequence = loadOrderSequence(loadableOrders);

                for (final Order order : orderSequence) {
                    if (orderIncludePossible(tour, order)) {
                        tour.addSourceOrder(order);

                        // orders on which the source station is reached
                        availableOrders.remove(order);
                    }
                }

            }

            tour.gotoDestinationDepot();

            if (tour.hasOrderActions()) {
                vehicles.remove(vehicle);
                plan.addTour(tour);
            } else {
                vehicles.add(vehicle);
            }
        }

        plan.lock();

        return plan;
    }

    /**
     * find possible next stations that can be approached without later
     * restriction violations.
     * 
     * @param tour
     *            tour which should be used
     * @param orders
     *            orders that should be handled
     * @return possible next stations that can be approached
     */
    private Set<Station> findPossibleNextStations(final Tour tour, final Collection<Order> orders) {
        final Station actualStation = tour.actualStation();
        final Station destinationDepot = tour.getVehicle().getDestinationDepot();
        final double freeLength = tour.freeLength();
        final Set<OrderStation> processableStations = OrderStation.getAllDestinationStations(tour.notDeliveredOrders());

        final Set<Station> possibleNextStations = this.simpleConstruction.findPossibleNextStationsWithTimewindows(actualStation, processableStations,
                destinationDepot, freeLength, tour.actualTime(), tour.getVehicle());

        for (final Order order : orders) {
            if (orderIncludePossible(tour, order)) {
                possibleNextStations.add(order.getSourceStation());
            }
        }

        return possibleNextStations;
    }

    /**
     * checks if it is possible to load the given order.
     * 
     * @param tour
     *            tour to which the order should be added
     * @param order
     *            order to add
     * @return true: it is possible to load the order.
     */
    private boolean orderIncludePossible(final Tour tour, final Order order) {
        if (tour.freeAmount() < order.getAmount()) {
            return false;
        }

        final Vehicle vehicle = tour.getVehicle();
        if (!vehicle.canTransport(order)) {
            return false;
        }

        final Station sourceStation = order.getSourceStation();
        final Set<OrderStation> stations = OrderStation.getAllDestinationStations(tour.notDeliveredOrders());

        stations.add(order.getDestination());
        double actualTime = tour.actualTime();

        final double orderLength = vehicle.getVelocity() * order.serviceTime();
        final double distanceToOrder = tour.actualStation().distance(sourceStation);
        final double freeLength = tour.freeLength() - orderLength - distanceToOrder;

        actualTime += vehicle.calculateTavelingTime(distanceToOrder);
        if (!order.getSource().getTimeWindow().between(actualTime)) {
            return false;
        }
        actualTime += order.getSource().getServiceTime();

        final Station destinationDepot = vehicle.getDestinationDepot();

        final Set<Station> possibleNextStations = this.simpleConstruction.findPossibleNextStationsWithTimewindows(sourceStation, stations,
                destinationDepot, freeLength, actualTime, tour.getVehicle());

        return !possibleNextStations.isEmpty();
    }

    /**
     * choose the next station.
     * 
     * @param tour
     *            tour so far
     * @param stations
     *            possible next stations.
     * @return the next station.
     */
    protected abstract Station nextStation(Tour tour, Collection<Station> stations);

    /**
     * choose the next vehicle.
     * 
     * @param vehicle
     *            possible vehicles.
     * @return the next vehicle
     */
    protected abstract Vehicle nextVehicle(Collection<Vehicle> vehicle);

    /**
     * define a loading sequence of the given orders.
     * 
     * @param orders
     *            orders to be sorted
     * @return loading sequence of the given orders.
     */
    protected abstract List<Order> loadOrderSequence(Collection<Order> orders);

    /**
     * is called when a order is unloaded
     * 
     * @param order
     *            order which is unloaded
     */
    protected void unloadOrder(final Order order) {
    }
}
