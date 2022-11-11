package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransferService {

    private AccountService accountService = new AccountService("http://localhost:8080/account/");
    private UserService userService = new UserService();

    public TransferService() {}

    public static final String API_TRANSFER_BASE_URL = "http://localhost:8080/transfer/";

    private static int transferStatusPending = 1;
    private static int transferStatusApproved = 2;
    private static int transferStatusRejected = 3;

    private static int transferTypeRequest = 1;
    private static int transferTypeSend = 2;

    private RestTemplate restTemplate = new RestTemplate();

    private String authToken;
    public void setAuthToken(String authToken) {this.authToken = authToken;}

    //-----------------------------------------------------------------------------------------------



    public Transfer createSendTransferRecord(Account senderAccount, Account receiverAccount, BigDecimal transferAmount) {
        Transfer newTransfer = new Transfer();
        if(accountService.sendBucksTransaction(senderAccount, receiverAccount, transferAmount)) {
            newTransfer.setTransferTypeId(transferTypeSend);
            newTransfer.setTransferStatusId(transferStatusApproved);
            newTransfer.setAccountFrom(senderAccount.getAccountId());
            newTransfer.setAccountTo(receiverAccount.getAccountId());
            newTransfer.setAmount(transferAmount);
        }
        return newTransfer;
    }


    public Transfer createTransferLogToServer(Transfer transfer) {
        Transfer createdTransfer = new Transfer();
        try{
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_TRANSFER_BASE_URL + "create", HttpMethod.POST, makeTransferEntity(transfer), Transfer.class);
            createdTransfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return createdTransfer;
    }


//CREATE METHODS TO CREATE LIST HERE AND THEN PRINT THE LISTS ONE AFTER THE OTHER


    public List<Transfer> getListOfAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        try{
            ResponseEntity<List<Transfer>> response =
                    restTemplate.exchange(API_TRANSFER_BASE_URL,
                            HttpMethod.GET, makeAuthEntity(), new ParameterizedTypeReference<List<Transfer>>(){});
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public List<Transfer> filterForAccountsSendingToUser(List<Transfer> transferList, int currentUserAccountId) {
        List<Transfer> newList = new ArrayList<>();
        for (Transfer transfer : transferList) {
            if (transfer.getAccountFrom() != currentUserAccountId && transfer.getAccountTo() == currentUserAccountId) {
                newList.add(transfer);
            }
        }
        return newList;
    }

    public List<Transfer> filterForAccountsReceivingFromUser(List<Transfer> transferList, int currentUserAccountId) {
        List<Transfer> newList = new ArrayList<>();
        for (Transfer transfer : transferList) {
            if (transfer.getAccountTo() != currentUserAccountId && transfer.getAccountFrom() == currentUserAccountId) {
                newList.add(transfer);
            }
        }
        return newList;
    }


    public void printTransfersFromList (List<Transfer> list, int currentUserAccountId) {
        for (Transfer transfer : list) {
            System.out.printf("%-8d  %-18s  %-9s", transfer.getTransferId(), getOtherUsername(transfer, currentUserAccountId), "$"+transfer.getAmount());
            System.out.println();
        }
        System.out.println();
    }


    public void printTransferDetails2 (List<Transfer> transfersList, int transferId) {
        System.out.println("----------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("----------------------------------------------");
        for (Transfer transfer : transfersList) {
            if (transfer.getTransferId() == transferId) {
                System.out.println("ID: " + transfer.getTransferId());
                System.out.println("From: " + userService.getUsernameForList(accountService.getUserIdSearchByAccountId(transfer.getAccountFrom())));
                System.out.println("To: " + userService.getUsernameForList(accountService.getUserIdSearchByAccountId(transfer.getAccountTo())));
                System.out.println("Type: " + transferType(transfer.getTransferTypeId()));
                System.out.println("Status: " + statusString(transfer.getTransferStatusId()));
                System.out.println("Amount: $" + transfer.getAmount());
            }
        }
    }

    public String transferType(int id) {
        String string = "";
        if (id == transferTypeRequest) {
            string = "Request";
        } else if (id == transferTypeSend) {
            string = "Send";
        }
        return string;
    }

    public String statusString(int id) {
        String string = "";
        if (id == transferStatusApproved) {
            string = "Approved";
        } else if (id == transferStatusPending) {
            string = "Pending";
        } else if (id == transferStatusRejected) {
            string = "Rejected";
        }
        return string;
    }

    public String getOtherUsername (Transfer transfer, int currentUserAccountId) {
        String fromToString = "";
        if (transfer.getAccountFrom() != currentUserAccountId) {

            int otherUserId = accountService.getUserIdSearchByAccountId(transfer.getAccountFrom());
            String otherUsername = userService.getUsernameForList(otherUserId);
            fromToString = "From: " + otherUsername;
        } else if (transfer.getAccountTo() != currentUserAccountId) {

            int otherUserId = accountService.getUserIdSearchByAccountId(transfer.getAccountTo());
            String otherUsername = userService.getUsernameForList(otherUserId);
            fromToString = "To:   " + otherUsername;
        }
        return fromToString;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }


}
