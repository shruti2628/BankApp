package com.bankapp.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.dto.TransactionResponseDto;
import com.bankapp.entities.Transaction;
import com.bankapp.service.TransactionService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/manager/transactions")
public class ManagerTransactionController {

    private final TransactionService transactionService;

    public ManagerTransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{transactionId}/approve")
    public TransactionResponseDto approveTransaction(
            @PathVariable Long transactionId,
            Authentication authentication) {

        String managerUsername = authentication.getName();

        Transaction transaction =
                transactionService.approveTransaction(transactionId, managerUsername);

        return TransactionResponseDto.from(transaction);
    }

    
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{transactionId}/reject")
    public Transaction rejectTransaction(@PathVariable Long transactionId,
                                          Authentication authentication) {

        String managerUsername = authentication.getName();
        return transactionService.rejectTransaction(transactionId, managerUsername);
    }
    
    
    
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/pending")
    public List<TransactionResponseDto> getPendingApprovals() {
        return transactionService.getPendingApprovalTransactions();
    }
    
    

}
