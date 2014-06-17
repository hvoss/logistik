package de.hsbremen.kss.construction;

import java.util.Set;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.model.Plan;

public class MultipleSavingsTourConstruction implements Construction {

    private final SavingsTourConstruction savingsTourConstruction = new SavingsTourConstruction();

    @Override
    public Plan constructPlan(final Configuration configuration) {
        Plan bestPlan = null;
        final Set<Order> orders = configuration.getOrders();

        for (final Order order : orders) {
            final Plan plan = this.savingsTourConstruction.constructPlan(configuration, order);

            if (bestPlan == null || plan.length() < bestPlan.length()) {
                bestPlan = plan;
            }
        }

        final Plan plan = new Plan(MultipleSavingsTourConstruction.class, bestPlan);

        plan.lock();
        return plan;
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

}
