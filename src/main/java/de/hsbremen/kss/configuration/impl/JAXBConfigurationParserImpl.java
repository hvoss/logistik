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
import de.hsbremen.kss.model.Item;
import de.hsbremen.kss.model.Order;
import de.hsbremen.kss.model.Product;
import de.hsbremen.kss.model.ProductGroup;
import de.hsbremen.kss.model.Station;
import de.hsbremen.kss.model.Vehicle;
import de.hsbremen.kss.xml.ConfigurationElement;
import de.hsbremen.kss.xml.ItemElement;
import de.hsbremen.kss.xml.OrderElement;
import de.hsbremen.kss.xml.ProductElement;
import de.hsbremen.kss.xml.ProductGroupElement;
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
			List<ProductGroupElement> productGroupElements = configuration.getProductGroups();
			List<ProductElement> productElements = configuration.getProducts();

			Map<Integer, Station> stationMap = new HashMap<>(
					stationElements.size());
			List<Station> stationList = new ArrayList<>(stationElements.size());

			Map<Integer, Order> orderMap = new HashMap<>(orderElements.size());
			List<Order> orderList = new ArrayList<>(orderElements.size());

			Map<Integer, Vehicle> vehicleMap = new HashMap<>(
					vehicleElements.size());
			List<Vehicle> vehicleList = new ArrayList<>(vehicleElements.size());
			
			Map<Integer, ProductGroup> productGroupMap = new HashMap<>(
					productGroupElements.size());
			List<ProductGroup> productGroupList = new ArrayList<>(productGroupElements.size());
			
			Map<Integer, Product> productMap = new HashMap<>(
					productElements.size());
			List<Product> productList = new ArrayList<>(productElements.size());

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
				Vehicle vehicle = convert(element, stationMap);
				Vehicle oldVehicle = vehicleMap.put(vehicle.getId(), vehicle);
				vehicleList.add(vehicle);

				if (oldVehicle != null) {
					throw new DuplicateIdException(Vehicle.class, oldVehicle,
							vehicle);
				}
			}
			
			
			
			for (ProductElement element : productElements) {
				Product product = convert(element);
				Product oldProduct = productMap.put(product.getId(), product);
				productList.add(product);

				if (oldProduct != null) {
					throw new DuplicateIdException(ProductElement.class, oldProduct,
							product);
				}
			}
			
			for (ProductGroupElement element : productGroupElements) {
				ProductGroup productGroup = convert(element);
				ProductGroup oldProductGroup = productGroupMap.put(productGroup.getId(), productGroup);
				productGroupList.add(productGroup);

				if (oldProductGroup != null) {
					throw new DuplicateIdException(ProductGroupElement.class, oldProductGroup,
							productGroup);
				}
				
				for(Integer productId : element.getProductIds()) {
					Product product = productMap.get(productId);
					product.getProductGroup().add(productGroup);
					productGroup.getProductGroup().add(productGroup);
				}
			}

			for (OrderElement element : orderElements) {
				

				Order order = convert(element, stationMap, productMap);
				Order oldOrder = orderMap.put(order.getId(), order);
				orderList.add(order);

				if (oldOrder != null) {
					throw new DuplicateIdException(Station.class, oldOrder,
							order);
				}
			}

			return new Configuration(orderList, stationList, vehicleList, productList, productGroupList);

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

	private Order convert(OrderElement element, Map<Integer, Station> stationMap, Map<Integer, Product> productMap) {
		LOG.trace("convert element: " + element);
		Station sourceStation = stationMap.get(element
				.getSourceStationId());
		Station destinationStation = stationMap.get(element
				.getDestinationStationId());
		
		Order order = new Order(element.getId(), element.getName(), sourceStation,
				destinationStation);
		
		if (element.getItems() != null) {
			for (ItemElement itemElement : element.getItems()) {
				Item item = convert(itemElement, order, productMap);
				order.getItems().add(item);
			}
		}
		
		if (sourceStation != null) { // not necessary
			sourceStation.getSourceOrders().add(order);
		}
		if (destinationStation != null) {
			destinationStation.getDestinationOrders().add(order);
		}
		
		return order;
	}
	
	private Item convert(ItemElement element, Order order, Map<Integer, Product> productMap) {
		Product product = productMap.get(element.getProductId());
		return new Item(order, product, element.getAmount());
	}

	private Vehicle convert(VehicleElement element, Map<Integer, Station> stationMap) {
		LOG.trace("convert element: " + element);
		Station sourceStation = stationMap.get(element.getSourceDepotId());
		Station destinationStation = stationMap.get(element.getDestinationDepotId());
		return new Vehicle(element.getId(), element.getName(), sourceStation, destinationStation);
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
	
	private Product convert(ProductElement element) {
		return new Product(element.getId(), element.getName());
	}
	
	private ProductGroup convert(ProductGroupElement element) {
		return new ProductGroup(element.getId(), element.getName());
	}

}
