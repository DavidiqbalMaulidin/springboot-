package com.david.dompetkuplus.controller;


import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;
import com.david.dompetkuplus.model.Transaction;
import com.david.dompetkuplus.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransactionController {

    private final TransactionService service;


    
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/transactions")
    public String transactions(Model model) {

        model.addAttribute(
                "transactions",
                service.findAll()
        );

        return "transactions";
    }

    @GetMapping("/transactions/add")
    public String addTransaction(Model model) {

        model.addAttribute(
                "transaction",
                new Transaction()
        );

        return "add-transaction";
    }

    @PostMapping("/transactions/save")
    public RedirectView saveTransaction(
            @ModelAttribute Transaction transaction) {

        service.save(transaction);

        return new RedirectView("/transactions");
    }

    @GetMapping("/transactions/edit/{id}")
    public String editTransaction(
            @PathVariable Long id,
            Model model) {

        Transaction transaction =
                service.findById(id);

        model.addAttribute(
                "transaction",
                transaction
        );

        return "edit-transaction";
    }

    @PostMapping("/transactions/update")
    public RedirectView updateTransaction(
            @ModelAttribute Transaction transaction) {

        service.save(transaction);

        return new RedirectView("/transactions");
    }

    @GetMapping("/transactions/delete/{id}")
    public RedirectView deleteTransaction(@PathVariable Long id) {

        service.delete(id);

        return new RedirectView("/transactions");
    }

    }