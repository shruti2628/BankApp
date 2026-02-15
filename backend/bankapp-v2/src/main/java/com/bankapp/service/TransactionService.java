package com.bankapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankapp.dto.TransactionResponseDto;
import com.bankapp.entities.Account;
import com.bankapp.entities.AuditLog;
import com.bankapp.entities.Role;
import com.bankapp.entities.Transaction;
import com.bankapp.entities.TransactionStatus;
import com.bankapp.entities.TransactionType;
import com.bankapp.entities.User;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.AuditLogRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.repository.UserRepository;

@Service
public class TransactionService {

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	private final UserRepository userRepository;

	private static final double APPROVAL_LIMIT = 200000;

	private final AuditLogRepository auditLogRepository;


	public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository,
			UserRepository userRepository, AuditLogRepository auditLogRepository) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.userRepository = userRepository;
		this.auditLogRepository = auditLogRepository;
	}

	@Transactional
	public Transaction deposit(String accountNumber, Double amount, String clerkUsername) {

		if (amount <= 0) {
			throw new IllegalArgumentException("Deposit amount must be positive");
		}
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new RuntimeException("Account not found"));

		User clerk = userRepository.findByUsername(clerkUsername)
				.orElseThrow(() -> new RuntimeException("Clerk not found"));

		account.setBalance(account.getBalance() + amount);

		Transaction transaction = Transaction.builder().account(account).type(TransactionType.DEPOSIT).amount(amount)
				.performedBy(clerk).status(TransactionStatus.COMPLETED).build();

		accountRepository.save(account);
		Transaction savedTransaction = transactionRepository.save(transaction);

		AuditLog log = new AuditLog();
		log.setUsername(clerkUsername);
		log.setRole(clerk.getRole());
		log.setAction("DEPOSIT");
		log.setTransactionId(savedTransaction.getId());

		auditLogRepository.save(log);

		return savedTransaction;
		// return transactionRepository.save(transaction);
	}

	@Transactional
	public Transaction withdraw(String accountNumber, Double amount, String clerkUsername) {

		if (amount <= 0) {
			throw new IllegalArgumentException("Withdrawal amount must be positive");
		}

		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new RuntimeException("Account not found"));

		User clerk = userRepository.findByUsername(clerkUsername)
				.orElseThrow(() -> new RuntimeException("Clerk not found"));

		if (account.getBalance() < amount) {
			throw new RuntimeException("Insufficient balance");
		}

		TransactionStatus status;

		if (amount <= APPROVAL_LIMIT) {
			// Direct withdrawal
			account.setBalance(account.getBalance() - amount);
			status = TransactionStatus.COMPLETED;
		} else {
			// Needs manager approval
			status = TransactionStatus.PENDING_APPROVAL;
		}

		Transaction transaction = Transaction.builder().account(account).type(TransactionType.WITHDRAWAL).amount(amount)
				.performedBy(clerk).status(status).build();

		if (status == TransactionStatus.COMPLETED) {
			accountRepository.save(account);
		}

		Transaction savedTransaction = transactionRepository.save(transaction);

		AuditLog log = new AuditLog();
		log.setUsername(clerkUsername);
		log.setRole(clerk.getRole());
		log.setAction("WITHDRAW");
		log.setTransactionId(savedTransaction.getId());

		auditLogRepository.save(log);

		return savedTransaction;
		// return transactionRepository.save(transaction);
	}

	@Transactional
	public Transaction approveTransaction(Long transactionId, String managerUsername) {

		Transaction transaction = transactionRepository.findById(transactionId)
				.orElseThrow(() -> new RuntimeException("Transaction not found"));

		if (transaction.getStatus() != TransactionStatus.PENDING_APPROVAL) {
			throw new RuntimeException("Transaction is not pending approval");
		}

		if (transaction.getType() != TransactionType.WITHDRAWAL) {
			throw new RuntimeException("Only WITHDRAWAL transactions require approval");
		}

		User manager = userRepository.findByUsername(managerUsername)
				.orElseThrow(() -> new RuntimeException("Manager not found"));

		if (manager.getRole() != Role.MANAGER) {
			throw new RuntimeException("Only managers can approve transactions");
		}

		Account account = transaction.getAccount();

		if (account.getBalance() < transaction.getAmount()) {
			throw new RuntimeException("Insufficient balance at approval time");
		}

		account.setBalance(account.getBalance() - transaction.getAmount());
		transaction.setStatus(TransactionStatus.COMPLETED);
		transaction.setApprovedBy(manager);

		accountRepository.save(account);
		Transaction savedTransaction = transactionRepository.save(transaction);

		AuditLog log = new AuditLog();
		log.setUsername(managerUsername);
		log.setRole(Role.MANAGER);
		log.setAction("APPROVE");
		log.setTransactionId(transaction.getId());

		auditLogRepository.save(log);
		return savedTransaction;

		// return transactionRepository.save(transaction);
	}

	@Transactional
	public Transaction rejectTransaction(Long transactionId, String managerUsername) {

		Transaction transaction = transactionRepository.findById(transactionId)
				.orElseThrow(() -> new RuntimeException("Transaction not found"));

		if (transaction.getStatus() != TransactionStatus.PENDING_APPROVAL) {
			throw new RuntimeException("Only pending transactions can be rejected");
		}

		User manager = userRepository.findByUsername(managerUsername)
				.orElseThrow(() -> new RuntimeException("Manager not found"));

		if (manager.getRole() != Role.MANAGER) {
			throw new RuntimeException("Only managers can reject transactions");
		}

		transaction.setStatus(TransactionStatus.REJECTED);
		transaction.setApprovedBy(manager);

		Transaction savedTransaction = transactionRepository.save(transaction);

		AuditLog log = new AuditLog();
		log.setUsername(managerUsername);
		log.setRole(Role.MANAGER);
		log.setAction("REJECT");
		log.setTransactionId(savedTransaction.getId());

		auditLogRepository.save(log);

		return savedTransaction;
		// return transactionRepository.save(transaction);
	}

	@Transactional(readOnly = true)
	public Page<Transaction> getTransactionsByAccount(String accountNumber, Pageable pageable) {

		return transactionRepository.findByAccount_AccountNumberOrderByIdDesc(accountNumber, pageable);
	}

	public List<TransactionResponseDto> getPendingApprovalTransactions() {

		return transactionRepository.findByStatus(TransactionStatus.PENDING_APPROVAL).stream()
				.map(TransactionResponseDto::from).toList();
	}

}
