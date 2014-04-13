package de.hsbremen.kss.configuration;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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

/**
 * parses the configuration from a XML-File.
 * 
 * @author henrik
 * 
 */
public final class JAXBConfigurationParserImpl implements ConfigurationParser {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(JAXBConfigurationParserImpl.class);

    /** map of all stations (key: id, value: station) */
    private Map<Integer, Station> stationMap;

    /** map of all orders (key: id, value: order) */
    private Map<Integer, Order> orderMap;

    /** map of all vehicles (key: id, value: vehicle) */
    private Map<Integer, Vehicle> vehicleMap;

    /** map of all product groups (key: id, value: product group) */
    private Map<Integer, ProductGroup> productGroupMap;

    /** map of all products (key: id, value: product) */
    private Map<Integer, Product> productMap;

    /** the JAXB-context of the {@link ConfigurationElement}-class. */
    private static final JAXBContext CONTEXT;

    static {
        try {
            CONTEXT = JAXBContext.newInstance(ConfigurationElement.class);
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Configuration parseConfiguration(final File file) {
        try {
            final Unmarshaller unmarshaller = JAXBConfigurationParserImpl.CONTEXT.createUnmarshaller();
            final ConfigurationElement configuration = (ConfigurationElement) unmarshaller.unmarshal(file);

            JAXBConfigurationParserImpl.LOG.trace("parsed configuration: " + configuration);

            init(configuration);

            convertStations(configuration.getStations());
            convertProducts(configuration.getProducts());
            convertProductGroups(configuration.getProductGroups());
            convertVehicles(configuration.getVehicles());
            convertOrders(configuration.getOrders());

            return createConfiguration();

        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize the parser.
     * 
     * @param configuration
     *            parsed XML configuration
     */
    private void init(final ConfigurationElement configuration) {
        this.stationMap = new HashMap<>(configuration.getStations().size());
        this.orderMap = new HashMap<>(configuration.getOrders().size());
        this.vehicleMap = new HashMap<>(configuration.getVehicles().size());
        this.productGroupMap = new HashMap<>(configuration.getProductGroups().size());
        this.productMap = new HashMap<>(configuration.getProducts().size());
    }

    /**
     * creates the configuration from the property-maps.
     * 
     * @return the configuration
     */
    private Configuration createConfiguration() {
        final Set<Order> orders = new HashSet<>(this.orderMap.values());
        final Set<Station> stations = new HashSet<>(this.stationMap.values());
        final Set<Vehicle> vehicles = new HashSet<>(this.vehicleMap.values());
        final Set<Product> products = new HashSet<>(this.productMap.values());
        final Set<ProductGroup> productGroups = new HashSet<>(this.productGroupMap.values());

        return new Configuration(orders, stations, vehicles, products, productGroups);
    }

    /**
     * converts the XML orders.
     * 
     * @param orders
     *            the XML-orders.
     */
    private void convertOrders(final Collection<OrderElement> orders) {
        for (final OrderElement element : orders) {
            final Order order = convert(element);
            addOrder(order);
        }
    }

    /**
     * converts the XML vehicles.
     * 
     * @param vehicles
     *            the XML-vehicles.
     */
    private void convertVehicles(final Collection<VehicleElement> vehicles) {
        for (final VehicleElement element : vehicles) {
            final Vehicle vehicle = convert(element);
            addVehicle(vehicle);

            convertCapacities(element.getCapacities(), vehicle);
        }
    }

    /**
     * converts the XML capacities.
     * 
     * @param capacities
     *            the XML-capacities.
     * @param vehicle
     *            the vehicle to which the capacities belongs to
     */
    private void convertCapacities(final Collection<CapacityElement> capacities, final Vehicle vehicle) {
        for (final CapacityElement capacityElement : capacities) {
            final Capacity capacity = convert(capacityElement, vehicle);
            vehicle.addCapacity(capacity);
        }
    }

    /**
     * converts the XML product groups.
     * 
     * @param productGroups
     *            the XML-product groups.
     */
    private void convertProductGroups(final Collection<ProductGroupElement> productGroups) {
        for (final ProductGroupElement element : productGroups) {
            final ProductGroup productGroup = convert(element);
            addProductGroup(productGroup);

            for (final Integer productId : element.getProductIds()) {
                final Product product = this.productMap.get(productId);
                product.addProductGroup(productGroup);
                productGroup.addProduct(product);
            }
        }
    }

    /**
     * converts the XML products.
     * 
     * @param products
     *            the XML-products.
     */
    private void convertProducts(final Collection<ProductElement> products) {
        for (final ProductElement element : products) {
            final Product product = convert(element);
            addProduct(product);
        }
    }

    /**
     * converts the XML stations.
     * 
     * @param stations
     *            the XML-stations.
     */
    private void convertStations(final Collection<StationElement> stations) {
        for (final StationElement element : stations) {
            final Station station = JAXBConfigurationParserImpl.convert(element);
            addStation(station);
        }
    }

    /**
     * adds a order to the map.
     * 
     * @param order
     *            order to add.
     */
    private void addOrder(final Order order) {
        final Order oldOrder = this.orderMap.put(order.getId(), order);
        if (oldOrder != null) {
            throw new DuplicateIdException(Station.class, oldOrder, order);
        }
    }

    /**
     * adds a vehicle to the map.
     * 
     * @param vehicle
     *            vehicle to add.
     */
    private void addVehicle(final Vehicle vehicle) {
        final Vehicle oldVehicle = this.vehicleMap.put(vehicle.getId(), vehicle);
        if (oldVehicle != null) {
            throw new DuplicateIdException(Vehicle.class, oldVehicle, vehicle);
        }
    }

    /**
     * adds a productGroup to the map.
     * 
     * @param productGroup
     *            productGroup to add.
     */
    private void addProductGroup(final ProductGroup productGroup) {
        final ProductGroup oldProductGroup = this.productGroupMap.put(productGroup.getId(), productGroup);
        if (oldProductGroup != null) {
            throw new DuplicateIdException(ProductGroupElement.class, oldProductGroup, productGroup);
        }
    }

    /**
     * adds a station to the map.
     * 
     * @param station
     *            station to add.
     */
    private void addStation(final Station station) {
        final Station oldStation = this.stationMap.put(station.getId(), station);
        if (oldStation != null) {
            throw new DuplicateIdException(Station.class, oldStation, station);
        }
    }

    /**
     * adds a product to the map.
     * 
     * @param product
     *            product to add.
     */
    private void addProduct(final Product product) {
        final Product oldProduct = this.productMap.put(product.getId(), product);
        if (oldProduct != null) {
            throw new DuplicateIdException(Product.class, oldProduct, product);
        }
    }

    /**
     * converts an {@link OrderElement} into a {@link Order}.
     * 
     * @param element
     *            XML-element to convert
     * @return converted {@link Order}
     */
    private Order convert(final OrderElement element) {
        final Station sourceStation = getStation(element.getSourceStationId());
        Station destinationStation;

        if (element.getDestinationStationId() != null) {
            destinationStation = getStation(element.getDestinationStationId());
        } else {
            destinationStation = null;
        }

        final Order order = new Order(element.getId(), element.getName(), sourceStation, destinationStation);

        if (element.getItems() != null) {
            for (final ItemElement itemElement : element.getItems()) {
                final Item item = convert(itemElement, order);
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

    /**
     * converts an {@link ItemElement} into a {@link Item}.
     * 
     * @param element
     *            XML-element to convert
     * @param order
     *            order the item belongs to
     * 
     * @return converted {@link Item}
     */
    private Item convert(final ItemElement element, final Order order) {
        final Product product = getProduct(element.getProductId());

        return new Item(order, product, element.getAmount());
    }

    /**
     * converts an {@link VehicleElement} into a {@link Vehicle}.
     * 
     * @param element
     *            XML-element to convert
     * @return converted {@link Vehicle}
     */
    private Vehicle convert(final VehicleElement element) {
        final Station sourceStation = getStation(element.getSourceDepotId());
        final Station destinationStation = getStation(element.getDestinationDepotId());
        return new Vehicle(element.getId(), element.getName(), sourceStation, destinationStation);
    }

    /**
     * converts an {@link StationElement} into a {@link Station}.
     * 
     * @param element
     *            XML-element to convert
     * @return converted {@link Station}
     */
    private static Station convert(final StationElement element) {
        final Vector2D coordinates = new Vector2D(element.getxCoordinate(), element.getyCoordinate());
        return new Station(element.getId(), element.getName(), coordinates);
    }

    /**
     * converts an {@link OrderElement} into a {@link Order}.
     * 
     * @param element
     *            XML-element to convert
     * @return converted {@link Order}
     */
    private Product convert(final ProductElement element) {
        return new Product(element.getId(), element.getName());
    }

    /**
     * converts an {@link OrderElement} into a {@link Order}.
     * 
     * @param element
     *            XML-element to convert
     * @return converted {@link Order}
     */
    private ProductGroup convert(final ProductGroupElement element) {
        return new ProductGroup(element.getId(), element.getName(), element.getMiscible());
    }

    /**
     * converts an {@link CapacityElement} into a {@link Capacity}.
     * 
     * @param element
     *            XML-element to convert
     * @param vehicle
     *            vehicle to which the capacity belongs to
     * @return converted {@link Capacity}
     */
    private Capacity convert(final CapacityElement element, final Vehicle vehicle) {
        final Integer productGroupId = element.getProductGroupId();
        final Integer productId = element.getProductId();
        final Integer capacity = element.getCapacity();

        Validate.isTrue(productId == null ^ productGroupId == null, "only productId xor productGroupId must be defined. productId: " + productId
                + ", productGroupId: " + productGroupId);

        if (productId != null) {
            final Product product = getProduct(productId);
            product.addVehicle(vehicle);
            return new Capacity(product, vehicle, capacity);
        } else {
            final ProductGroup productGroup = getProductGroup(productGroupId);
            productGroup.addVehicle(vehicle);
            return new Capacity(productGroup, vehicle, capacity);
        }
    }

    /**
     * gets a {@link Product} from the map.
     * 
     * @param productId
     *            id of {@link Product}.
     * @return the {@link Product} with the given id.
     */
    private Product getProduct(final Integer productId) {
        final Product product = this.productMap.get(productId);
        Validate.notNull(product, "product with id \"" + productId + "\" not found");
        return product;
    }

    /**
     * gets a {@link ProductGroup} from the map.
     * 
     * @param productGroupId
     *            id of {@link ProductGroup}.
     * @return the {@link ProductGroup} with the given id.
     */
    private ProductGroup getProductGroup(final Integer productGroupId) {
        final ProductGroup productGroup = this.productGroupMap.get(productGroupId);
        Validate.notNull(productGroup, "product with id \"" + productGroupId + "\" not found");
        return productGroup;
    }

    /**
     * gets a {@link Station} from the map.
     * 
     * @param stationId
     *            id of {@link Station}.
     * @return the {@link Station} with the given id.
     */
    private Station getStation(final Integer stationId) {
        final Station station = this.stationMap.get(stationId);
        Validate.notNull(station, "station with id \"" + stationId + "\" not found");
        return station;
    }

    /**
     * gets a {@link Order} from the map.
     * 
     * @param orderId
     *            id of {@link Order}.
     * @return the {@link Order} with the given id.
     */
    @SuppressWarnings("unused")
    private Order getOrder(final Integer orderId) {
        final Order order = this.orderMap.get(orderId);
        Validate.notNull(order, "order with id \"" + orderId + "\" not found");
        return order;
    }

    /**
     * gets a {@link Vehicle} from the map.
     * 
     * @param vehicleId
     *            id of {@link Vehicle}.
     * @return the {@link Vehicle} with the given id.
     */
    @SuppressWarnings("unused")
    private Vehicle getVehicle(final Integer vehicleId) {
        final Vehicle vehicle = this.vehicleMap.get(vehicleId);
        Validate.notNull(vehicle, "vehicle with id \"" + vehicleId + "\" not found");
        return vehicle;
    }

}
