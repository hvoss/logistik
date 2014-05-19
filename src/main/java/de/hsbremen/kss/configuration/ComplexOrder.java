package de.hsbremen.kss.configuration;

import org.apache.commons.lang3.Validate;

/**
 * a complex order is a combination of two {@link Order}s.
 * 
 * @author henrik
 * 
 */
public final class ComplexOrder {

    /** the id */
    private final Integer id;

    /** order which must be performed first */
    private final Order firstOrder;

    /** order which must be performed after the first order */
    private final Order secondOrder;

    /** max duration between both orders */
    private final Double maxDuration;

    /**
     * ctor.
     * 
     * @param id
     *            the id
     * @param firstOrder
     *            order which must be performed first
     * @param secondOrder
     *            order which must be performed after the first order
     * @param maxDuration
     *            max duration between both orders
     */
    public ComplexOrder(final Integer id, final Order firstOrder, final Order secondOrder, final Double maxDuration) {
        Validate.notNull(id, "id is null");
        Validate.notNull(firstOrder, "firstOrder is null");
        Validate.notNull(secondOrder, "secondOrder is null");
        Validate.notNull(maxDuration, "maxDuration is null");

        Validate.isTrue(firstOrder.getDestinationStation().equals(secondOrder.getDestinationStation()), "destination stations must be the same");
        Validate.isTrue(!firstOrder.getProduct().equals(secondOrder.getProduct()), "products must be different");

        final Double start = firstOrder.getDestination().getTimeWindow().getStart() + firstOrder.getDestination().getServiceTime();
        final Double end = firstOrder.getDestination().getTimeWindow().getEnd() + firstOrder.getDestination().getServiceTime();

        final boolean betweenStart = secondOrder.getDestination().getTimeWindow().between(start);
        final boolean betweenEnd = secondOrder.getDestination().getTimeWindow().between(end);

        Validate.isTrue(betweenStart && betweenEnd);

        this.id = id;
        this.firstOrder = firstOrder;
        this.secondOrder = secondOrder;
        this.maxDuration = maxDuration;
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
     * Gets the order which must be performed first.
     * 
     * @return the order which must be performed first
     */
    public Order getFirstOrder() {
        return this.firstOrder;
    }

    /**
     * Gets the order which must be performed after the first order.
     * 
     * @return the order which must be performed after the first order
     */
    public Order getSecondOrder() {
        return this.secondOrder;
    }

    /**
     * Gets the max duration between both orders.
     * 
     * @return the max duration between both orders
     */
    public Double getMaxDuration() {
        return this.maxDuration;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
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
        final ComplexOrder other = (ComplexOrder) obj;
        if (this.getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!this.getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

}
