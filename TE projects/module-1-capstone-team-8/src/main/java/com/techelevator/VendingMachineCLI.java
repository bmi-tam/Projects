package com.techelevator;

import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachineCLI {

	Scanner inputScan = new Scanner(System.in);

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT };

	private static final String SUB_MENU_FEED_MONEY = "Feed Money";
	private static final String SUB_MENU_SELECT_PRODUCT = "Select Product";
	private static final String SUB_MENU_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] SUB_MENU_OPTIONS = {SUB_MENU_FEED_MONEY, SUB_MENU_SELECT_PRODUCT, SUB_MENU_FINISH_TRANSACTION};

	private Menu menu;

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachine machine = new VendingMachine();
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
		machine.addToInventoryList();
	}

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {

		boolean vending = true;
		Balance balance = new Balance();
		VendingMachine machine = new VendingMachine();
		machine.addToInventoryList();
		File logFile = new File("log.txt");
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
		LocalDateTime timeNow = LocalDateTime.now();
		List<String> logList = new ArrayList<>();
		int logIndexCounter = 0;

		while (vending) {

			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				// display vending machine items
				machine.printInventory();

			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
				System.out.println();
				System.out.println("Your current balance is $" + balance.getBalance());
				String purchaseChoice = (String)menu.getChoiceFromOptions(SUB_MENU_OPTIONS);

				if(purchaseChoice.equals(SUB_MENU_FEED_MONEY)){
					try {
						System.out.println("How much money would you like to insert? (Whole Dollar Amount)");
						String inputMoney = inputScan.nextLine();
						Double moneyAdded = Double.parseDouble(inputMoney);
						//Check if input is an integer amount
						if (moneyAdded % 1 == 0) {
							balance.setBalance(moneyAdded);
							System.out.println("Your current balance is $" + balance.getBalance());
							logList.add(dateFormat.format(timeNow) + " " + "FEED MONEY: " + "$"+String.format("%.2f", moneyAdded) + " $" + balance.getBalance());
							logIndexCounter++;
						} else {
							System.out.println("Invalid input! Please enter a Whole Dollar (integer) amount");
						}
						} catch (NumberFormatException e) {
						System.out.println("Invalid input! Please enter a Whole Dollar (integer) amount");
					}
				} else if (purchaseChoice.equals(SUB_MENU_SELECT_PRODUCT)) {
					try{
						machine.printInventory();
						System.out.println();
						System.out.println("Your current balance is $" + balance.getBalance());
						System.out.println();
						System.out.println("Please select item code >>> ");
						String itemCodeInput = inputScan.nextLine();
						//Purchase methods after selection
						if (balance.getBalance().compareTo(BigDecimal.valueOf(Double.parseDouble(machine.getCurrentProduct(itemCodeInput).getPrice()))) >= 0) {
							if (machine.getCurrentProduct(itemCodeInput).getNumberOFItems() > 0) {
								balance.purchase(machine.readInventoryForPrice(itemCodeInput));
								machine.adjustStock(itemCodeInput);
								System.out.println(machine.inventoryItemLine(itemCodeInput));
								System.out.println("Your new balance is: $" + balance.getBalance());
								System.out.println();
								System.out.println(machine.itemSound(itemCodeInput));
								logList.add(dateFormat.format(timeNow) + " " + machine.logItemLine(itemCodeInput) + " $"+String.format("%.2f", machine.readInventoryForPrice(itemCodeInput)) + " $" + balance.getBalance());
								logIndexCounter++;
							} else {
								System.out.println("Item is out of Stock!");
							}
						} else {
							System.out.println("Insufficient Balance: Please insert additional money");
							System.out.println("Your current balance is $" + balance.getBalance());
						}
					} catch (Exception e) {
						System.out.println("Invalid item code");
					}
				}
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				logList.add(dateFormat.format(timeNow) + " " + "GIVE CHANGE: " + "$"+String.format("%.2f", balance.getBalance()) + " $0.00");
				//PRINT LOG TO FILE
				try (PrintWriter logOutput = new PrintWriter(logFile)) {
					for (String logInfo : logList) {
						logOutput.println(logInfo);
					}
				} catch (FileNotFoundException e) {
					System.out.println("Log file path is invalid");
				}
				System.out.println("\nYour change is: $" + balance.getBalance());
				System.out.println(balance.getChange());
				System.out.println("Thank you for using the Tech Elevator Munchinator Machine!");
				vending = false;
			}
		}
	}
}