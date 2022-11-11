package com.techelevator;

import org.junit.Assert;
import org.junit.Test;

public class VendingMachineTests {

    @Test
    public void Test_ReadInventoryForPrice(){
        VendingMachine test = new VendingMachine();
        test.addToInventoryList();
        double price = Double.parseDouble(String.valueOf(test.readInventoryForPrice("A1")));
        Assert.assertEquals(3.05, price, 0);
    }

    @Test
    public void Test_AdjustStock(){
        VendingMachine test = new VendingMachine();
        test.addToInventoryList();
        Product testProduct = test.getCurrentProduct("A1");
        test.adjustStock("A1");
        Assert.assertEquals(4, testProduct.getNumberOFItems());
    }

    @Test
    public void Test_InventoryItemLine(){
        VendingMachine test = new VendingMachine();
        test.addToInventoryList();
        Assert.assertEquals("A1" + " | " + "Potato Crisps" + " | " + "5" + " items remaining", test.inventoryItemLine("A1"));
    }

    @Test
    public void Test_LogItemLine(){
        VendingMachine test = new VendingMachine();
        test.addToInventoryList();
        Assert.assertEquals("A1" + " | " + "Potato Crisps", test.logItemLine("A1"));
    }

    @Test
    public void Test_ItemSound(){
        VendingMachine test = new VendingMachine();
        test.addToInventoryList();
        Assert.assertEquals("Crunch Crunch, Yum!", test.itemSound("A1"));
    }

}

