package de.hsbremen.kss.model;


public class Item {

	private final Order order;
	
	private final Product product;
	
	private final Integer amount;
	
	
	public Item(Order order, Product product, Integer amount) {
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
