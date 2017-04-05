package com.jpm.processor.message.sale.entity;

/*
 * Represents a sale & an adjustment to be made to previous sales. 
 * Each AdjustmentSale object represents one Message received from Client.
 * Inheriting rather than composing as we need to record the list of all sales which would be easier with Inheritance (in a single java.util.List)
 * */
public class AdjustmentSale extends Sale {

	private double adjustAmount;

	private AdjustmentType adjustmentType;

	public AdjustmentSale(double price, String productType, double adjustAmount, AdjustmentType adjustmentType) {
		super(productType, price);
		this.adjustAmount = adjustAmount;
		this.adjustmentType = adjustmentType;
	}
	
	public double getAdjustAmount() {
		return adjustAmount;
	}

	public void setAdjustAmount(double adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	public AdjustmentType getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(AdjustmentType adjustmentType) {
		this.adjustmentType = adjustmentType;
	}

	@Override
	public String toString() {
		return "AdjustmentSale:" + "[productType = " + productType+ ", adjustAmount=" + adjustAmount + ", adjustmentType=" + adjustmentType
				+ "]\n";
	}

}
