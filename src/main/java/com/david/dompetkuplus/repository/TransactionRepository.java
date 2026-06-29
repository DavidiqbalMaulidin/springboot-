package com.david.dompetkuplus.repository;

import com.david.dompetkuplus.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}