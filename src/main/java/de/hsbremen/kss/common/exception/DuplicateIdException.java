package de.hsbremen.kss.common.exception;

/**
 * The Class DuplicateIdException.
 */
public class DuplicateIdException extends RuntimeException {

    /** serial version uid. */
    private static final long serialVersionUID = 4079626942568305909L;

    /**
     * Instantiates a new duplicate id exception.
     * 
     * @param clazz
     *            the clazz
     * @param firstOccurrence
     *            the first occurrence
     * @param secondOccurrence
     *            the second occurrence
     */
    public DuplicateIdException(final Class<?> clazz, final Object firstOccurrence, final Object secondOccurrence) {
        super("found " + clazz.getSimpleName() + " element with the same id. firstOccurrence: " + firstOccurrence + " ; secondOccurrence"
                + secondOccurrence);
    }
}
