package de.hsbremen.kss.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

/**
 * some random utils.
 * 
 * @author henrik
 * 
 */
public final class RandomUtils {

    /** random */
    private final Random random;

    /**
     * ctor.
     * 
     * @param seed
     *            seed used for random
     */
    public RandomUtils(final long seed) {
        this.random = new Random(seed);
    }

    /**
     * returns a random element of a collection.
     * 
     * @param elements
     *            the collection of elements.
     * @param <T>
     *            type of the elements
     * @return a random element of the given collection
     */
    public <T> T randomElement(final Collection<T> elements) {
        final int size = elements.size();
        final int idx = nextInt(0, size);

        return CollectionUtils.get(elements, idx);
    }

    /**
     * Returns a random integer within the specified range.
     * 
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endExclusive
     *            the upper bound (not included), must be non-negative
     * @return the random integer
     */
    public int nextInt(final int startInclusive, final int endExclusive) {
        Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        Validate.isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return startInclusive + this.random.nextInt(endExclusive - startInclusive);
    }

    /**
     * shuffles a collection.
     * 
     * @param elements
     *            elements to shuffle.
     * @param <T>
     *            type of the elements
     * @return a shuffled list.
     */
    public <T> List<T> shuffle(final Collection<T> elements) {
        final List<T> shuffle = new ArrayList<>(elements);
        Collections.shuffle(shuffle, this.random);
        return shuffle;
    }
}
