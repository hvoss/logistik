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

	/** the id of the source station */
	@XmlElement(name = "SourceStationId")
	private Integer sourceStationId;

	/** the id of the destination station */
	@XmlElement(name = "DestinationStationId")
	private Integer destinationStationId;

	/** a list of all stations */
	@XmlElementWrapper(name = "Items")
	@XmlElement(name = "Item")
	private List<ItemElement> items;


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

	public Integer getSourceStationId() {
		return sourceStationId;
	}

	public void setSourceStationId(Integer sourceStationId) {
		this.sourceStationId = sourceStationId;
	}

	public Integer getDestinationStationId() {
		return destinationStationId;
	}

	public void setDestinationStationId(Integer destinationStationId) {
		this.destinationStationId = destinationStationId;
	}

	@Override
	public String toString() {
		return "OrderElement [id=" + id + ", name=" + name
				+ ", sourceStationId=" + sourceStationId
				+ ", destinationStationId=" + destinationStationId + "]";
	}

	public List<ItemElement> getItems() {
		return items;
	}

	public void setItems(List<ItemElement> items) {
		this.items = items;
	}


}
