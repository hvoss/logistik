package de.hsbremen.kss.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ProductGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductGroupElement {

	/** the id */
	@XmlAttribute
	private Integer id;

	/** the name */
	@XmlElement(name = "Name")
	private String name;

	/** a list of all stations */
	@XmlElementWrapper(name = "Products")
	@XmlElement(name = "ProductId")
	private List<Integer> productIds;

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

	public List<Integer> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<Integer> productIds) {
		this.productIds = productIds;
	}

}
