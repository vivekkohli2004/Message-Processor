package com.jpm.processor.message.sale.entity;

/*
 * 
 * Represents a sale of a product at a given price. Each Sale object represents one Message from Client.
 * */
public class Sale {
	
	//Product type is not fixed. Hence not limiting to enum.
	String productType; 
	
	double price;

	public Sale(String productType, double price) {
		super();
		this.productType = productType;
		this.price = price;
	}
	
	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Sale [productType=" + productType + ", price=" + price + "]\n";
	}
	
}