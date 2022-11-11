package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/account")
//@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao dao;

    public  AccountController(AccountDao dao) {this.dao = dao; }


    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Account get(@PathVariable int id) {
        Account account = dao.get(id);
        if (account == null) {
            account = dao.getByAccountId(id);
            if (account == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
            } else {
                return account;
            }
        } else {
            return account;
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Account> list() {
            return dao.list();
        }



    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Account updateAccount(@RequestBody Account updatedAccount, @PathVariable int id) {
        Account updateAccount = dao.update(updatedAccount, id);
        if (updateAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account User not found.");
        } else {
            return updateAccount;
        }
    }


}
