package de.hsbremen.kss.genetic;

import java.util.Collection;

import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public final class AllocateShortestRouteMutationImpl extends AllocateRouteMutationImpl {

    public AllocateShortestRouteMutationImpl(final RandomUtils randomUtils) {
        super(randomUtils);
    }

    @Override
    protected Tour chooseTourToAllocate(final Collection<Tour> tours) {
        return Tour.findShortestTour(tours);
    }

}
