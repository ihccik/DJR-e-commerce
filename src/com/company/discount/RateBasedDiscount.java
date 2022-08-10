package com.company.discount;

import java.util.UUID;

public class RateBasedDiscount extends Discount{

    private Double rateAmount;


    public RateBasedDiscount(UUID id, String name, Double thresholdAmount, Double rateAmount) {
        super(id, name, thresholdAmount);
        this.rateAmount = rateAmount;
    }

    @Override
    public Double calculateCartAmountAfterDiscountApplied(Double amount) {
        return amount - (amount * rateAmount / 100) ;

        // 400 %15 = 340
    }

    public Double getRateAmount() {
        return rateAmount;
    }
}
