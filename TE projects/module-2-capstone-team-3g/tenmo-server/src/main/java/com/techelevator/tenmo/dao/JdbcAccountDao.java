package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}


    @Override
    public List<Account> list() {
    List<Account> accounts = new ArrayList<>();
    String sql = "SELECT account_id, user_id, balance FROM account";
    SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
    while (results.next()) {
        accounts.add(mapRowToAccount(results));
    }
    return accounts;
    }


    @Override
    public Account get(int id) {

        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) { //true if there is a result {
                  return mapRowToAccount(results);
        }
        return null;
    }


    @Override
    public Account getByAccountId(int id) {

        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) { //true if there is a result {
            return mapRowToAccount(results);
        }
        return null;
    }



    @Override
    public Account update(Account updatedAccount, int id) {

        String sql = "UPDATE account " +
                "SET balance = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, updatedAccount.getBalance(), updatedAccount.getUserId());

        Account newAccount = new Account();

        String getNewAccount = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            newAccount = mapRowToAccount(results);
        }
        return newAccount;
    }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }

}
