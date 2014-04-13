package de.hsbremen.kss.configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

public final class ProductGroup {

    /** the id */
    private final Integer id;

    /** the name */
    private final String name;

    /** Products belonging to this product group */
    private final Set<Product> products;

    /**
     * indicates whether different products can be tranported in the same
     * capacity
     */
    private final Boolean miscible;

    /** vehicles which can transport this product group */
    private final Set<Vehicle> vehicles;

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

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Set<Product> getProducts() {
        return Collections.unmodifiableSet(this.products);
    }

    public Set<Vehicle> getVehicles() {
        return Collections.unmodifiableSet(this.vehicles);
    }

    public boolean contains(final Product product) {
        return this.vehicles.contains(product);
    }

    void addProduct(final Product product) {
        Validate.notNull(product);
        this.products.add(product);
    }

    void addVehicle(final Vehicle vehicle) {
        Validate.notNull(vehicle);
        this.vehicles.add(vehicle);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
