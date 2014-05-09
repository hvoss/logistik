package de.hsbremen.kss.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Vehicle")
@XmlAccessorType(XmlAccessType.FIELD)
public class VehicleElement {

    /** the id */
    @XmlAttribute
    private Integer id;

    /** the name */
    @XmlElement(name = "Name")
    private String name;

    /** Id of the source depot (station) */
    @XmlElement(name = "SourceDepotId")
    private Integer sourceDepotId;

    /** Id of the destination depot (station) */
    @XmlElement(name = "DestinationDepotId")
    private Integer destinationDepotId;

    @XmlElementWrapper(name = "Capacities")
    @XmlElement(name = "Capacity")
    private List<CapacityElement> capacities;

    @XmlElement(name = "Velocity")
    private Double velocity;

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

    @Override
    public String toString() {
        return "VehicleElement [id=" + this.id + ", name=" + this.name + "]";
    }

    public Integer getSourceDepotId() {
        return this.sourceDepotId;
    }

    public void setSourceDepotId(final Integer sourceDepotId) {
        this.sourceDepotId = sourceDepotId;
    }

    public Integer getDestinationDepotId() {
        return this.destinationDepotId;
    }

    public void setDestinationDepotId(final Integer destinationDepotId) {
        this.destinationDepotId = destinationDepotId;
    }

    public List<CapacityElement> getCapacities() {
        return this.capacities;
    }

    public void setCapacities(final List<CapacityElement> capacities) {
        this.capacities = capacities;
    }

    public Double getVelocity() {
        return this.velocity;
    }

    public void setVelocity(final Double velocity) {
        this.velocity = velocity;
    }

}
