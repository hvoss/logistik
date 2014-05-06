package de.hsbremen.kss.configuration;

import java.util.Collection;

import org.apache.commons.lang3.Validate;

/**
 * represents a item of an order.
 * 
 * @author henrik
 * 
 */
public final class Item {

    /** order belongs to this item. */
    private final Order order;

    /** product to deliver. */
    private final Product product;

    /** num of products to deliver. */
    private final Integer amount;

    /**
     * Instantiates a new item.
     * 
     * @param order
     *            the order
     * @param product
     *            the product
     * @param amount
     *            the amount
     */
    Item(final Order order, final Product product, final Integer amount) {
        Validate.notNull(order, "order is null");
        Validate.notNull(product, "product is null");
        Validate.notNull(amount, "amount is null");

        this.order = order;
        this.product = product;
        this.amount = amount;
    }

    /**
     * Gets the product to deliver.
     * 
     * @return the product to deliver
     */
    public Product getProduct() {
        return this.product;
    }

    /**
     * Gets the num of products to deliver.
     * 
     * @return the num of products to deliver
     */
    public Integer getAmount() {
        return this.amount;
    }

    /**
     * Gets the order belongs to this item.
     * 
     * @return the order belongs to this item
     */
    public Order getOrder() {
        return this.order;
    }

    /**
     * returns the weight of this item.
     * 
     * @return weight of this item
     */
    public int weight() {
        return this.amount * this.product.getWeight();
    }

    @Override
    public String toString() {
        return "Item[" + this.product.getName() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.order == null) ? 0 : this.order.hashCode());
        result = prime * result + ((this.product == null) ? 0 : this.product.hashCode());
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
        final Item other = (Item) obj;
        if (this.amount == null) {
            if (other.amount != null) {
                return false;
            }
        } else if (!this.amount.equals(other.amount)) {
            return false;
        }
        if (this.order == null) {
            if (other.order != null) {
                return false;
            }
        } else if (!this.order.equals(other.order)) {
            return false;
        }
        if (this.product == null) {
            if (other.product != null) {
                return false;
            }
        } else if (!this.product.equals(other.product)) {
            return false;
        }
        return true;
    }

    /**
     * aggregates the weight of the given items.
     * 
     * @param items
     *            items to aggregate
     * @return the weight of all items.
     */
    public static Integer aggregateWeight(final Collection<Item> items) {
        int sum = 0;
        for (final Item item : items) {
            sum += item.weight();
        }
        return sum;
    }

}
