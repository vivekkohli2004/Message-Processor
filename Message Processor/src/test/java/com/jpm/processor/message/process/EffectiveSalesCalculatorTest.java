package com.jpm.processor.message.process;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jpm.processor.message.sale.entity.AdjustmentSale;
import com.jpm.processor.message.sale.entity.AdjustmentType;
import com.jpm.processor.message.sale.entity.MultipleSale;
import com.jpm.processor.message.sale.entity.Sale;

public class EffectiveSalesCalculatorTest {

	@Test
	public void TestComputeEffectiveSales_AllSingleSales() {
		EffectiveSalesCalculator effSalesCal = new EffectiveSalesCalculator();
		Sale appSale1 = new Sale("apple", 4);
		Sale appSale2 = new Sale("apple", 3);
		Sale orgSale1 = new Sale("orange", 4);
		Sale appSale3 = new Sale("apple", 9);
		Sale bananaSale1 = new Sale("banana", 6);
		Sale bananaSale2 = new Sale("banana", 2);
		Sale pearSale1 = new Sale("pear", 2);
		Sale pearSale2 = new Sale("pear", 3);
		Sale mangoSale1 = new Sale("mango", 2);
		Sale mangoSale2 = new Sale("mango", 7);
		List<Sale> listOfSales = new ArrayList<Sale>();
		listOfSales.add(appSale1);listOfSales.add(appSale2); listOfSales.add(appSale3);
		listOfSales.add(orgSale1);
		listOfSales.add(mangoSale1);listOfSales.add(mangoSale2);
		listOfSales.add(bananaSale1);listOfSales.add(bananaSale2);
		listOfSales.add(pearSale1);	listOfSales.add(pearSale2);
		Map<String, List<Double>> productSalesMap = effSalesCal
				.computeEffectiveSales(listOfSales);

		assertEquals(3, productSalesMap.get("apple").size());
		assertEquals(2, productSalesMap.get("banana").size());
		assertEquals(2, productSalesMap.get("pear").size());
		assertEquals(2, productSalesMap.get("mango").size());
		assertEquals(1, productSalesMap.get("orange").size());

		List<Double> appleSales = productSalesMap.get("apple");
		double totalAppleSales = appleSales.get(0) + appleSales.get(1)
				+ appleSales.get(2);
		
		assertEquals(16.0, totalAppleSales,0);
	}
	
	@Test
	public void TestComputeEffectiveSales_AllSalesTypes() {
		EffectiveSalesCalculator effSalesCal = new EffectiveSalesCalculator();
		Sale appSale1 = new Sale("apple", 4);
		Sale appSale2 = new Sale("apple", 3);
		Sale orgSale1 = new Sale("orange", 4);
		Sale appSale3 = new Sale("apple", 9);
		Sale bananaSale1 = new Sale("banana", 6);
		Sale pearSale1 = new Sale("pear", 7);
		Sale multiSale = new MultipleSale("orange", 2, 5);// 5 Orange sales of amt 2 each
		Sale adjSale1 = new AdjustmentSale(7, "orange", 2, AdjustmentType.ADD); //Another Orange sale of amt 7 & adj of +2 to all previous Orange sales
		Sale adjSale2 = new AdjustmentSale(3, "banana", 2, AdjustmentType.MULTIPLY); //Another Banana sale of amt 3 & adj of *2 to all previous Banana sales
		Sale adjSale3 = new AdjustmentSale(4, "pear", 2, AdjustmentType.SUBTRACT);
		List<Sale> listOfSales = new ArrayList<Sale>();
		listOfSales.add(appSale1);listOfSales.add(appSale2); listOfSales.add(appSale3);
		listOfSales.add(orgSale1);
		listOfSales.add(multiSale);
		listOfSales.add(bananaSale1);
		listOfSales.add(pearSale1);	
		listOfSales.add(adjSale1); listOfSales.add(adjSale2);listOfSales.add(adjSale3);
		Map<String, List<Double>> productSalesMap = effSalesCal
				.computeEffectiveSales(listOfSales);

		assertEquals(3, productSalesMap.get("apple").size());
		assertEquals(2, productSalesMap.get("banana").size()); 
		assertEquals(7, productSalesMap.get("orange").size());// 1 individual & 5 added as part of Multisale, 1 with Adjusment sale.
		assertEquals(2, productSalesMap.get("pear").size());
		

		List<Double> orangeSales = productSalesMap.get("orange");
		double totalOrangeSales = orangeSales.get(0) + orangeSales.get(1)
				+ orangeSales.get(2)+ orangeSales.get(3) + orangeSales.get(4) + orangeSales.get(5)+ orangeSales.get(6);
		
		List<Double> bananaSales = productSalesMap.get("banana");
		double totalBananSales = bananaSales.get(0) + bananaSales.get(1);
		
		List<Double> pearSales = productSalesMap.get("pear");
		double totalPearSales = pearSales.get(0) + pearSales.get(1);
		
		assertEquals(35.0, totalOrangeSales,0); //(4+2) + (2+2) + (2+2) + (2+2) + (2+2) + (2+2) + (7+2)
		assertEquals(18.0, totalBananSales,0); //(6*2) + (3*2)=18.
		assertEquals(7.0, totalPearSales,0); //(7-2) + (4-2)=7
	}

}