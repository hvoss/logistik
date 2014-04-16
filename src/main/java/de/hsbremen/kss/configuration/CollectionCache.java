package de.hsbremen.kss.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Holds a collection in cache.
 * 
 * @author henrik
 * 
 * @param <C>
 *            type of the collection
 * @param <T>
 *            type of the elements
 */
final class CollectionCache<C extends Collection<T>, T> {

    /** the cached collection */
    private C collection;

    /** indicates whether the cached collection is valid or not. */
    private boolean valid;

    /**
     * ctor.
     */
    CollectionCache() {
        this.valid = false;
    }

    /**
     * clears the cache.
     */
    void clearCache() {
        this.collection = null;
        this.valid = false;
    }

    /**
     * indicates whether the cached collection is valid or not.
     * 
     * @return true: cached collection is valid. ; false: otherwise
     */
    boolean isValid() {
        return this.valid;
    }

    /**
     * sets the collection.
     * 
     * @param collection
     *            the collections.
     */
    void setCollection(final C collection) {
        this.collection = CollectionCache.unmodifiableCollection(collection);
        this.valid = true;
    }

    /**
     * returns the collection.
     * 
     * @return the collection
     */
    C getCollection() {
        return this.collection;
    }

    /**
     * creates a unmodifiable collection of the given collection.
     * 
     * @param collection
     *            collection to make unmodifiable.
     * @param <C>
     *            type of the collection
     * @param <T>
     *            type of the containing elements
     * @return unmodifiable collection of the given collection
     */
    @SuppressWarnings("unchecked")
    private static <C extends Collection<T>, T> C unmodifiableCollection(final C collection) {
        if (collection instanceof Set) {
            return (C) Collections.unmodifiableSet((Set<T>) collection);
        } else if (collection instanceof List) {
            return (C) Collections.unmodifiableList((List<T>) collection);
        } else {
            throw new NotImplementedException("the class " + collection.getClass().getSimpleName() + " can not be wrapped.");
        }
    }

}
