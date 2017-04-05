package com.jpm.processor.message.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jpm.processor.message.sale.entity.AdjustmentSale;
import com.jpm.processor.message.sale.entity.Sale;

/**
 * Performs the task of printing report to console. Delegates the computations
 * to another component before doing so.
 * */
public class ReportsGenerator {

	private EffectiveSalesCalculator effectiveSalesCalculator;

	public ReportsGenerator() {
		effectiveSalesCalculator = new EffectiveSalesCalculator();
	}

	public void generateSalesReport(List<Sale> sales) {
		Map<String, List<Double>> productSalesMap = effectiveSalesCalculator
				.computeEffectiveSales(sales);
		Map<String, Double> productTotalSalesMap = new HashMap<String, Double>();
		Iterator<String> keyIterator = productSalesMap.keySet().iterator();

		while (keyIterator.hasNext()) {
			String prodType = keyIterator.next();
			List<Double> allSalesForProd = productSalesMap.get(prodType);
			double totalSalesForProd = 0.0;
			for (Double saleAmt : allSalesForProd) {
				totalSalesForProd += saleAmt;
			}
			productTotalSalesMap.put(prodType, totalSalesForProd);
		}

		System.out
				.println("\n\nPRODUCT WISE ALL SALES MADE:" + productSalesMap);
		System.out.println("PRODUCT WISE TOTAL SALES:" + productTotalSalesMap);
	}

	public void generateAdjustmentReport(List<Sale> allSales) {
		// Below map represents Map<ProdType, Map<AdjOperation, TotalAdjAmt>>
		Map<String, Map<String, Double>> productAdjSalesMap = new HashMap<String, Map<String, Double>>();

		for (Sale sale : allSales) {
			if (sale instanceof AdjustmentSale) {
				switch (((AdjustmentSale) sale).getAdjustmentType()) {
				case ADD: {
					addOrUpdateAdjAmtForProduct(productAdjSalesMap, sale,
							"ADD", false);
					break;
				}

				case SUBTRACT: {
					addOrUpdateAdjAmtForProduct(productAdjSalesMap, sale,
							"SUBTRACT", false);
					break;
				}

				case MULTIPLY: {
					addOrUpdateAdjAmtForProduct(productAdjSalesMap, sale,
							"MULTIPLY", true);
					break;
				}
				}
			}
		}

		System.out.println("\n\nNET ADJUSTMENTS MADE:" + productAdjSalesMap);
	}

	private void addOrUpdateAdjAmtForProduct(
			Map<String, Map<String, Double>> productAdjSalesMap, Sale sale,
			String keyInAdjMap, boolean isMultiply) {
		String prodType = sale.getProductType();
		Map<String, Double> adjValueMap = productAdjSalesMap.get(prodType);
		double adjAmount = ((AdjustmentSale) sale).getAdjustAmount();
		if (adjValueMap == null) {
			adjValueMap = new HashMap<String, Double>();
			adjValueMap.put(keyInAdjMap, adjAmount);
			productAdjSalesMap.put(prodType, adjValueMap);
		} else {
			Double existingAdjAmt = adjValueMap.get(keyInAdjMap);
			if (existingAdjAmt == null) { // could be null when another
											// adjustmentType operation is
											// done for same prod type
				adjValueMap.put(keyInAdjMap, adjAmount);
			} else {
				if (isMultiply) {
					adjValueMap.put(keyInAdjMap, existingAdjAmt * adjAmount);
				} else {
					adjValueMap.put(keyInAdjMap, existingAdjAmt + adjAmount);
				}
			}
		}
	}
}