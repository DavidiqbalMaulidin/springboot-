package com.david.dompetkuplus.repository;

import com.david.dompetkuplus.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // =========================
    // HITUNG TOTAL PENGELUARAN PER CATEGORY
    // =========================
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.category.id = :categoryId
        AND t.type = com.david.dompetkuplus.model.TransactionType.EXPENSE
    """)
    BigDecimal sumSpentByCategory(@Param("categoryId") Long categoryId);
    Optional<Budget> findByCategoryId(Long categoryId);
}
