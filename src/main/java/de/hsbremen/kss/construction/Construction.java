package de.hsbremen.kss.construction;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

public interface Construction {

	Plan constructPlan(Configuration configuration);
}
