package de.hsbremen.kss.model;

import org.apache.commons.lang3.Validate;


public class Item {

	private final Order order;
	
	private final Product product;
	
	private final Integer amount;
	
	
	public Item(Order order, Product product, Integer amount) {
		Validate.notNull(order, "order is null");
		Validate.notNull(product, "product is null");
		Validate.notNull(amount, "amount is null");
		
		
		this.order = order;
		this.product = product;
		this.amount = amount;
	}

	public Product getProduct() {
		return product;
	}

	public Integer getAmount() {
		return amount;
	}

	public Order getOrder() {
		return order;
	}
	
}
