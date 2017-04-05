package com.jpm.processor.message.process;

import static com.jpm.processor.message.process.MsgProcessorConstants.BATCH_SIZE;
import static com.jpm.processor.message.process.MsgProcessorConstants.computeSalesFromBeginning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.jpm.processor.message.sale.entity.AdjustmentSale;
import com.jpm.processor.message.sale.entity.AdjustmentType;
import com.jpm.processor.message.sale.entity.MultipleSale;
import com.jpm.processor.message.sale.entity.Sale;

/*
 * This class calculates the total value of all sales made. 
 * The computation depends on the type of the sale.
 * */
public class EffectiveSalesCalculator {

	/**
	 * List of individual sales amount is maintained (value in the Map) rather
	 * than total product sales to allow for adjustments.
	 **/
	private Map<String, List<Double>> productSalesMap;

	public EffectiveSalesCalculator() {
		productSalesMap = new HashMap<String, List<Double>>();
	}

	public Map<String, List<Double>> computeEffectiveSales(List<Sale> sales) {
		/**
		 * Having this flag as false would be performant. Computing every
		 * effetive sale amt from beginning everytime is not advised. Have kept
		 * it configurable here though. Adjustments being made would be
		 * applicable only to sales already made, so we're safe in saving the
		 * netted sales after each such batch (in productSalesMap).
		 **/
		int startIndex = computeSalesFromBeginning ? 0 : sales.size() - BATCH_SIZE;
		for (int i = startIndex; i < sales.size(); i++) {
			if (sales.get(i) instanceof AdjustmentSale) {
				updateAdjustmentSale(((AdjustmentSale) sales.get(i)));
			} else if (sales.get(i) instanceof MultipleSale) {
				updateMultiSale(((MultipleSale) sales.get(i)));
			} else {
				updateSingleSale(sales.get(i));
			}
		}
		return productSalesMap;
	}

	private List<Double> getSalesListForProduct(String prodType) {
		List<Double> prodSalesList = productSalesMap.get(prodType);
		if (prodSalesList == null) {
			prodSalesList = new ArrayList<Double>();
			productSalesMap.put(prodType, prodSalesList);
		}
		return prodSalesList;

	}

	private void updateSingleSale(Sale sale) {
		List<Double> existingSalesForProduct = getSalesListForProduct(sale.getProductType());
		Double saleAmt = sale.getPrice();
		existingSalesForProduct.add(new Double(saleAmt));
	}

	private void updateMultiSale(MultipleSale multipleSale) {
		List<Double> existingSalesForProduct = getSalesListForProduct(multipleSale.getProductType());
		int noOfSales = multipleSale.getNoOfOccurrences();
		Double saleAmt = multipleSale.getPrice();
		for (int i = 1; i <= noOfSales; i++) {
			existingSalesForProduct.add(new Double(saleAmt));
		}

	}

	private void updateAdjustmentSale(AdjustmentSale sale) {
		List<Double> existingSalesForProduct = getSalesListForProduct(sale.getProductType());
		existingSalesForProduct.add(new Double(sale.getPrice()));
		AdjustmentType adjType = sale.getAdjustmentType();
		Double adjAmt = sale.getAdjustAmount();
		switch (adjType) {

		case ADD: {
			ListIterator<Double> listIterator = existingSalesForProduct
					.listIterator();
			Double currSaleAmt;
			while (listIterator.hasNext()) {
				currSaleAmt = listIterator.next();
				listIterator.set(currSaleAmt + adjAmt.doubleValue());
			}
			break;
		}

		case SUBTRACT: {
			ListIterator<Double> listIterator = existingSalesForProduct
					.listIterator();
			Double currSaleAmt;
			while (listIterator.hasNext()) {
				currSaleAmt = listIterator.next();
				listIterator.set(currSaleAmt - adjAmt.doubleValue());
			}
			break;
		}
		case MULTIPLY: {
			ListIterator<Double> listIterator = existingSalesForProduct
					.listIterator();
			Double currSaleAmt;
			while (listIterator.hasNext()) {
				currSaleAmt = listIterator.next();
				listIterator.set(currSaleAmt * adjAmt.doubleValue());
			}
			break;
		}

		}
	}
}