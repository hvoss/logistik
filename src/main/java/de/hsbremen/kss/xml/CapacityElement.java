package de.hsbremen.kss.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "Capacity")
@XmlAccessorType(XmlAccessType.FIELD)
public class CapacityElement {

    @XmlAttribute(name = "productId")
    private Integer productId;

    @XmlAttribute(name = "productGroupId")
    private Integer productGroupId;

    @XmlValue
    private Integer capacity;

    public Integer getProductId() {
        return this.productId;
    }

    public void setProductId(final Integer productId) {
        this.productId = productId;
    }

    public Integer getProductGroupId() {
        return this.productGroupId;
    }

    public void setProductGroupId(final Integer productGroupId) {
        this.productGroupId = productGroupId;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public void setCapacity(final Integer capacity) {
        this.capacity = capacity;
    }

}
