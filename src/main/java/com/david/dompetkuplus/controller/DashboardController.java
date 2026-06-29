package com.david.dompetkuplus.controller;

import com.david.dompetkuplus.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final TransactionService transactionService;

    public DashboardController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        model.addAttribute("totalIncome", transactionService.getTotalIncome());
        model.addAttribute("totalExpense", transactionService.getTotalExpense());
        model.addAttribute("balance", transactionService.getBalance());
        model.addAttribute("totalTransaction", transactionService.getTotalTransaction());

        model.addAttribute(
                "transactions",
                transactionService.findLatestTransactions()
        );
        return "dashboard";
    }
}