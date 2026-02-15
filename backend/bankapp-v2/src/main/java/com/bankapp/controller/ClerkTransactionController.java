package com.bankapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.dto.TransactionResponseDto;
import com.bankapp.dto.TransactionResponseDto;
import com.bankapp.entities.Transaction;
import com.bankapp.service.TransactionService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/clerk/transactions")
public class ClerkTransactionController {

    private final TransactionService transactionService;
    

    public ClerkTransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PreAuthorize("hasRole('CLERK')")
    @PostMapping("/deposit")
    public Transaction deposit(@RequestParam String accountNumber,
                               @RequestParam Double amount,
                               Authentication authentication) {

        String clerkUsername = authentication.getName();
        return transactionService.deposit(accountNumber, amount, clerkUsername);
    }
    
    @PreAuthorize("hasRole('CLERK')")
    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestParam String accountNumber,
                                @RequestParam Double amount,
                                Authentication authentication) {

        String clerkUsername = authentication.getName();
        return transactionService.withdraw(accountNumber, amount, clerkUsername);
    }

    @PreAuthorize("hasRole('CLERK')")
    @GetMapping("/account/{accountNumber}")
    public Page<TransactionResponseDto> getTransactionsByAccount(
            @PathVariable String accountNumber,
            Pageable pageable) {

        return transactionService
                .getTransactionsByAccount(accountNumber, pageable)
                .map(TransactionResponseDto::from);
    }

}
