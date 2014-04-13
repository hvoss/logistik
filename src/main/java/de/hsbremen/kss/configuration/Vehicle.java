package de.hsbremen.kss.configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

public final class Vehicle {

    /** the id */
    private final Integer id;

    /** the name */
    private final String name;

    /** the source depot (station) */
    private final Station sourceDepot;

    /** the destination depot (station) */
    private final Station destinationDepot;

    /** the capacities */
    private final Set<Capacity> capacities;

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

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Station getSourceDepot() {
        return this.sourceDepot;
    }

    public Station getDestinationDepot() {
        return this.destinationDepot;
    }

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

    public void addCapacity(final Capacity capacity) {
        Validate.notNull(capacity);
        this.capacities.add(capacity);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
