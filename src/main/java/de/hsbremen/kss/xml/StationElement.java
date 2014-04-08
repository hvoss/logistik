package de.hsbremen.kss.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Station")
@XmlAccessorType(XmlAccessType.FIELD)
public class StationElement {

	/** the id */
	@XmlAttribute(required = true)
	protected Integer id;

	/** the name */
	@XmlElement(name = "Name", required = true)
	protected String name;

	/** the x coordinate */
	@XmlElement(name = "XCoordinate")
	protected Double xCoordinate;

	/** the y coordinate */
	@XmlElement(name = "YCoordinate")
	protected Double yCoordinate;

	
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

	public Double getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(Double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public Double getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(Double yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
	
	@Override
	public String toString() {
		return "StationElement [id=" + id + ", name=" + name + ", xCoordinate="
				+ xCoordinate + ", yCoordinate=" + yCoordinate + "]";
	}
}
