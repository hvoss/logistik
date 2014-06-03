package de.hsbremen.kss.events;

import de.hsbremen.kss.model.Plan;

public class NewPlanEvent {

    public Plan plan;

    public NewPlanEvent(final Plan plan) {
        this.plan = plan;
    }
}
