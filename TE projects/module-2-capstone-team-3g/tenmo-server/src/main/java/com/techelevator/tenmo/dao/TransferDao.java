package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    ////////////////////////////////////////

    List<Transfer>list();


    Transfer createTransfer(Transfer transfer);

    Transfer searchByTransferId(int transferId);


}
