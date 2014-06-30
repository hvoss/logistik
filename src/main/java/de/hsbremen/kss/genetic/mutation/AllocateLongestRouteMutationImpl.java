package de.hsbremen.kss.genetic.mutation;

import java.util.Collection;

import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public final class AllocateLongestRouteMutationImpl extends AllocateRouteMutationImpl {

    public AllocateLongestRouteMutationImpl(final RandomUtils randomUtils) {
        super(randomUtils);
    }

    @Override
    protected Tour chooseTourToAllocate(final Collection<Tour> tours) {
        return Tour.findLongestTour(tours);
    }

}
