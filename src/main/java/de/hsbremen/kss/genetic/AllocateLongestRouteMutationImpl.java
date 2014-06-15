package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.construction.SweepConstruction;
import de.hsbremen.kss.model.OrderAction;
import de.hsbremen.kss.model.OrderLoadAction;
import de.hsbremen.kss.model.OrderUnloadAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public class AllocateLongestRouteMutationImpl implements Mutation {

	private final RandomUtils randomUtils;

	public AllocateLongestRouteMutationImpl(final RandomUtils randomUtils) {
		this.randomUtils = randomUtils;
	}

	@Override
	public Plan mutate(final Plan plan) {
		final Plan newPlan = new Plan(SweepConstruction.class);
		
		final List<Tour> tours = new ArrayList<>(plan.getTours());
		Tour tourToRemove = tours.get(0);

		for (Tour tour : tours) {
			if (tourToRemove.length() < tour.length()) {
				tourToRemove = tour;
			}
		}

		final List<OrderAction> allocatedActions = tourToRemove
				.getOrderActions();
		List<OrderAction> newTourActions;
		tours.remove(tourToRemove);

		Tour tourToManipulate = tours.get(0);
		newTourActions = tourToManipulate.getOrderActions();
		Tour newTour = newPlan.newTour(tourToManipulate.getVehicle());
		tours.remove(tourToManipulate);
		newTour.leafSourceDepot();
		//newTour.addOtherActions(newTourActions);

		while (!allocatedActions.isEmpty()) {
			Order orderToMove = allocatedActions.get(0).getOrder();

			this.randomUtils.insertAtRandomPosition(newTourActions,
					new OrderLoadAction(orderToMove));
			this.randomUtils.insertAtRandomPosition(newTourActions,
					new OrderUnloadAction(orderToMove));

			allocatedActions.remove(0);

		}
		
		newTour.addOtherActions(newTourActions);
		newTour.gotoDestinationDepot();
		tours.add(newTour);

		for (Tour tour : tours) {
			newPlan.addTour(tour);
		}

		return newPlan;
	}

}
