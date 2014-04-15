package de.hsbremen.kss.configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

/**
 * The Class Vehicle.
 */
public final class Vehicle {

    /** the id. */
    private final Integer id;

    /** the name. */
    private final String name;

    /** the source depot (station). */
    private final Station sourceDepot;

    /** the destination depot (station). */
    private final Station destinationDepot;

    /** the capacities. */
    private final Set<Capacity> capacities;

    /**
     * Instantiates a new vehicle.
     * 
     * @param id
     *            the id
     * @param name
     *            the name
     * @param sourceDepot
     *            the source depot
     * @param destinationDepot
     *            the destination depot
     */
    Vehicle(final Integer id, final String name, final Station sourceDepot, final Station destinationDepot) {
        Validate.notNull(id, "id is null");
        Validate.notNull(name, "name is null");
        Validate.notNull(sourceDepot, "sourceDepot is null");
        Validate.notNull(destinationDepot, "destinationDepot is null");

        this.id = id;
        this.name = name;
        this.sourceDepot = sourceDepot;
        this.destinationDepot = destinationDepot;
        this.capacities = new HashSet<>();
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the source depot (station).
     * 
     * @return the source depot (station)
     */
    public Station getSourceDepot() {
        return this.sourceDepot;
    }

    /**
     * Gets the destination depot (station).
     * 
     * @return the destination depot (station)
     */
    public Station getDestinationDepot() {
        return this.destinationDepot;
    }

    /**
     * Gets the capacities.
     * 
     * @return the capacities
     */
    public Set<Capacity> getCapacities() {
        return Collections.unmodifiableSet(this.capacities);
    }

    /**
     * verifies that the product can be transported by this vehicle.
     * 
     * @param checkProduct
     *            product to check.
     * @return true: product can be transported; false: otherwise
     */
    public boolean canBeTransported(final Product checkProduct) {
        for (final Capacity capacity : this.capacities) {
            if (capacity.contains(checkProduct)) {
                return true;
            }
        }

        return false;
    }

    /**
     * adds a capacity
     * 
     * @param capacity
     *            capacity to add
     */
    public void addCapacity(final Capacity capacity) {
        Validate.notNull(capacity);
        if (!this.capacities.add(capacity)) {
            throw new IllegalStateException("vehicle " + this.name + " already contain the given capacity: " + capacity);
        }
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vehicle other = (Vehicle) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
