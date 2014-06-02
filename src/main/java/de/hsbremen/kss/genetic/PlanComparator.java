package de.hsbremen.kss.genetic;

import java.util.Comparator;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.validate.Validator;

public final class PlanComparator implements Comparator<Plan> {

    private final Validator validator;

    private final Configuration configuration;

    public PlanComparator(final Validator validator, final Configuration configuration) {
        super();
        this.validator = validator;
        this.configuration = configuration;
    }

    @Override
    public int compare(final Plan plan1, final Plan plan2) {
        final boolean validatePlan1 = this.validator.validate(this.configuration, plan1);
        final boolean validatePlan2 = this.validator.validate(this.configuration, plan1);

        if (!validatePlan1 && !validatePlan2) {
            return 0;
        } else if (!validatePlan1) {
            return 1;
        } else if (!validatePlan2) {
            return -1;
        } else if (plan1.length() < plan2.length()) {
            return -1;
        } else if (plan1.length() > plan2.length()) {
            return 1;
        } else {
            return 0;
        }
    }
}
