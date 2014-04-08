package de.hsbremen.kss.common.exception;

public class DuplicateIdException extends RuntimeException {

	/** serial version uid */
	private static final long serialVersionUID = 4079626942568305909L;

	public DuplicateIdException(Class<?> clazz, Object firstOccurrence, Object secondOccurrence) {
		super("found "+clazz.getSimpleName()+" element with the same id. firstOccurrence: " + firstOccurrence + " ; secondOccurrence" + secondOccurrence);
	}
}
