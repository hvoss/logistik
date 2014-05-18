package de.hsbremen.kss.construction;

import java.util.Set;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.simpleconstruction.SimpleConstruction;

/**
 * The Class MultipleRadialConstruction.
 */
public final class MultipleSweepConstruction implements Construction {

    /** The radial construction. */
    private final SweepConstruction sweepConstruction;

    /**
     * ctor.
     * 
     * @param simpleConstruction
     *            construction methods to find simple routes
     */
    public MultipleSweepConstruction(final SimpleConstruction simpleConstruction) {
        this.sweepConstruction = new SweepConstruction(simpleConstruction);
    }

    @Override
    public Plan constructPlan(final Configuration configuration) {
        Plan bestPlan = null;
        final Set<Station> allSourceStations = Order.getAllSourceStations(configuration.getOrders());

        for (final Station station : allSourceStations) {
            Plan plan;
            this.sweepConstruction.setStartStation(station);
            this.sweepConstruction.setForward(true);
            plan = this.sweepConstruction.constructPlan(configuration);
            if (bestPlan == null || plan.length() < bestPlan.length()) {
                bestPlan = plan;
            }

            this.sweepConstruction.setStartStation(station);
            this.sweepConstruction.setForward(false);
            plan = this.sweepConstruction.constructPlan(configuration);
            if (bestPlan == null || plan.length() < bestPlan.length()) {
                bestPlan = plan;
            }
        }

        return new Plan(MultipleSweepConstruction.class, bestPlan);
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

}
