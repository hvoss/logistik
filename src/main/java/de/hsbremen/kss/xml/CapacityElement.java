package de.hsbremen.kss.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


@XmlRootElement(name = "Capacity")
@XmlAccessorType(XmlAccessType.FIELD)
public class CapacityElement {

	@XmlAttribute (name = "productId")
	private Integer productId;

	@XmlAttribute (name = "productGroupId")
	private Integer productGroupId;
	
	@XmlAttribute(name = "miscible")
	private Boolean miscible;

	@XmlValue
	private Integer capacity;

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductGroupId() {
		return productGroupId;
	}

	public void setProductGroupId(Integer productGroupId) {
		this.productGroupId = productGroupId;
	}

	public Boolean getMiscible() {
		return miscible;
	}

	public void setMiscible(Boolean miscible) {
		this.miscible = miscible;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	
	
}
