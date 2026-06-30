package com.david.dompetkuplus.dto;

import java.math.BigDecimal;

public class BudgetAnalyticsDTO {

    private String categoryName;
    private BigDecimal limitAmount;
    private BigDecimal spentAmount;
    private double percentage;
    private String status;

    public BudgetAnalyticsDTO(
            String categoryName,
            BigDecimal limitAmount,
            BigDecimal spentAmount,
            double percentage,
            String status
    ) {
        this.categoryName = categoryName;
        this.limitAmount = limitAmount;
        this.spentAmount = spentAmount;
        this.percentage = percentage;
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getStatus() {
        return status;
    }
}