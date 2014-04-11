package de.hsbremen.kss.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.common.exception.DuplicateIdException;
import de.hsbremen.kss.xml.CapacityElement;
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

	private Map<Integer, Station> stationMap;

	private Map<Integer, Order> orderMap;

	private Map<Integer, Vehicle> vehicleMap;

	private Map<Integer, ProductGroup> productGroupMap;

	private Map<Integer, Product> productMap;

	/** the JAXB-context of the {@link ConfigurationElement}-class. */
	private final static JAXBContext CONTEXT;

	static {
		try {
			CONTEXT = JAXBContext.newInstance(ConfigurationElement.class);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	private void init(ConfigurationElement configuration) {
		stationMap = new HashMap<>(configuration.getStations().size());
		orderMap = new HashMap<>(configuration.getOrders().size());
		vehicleMap = new HashMap<>(configuration.getVehicles().size());
		productGroupMap = new HashMap<>(configuration.getProductGroups().size());
		productMap = new HashMap<>(configuration.getProducts().size());
	}

	@Override
	public Configuration parseConfiguration(File file) {
		try {
			Unmarshaller unmarshaller = CONTEXT.createUnmarshaller();
			ConfigurationElement configuration = (ConfigurationElement) unmarshaller
					.unmarshal(file);

			LOG.trace("parsed configuration: " + configuration);

			init(configuration);

			convertStations(configuration.getStations());
			convertProducts(configuration.getProducts());
			convertProductGroups(configuration.getProductGroups());
			convertVehicles(configuration.getVehicles());
			convertOrders(configuration.getOrders());

			return createConfiguration();

		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	private Configuration createConfiguration() {
		Set<Order> orders = new HashSet<>(this.orderMap.values());
		Set<Station> stations = new HashSet<>(this.stationMap.values());
		Set<Vehicle> vehicles = new HashSet<>(this.vehicleMap.values());
		Set<Product> products = new HashSet<>(this.productMap.values());
		Set<ProductGroup> productGroups = new HashSet<>(
				this.productGroupMap.values());

		return new Configuration(orders, stations, vehicles, products,
				productGroups);
	}

	private void convertOrders(Collection<OrderElement> orders) {
		for (OrderElement element : orders) {
			Order order = convert(element);
			addOrder(order);
		}
	}

	private void convertVehicles(Collection<VehicleElement> vehicles) {
		for (VehicleElement element : vehicles) {
			Vehicle vehicle = convert(element);
			addVehicle(vehicle);

			convertCapacities(element.getCapacities(), vehicle);
		}
	}

	private void convertCapacities(Collection<CapacityElement> capacities, Vehicle vehicle) {
		for (CapacityElement capacityElement : capacities) {
			Capacity capacity = convert(capacityElement, vehicle);
			vehicle.addCapacity(capacity);
		}
	}

	private void convertProductGroups(Collection<ProductGroupElement> productGroups) {
		for (ProductGroupElement element : productGroups) {
			ProductGroup productGroup = convert(element);
			addProductGroup(productGroup);

			for (Integer productId : element.getProductIds()) {
				Product product = productMap.get(productId);
				product.addProductGroup(productGroup);
				productGroup.addProduct(product);
			}
		}
	}

	private void convertProducts(Collection<ProductElement> products) {
		for (ProductElement element : products) {
			Product product = convert(element);
			addProduct(product);
		}
	}

	private void convertStations(Collection<StationElement> stations) {
		for (StationElement element : stations) {
			Station station = convert(element);
			addStation(station);
		}
	}

	private void addOrder(Order order) {
		Order oldOrder = orderMap.put(order.getId(), order);
		if (oldOrder != null) {
			throw new DuplicateIdException(Station.class, oldOrder, order);
		}
	}

	private void addVehicle(Vehicle vehicle) {
		Vehicle oldVehicle = vehicleMap.put(vehicle.getId(), vehicle);
		if (oldVehicle != null) {
			throw new DuplicateIdException(Vehicle.class, oldVehicle, vehicle);
		}
	}

	private void addProductGroup(ProductGroup productGroup) {
		ProductGroup oldProductGroup = productGroupMap.put(
				productGroup.getId(), productGroup);
		if (oldProductGroup != null) {
			throw new DuplicateIdException(ProductGroupElement.class,
					oldProductGroup, productGroup);
		}
	}

	private void addStation(Station station) {
		Station oldStation = stationMap.put(station.getId(), station);
		if (oldStation != null) {
			throw new DuplicateIdException(Station.class, oldStation, station);
		}
	}

	private void addProduct(Product product) {
		Product oldProduct = this.productMap.put(product.getId(), product);
		if (oldProduct != null) {
			throw new DuplicateIdException(Product.class, oldProduct, product);
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

	private Order convert(OrderElement element) {
		LOG.trace("convert element: " + element);
		Station sourceStation = getStation(element.getSourceStationId());
		Station destinationStation;

		if (element.getDestinationStationId() != null) {
			destinationStation = getStation(element.getDestinationStationId());
		} else {
			destinationStation = null;
		}

		Order order = new Order(element.getId(), element.getName(),
				sourceStation, destinationStation);

		if (element.getItems() != null) {
			for (ItemElement itemElement : element.getItems()) {
				Item item = convert(itemElement, order);
				order.addItem(item);
			}
		}

		if (sourceStation != null) { // not necessary
			sourceStation.addSourceOrder(order);
		}
		if (destinationStation != null) {
			destinationStation.addDestinationOrder(order);
		}

		return order;
	}

	private Item convert(ItemElement element, Order order) {
		Product product = getProduct(element.getProductId());

		return new Item(order, product, element.getAmount());
	}

	private Vehicle convert(VehicleElement element) {
		LOG.trace("convert element: " + element);
		Station sourceStation = getStation(element.getSourceDepotId());
		Station destinationStation = getStation(element.getDestinationDepotId());
		return new Vehicle(element.getId(), element.getName(), sourceStation,
				destinationStation);
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

	private Capacity convert(CapacityElement element, Vehicle vehicle) {
		Integer productGroupId = element.getProductGroupId();
		Integer productId = element.getProductId();
		Boolean miscible = element.getMiscible();
		Integer capacity = element.getCapacity();

		Validate.isTrue(productId == null ^ productGroupId == null,
				"only productId xor productGroupId must be defined. productId: "
						+ productId + ", productGroupId: " + productGroupId);

		if (productId != null) {
			Product product = getProduct(productId);
			product.addVehicle(vehicle);
			return new Capacity(product, vehicle, miscible, capacity);
		} else {
			ProductGroup productGroup = getProductGroup(productGroupId);
			productGroup.addVehicle(vehicle);
			return new Capacity(productGroup, vehicle, miscible, capacity);
		}
	}

	private Product getProduct(Integer productId) {
		Product product = this.productMap.get(productId);
		Validate.notNull(product, "product with id \"" + productId
				+ "\" not found");
		return product;
	}

	private ProductGroup getProductGroup(Integer productGroupId) {
		ProductGroup productGroup = this.productGroupMap.get(productGroupId);
		Validate.notNull(productGroup, "product with id \"" + productGroupId
				+ "\" not found");
		return productGroup;
	}

	private Station getStation(Integer stationId) {
		Station station = this.stationMap.get(stationId);
		Validate.notNull(station, "station with id \"" + stationId
				+ "\" not found");
		return station;
	}

	@SuppressWarnings("unused")
	private Order getOrder(Integer orderId) {
		Order order = this.orderMap.get(orderId);
		Validate.notNull(order, "order with id \"" + orderId + "\" not found");
		return order;
	}

	@SuppressWarnings("unused")
	private Vehicle getVehicle(Integer vehicleId) {
		Vehicle vehicle = this.vehicleMap.get(vehicleId);
		Validate.notNull(vehicle, "vehicle with id \"" + vehicleId
				+ "\" not found");
		return vehicle;
	}

}
