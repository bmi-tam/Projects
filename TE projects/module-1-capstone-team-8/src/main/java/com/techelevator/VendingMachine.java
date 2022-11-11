package com.techelevator;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {

    File inventory = new File("vendingmachine.csv");

    public List<Product> products = new ArrayList<>();

    public void printInventory() {
        for (Product itemInfo : products) {
            System.out.printf("%-2s | %-19s | %-5s | %-16s", itemInfo.getSlotNumber(), itemInfo.getName(), itemInfo.getPrice(), itemInfo.getNumberOFItems()  + " items in stock\n");
        }
    }


    public Product getCurrentProduct(String itemCode) {
        Product currentProduct = null;
        try {
            for (Product itemInfo : products) {
                if (itemInfo.getSlotNumber().equals(itemCode)) {
                    currentProduct = itemInfo;
                }
            }
        } catch (Exception e) {
            System.out.println("Item code invalid");
        }
        return currentProduct;
    }


    public double readInventoryForPrice(String itemCode) {
        double itemPrice = 0.0;
        try {
            for (Product itemInfo : products) {
                if (itemInfo.getSlotNumber().equals(itemCode)) {
                    return Double.parseDouble(itemInfo.getPrice());
                }
            }
        } catch (Exception e) {
            System.out.println("Item code invalid");
        }
        return itemPrice;
    }


    public void adjustStock (String itemCode) {
        try {
            for (Product itemInfo : products) {
                if (itemInfo.getSlotNumber().equals(itemCode)) {
                    itemInfo.adjustStock();
                }
            }
        } catch (Exception e) {
            System.out.println("Item code invalid");
        }
    }


    public String inventoryItemLine(String itemCode) {
        String itemLine = "";
        try {
            for (Product itemInfo : products) {
                if (itemInfo.getSlotNumber().equals(itemCode)) {
                    itemLine = itemInfo.getSlotNumber() + " | " + itemInfo.getName() + " | " + itemInfo.getNumberOFItems() + " items remaining";
                }
            }
        } catch (Exception e) {
            System.out.println("Item code invalid");
        }
        return itemLine;
    }


    public String logItemLine(String itemCode) {
        String itemLine = "";
        try {
            for (Product itemInfo : products) {
                if (itemInfo.getSlotNumber().equals(itemCode)) {
                    itemLine = itemInfo.getSlotNumber() + " | " + itemInfo.getName();
                }
            }
        } catch (Exception e) {
            System.out.println("Item code invalid");
        }
        return itemLine;
    }


    public String itemSound(String itemCode) {
        String sound = "";
        try {
            for (Product itemInfo : products) {
                if (itemInfo.getSlotNumber().equals(itemCode)) {
                    sound = itemInfo.getSound();
                }
            }
        } catch (Exception e) {
            System.out.println("Item code invalid");
        }
        return sound;
    }


    public void addToInventoryList() {
        try (Scanner inventoryFile = new Scanner(inventory)) {
            while (inventoryFile.hasNextLine()) {
                String line = inventoryFile.nextLine();
                String[] productInfo = line.split("\\|");
                Product lineProduct = new Product(productInfo[0], productInfo[1], productInfo[2], productInfo[3]);
                products.add(lineProduct);
            }
        } catch (FileNotFoundException e) {
            System.out.println("There is no inventory");
        }
    }
}
