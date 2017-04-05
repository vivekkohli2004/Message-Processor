package com.jpm.processor.message.sale.entity;

/*
 * Represents a Sale where a product was sold at a given price for a given number of times.
 * Each MultipleSale object represents one Message from Client.
 * Inheriting from Sale rather than composing as we need to record the list of all sales which would be 
 * easier with Inheritance (in a single java.util.List) 
 * */
public class MultipleSale extends Sale {

	private int noOfOccurrences;

	public MultipleSale(String productType, double price, int noOfOccurrences) {
		super(productType, price);
		this.noOfOccurrences = noOfOccurrences;
	}

	
	public int getNoOfOccurrences() {
		return noOfOccurrences;
	}

	public void setNoOfOccurrences(int noOfOccurrences) {
		this.noOfOccurrences = noOfOccurrences;
	}

	@Override
	public String toString() {
		return "MultipleSale:" + super.toString()+ ", [noOfOccurrences=" + noOfOccurrences + "]\n";
	}

}
