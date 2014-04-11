package de.hsbremen.kss.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Vehicle")
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "VehicleElement [id=" + id + ", name=" + name + "]";
	}

	public Integer getSourceDepotId() {
		return sourceDepotId;
	}

	public void setSourceDepotId(Integer sourceDepotId) {
		this.sourceDepotId = sourceDepotId;
	}

	public Integer getDestinationDepotId() {
		return destinationDepotId;
	}

	public void setDestinationDepotId(Integer destinationDepotId) {
		this.destinationDepotId = destinationDepotId;
	}

	public List<CapacityElement> getCapacities() {
		return capacities;
	}

	public void setCapacities(List<CapacityElement> capacities) {
		this.capacities = capacities;
	}
	
	
}
