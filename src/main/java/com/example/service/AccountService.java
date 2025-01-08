package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account findAccountByUsername(String username) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            return null;
        }
    }

    public Account findAccountById(int id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            return null;
        }
    }

}
