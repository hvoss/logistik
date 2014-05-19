package de.hsbremen.kss.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ComplexOrder")
@XmlAccessorType(XmlAccessType.FIELD)
public class ComplexOrderElement {

    /** the id */
    @XmlAttribute
    private Integer id;

    @XmlElement(name = "FirstOrderId")
    private Integer firstOrderId;

    @XmlElement(name = "SecondOrderId")
    private Integer secondOrderId;

    @XmlElement(name = "MaxDuration")
    private Double maxDuration;

    public Integer getFirstOrderId() {
        return this.firstOrderId;
    }

    public void setFirstOrderId(final Integer firstOrderId) {
        this.firstOrderId = firstOrderId;
    }

    public Integer getSecondOrderId() {
        return this.secondOrderId;
    }

    public void setSecondOrderId(final Integer secondOrderId) {
        this.secondOrderId = secondOrderId;
    }

    public Double getMaxDuration() {
        return this.maxDuration;
    }

    public void setMaxDuration(final Double maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
