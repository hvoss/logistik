package de.hsbremen.kss.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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

    /** a list of all stations */
    @XmlElementWrapper(name = "Items")
    @XmlElement(name = "Item")
    private List<ItemElement> items;

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

    public List<ItemElement> getItems() {
        return this.items;
    }

    public void setItems(final List<ItemElement> items) {
        this.items = items;
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

    @Override
    public String toString() {
        return "OrderElement [id=" + this.id + ", name=" + this.name + ", source=" + this.source + ", destination=" + this.destination + ", items="
                + this.items + "]";
    }

}
