package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    public JdbcTransferDao(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}


    @Override
    public List<Transfer> list() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }



    @Override
    public Transfer searchByTransferId(int transferId) {
        Transfer matchedTransfer = new Transfer();

        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);

        if (result.next()) {
            matchedTransfer = mapRowToTransfer(result);
        }
        return matchedTransfer;
    }


    @Override
    public Transfer createTransfer(Transfer transfer) {

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(),
                transfer.getAccountTo(), transfer.getAmount());

        return searchByTransferId(id);
    }


    public Transfer mapRowToTransfer(SqlRowSet sql) {
        Transfer transfer = new Transfer();

        transfer.setTransferId(sql.getInt("transfer_id"));
        transfer.setTransferTypeId(sql.getInt("transfer_type_id"));
        transfer.setTransferStatusId(sql.getInt("transfer_status_id"));
        transfer.setAccountFrom(sql.getInt("account_from"));
        transfer.setAccountTo(sql.getInt("account_to"));
        transfer.setAmount(sql.getBigDecimal("amount"));

        return transfer;
    }



}
