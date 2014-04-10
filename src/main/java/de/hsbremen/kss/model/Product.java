package de.hsbremen.kss.model;

import java.util.HashSet;
import java.util.Set;

public class Product {

	/** the id */
	private final Integer id;

	/** the name */
	private final String name;

	/** Product groups to which the product belongs */
	private final Set<ProductGroup> productGroup;

	public Product(Integer id, String name) {
		this.id = id;
		this.name = name;
		this.productGroup = new HashSet<>();
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Set<ProductGroup> getProductGroup() {
		return productGroup;
	}

}
