package de.hsbremen.kss.configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

/**
 * The Class ProductGroup.
 */
public final class ProductGroup {

    /** the id. */
    private final Integer id;

    /** the name. */
    private final String name;

    /** Products belonging to this product group. */
    private final Set<Product> products;

    /**
     * indicates whether different products can be tranported in the same
     * capacity.
     */
    private final Boolean miscible;

    /** vehicles which can transport this product group. */
    private final Set<Vehicle> vehicles;

    /**
     * Instantiates a new product group.
     * 
     * @param id
     *            the id
     * @param name
     *            the name
     * @param miscible
     *            the miscible
     */
    ProductGroup(final Integer id, final String name, final Boolean miscible) {
        Validate.notNull(id, "id is null");
        Validate.notNull(name, "name is null");
        Validate.notNull(miscible, "miscible is null");

        this.id = id;
        this.name = name;
        this.miscible = miscible;
        this.products = new HashSet<>();
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
     * Gets the products belonging to this product group.
     * 
     * @return the products belonging to this product group
     */
    public Set<Product> getProducts() {
        return Collections.unmodifiableSet(this.products);
    }

    /**
     * Gets the vehicles which can transport this product group.
     * 
     * @return the vehicles which can transport this product group
     */
    public Set<Vehicle> getVehicles() {
        return Collections.unmodifiableSet(this.vehicles);
    }

    /**
     * Gets the indicates whether different products can be tranported in the
     * same capacity.
     * 
     * @return the indicates whether different products can be tranported in the
     *         same capacity
     */
    public Boolean getMiscible() {
        return this.miscible;
    }

    /**
     * returns true if the product group contains the given product.
     * 
     * @param product
     *            product to check
     * @return true: the product group contains the product; false: otherwise
     */
    public boolean contains(final Product product) {
        return this.vehicles.contains(product);
    }

    /**
     * adds a product to this product group
     * 
     * @param product
     *            product to add
     */
    void addProduct(final Product product) {
        Validate.notNull(product);
        this.products.add(product);
    }

    /**
     * adds a vehicle which can transport this product group.
     * 
     * @param vehicle
     *            vehicle which can transport this product group.
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
