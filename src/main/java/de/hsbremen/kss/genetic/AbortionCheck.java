package de.hsbremen.kss.genetic;

import java.util.List;

import de.hsbremen.kss.model.Plan;

public interface AbortionCheck {

    public boolean checkAbort(int iteration, List<Plan> population);
}
