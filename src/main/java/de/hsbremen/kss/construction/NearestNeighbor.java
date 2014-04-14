package de.hsbremen.kss.construction;

import java.util.Collection;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.configuration.Station;

public class NearestNeighbor implements Construction {
	
	private Plan nearestNeighborPlan;
	
	/** a collection of all stations */
	private Collection<Station> stations;

	@Override
	public Plan constructPlan(Configuration configuration) {
		stations = configuration.getStations();
		return nearestNeighborPlan;
	}

}
