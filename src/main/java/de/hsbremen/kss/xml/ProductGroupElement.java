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

    @XmlElement(name = "Miscible")
    private Boolean miscible;

    /** a list of all stations */
    @XmlElementWrapper(name = "Products")
    @XmlElement(name = "ProductId")
    private List<Integer> productIds;

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

    public List<Integer> getProductIds() {
        return this.productIds;
    }

    public void setProductIds(final List<Integer> productIds) {
        this.productIds = productIds;
    }

    public Boolean getMiscible() {
        return this.miscible;
    }

    public void setMiscible(final Boolean miscible) {
        this.miscible = miscible;
    }

}
