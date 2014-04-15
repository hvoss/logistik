package de.hsbremen.kss.util;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;

/**
 * some random utils.
 * 
 * @author henrik
 * 
 */
public final class RandomUtils {

    /**
     * static class.
     */
    private RandomUtils() {

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
    public static <T> T randomElement(final Collection<T> elements) {
        final int size = elements.size();
        final int idx = org.apache.commons.lang3.RandomUtils.nextInt(0, size);

        return CollectionUtils.get(elements, idx);
    }
}
