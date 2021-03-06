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
import de.hsbremen.kss.xml.ComplexOrderElement;
import de.hsbremen.kss.xml.ConfigurationElement;
import de.hsbremen.kss.xml.OrderElement;
import de.hsbremen.kss.xml.OrderStationElement;
import de.hsbremen.kss.xml.ProductElement;
import de.hsbremen.kss.xml.StationElement;
import de.hsbremen.kss.xml.TimeWindowElement;
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
    private Map<String, Vehicle> vehicleMap;

    /** map of all products (key: id, value: product) */
    private Map<Integer, Product> productMap;

    /** map of all complex orders (key: id, value: complex order) */
    private Map<Integer, ComplexOrder> complexOrderMap;

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
        this.productMap = new HashMap<>(configuration.getProducts().size());
        this.complexOrderMap = new HashMap<>(configuration.getComplexOrders().size());
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
        final Set<ComplexOrder> complexOrders = new HashSet<>(this.complexOrderMap.values());

        return new Configuration(orders, stations, vehicles, products, complexOrders);
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
     * converts the XML complex orders.
     * 
     * @param complexOrders
     *            the XML-complexorders.
     */
    private void convertComplexOrders(final Collection<ComplexOrderElement> complexOrders) {
        for (final ComplexOrderElement element : complexOrders) {
            final ComplexOrder order = convert(element);
            addComplexOrder(order);
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
            for (int i = 0; i < element.getNumber(); i++) {
                final Vehicle vehicle = convert(element, i);
                addVehicle(vehicle);
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
            throw new DuplicateIdException(Order.class, oldOrder, order);
        }
    }

    /**
     * adds a complex order to the map.
     * 
     * @param complexOrder
     *            order to add.
     */
    private void addComplexOrder(final ComplexOrder complexOrder) {
        final ComplexOrder oldComplexOrder = this.complexOrderMap.put(complexOrder.getId(), complexOrder);
        if (oldComplexOrder != null) {
            throw new DuplicateIdException(ComplexOrder.class, oldComplexOrder, complexOrder);
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
        final OrderStation source = convert(element.getSource());
        final OrderStation destination = convert(element.getDestination());

        final Product product = getProduct(element.getProductId());

        final Order order = new Order(element.getId(), element.getName(), source, destination, product, element.getAmount());

        return order;
    }

    /**
     * converts an {@link ComplexOrderElement} into a {@link ComplexOrder}.
     * 
     * @param element
     *            XML-element to convert
     * @return converted {@link ComplexOrder}
     */
    private ComplexOrder convert(final ComplexOrderElement element) {
        final Order firstOrder = getOrder(element.getFirstOrderId());
        final Order secondOrder = getOrder(element.getSecondOrderId());

        final ComplexOrder complexOrder = new ComplexOrder(element.getId(), firstOrder, secondOrder, element.getMaxDuration());

        return complexOrder;
    }

    /**
     * converts an {@link OrderStationElement} into a {@link OrderStation}.
     * 
     * @param element
     *            XML-element to convert
     * 
     * @return converted {@link OrderStation}
     */
    private OrderStation convert(final OrderStationElement element) {
        final Station station = getStation(element.getStationId());

        final TimeWindow timeWindow = convert(element.getTimeWindow());

        return new OrderStation(station, timeWindow, element.getServiceTime());
    }

    /**
     * converts an {@link TimeWindowElement} into a {@link TimeWindow}.
     * 
     * @param element
     *            XML-element to convert
     * 
     * @return converted {@link TimeWindow}
     */
    private TimeWindow convert(final TimeWindowElement element) {
        return new TimeWindow(element.getStart(), element.getEnd());
    }

    /**
     * converts an {@link VehicleElement} into a {@link Vehicle}.
     * 
     * @param element
     *            XML-element to convert
     * @param subId
     *            the sub id
     * @return converted {@link Vehicle}
     */
    private Vehicle convert(final VehicleElement element, final Integer subId) {
        final Station sourceStation = getStation(element.getSourceDepotId());
        final Station destinationStation = getStation(element.getDestinationDepotId());
        final String id = element.getId() + "-" + subId;

        final TimeWindow timespan = convert(element.getTimespan());

        final Product product = getProduct(element.getProductId());

        return new Vehicle(id, element.getName(), sourceStation, destinationStation, element.getVelocity(), timespan, product, element.getCapacity());
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
