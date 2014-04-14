package de.hsbremen.kss.configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

/**
 * The Class Product.
 */
public final class Product {

    /** the id. */
    private final Integer id;

    /** the name. */
    private final String name;

    /** vehicles which can transport this product. */
    private final Set<Vehicle> vehicles;

    /** Product groups to which the product belongs. */
    private final Set<ProductGroup> productGroups;

    /**
     * Instantiates a new product.
     * 
     * @param id
     *            the id
     * @param name
     *            the name
     */
    Product(final Integer id, final String name) {
        this.id = id;
        this.name = name;
        this.productGroups = new HashSet<>();
        this.vehicles = new HashSet<>();
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
     * Gets the product groups to which the product belongs.
     * 
     * @return the product groups to which the product belongs
     */
    public Set<ProductGroup> getProductGroups() {
        return Collections.unmodifiableSet(this.productGroups);
    }

    /**
     * Gets the vehicles which can transport this product.
     * 
     * @return the vehicles which can transport this product
     */
    public Set<Vehicle> getVehicles() {
        return Collections.unmodifiableSet(this.vehicles);
    }

    /**
     * adds a product group to which the product belongs.
     * 
     * @param productGroup
     *            product group to which the product belongs
     */
    void addProductGroup(final ProductGroup productGroup) {
        Validate.notNull(productGroup);
        this.productGroups.add(productGroup);
    }

    /**
     * adds vehicle which can transport this product.
     * 
     * @param vehicle
     *            vehicle which can transport this product
     */
    void addVehicle(final Vehicle vehicle) {
        Validate.notNull(vehicle);
        this.vehicles.add(vehicle);
    }

    @Override
    public String toString() {
        return this.name;
    }

}
