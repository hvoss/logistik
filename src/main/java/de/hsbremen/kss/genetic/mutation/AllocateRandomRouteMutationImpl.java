package de.hsbremen.kss.genetic.mutation;

import java.util.Collection;

import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

public final class AllocateRandomRouteMutationImpl extends AllocateRouteMutationImpl {

    private final RandomUtils randomUtils;

    public AllocateRandomRouteMutationImpl(final RandomUtils randomUtils) {
        super(randomUtils);
        this.randomUtils = randomUtils;
    }

    @Override
    protected Tour chooseTourToAllocate(final Collection<Tour> tours) {
        return this.randomUtils.randomElement(tours);
    }

}
