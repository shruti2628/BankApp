package com.bankapp.dto;

import java.time.LocalDateTime;

import com.bankapp.entities.Transaction;
import com.bankapp.entities.TransactionStatus;
import com.bankapp.entities.TransactionType;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class TransactionResponseDto {
	private Long id;
    private TransactionType type;
    private Double amount;
    private TransactionStatus status;
    private LocalDateTime transactionTime;

    private String accountNumber;
    private Double accountBalance;

    private String performedBy;
    private String approvedBy;

    public static TransactionResponseDto from(Transaction tx) {
        return TransactionResponseDto.builder()
                .id(tx.getId())
                .type(tx.getType())
                .amount(tx.getAmount())
                .status(tx.getStatus())
                .transactionTime(tx.getTransactionTime())
                .accountNumber(tx.getAccount().getAccountNumber())
                .accountBalance(tx.getAccount().getBalance())
                .performedBy(tx.getPerformedBy().getUsername())
                .approvedBy(
                        tx.getApprovedBy() != null
                                ? tx.getApprovedBy().getUsername()
                                : null
                )
                .build();
    }
}
