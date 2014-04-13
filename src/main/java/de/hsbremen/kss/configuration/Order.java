package de.hsbremen.kss.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

/**
 * represents a order of a customer.
 * 
 * @author henrik
 * 
 */
public final class Order {

	/** the id */
	private final Integer id;

	/** the name */
	private final String name;

	/** the source station */
	private final Station source;

	/** the destination station. could be null */
	private final Station destination;
	
	/** all items */ 
	private Set<Item> items;
	
	Order(Integer id, String name, Station source) {
		this(id, name, source, null);
	}

	Order(Integer id, String name, Station source, Station destination) {
		Validate.notNull(id, "id is null");
		Validate.notNull(name, "name is null");
		Validate.notNull(source, "source station is null");

		this.id = id;
		this.name = name;
		this.source = source;
		this.destination = destination;
		this.items = new HashSet<>();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Station getSource() {
		return source;
	}

	public Station getDestination() {
		return destination;
	}

	public Set<Item> getItems() {
		return Collections.unmodifiableSet(items);
	}
	
	void addItem(Item item) {
		Validate.notNull(item, "item is null");
		this.items.add(item);
	}
	
	public Set<Product> getProducts() {
		Set<Product> products = new HashSet<>(this.items.size());
		
		for (Item item : this.items) {
			products.add(item.getProduct());
		}
		
		return products;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public static Set<Station> getAllSourceStations(Collection<Order> orders) {
		Set<Station> stations = new HashSet<>(orders.size());
		
		for (Order order : orders) {
			stations.add(order.getSource());
		}
		
		return stations;
	}
}
