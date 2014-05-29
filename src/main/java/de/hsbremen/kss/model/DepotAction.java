package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.TimeWindow;

/**
 * 
 * @author henrik
 * 
 */
abstract class DepotAction implements Action {

    /** the depot where the action is performed */
    private final Station depot;

    private final TimeWindow timewindow;

    /**
     * ctor.
     * 
     * @param depot
     *            the depot where the action is performed
     */
    DepotAction(final Station depot, final TimeWindow timewindow) {
        this.depot = depot;
        this.timewindow = timewindow;
    }

    @Override
    public double duration() {
        return 0;
    }

    @Override
    public final Station getStation() {
        return this.depot;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " : " + this.depot;
    }

    @Override
    public TimeWindow timewindow() {
        return this.timewindow;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((depot == null) ? 0 : depot.hashCode());
		result = prime * result
				+ ((timewindow == null) ? 0 : timewindow.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DepotAction other = (DepotAction) obj;
		if (depot == null) {
			if (other.depot != null)
				return false;
		} else if (!depot.equals(other.depot))
			return false;
		if (timewindow == null) {
			if (other.timewindow != null)
				return false;
		} else if (!timewindow.equals(other.timewindow))
			return false;
		return true;
	}
}
