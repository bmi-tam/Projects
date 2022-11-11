package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private AccountService accountService = new AccountService(API_BASE_URL + "account/");
    private TransferService transferService = new TransferService();
    private UserService userService = new UserService();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        // SET TOKENS
        accountService.setAuthToken(currentUser.getToken());
        userService.setAuthToken(currentUser.getToken());
        transferService.setAuthToken(currentUser.getToken());
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        System.out.println("Your current account balance is: $" +
                accountService.getAccount(currentUser.getUser().getId()).getBalance());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        System.out.println("----------------------------------------------");
        System.out.println("Transfers");
        System.out.printf("%-8s  %-18s  %-9s", "ID", "From/To", "Amount"); System.out.println();
        System.out.println("----------------------------------------------");

        //SETUP OVERALL LISTS AND ACCOUNT INFO
        List<Transfer> allTransfers = transferService.getListOfAllTransfers();
        Account currentUserAccount = accountService.getAccount(currentUser.getUser().getId());
        int currentUserAccountId = currentUserAccount.getAccountId();
        List<Transfer> accountsToUser = transferService.filterForAccountsSendingToUser(allTransfers, currentUserAccountId);
        List<Transfer> accountsReceivedFromUser = transferService.filterForAccountsReceivingFromUser(allTransfers, currentUserAccountId);


        //PRINT FILTERED TRANSFERS FOR CURRENT USER
        transferService.printTransfersFromList(accountsToUser, currentUserAccountId);
        transferService.printTransfersFromList(accountsReceivedFromUser, currentUserAccountId);
        System.out.println("----------------------------------------------");

        //PRINT DETAILED TRANSFER REPORT
        int transferIdForDetails = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        transferService.printTransferDetails2(allTransfers, transferIdForDetails);
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
        System.out.println("Oops! Feature coming soon!");
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        Account currentUserAccount = accountService.getAccount(currentUser.getUser().getId());
        Account recipientAccount = new Account();

        // Determine User to receive funds
        userService.printUsers(currentUser.getUser().getId());
        int receivingUserId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
        BigDecimal transferAmount = BigDecimal.valueOf(consoleService.promptForInt("Enter Amount: "));
        recipientAccount = accountService.getAccount(receivingUserId);

        // Perform Transaction
        boolean sendComplete = accountService.sendBucksTransaction(currentUserAccount, recipientAccount, transferAmount);

        //Create Transfer Record
        Transfer newTransfer = transferService.createSendTransferRecord(currentUserAccount, recipientAccount, transferAmount);
        transferService.createTransferLogToServer(newTransfer);

        //ADD MESSAGE TO INFORM THIS WAS COMPLETED
        if (sendComplete) {
            System.out.println("Send Transaction Completed!");
        } else {
            System.out.println("Error, we could not perform this transaction");
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
        System.out.println("Oops! Feature coming soon!");
	}

}
