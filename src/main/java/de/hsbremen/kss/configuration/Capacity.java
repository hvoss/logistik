package de.hsbremen.kss.configuration;

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
    private final Product product;

    /** the product group. */
    private final ProductGroup productGroup;

    /** the actual capacity. */
    private final Integer capacity;

    /** vehicle the capacity belongs to. */
    private final Vehicle vehicle;

    /**
     * ctor for a product.
     * 
     * @param product
     *            product the capacity belongs to
     * @param vehicle
     *            vehicle the capacity belongs to
     * @param capacity
     *            the actual capacity
     */
    Capacity(final Product product, final Vehicle vehicle, final Integer capacity) {
        this(product, null, vehicle, capacity);
    }

    /**
     * ctor for a product group.
     * 
     * @param productGroup
     *            product group the capacity belongs to
     * @param vehicle
     *            vehicle the capacity belongs to
     * @param capacity
     *            the actual capacity
     */
    Capacity(final ProductGroup productGroup, final Vehicle vehicle, final Integer capacity) {
        this(null, productGroup, vehicle, capacity);
    }

    /**
     * private ctor.
     * 
     * @param product
     *            product the capacity belongs to
     * @param productGroup
     *            product group the capacity belongs to
     * @param vehicle
     *            vehicle the capacity belongs to
     * @param capacity
     *            the actual capacity
     */
    private Capacity(final Product product, final ProductGroup productGroup, final Vehicle vehicle, final Integer capacity) {
        Validate.notNull(capacity, "capacity is null");
        Validate.notNull(vehicle);
        Validate.isTrue(product != null || productGroup != null, "product and product group is null");

        this.product = product;
        this.productGroup = productGroup;
        this.capacity = capacity;
        this.vehicle = vehicle;
    }

    /**
     * Gets the product.
     * 
     * @return the product
     */
    public Product getProduct() {
        return this.product;
    }

    /**
     * Gets the product group.
     * 
     * @return the product group
     */
    public ProductGroup getProductGroup() {
        return this.productGroup;
    }

    /**
     * Gets the actual capacity.
     * 
     * @return the actual capacity
     */
    public Integer getCapacity() {
        return this.capacity;
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
        if (checkProduct == null) {
            return false;
        }

        if (checkProduct.equals(this.product)) {
            return true;
        }

        if (this.productGroup != null) {
            return this.productGroup.contains(checkProduct);
        }

        return false;
    }

    @Override
    public String toString() {
        if (this.product != null) {
            return "Capacity[Product: " + this.product.getName() + "]";
        } else {
            return "Capacity[ProductGroup: " + this.productGroup.getName() + "]";
        }
    }
}
