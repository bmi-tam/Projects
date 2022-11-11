package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

    List<Account>list();

    Account get(int id);

    Account getByAccountId(int id);

    Account update(Account updatedAccount, int id);

}
