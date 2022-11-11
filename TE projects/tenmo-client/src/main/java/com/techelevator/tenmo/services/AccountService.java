package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private final String baseUrl;

    public static final String API_BASE_URL = "http://localhost:8080/account/";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken;

    public void setAuthToken(String authToken) {this.authToken = authToken;}


    public Account getAccount(int id) {
        Account account = null;

        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + id, HttpMethod.GET, makeAuthEntity(),
                            Account.class);
            account = response.getBody();
            } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            }
        return account;
    }


    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }


    public AccountService(String url) {this.baseUrl = url;}


    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }


    public Account updateSendingAccount (Account sender, BigDecimal transferAmount) {
        BigDecimal newBalance;
        BigDecimal originalBalance = sender.getBalance();
        newBalance = originalBalance.subtract(transferAmount);
        sender.setBalance(newBalance);
        return sender;
    }


    public Account updateAccountInServer(Account account, int accountId) {
        Account updatedAccount = new Account();
        try{
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + accountId, HttpMethod.PUT, makeAccountEntity(account), Account.class);
            updatedAccount = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return updatedAccount;
    }


    public Account updateReceivingAccount (Account receiver, BigDecimal transferAmount) {
        BigDecimal newBalance;
        BigDecimal originalBalance = receiver.getBalance();
        newBalance = originalBalance.add(transferAmount);
        receiver.setBalance(newBalance);
        return receiver;
    }

    public boolean senderHasAdequateFunds(Account account, BigDecimal transferAmount) {
        boolean balanceCheckSuccessful = false;
        if(account.getBalance().compareTo(transferAmount) >= 0)
            balanceCheckSuccessful = true;
        return balanceCheckSuccessful;
    }

    public boolean sendBucksTransaction(Account sender, Account receiver, BigDecimal transferAmount) {
        boolean transactionSuccessful = false;
        if (transferAmount.compareTo(BigDecimal.valueOf(0)) > 0 && receiver.getUserId() != sender.getUserId()) {
            if (senderHasAdequateFunds(sender, transferAmount)) {
                updateSendingAccount(sender, transferAmount);
                updateAccountInServer(sender, sender.getUserId());
                updateReceivingAccount(receiver, transferAmount);
                updateAccountInServer(receiver, receiver.getUserId());
                transactionSuccessful = true;
            }
        }
        return transactionSuccessful;
    }


    public int getUserIdSearchByAccountId(int accountId) {
        Account account = new Account();
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + accountId, HttpMethod.GET, makeAuthEntity(),
                            Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account.getUserId();
    }


}
