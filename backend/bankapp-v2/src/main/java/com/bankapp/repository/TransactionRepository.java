package com.bankapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.entities.Transaction;
import com.bankapp.entities.TransactionStatus;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	Page<Transaction> findByAccount_AccountNumberOrderByIdDesc(
            String accountNumber,
            Pageable pageable
    );
	
	List<Transaction> findByStatus(TransactionStatus status);
	

}