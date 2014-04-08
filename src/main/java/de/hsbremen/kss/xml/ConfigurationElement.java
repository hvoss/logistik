package de.hsbremen.kss.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "Configuration")
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

	@Override
	public String toString() {
		return "ConfigurationElement [orders=" + orders + ", stations="
				+ stations + ", vehicles=" + vehicles + "]";
	}
	
}
