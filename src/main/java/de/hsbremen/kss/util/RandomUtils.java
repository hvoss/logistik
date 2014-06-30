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
public class RandomUtils {

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

    public RandomUtils() {
        this.random = new Random();
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
        if (size > 0) {
            final int idx = nextInt(0, size);

            return CollectionUtils.get(elements, idx);
        }

        return null;
    }

    /**
     * returns a random element of a collection.
     * 
     * @param elements
     *            the collection of elements.
     * @param <T>
     *            type of the elements
     * @param except
     *            element to except
     * @return a random element of the given collection
     */
    public <T> T randomElement(final Collection<T> elements, final T except) {
        T element;
        do {
            element = randomElement(elements);
        } while (element.equals(except));
        return element;
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
        Validate.isTrue(endExclusive >= startInclusive, "Start value (" + startInclusive + ") must be smaller or equal to end value (" + endExclusive
                + ").");

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

    /**
     * removes a random element from a list and returns it.
     * 
     * @param elements
     *            list of elements.
     * @return removed element.
     */
    public <T> T removeRandomElement(final List<T> elements) {
        final int idx = nextInt(0, elements.size());
        return elements.remove(idx);
    }

    /**
     * insert a element at a random position in the list.
     * 
     * @param elements
     *            list of elements.
     * @param elementToInsert
     *            element to insert
     */
    public <T> int insertAtRandomPosition(final List<T> elements, final T elementToInsert) {
        return insertAfterAtRandomPosition(elements, elementToInsert, -1);
    }

    public <T> T randomElementByLinearDistribution(final List<T> elements) {
        final int num = elements.size();
        final int max = sumN(num);
        final int randomInt = nextInt(0, max);

        int sum = 0;
        for (int i = 0; true; i++) {
            sum += num - i;
            if (randomInt < sum) {
                return elements.get(i);
            }
        }
    }

    private static int sumN(final int n) {
        return n * (n + 1) / 2;
    }

    public boolean randomBoolean(final double probability) {
        return this.random.nextDouble() < probability;
    }

    public boolean randomBoolean() {
        return this.random.nextBoolean();
    }

    public <T> List<T> randomSublist(final List<T> orderActions) {
        final int size = orderActions.size();
        final int start = nextInt(0, size);
        final int end = nextInt(start + 1, size + 1);
        return orderActions.subList(start, end);
    }

    public <T> List<T> removeRandomSublist(final List<T> orderActions) {
        final List<T> subList = randomSublist(orderActions);
        final ArrayList<T> copy = new ArrayList<>(subList);
        subList.clear();
        return copy;

    }

    public <T> void insertAtRandomPosition(final List<T> orderActions, final List<T> subRoute) {
        final int position = nextInt(0, orderActions.size());
        orderActions.addAll(position, subRoute);
    }

    public <T> List<T> removeRandomElements(final List<T> elements, final int num) {
        final List<T> removedElements = new ArrayList<>(num);

        for (int i = 0; i < num; i++) {
            final T removedElement = removeRandomElement(elements);
            removedElements.add(removedElement);
        }

        return removedElements;
    }

    /**
     * insert a element at a random position in the list.
     * 
     * @param elements
     *            list of elements.
     * @param elementToInsert
     *            element to insert
     */
    public <T> int insertAfterAtRandomPosition(final List<T> elements, final T elementToInsert, final int afterIdx) {
        final int idx = nextInt(afterIdx + 1, elements.size());

        elements.add(idx, elementToInsert);

        return idx;
    }

    public <T> int insertBeforeAtRandomPosition(final List<T> elements, final T elementToInsert, final int beforeIdx) {
        final int idx = nextInt(0, beforeIdx);

        elements.add(idx, elementToInsert);

        return idx;
    }

}
