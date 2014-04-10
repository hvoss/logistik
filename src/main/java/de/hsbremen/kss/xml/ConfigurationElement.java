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

	/** a list of all stations */
	@XmlElementWrapper(name = "ProductGroups")
	@XmlElement(name = "ProductGroup")
	private List<ProductGroupElement> productGroups;

	public List<OrderElement> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderElement> orders) {
		this.orders = orders;
	}

	public List<StationElement> getStations() {
		return stations;
	}

	public void setStations(List<StationElement> stations) {
		this.stations = stations;
	}

	public List<VehicleElement> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<VehicleElement> vehicles) {
		this.vehicles = vehicles;
	}

	public List<ProductElement> getProducts() {
		return products;
	}

	public void setProducts(List<ProductElement> products) {
		this.products = products;
	}

	public List<ProductGroupElement> getProductGroups() {
		return productGroups;
	}

	public void setProductGroups(List<ProductGroupElement> productGroups) {
		this.productGroups = productGroups;
	}

	@Override
	public String toString() {
		return "ConfigurationElement [orders=" + orders + ", stations="
				+ stations + ", vehicles=" + vehicles + "]";
	}
}
