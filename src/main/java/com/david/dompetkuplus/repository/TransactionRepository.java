package com.david.dompetkuplus.repository;

import com.david.dompetkuplus.model.Transaction;
import com.david.dompetkuplus.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    @Query("""
        SELECT COALESCE(SUM(t.amount),0)
        FROM Transaction t
        WHERE t.type = :type
    """)
    Double sumByType(
            @Param("type") TransactionType type);

    @Query("""
        SELECT COALESCE(SUM(t.amount),0)
        FROM Transaction t
        WHERE t.category.id = :categoryId
        AND t.type = com.david.dompetkuplus.model.TransactionType.EXPENSE
    """)
    Double getExpenseByCategory(
            @Param("categoryId") Long categoryId);

    List<Transaction> findTop5ByOrderByTransactionDateDesc();

    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.category.id = :categoryId
        AND t.type = :type
    """)
    Double sumAmountByCategoryAndType(
            @Param("categoryId") Long categoryId,
            @Param("type") TransactionType type
    );

    List<Transaction> findTop10ByOrderByTransactionDateDesc();

    List<Transaction> findTop5ByTypeOrderByAmountDesc(
            TransactionType type
    );

}