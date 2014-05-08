package de.hsbremen.kss.construction;

import java.util.Set;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.model.Plan;

public class MultipleSavingsConstruction implements Construction{
	
	private final SavingsContruction savingsConstruction = new SavingsContruction();

	@Override
	public Plan constructPlan(Configuration configuration) {
		Plan bestPlan = null;
        final Set<Station> allSourceStations = Order.getAllSourceStations(configuration.getOrders());

        for (final Station station : allSourceStations) {
            Plan plan;
            plan = this.savingsConstruction.constructPlan(configuration, station);
            if (bestPlan == null || plan.length() < bestPlan.length()) {
                bestPlan = plan;
            }

        }

        return new Plan(MultipleSavingsConstruction.class, bestPlan);
	}

	@Override
	public void logStatistic() {
		// TODO Auto-generated method stub
		
	}

}
