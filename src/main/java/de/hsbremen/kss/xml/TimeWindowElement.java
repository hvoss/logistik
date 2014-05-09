package de.hsbremen.kss.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class TimeWindowElement {

    @XmlElement(name = "Start")
    private Double start;

    @XmlElement(name = "End")
    private Double end;

    public Double getStart() {
        return this.start;
    }

    public void setStart(final Double start) {
        this.start = start;
    }

    public Double getEnd() {
        return this.end;
    }

    public void setEnd(final Double end) {
        this.end = end;
    }
}
