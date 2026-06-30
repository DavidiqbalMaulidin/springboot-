package com.david.dompetkuplus.controller;

import com.david.dompetkuplus.dto.BudgetAnalyticsDTO;
import com.david.dompetkuplus.model.Budget;
import com.david.dompetkuplus.model.Transaction;
import com.david.dompetkuplus.model.TransactionType;
import com.david.dompetkuplus.repository.TransactionRepository;
import com.david.dompetkuplus.service.BudgetService;
import com.david.dompetkuplus.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.HashMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AnalyticsController {

    private final TransactionService transactionService;
    private final BudgetService budgetService;
    private final TransactionRepository transactionRepository;

    public AnalyticsController(
            TransactionService transactionService,
            BudgetService budgetService,
            TransactionRepository transactionRepository
    ) {
        this.transactionService = transactionService;
        this.budgetService = budgetService;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {

        // =========================
        // RINGKASAN KEUANGAN
        // =========================

        Double totalIncome = transactionService.getTotalIncome();
        Double totalExpense = transactionService.getTotalExpense();

        if (totalIncome == null) {
            totalIncome = 0.0;
        }

        if (totalExpense == null) {
            totalExpense = 0.0;
        }

        Double balance = totalIncome - totalExpense;

        double savingRate = 0;

        if (totalIncome > 0) {
            savingRate = (balance / totalIncome) * 100;
        }

        // =========================
        // TOP 5 PENGELUARAN
        // =========================

        List<Transaction> topExpenses =
                transactionRepository.findTop5ByTypeOrderByAmountDesc(
                        TransactionType.EXPENSE
                );

        Map<String, Double> expenseByCategory =
                new HashMap<>();

        for (Transaction trx : transactionRepository.findAll()) {

            if (trx.getType() == TransactionType.EXPENSE
                    && trx.getCategory() != null) {

                String category =
                        trx.getCategory().getName();

                double amount =
                        trx.getAmount().doubleValue();

                expenseByCategory.put(
                        category,
                        expenseByCategory.getOrDefault(
                                category,
                                0.0
                        ) + amount
                );
            }
        }

        // =========================
        // TRANSAKSI TERBARU
        // =========================

        List<Transaction> latestTransactions =
                transactionRepository.findTop10ByOrderByTransactionDateDesc();

        // =========================
        // BUDGET ANALYTICS
        // =========================

        List<Budget> budgets = budgetService.getAll();

        List<BudgetAnalyticsDTO> budgetAnalytics =
                new ArrayList<>();

        List<String> insights =
                new ArrayList<>();

        int safeCount = 0;
        int warningCount = 0;
        int overCount = 0;

        double highestPercentage = 0;
        String highestCategory = null;

        for (Budget budget : budgets) {

            if (budget.getCategory() == null) {
                continue;
            }

            BigDecimal spent =
                    budgetService.calculateSpentByCategory(
                            budget.getCategory().getId()
                    );

            if (spent == null) {
                spent = BigDecimal.ZERO;
            }

            BigDecimal limit =
                    budget.getLimitAmount();

            if (limit == null) {
                limit = BigDecimal.ZERO;
            }

            double percentage = 0;

            if (limit.compareTo(BigDecimal.ZERO) > 0) {

                percentage =
                        spent.divide(
                                        limit,
                                        4,
                                        RoundingMode.HALF_UP
                                )
                                .multiply(BigDecimal.valueOf(100))
                                .doubleValue();
            }

            String status;

            if (percentage < 80) {

                status = "AMAN";
                safeCount++;

            } else if (percentage < 100) {

                status = "WARNING";
                warningCount++;

            } else {

                status = "OVER";
                overCount++;
            }

            budgetAnalytics.add(
                    new BudgetAnalyticsDTO(
                            budget.getCategory().getName(),
                            limit,
                            spent,
                            percentage,
                            status
                    )
            );

            // =========================
            // INSIGHT BUDGET
            // =========================

            if (percentage >= 100) {

                insights.add(
                        "Budget kategori "
                                + budget.getCategory().getName()
                                + " telah melebihi batas."
                );

            } else if (percentage >= 80) {

                insights.add(
                        "Budget kategori "
                                + budget.getCategory().getName()
                                + " hampir mencapai limit."
                );
            }

            // =========================
            // KATEGORI DOMINAN
            // =========================

            if (percentage > highestPercentage) {

                highestPercentage = percentage;
                highestCategory =
                        budget.getCategory().getName();
            }
        }

        // =========================
        // INSIGHT TAMBAHAN
        // =========================

        insights.add(
                "Saving rate saat ini sebesar "
                        + Math.round(savingRate)
                        + "%."
        );

        if (highestCategory != null) {

            insights.add(
                    "Pengeluaran terbesar berasal dari kategori "
                            + highestCategory + "."
            );
        }

        // =========================
        // MODEL
        // =========================

        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("balance", balance);

        model.addAttribute(
                "chartIncome",
                totalIncome
        );

        model.addAttribute(
                "chartExpense",
                totalExpense
        );

        model.addAttribute("savingRate", Math.round(savingRate));

        model.addAttribute("safeCount", safeCount);
        model.addAttribute("warningCount", warningCount);
        model.addAttribute("overCount", overCount);

        model.addAttribute("topExpenses", topExpenses);
        model.addAttribute("latestTransactions", latestTransactions);

        model.addAttribute("budgets", budgets);
        model.addAttribute("budgetAnalytics", budgetAnalytics);
        model.addAttribute(
                "expenseByCategory",
                expenseByCategory
        );

        model.addAttribute("insights", insights);
        int healthScore;

        if (savingRate >= 70) {
            healthScore = 100;
        }
        else if (savingRate >= 50) {
            healthScore = 80;
        }
        else if (savingRate >= 30) {
            healthScore = 60;
        }
        else {
            healthScore = 40;
        }

        model.addAttribute(
                "healthScore",
                healthScore
        );

        return "analytics";
    }
}