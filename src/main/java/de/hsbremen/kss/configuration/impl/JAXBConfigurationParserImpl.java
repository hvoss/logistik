package de.hsbremen.kss.configuration.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.common.exception.DuplicateIdException;
import de.hsbremen.kss.configuration.ConfigurationParser;
import de.hsbremen.kss.model.Configuration;
import de.hsbremen.kss.model.Order;
import de.hsbremen.kss.model.Station;
import de.hsbremen.kss.model.Vehicle;
import de.hsbremen.kss.xml.ConfigurationElement;
import de.hsbremen.kss.xml.OrderElement;
import de.hsbremen.kss.xml.StationElement;
import de.hsbremen.kss.xml.VehicleElement;

public class JAXBConfigurationParserImpl implements ConfigurationParser {

	private final static Logger LOG = LoggerFactory
			.getLogger(JAXBConfigurationParserImpl.class);

	/** the JAXB-context of the {@link ConfigurationElement}-class. */
	private final static JAXBContext CONTEXT;

	static {
		try {
			CONTEXT = JAXBContext.newInstance(ConfigurationElement.class);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Configuration parseConfiguration(File file) {
		try {
			Unmarshaller unmarshaller = CONTEXT.createUnmarshaller();
			ConfigurationElement configuration = (ConfigurationElement) unmarshaller
					.unmarshal(file);

			LOG.trace("parsed configuration: " + configuration);

			List<StationElement> stationElements = configuration.getStations();
			List<OrderElement> orderElements = configuration.getOrders();
			List<VehicleElement> vehicleElements = configuration.getVehicles();

			Map<Integer, Station> stationMap = new HashMap<>(
					stationElements.size());
			List<Station> stationList = new ArrayList<>(stationElements.size());

			Map<Integer, Order> orderMap = new HashMap<>(orderElements.size());
			List<Order> orderList = new ArrayList<>(orderElements.size());

			Map<Integer, Vehicle> vehicleMap = new HashMap<>(
					vehicleElements.size());
			List<Vehicle> vehicleList = new ArrayList<>(vehicleElements.size());

			for (StationElement element : stationElements) {
				Station station = convert(element);
				Station oldStation = stationMap.put(station.getId(), station);
				stationList.add(station);

				if (oldStation != null) {
					throw new DuplicateIdException(Station.class, oldStation,
							station);
				}
			}

			for (VehicleElement element : vehicleElements) {
				Vehicle vehicle = convert(element);
				Vehicle oldVehicle = vehicleMap.put(vehicle.getId(), vehicle);
				vehicleList.add(vehicle);

				if (oldVehicle != null) {
					throw new DuplicateIdException(Station.class, oldVehicle,
							vehicle);
				}
			}

			for (OrderElement element : orderElements) {
				Station sourceStation = stationMap.get(element
						.getSourceStationId());
				Station destinationStation = stationMap.get(element
						.getDestinationStationId());

				Order order = convert(element, sourceStation,
						destinationStation);
				Order oldOrder = orderMap.put(order.getId(), order);
				orderList.add(order);

				if (oldOrder != null) {
					throw new DuplicateIdException(Station.class, oldOrder,
							order);
				}

				if (sourceStation != null) { // not necessary
					sourceStation.getSourceOrders().add(order);
				}
				if (destinationStation != null) {
					destinationStation.getDestinationOrders().add(order);
				}
			}

			return new Configuration(orderList, stationList, vehicleList);

		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeConfiguration(File file, Configuration configuration) {
		try {
			Marshaller marshaller = CONTEXT.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			marshaller.marshal(convert(configuration), new FileOutputStream(
					file));
		} catch (JAXBException | FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private Order convert(OrderElement element, Station sourceStation,
			Station destinationStation) {
		LOG.trace("convert element: " + element);
		return new Order(element.getId(), element.getName(), sourceStation,
				destinationStation);
	}

	private Vehicle convert(VehicleElement element) {
		LOG.trace("convert element: " + element);
		return new Vehicle(element.getId(), element.getName());
	}

	/**
	 * converts the {@link StationElement} into a {@link Station}.
	 * 
	 * @param element
	 *            element to convert
	 * @return the new {@link Station}.
	 */
	private static Station convert(StationElement element) {
		LOG.trace("convert element: " + element);
		Vector2D coordinates = new Vector2D(element.getxCoordinate(),
				element.getyCoordinate());
		return new Station(element.getId(), element.getName(), coordinates);
	}

	private static StationElement convert(Station station) {
		StationElement element = new StationElement();
		element.setId(station.getId());
		element.setName(station.getName());
		return element;
	}

	private static ConfigurationElement convert(Configuration configuration) {
		ConfigurationElement element = new ConfigurationElement();

		element.setStations(convert(configuration.getStations()));

		return element;
	}

	private static List<StationElement> convert(Collection<Station> stations) {
		List<StationElement> stationElements = new ArrayList<>(stations.size());
		for (Station station : stations) {
			stationElements.add(convert(station));
		}
		return stationElements;
	}

}
