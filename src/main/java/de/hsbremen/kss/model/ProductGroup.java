package de.hsbremen.kss.model;

import java.util.HashSet;
import java.util.Set;

public class ProductGroup {

	/** the id */
	private final Integer id;
	
	/** the name */
	private final String name;

	/** Products belonging to this product group */
	private final Set<ProductGroup> productGroup;

	
	public ProductGroup(Integer id, String name) {
		this.id = id;
		this.name = name;
		this.productGroup = new HashSet<ProductGroup>();
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
