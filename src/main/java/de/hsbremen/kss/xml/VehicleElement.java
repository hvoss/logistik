package de.hsbremen.kss.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
	
	
}
