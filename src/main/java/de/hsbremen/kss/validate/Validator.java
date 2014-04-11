package de.hsbremen.kss.validate;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

public interface Validator {

	boolean validate(Configuration configuration, Plan plan);
}
