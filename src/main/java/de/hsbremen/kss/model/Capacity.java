package de.hsbremen.kss.model;

import org.apache.commons.lang3.Validate;

public class Capacity {

	/** the product */
	private final Product product;

	/** the product group */
	private final ProductGroup productGroup;

	private final Boolean miscible;

	private final Integer capacity;
	
	private final Vehicle vehicle;

	public Capacity(Product product, Vehicle vehicle, Boolean miscible, Integer capacity) {
		this(product, null, vehicle, miscible, capacity);
	}

	public Capacity(ProductGroup productGroup, Vehicle vehicle, Boolean miscible,
			Integer capacity) {
		this(null, productGroup, vehicle, miscible, capacity);
	}

	private Capacity(Product product, ProductGroup productGroup,
			Vehicle vehicle, Boolean miscible, Integer capacity) {
		Validate.notNull(miscible, "miscible is null");
		Validate.notNull(capacity, "capacity is null");
		Validate.notNull(vehicle);
		Validate.isTrue(product != null || productGroup != null,
				"product and product group is null");

		this.product = product;
		this.productGroup = productGroup;
		this.miscible = miscible;
		this.capacity = capacity;
		this.vehicle = vehicle;
	}

	public Product getProduct() {
		return product;
	}

	public ProductGroup getProductGroup() {
		return productGroup;
	}

	public Boolean getMiscible() {
		return miscible;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public boolean contains(Product product) {
		if (product == null) {
			return false;
		}
		
		if (product.equals(this.product)) {
			return true;
		}
		
		if (this.productGroup != null) {
			return this.productGroup.contains(product);
		}
		
		return false;
	}
}
