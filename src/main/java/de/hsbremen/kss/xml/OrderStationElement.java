package de.hsbremen.kss.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrderStationElement {

    /** the id of the station */
    @XmlElement(name = "StationId")
    private Integer stationId;

    /** the time window */
    @XmlElement(name = "TimeWindow")
    private TimeWindowElement timeWindow;

    /** the service time */
    @XmlElement(name = "ServiceTime")
    private Double serviceTime;

    public Integer getStationId() {
        return this.stationId;
    }

    public void setStationId(final Integer stationId) {
        this.stationId = stationId;
    }

    public TimeWindowElement getTimeWindow() {
        return this.timeWindow;
    }

    public void setTimeWindow(final TimeWindowElement timeWindow) {
        this.timeWindow = timeWindow;
    }

    public Double getServiceTime() {
        return this.serviceTime;
    }

    public void setServiceTime(final Double serviceTime) {
        this.serviceTime = serviceTime;
    }

}
