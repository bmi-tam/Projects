package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transfer/")
//@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao dao;
    public TransferController(TransferDao dao) {this.dao = dao;}


    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Transfer> list() {
        return dao.list();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public Transfer getByTransferId(@PathVariable int transferId) {
        Transfer matchedTransfer = dao.searchByTransferId(transferId);

        if (matchedTransfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        } else {
            return matchedTransfer;
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "create", method = RequestMethod.POST)
    public Transfer create(@RequestBody Transfer newTransfer) {
        Transfer transfer = dao.createTransfer(newTransfer);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to create Transfer Log");
        } else {
            return transfer;
        }
    }


}
