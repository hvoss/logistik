package de.hsbremen.kss.configuration;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

/**
 * represents a capacity of an {@link Vehicle}.
 * 
 * a capacity can hold either a product or an product group.
 * 
 * @author henrik
 * 
 */
public final class Capacity {

    /** the product. */
    private final Set<Product> products;

    /** the maximum capacity weight. */
    private final Integer capacityWeight;

    /**
     * indicates whether different products can be tranported in the same
     * capacity.
     */
    private final Boolean miscible;

    /** vehicle the capacity belongs to. */
    private final Vehicle vehicle;

    /**
     * ctor for a product.
     * 
     * @param vehicle
     *            vehicle the capacity belongs to
     * @param capacityWeight
     *            the actual capacity
     * @param miscible
     *            indicates whether different products can be tranported in the
     *            same capacity.
     */
    Capacity(final Vehicle vehicle, final Integer capacityWeight, final Boolean miscible) {
        Validate.notNull(capacityWeight, "capacity is null");
        Validate.notNull(vehicle, "vehicle is null");
        Validate.notNull(miscible, "miscible is null");

        this.capacityWeight = capacityWeight;
        this.vehicle = vehicle;
        this.miscible = miscible;

        this.products = new HashSet<>();
    }

    /**
     * Gets the actual capacity.
     * 
     * @return the actual capacity
     */
    public Integer getCapacityWeight() {
        return this.capacityWeight;
    }

    /**
     * Gets the vehicle the capacity belongs to.
     * 
     * @return the vehicle the capacity belongs to
     */
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    /**
     * verifies that the product belongs to the capacity.
     * 
     * @param checkProduct
     *            product to check.
     * @return true: the product belongs to the capacity; false: otherwise
     */
    public boolean contains(final Product checkProduct) {
        return this.products.contains(checkProduct);
    }

    /**
     * adds a new product
     * 
     * @param product
     *            product to add
     */
    public void addProduct(final Product product) {
        Validate.notNull(product, "product is null");
        if (!this.products.add(product)) {
            throw new IllegalArgumentException("product " + product + " already added");
        }
    }

    /**
     * Capacity weight.
     * 
     * @return the integer
     */
    public Integer capacityWeight() {
        return this.capacityWeight;
    }

    @Override
    public String toString() {
        return "Capacity[Products: " + this.products + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.capacityWeight == null) ? 0 : this.capacityWeight.hashCode());
        result = prime * result + ((this.miscible == null) ? 0 : this.miscible.hashCode());
        result = prime * result + ((this.products == null) ? 0 : this.products.hashCode());
        result = prime * result + ((this.vehicle == null) ? 0 : this.vehicle.hashCode());
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
        final Capacity other = (Capacity) obj;
        if (this.capacityWeight == null) {
            if (other.capacityWeight != null) {
                return false;
            }
        } else if (!this.capacityWeight.equals(other.capacityWeight)) {
            return false;
        }
        if (this.miscible == null) {
            if (other.miscible != null) {
                return false;
            }
        } else if (!this.miscible.equals(other.miscible)) {
            return false;
        }
        if (this.products == null) {
            if (other.products != null) {
                return false;
            }
        } else if (!this.products.equals(other.products)) {
            return false;
        }
        if (this.vehicle == null) {
            if (other.vehicle != null) {
                return false;
            }
        } else if (!this.vehicle.equals(other.vehicle)) {
            return false;
        }
        return true;
    }

}
