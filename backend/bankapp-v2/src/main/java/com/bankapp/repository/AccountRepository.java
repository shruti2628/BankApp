package com.bankapp.repository;

import com.bankapp.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByPanNumber(String panNumber);

    boolean existsByEmail(String email);
}