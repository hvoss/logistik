package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.List;

/**
 * interface that combines the {@link Cloneable} and the {@link Construction}
 * interface. a combining with generics is not possible because the
 * {@link Cloneable} interface don't define the clone-Method.
 * 
 * @author henrik
 * 
 */
public interface CloneableConstruction extends Cloneable, Construction {

    /**
     * creates a clone of a CloneableConstruction.
     * 
     * @return clone of a CloneableConstruction
     */
    CloneableConstruction clone();

    /**
     * some {@link CloneableConstruction}-Utils
     * 
     * @author henrik
     * 
     */
    public static final class Utils {

        /** static class */
        private Utils() {

        }

        /**
         * creates the given number of clones of the given construction object.
         * 
         * @param cloneableConstruction
         *            construction method to clone
         * @param numOfCopies
         *            number of clones
         * @return the cloned constructions
         */
        public static List<CloneableConstruction> createClones(final CloneableConstruction cloneableConstruction, final int numOfCopies) {
            final List<CloneableConstruction> copies = new ArrayList<>(numOfCopies);

            for (int i = 0; i < numOfCopies; i++) {
                final CloneableConstruction clone = cloneableConstruction.clone();
                copies.add(clone);
            }

            return copies;
        }
    }
}
