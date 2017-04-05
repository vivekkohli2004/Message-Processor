package com.jpm.processor.message.process;

import java.util.ArrayList;
import java.util.List;

import com.jpm.processor.message.sale.entity.AdjustmentSale;
import com.jpm.processor.message.sale.entity.AdjustmentType;
import com.jpm.processor.message.sale.entity.MultipleSale;
import com.jpm.processor.message.sale.entity.Sale;

/***
 * This is the client class creating sample sale objects and calling the desired
 * business methods.
 **/
public class MessageProcessorClient {

	public static void main(String[] args) {

		List<Sale> sales = createSales();
		MessageProcessor msgProcessor = new MessageProcessor();
		for (Sale sale : sales)
			msgProcessor.performSale(sale);
	}

	private static List<Sale> createSales() {
		List<Sale> sales = new ArrayList<Sale>();
		for (int i = 1, app = 5, org = 3, mang = 4, ban = 2, pear = 3; i <= 9; i++, app += 3, org += 2, mang += 1, ban += 2, pear += 3) {
			Sale appSale = new Sale("apple", app);
			Sale orgSale = new Sale("orange", org);
			Sale mangSale = new Sale("mango", mang);
			Sale bananaSale = new Sale("banana", ban);
			Sale pearSale = new Sale("pear", pear);
			sales.add(appSale);
			sales.add(orgSale);
			sales.add(mangSale);
			sales.add(bananaSale);
			sales.add(pearSale);
			// 45 Sales messages
		}

		MultipleSale multiSale1 = new MultipleSale("orange", 3, 2); // 2 sales of amt 3 each
		MultipleSale multiSale2 = new MultipleSale("mango", 2, 3);// 3 sales of amt 2 each
		AdjustmentSale adjSale1 = new AdjustmentSale(3, "mango", 1, // A sale of amt 3 & adjustment of +1
				AdjustmentType.ADD);
		AdjustmentSale adjSale2 = new AdjustmentSale(6, "pear", 2, // A sale of amt 6 & adjustment of *2
				AdjustmentType.MULTIPLY);// This adj would not apply to sales made after this.
		AdjustmentSale adjSale3 = new AdjustmentSale(7, "pear", 2,
				AdjustmentType.MULTIPLY);

		sales.add(multiSale1);
		sales.add(multiSale2);
		sales.add(adjSale1);
		sales.add(adjSale2);
		sales.add(adjSale3); // 50 sales messages

		/***
		 * RUNNING THE LOOP AGAIN. BUT THIS WOULD NOT HAVE AN IMPACT.
		 **/
		for (int i = 1, app = 5, org = 3, mang = 4, ban = 2, pear = 3; i <= 9; i++, app += 3, org += 2, mang += 1, ban += 2, pear += 3) {
			Sale appSale = new Sale("apple", app);
			Sale orgSale = new Sale("orange", org);
			Sale mangSale = new Sale("mango", mang);
			Sale bananaSale = new Sale("banana", ban);
			Sale pearSale = new Sale("pear", pear);
			sales.add(appSale);
			sales.add(orgSale);
			sales.add(mangSale);
			sales.add(bananaSale);
			sales.add(pearSale);
		}

		System.out.println("NO OF SALES:" + sales.size());
		System.out.println("ALL SALES:" + sales);
		return sales;

	}

}
