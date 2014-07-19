package de.hsbremen.kss.configuration;

import org.apache.commons.lang3.Validate;

/**
 * The Class Product.
 */
public final class Product {

    /** the id. */
    private final Integer id;

    /** the name. */
    private final String name;

    /**
     * Instantiates a new product.
     *
     * @param id
     *            the id
     * @param name
     *            the name
     */
    Product(final Integer id, final String name) {
        Validate.notNull(id, "id is null");
        Validate.notNull(name, "name is null");

        this.id = id;
        this.name = name;
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
