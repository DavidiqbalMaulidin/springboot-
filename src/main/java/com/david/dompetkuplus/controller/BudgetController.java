package com.david.dompetkuplus.controller;

import com.david.dompetkuplus.model.Budget;
import com.david.dompetkuplus.service.BudgetService;
import com.david.dompetkuplus.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequestMapping("/budgeting")
public class BudgetController {

    private final BudgetService budgetService;
    private final CategoryService categoryService;

    public BudgetController(BudgetService budgetService,
                            CategoryService categoryService) {
        this.budgetService = budgetService;
        this.categoryService = categoryService;
    }

    // =========================
    // LIST BUDGET
    // =========================
    @GetMapping
    public String listBudget(Model model) {

        List<Budget> budgets = budgetService.getAll();

        for (Budget b : budgets) {

            if (b.getCategory() == null || b.getLimitAmount() == null) {
                b.setSpentAmount(BigDecimal.ZERO);
                b.setPercentage(0.0);
                continue;
            }

            Long categoryId = b.getCategory().getId();

            // spent dari service (WAJIB BigDecimal)
            BigDecimal spent = budgetService.calculateSpentByCategory(categoryId);

            b.setSpentAmount(spent);

            BigDecimal limit = b.getLimitAmount();

            double percentage = 0.0;

            if (limit.compareTo(BigDecimal.ZERO) > 0) {
                percentage = spent
                        .divide(limit, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
            }

            b.setPercentage(percentage);
        }

        model.addAttribute("budgets", budgets);
        return "budgeting";
    }

    // =========================
    // CREATE FORM
    // =========================
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("budget", new Budget());
        model.addAttribute("categories", categoryService.getAll());
        return "add-budget";
    }

    // =========================
    // SAVE
    // =========================
    @PostMapping("/save")
    public String save(@ModelAttribute Budget budget) {
        budgetService.save(budget);
        return "redirect:/budgeting";
    }

    // =========================
    // DELETE
    // =========================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        budgetService.delete(id);
        return "redirect:/budgeting";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        Budget budget = budgetService.getById(id);

        model.addAttribute("budget", budget);
        model.addAttribute("categories", categoryService.getAll());

        return "budget-edit";
    }

}