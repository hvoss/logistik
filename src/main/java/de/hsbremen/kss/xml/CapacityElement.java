package de.hsbremen.kss.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Capacity")
@XmlAccessorType(XmlAccessType.FIELD)
public class CapacityElement {

    @XmlElementWrapper(name = "Products")
    @XmlElement(name = "ProductId")
    private List<Integer> products;

    @XmlElement(name = "CapacityWeight")
    private Integer capacityWeight;

    /**
     * indicates whether different products can be tranported in the same
     * capacity.
     */

    @XmlElement(name = "Miscible")
    private Boolean miscible;

    public Integer getCapacityWeight() {
        return this.capacityWeight;
    }

    public void setCapacityWeight(final Integer capacityWeight) {
        this.capacityWeight = capacityWeight;
    }

    public List<Integer> getProducts() {
        return this.products;
    }

    public void setProducts(final List<Integer> products) {
        this.products = products;
    }

    public Boolean getMiscible() {
        return this.miscible;
    }

    public void setMiscible(final Boolean miscible) {
        this.miscible = miscible;
    }

}
