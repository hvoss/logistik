package de.hsbremen.kss.timing;

import org.apache.commons.lang3.Validate;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.construction.Construction;
import de.hsbremen.kss.model.Plan;

/**
 * Time measuring for {@link Construction}.
 * 
 * @author henrik
 * 
 */
public final class ConstructionTimeMeasuring extends TimeMeasuring {

    private Plan plan;

    private final Construction construction;
    private final Configuration configuration;

    /**
     * ctor.
     * 
     * @param construction
     *            the construction implementation
     * @param configuration
     *            the given configuration
     */
    public ConstructionTimeMeasuring(final Construction construction, final Configuration configuration) {
        this.configuration = configuration;
        this.construction = construction;
    }

    @Override
    protected void run() {
        this.plan = this.construction.constructPlan(this.configuration);
        Validate.notNull(this.plan, "plan is null, construction class: " + this.construction.getClass().getSimpleName());
    }

    public Plan getPlan() {
        return this.plan;
    }

    public Construction getConstruction() {
        return this.construction;
    }
}
