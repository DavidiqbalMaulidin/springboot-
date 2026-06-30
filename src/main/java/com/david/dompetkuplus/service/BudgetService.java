package com.david.dompetkuplus.service;

import com.david.dompetkuplus.model.Budget;
import com.david.dompetkuplus.model.TransactionType;
import com.david.dompetkuplus.repository.BudgetRepository;
import com.david.dompetkuplus.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    public BudgetService(BudgetRepository budgetRepository,
                         TransactionRepository transactionRepository) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    public List<Budget> getAll() {
        return budgetRepository.findAll();
    }

    public void delete(Long id) {
        budgetRepository.deleteById(id);
    }

    // =========================
    // FIX UTAMA (SPENT)
    // =========================
    public BigDecimal calculateSpentByCategory(Long categoryId) {

        Double total = transactionRepository
                .sumAmountByCategoryAndType(categoryId, TransactionType.EXPENSE);

        return (total == null)
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(total);
    }

    public Budget getById(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget tidak ditemukan"));
    }

    public Budget save(Budget budget) {

        Optional<Budget> existing =
                budgetRepository.findByCategoryId(
                        budget.getCategory().getId());

        if(existing.isPresent()
                && !existing.get().getId().equals(budget.getId())) {

            throw new RuntimeException(
                    "Budget kategori tersebut sudah ada");
        }

        return budgetRepository.save(budget);
    }

}