package com.bankapp.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.entities.Account;
import com.bankapp.service.AccountService;

@RestController
@RequestMapping("/manager/accounts")
public class ManagerAccountController {

    private final AccountService accountService;

    public ManagerAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/create")
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }
    
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }
}
