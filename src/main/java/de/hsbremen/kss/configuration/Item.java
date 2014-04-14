package de.hsbremen.kss.configuration;

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

    @Override
    public String toString() {
        return "Item[" + this.product.getName() + "]";
    }

}
