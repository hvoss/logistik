package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.construction.SweepConstruction;
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
    public Plan mutate(final Plan plan) {
        final Plan newPlan = new Plan(SweepConstruction.class);

        final List<Tour> tours = new ArrayList<>(plan.getTours());
        final Tour tourToRemove = chooseTourToAllocate(tours);
        tours.remove(tourToRemove);

        final List<List<OrderAction>> copiedTours = new ArrayList<>();
        for (final Tour oldTour : tours) {
            copiedTours.add(oldTour.getOrderActions());
        }

        final List<Order> allocatedOrder = new ArrayList<>(tourToRemove.getOrders());

        while (!allocatedOrder.isEmpty()) {
            final Order orderToMove = this.randomUtils.removeRandomElement(allocatedOrder);
            final List<OrderAction> tour = this.randomUtils.randomElement(copiedTours);

            final int sourceIndex = this.randomUtils.insertAtRandomPosition(tour, new OrderLoadAction(orderToMove));
            this.randomUtils.insertAtRandomPosition(tour, new OrderUnloadAction(orderToMove), sourceIndex);
        }

        for (int i = 0; i < tours.size(); i++) {
            final Tour newTour = newPlan.newTour(tours.get(i).getVehicle());
            newTour.leafSourceDepot();
            newTour.addOtherActions(copiedTours.get(i));
            newTour.gotoDestinationDepot();
            newPlan.addTour(newTour);
        }

        newPlan.lock();
        return newPlan;
    }

    protected abstract Tour chooseTourToAllocate(Collection<Tour> tours);

}
