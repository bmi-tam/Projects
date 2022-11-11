package com.techelevator;

import org.junit.Assert;
import org.junit.Test;

public class ProductTests {

    @Test
    public void Test_GetNumberOfItems(){
        Product test = new Product("A1", "Gum", "5.00", "Gum");
        Assert.assertEquals(5, test.getNumberOFItems());
    }

    @Test
    public void Test_GetSlotNumber(){
        Product test = new Product("A1", "Gum", "5.00", "Gum");
        Assert.assertEquals("A1", test.getSlotNumber());
    }

    @Test
    public void Test_GetPrice(){
        Product test = new Product("A1", "Gum", "5.00", "Gum");
        Assert.assertEquals("5.00", test.getPrice());
    }

    @Test
    public void Test_Name(){
        Product test = new Product("A1", "Gum", "5.00", "Gum");
        Assert.assertEquals("Gum", test.getName());
    }

    @Test
    public void Test_GetSound(){
        Product test = new Product("A1", "Gum", "5.00", "Gum");
        Assert.assertEquals("Chew Chew, Yum!", test.getSound());
    }

    @Test
    public void Test_AdjustStock(){
        Product test = new Product("A1", "Gum", "5.00", "Gum");
        test.adjustStock();
        Assert.assertEquals(4, test.getNumberOFItems());

    }

}
