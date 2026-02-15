package com.bankapp.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bankapp.entities.Account;
import com.bankapp.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {

        if (accountRepository.existsByPanNumber(account.getPanNumber())) {
            throw new RuntimeException("PAN already exists");
        }

        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        account.setAccountNumber(generateAccountNumber());
        account.setBalance(0.0);

        return accountRepository.save(account);
    }
    
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }


    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(0, 8);
    }
}
