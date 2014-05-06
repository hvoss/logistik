package de.hsbremen.kss.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigurationElement {

    /** a list of all orders */
    @XmlElementWrapper(name = "Orders")
    @XmlElement(name = "Order")
    private List<OrderElement> orders;

    /** a list of all stations */
    @XmlElementWrapper(name = "Stations")
    @XmlElement(name = "Station")
    private List<StationElement> stations;

    /** a list of all vehicles */
    @XmlElementWrapper(name = "Vehicles")
    @XmlElement(name = "Vehicle")
    private List<VehicleElement> vehicles;

    /** a list of all stations */
    @XmlElementWrapper(name = "Products")
    @XmlElement(name = "Product")
    private List<ProductElement> products;

    public List<OrderElement> getOrders() {
        return this.orders;
    }

    public void setOrders(final List<OrderElement> orders) {
        this.orders = orders;
    }

    public List<StationElement> getStations() {
        return this.stations;
    }

    public void setStations(final List<StationElement> stations) {
        this.stations = stations;
    }

    public List<VehicleElement> getVehicles() {
        return this.vehicles;
    }

    public void setVehicles(final List<VehicleElement> vehicles) {
        this.vehicles = vehicles;
    }

    public List<ProductElement> getProducts() {
        return this.products;
    }

    public void setProducts(final List<ProductElement> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "ConfigurationElement [orders=" + this.orders + ", stations=" + this.stations + ", vehicles=" + this.vehicles + "]";
    }
}
