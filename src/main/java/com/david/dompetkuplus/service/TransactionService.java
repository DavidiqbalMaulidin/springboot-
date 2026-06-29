package com.david.dompetkuplus.service;

import com.david.dompetkuplus.model.Transaction;
import com.david.dompetkuplus.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Transaction findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}