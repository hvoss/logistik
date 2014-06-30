package de.hsbremen.kss.genetic.mutation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Product;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.OrderAction;
import de.hsbremen.kss.model.OrderLoadAction;
import de.hsbremen.kss.model.OrderUnloadAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public abstract class AllocateRouteMutationImpl implements Mutation {

    private final RandomUtils randomUtils;

    public AllocateRouteMutationImpl(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    @Override
    public Plan mutate(final Configuration configuration, final Plan plan) {
        if (plan.getTours().size() >= 2) {
            final Plan newPlan = new Plan(AllocateRouteMutationImpl.class);

            final List<Tour> tours = new ArrayList<>(plan.getTours());
            final Tour tourToRemove = chooseTourToAllocate(tours);
            tours.remove(tourToRemove);

            final List<Order> allocatedOrder = new ArrayList<>(tourToRemove.getOrders());

            final Product product = tourToRemove.getVehicle().getProduct();
            final List<Pair<Vehicle, List<OrderAction>>> copiedTours = new ArrayList<>();
            for (final Tour oldTour : tours) {
                if (oldTour.getVehicle().getProduct().equals(product)) {
                    copiedTours.add(Pair.of(oldTour.getVehicle(), oldTour.getOrderActions()));
                } else {
                    newPlan.addTour(oldTour);
                }
            }

            while (!allocatedOrder.isEmpty()) {
                final Order orderToMove = this.randomUtils.removeRandomElement(allocatedOrder);
                final List<OrderAction> tour = this.randomUtils.randomElement(copiedTours).getRight();

                final int sourceIndex = this.randomUtils.insertAtRandomPosition(tour, new OrderLoadAction(orderToMove));
                this.randomUtils.insertAfterAtRandomPosition(tour, new OrderUnloadAction(orderToMove), sourceIndex + 1);
            }

            for (final Pair<Vehicle, List<OrderAction>> ptour : copiedTours) {
                final Tour newTour = new Tour(ptour.getLeft());
                newTour.leafSourceDepot();
                newTour.addOtherActions(ptour.getRight());
                newTour.gotoDestinationDepot();
                newPlan.addTour(newTour);
            }

            newPlan.lock();
            return newPlan;
        }
        return plan;
    }

    protected abstract Tour chooseTourToAllocate(Collection<Tour> tours);

}
