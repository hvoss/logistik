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

    /** the weight */
    private final Integer weight;

    /** vehicles which can transport this product. */
    private final Set<Vehicle> vehicles;

    /** vehicles wrapped by a {@link Collections#unmodifiableSet(Set)} */
    private final Set<Vehicle> umVehicles;

    /** Product groups to which the product belongs. */
    private final Set<ProductGroup> productGroups;

    /** product groups wrapped by a {@link Collections#unmodifiableSet(Set)} */
    private final Set<ProductGroup> umProductGroups;

    /**
     * Instantiates a new product.
     * 
     * @param id
     *            the id
     * @param name
     *            the name
     * @param weight
     *            the weight
     */
    Product(final Integer id, final String name, final Integer weight) {
        Validate.notNull(id, "id is null");
        Validate.notNull(name, "name is null");
        Validate.notNull(weight, "weight is null");

        this.id = id;
        this.name = name;
        this.weight = weight;

        this.productGroups = new HashSet<>();
        this.vehicles = new HashSet<>();

        this.umProductGroups = Collections.unmodifiableSet(this.productGroups);
        this.umVehicles = Collections.unmodifiableSet(this.vehicles);
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
     * Gets the weight.
     * 
     * @return the weight
     */
    public Integer getWeight() {
        return this.weight;
    }

    /**
     * Gets the product groups to which the product belongs.
     * 
     * @return the product groups to which the product belongs
     */
    public Set<ProductGroup> getProductGroups() {
        return this.umProductGroups;
    }

    /**
     * Gets the vehicles which can transport this product.
     * 
     * @return the vehicles which can transport this product
     */
    public Set<Vehicle> getVehicles() {
        return this.umVehicles;
    }

    /**
     * adds a product group to which the product belongs.
     * 
     * @param productGroup
     *            product group to which the product belongs
     */
    void addProductGroup(final ProductGroup productGroup) {
        Validate.notNull(productGroup);
        if (!this.productGroups.add(productGroup)) {
            throw new IllegalStateException("product" + this.name + " already contain the given product group: " + productGroup);
        }
    }

    /**
     * adds vehicle which can transport this product.
     * 
     * @param vehicle
     *            vehicle which can transport this product
     */
    void addVehicle(final Vehicle vehicle) {
        Validate.notNull(vehicle);
        if (!this.vehicles.add(vehicle)) {
            throw new IllegalStateException("product " + this.name + " already contain the given vehicle: " + vehicle);
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
        final Product other = (Product) obj;
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
