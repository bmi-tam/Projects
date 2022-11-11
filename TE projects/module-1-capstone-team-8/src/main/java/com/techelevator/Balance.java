package com.techelevator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Balance {

    private BigDecimal balance = BigDecimal.valueOf(0.00).setScale(2,RoundingMode.CEILING);
    private BigDecimal quarter = BigDecimal.valueOf(0.25);
    private BigDecimal dime = BigDecimal.valueOf(0.10);
    private BigDecimal nickel = BigDecimal.valueOf(0.05);
    private BigDecimal penny = BigDecimal.valueOf(0.01);

    int quarterCount = 0;
    int dimeCount = 0;
    int nickelCount = 0;
    int pennyCount = 0;

//***************************************
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(double insertMoney) {
        balance = balance.add(BigDecimal.valueOf(insertMoney));
    }


    public void purchase(double itemPrice) {
        balance = balance.subtract(BigDecimal.valueOf(itemPrice));
    }


    public String getChange() {
        String changeInCoins = "";
        while (balance.compareTo(quarter) >= 0) {
            balance = balance.subtract(quarter);
            quarterCount++;
        }
        while (balance.compareTo(dime) >= 0) {
            balance = balance.subtract(dime);
            dimeCount++;
        }
        while (balance.compareTo(nickel) >= 0) {
            balance = balance.subtract(nickel);
            nickelCount++;
        }
        while (balance.compareTo(penny) >= 0) {
            balance = balance.subtract(penny);
            pennyCount++;
        }
        changeInCoins = "Quarters: " + quarterCount + "\nDimes: " + dimeCount + "\nNickels: " + nickelCount + "\nPennies: " + pennyCount;
        return changeInCoins;
    }
}
