package de.hsbremen.kss.construction;

import java.util.Set;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.model.Plan;

/**
 * The Class MultipleRadialConstruction.
 */
public final class MultipleRadialConstruction implements Construction {

    /** The radial construction. */
    private final RadialConstruction radialConstruction = new RadialConstruction();

    @Override
    public Plan constructPlan(final Configuration configuration) {
        Plan bestPlan = null;
        final Set<Station> allSourceStations = Order.getAllSourceStations(configuration.getOrders());

        for (final Station station : allSourceStations) {
            Plan plan;
            plan = this.radialConstruction.constructPlan(configuration, station, true);
            if (bestPlan == null || plan.length() < bestPlan.length()) {
                bestPlan = plan;
            }

            plan = this.radialConstruction.constructPlan(configuration, station, false);
            if (bestPlan == null || plan.length() < bestPlan.length()) {
                bestPlan = plan;
            }
        }

        return new Plan(MultipleRadialConstruction.class, bestPlan);
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

}
