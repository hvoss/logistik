package de.hsbremen.kss.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Order")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderElement {

    /** the id */
    @XmlAttribute
    private Integer id;

    /** the name */
    @XmlElement(name = "Name")
    private String name;

    /** the source station */
    @XmlElement(name = "Source")
    private OrderStationElement source;

    /** the destination station */
    @XmlElement(name = "Destination")
    private OrderStationElement destination;

    /** id of a product */
    @XmlElement(name = "ProductId")
    private Integer productId;

    /** number of products */
    @XmlElement(name = "Amount")
    private Integer amount;

    public Integer getId() {
        return this.id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public OrderStationElement getSource() {
        return this.source;
    }

    public void setSource(final OrderStationElement source) {
        this.source = source;
    }

    public OrderStationElement getDestination() {
        return this.destination;
    }

    public void setDestination(final OrderStationElement destination) {
        this.destination = destination;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public void setProductId(final Integer productId) {
        this.productId = productId;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(final Integer amount) {
        this.amount = amount;
    }

}
