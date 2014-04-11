package de.hsbremen.kss.configuration;

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
	private List<Station> stationList;

	private Map<Integer, Order> orderMap;
	private List<Order> orderList;

	private Map<Integer, Vehicle> vehicleMap;
	private List<Vehicle> vehicleList;

	private Map<Integer, ProductGroup> productGroupMap;
	private List<ProductGroup> productGroupList;

	private Map<Integer, Product> productMap;
	private List<Product> productList;

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
		List<StationElement> stationElements = configuration.getStations();
		List<OrderElement> orderElements = configuration.getOrders();
		List<VehicleElement> vehicleElements = configuration.getVehicles();
		List<ProductGroupElement> productGroupElements = configuration
				.getProductGroups();
		List<ProductElement> productElements = configuration.getProducts();

		stationMap = new HashMap<>(stationElements.size());
		stationList = new ArrayList<>(stationElements.size());

		orderMap = new HashMap<>(orderElements.size());
		orderList = new ArrayList<>(orderElements.size());

		vehicleMap = new HashMap<>(vehicleElements.size());
		vehicleList = new ArrayList<>(vehicleElements.size());

		productGroupMap = new HashMap<>(productGroupElements.size());
		productGroupList = new ArrayList<>(productGroupElements.size());

		productMap = new HashMap<>(productElements.size());
		productList = new ArrayList<>(productElements.size());
	}

	@Override
	public Configuration parseConfiguration(File file) {
		try {
			Unmarshaller unmarshaller = CONTEXT.createUnmarshaller();
			ConfigurationElement configuration = (ConfigurationElement) unmarshaller
					.unmarshal(file);

			LOG.trace("parsed configuration: " + configuration);

			init(configuration);

			List<StationElement> stationElements = configuration.getStations();
			List<OrderElement> orderElements = configuration.getOrders();
			List<VehicleElement> vehicleElements = configuration.getVehicles();
			List<ProductGroupElement> productGroupElements = configuration
					.getProductGroups();
			List<ProductElement> productElements = configuration.getProducts();

			for (StationElement element : stationElements) {
				Station station = convert(element);
				addStation(station);
			}

			for (ProductElement element : productElements) {
				Product product = convert(element);
				addProduct(product);
			}

			for (ProductGroupElement element : productGroupElements) {
				ProductGroup productGroup = convert(element);
				addProductGroup(productGroup);

				for (Integer productId : element.getProductIds()) {
					Product product = productMap.get(productId);
					product.addProductGroup(productGroup);
					productGroup.addProduct(product);
				}
			}

			for (VehicleElement element : vehicleElements) {
				Vehicle vehicle = convert(element);
				addVehicle(vehicle);

				for (CapacityElement capacityElement : element.getCapacities()) {
					Capacity capacity = convert(capacityElement, vehicle);
					vehicle.addCapacity(capacity);
				}
			}

			for (OrderElement element : orderElements) {
				Order order = convert(element);
				addOrder(order);
			}

			return new Configuration(orderList, stationList, vehicleList,
					productList, productGroupList);

		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	private void addOrder(Order order) {
		Order oldOrder = orderMap.put(order.getId(), order);
		orderList.add(order);

		if (oldOrder != null) {
			throw new DuplicateIdException(Station.class, oldOrder, order);
		}
	}

	private void addVehicle(Vehicle vehicle) {
		Vehicle oldVehicle = vehicleMap.put(vehicle.getId(), vehicle);
		vehicleList.add(vehicle);

		if (oldVehicle != null) {
			throw new DuplicateIdException(Vehicle.class, oldVehicle, vehicle);
		}
	}

	private void addProductGroup(ProductGroup productGroup) {
		ProductGroup oldProductGroup = productGroupMap.put(
				productGroup.getId(), productGroup);
		productGroupList.add(productGroup);

		if (oldProductGroup != null) {
			throw new DuplicateIdException(ProductGroupElement.class,
					oldProductGroup, productGroup);
		}
	}

	private void addStation(Station station) {
		Station oldStation = stationMap.put(station.getId(), station);
		stationList.add(station);
		if (oldStation != null) {
			throw new DuplicateIdException(Station.class, oldStation, station);
		}
	}

	private void addProduct(Product product) {
		Product oldProduct = this.productMap.put(product.getId(), product);
		this.productList.add(product);
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
			sourceStation.getSourceOrders().add(order);
		}
		if (destinationStation != null) {
			destinationStation.getDestinationOrders().add(order);
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
