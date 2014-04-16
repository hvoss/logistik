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

    /** products wrapped by a {@link Collections#unmodifiableSet(Set)} */
    private final Set<Product> umProducts;

    /**
     * indicates whether different products can be tranported in the same
     * capacity.
     */
    private final Boolean miscible;

    /** vehicles which can transport this product group. */
    private final Set<Vehicle> vehicles;

    /** vehicles wrapped by a {@link Collections#unmodifiableSet(Set)} */
    private final Set<Vehicle> unVehicles;

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

        this.umProducts = Collections.unmodifiableSet(this.products);
        this.unVehicles = Collections.unmodifiableSet(this.vehicles);
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
        return this.umProducts;
    }

    /**
     * Gets the vehicles which can transport this product group.
     * 
     * @return the vehicles which can transport this product group
     */
    public Set<Vehicle> getVehicles() {
        return this.unVehicles;
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
        if (!this.products.add(product)) {
            throw new IllegalStateException("product group " + this.name + " already contain the given product: " + product);
        }
    }

    /**
     * adds a vehicle which can transport this product group.
     * 
     * @param vehicle
     *            vehicle which can transport this product group.
     */
    void addVehicle(final Vehicle vehicle) {
        Validate.notNull(vehicle);
        if (!this.vehicles.add(vehicle)) {
            throw new IllegalStateException("product group " + this.name + " already contain the given vehicle: " + vehicle);
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
        final ProductGroup other = (ProductGroup) obj;
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
