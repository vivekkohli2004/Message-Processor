package com.jpm.processor.message.process;

import static com.jpm.processor.message.process.MsgProcessorConstants.BATCH_SIZE;
import static com.jpm.processor.message.process.MsgProcessorConstants.MAX_MSGS;

import java.util.ArrayList;
import java.util.List;

import com.jpm.processor.message.sale.entity.Sale;

public class MessageProcessor {

	private ReportsGenerator reportsHandler;

	private List<Sale> allMessages;

	public MessageProcessor() {
		allMessages = new ArrayList<Sale>();
		reportsHandler = new ReportsGenerator();
	}

	/*
	 * This method accepts all possible Sales messages polymorphically offering
	 * a unified interface to clients.
	 */
	public void performSale(Sale sale) {
		allMessages.add(sale);// All Sales messages would be recorded in this
								// List. (Requirement #1)
		handleReportGeneration();
	}

	private void handleReportGeneration() {
		if (allMessages.size() % BATCH_SIZE == 0) {
			reportsHandler.generateSalesReport(allMessages);
		}

		if (allMessages.size() == MAX_MSGS) {
			System.out
					.println("\nMAXIMUM MESSAGE PROCESING LIMIT REACHED. SYSTEM WOULD GO DOWN NOW. HERE ARE THE ADJUSTMENTS MADE TO SALES....");
			reportsHandler.generateAdjustmentReport(allMessages);
			System.exit(0);
		}
	}

	public List<Sale> getAllMessages() {
		return allMessages;
	}

	public void setAllMessages(List<Sale> allMessages) {
		this.allMessages = allMessages;
	}
}
