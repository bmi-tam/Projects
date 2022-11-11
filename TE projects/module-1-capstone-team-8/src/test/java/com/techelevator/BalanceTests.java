package com.techelevator;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BalanceTests {

    @Test
    public void Test_GetBalance(){
        Balance test = new Balance();
        test.setBalance(10);
        Assert.assertEquals(BigDecimal.valueOf(10.00).setScale(2, RoundingMode.CEILING), test.getBalance());
    }

    @Test
    public void ProvideMoney_SetBalance(){
        Balance test = new Balance();
        test.setBalance(10);
        BigDecimal testBalance = BigDecimal.valueOf(10.00).setScale(2, RoundingMode.CEILING);
        Assert.assertEquals(testBalance, test.getBalance());
    }

    @Test
    public void Test_Purchase(){
        Balance test = new Balance();
        test.setBalance(10);
        test.purchase(10);
        BigDecimal testPurchase = BigDecimal.valueOf(0.00).setScale(2,RoundingMode.CEILING);
        Assert.assertEquals(testPurchase, test.getBalance());
    }

    @Test
    public void Test_GetChange(){
        Balance test = new Balance();
        test.setBalance(10);
        Assert.assertEquals("Quarters: " + "40" + "\nDimes: " + "0" + "\nNickels: " + "0" + "\nPennies: " + "0", test.getChange());
    }
}
