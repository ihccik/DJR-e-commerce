package com.company.checkout;

import com.company.balance.Balance;
import com.company.Customer;

public interface CheckoutService {
    boolean checkout(Customer customer, Double totalAmount);
}
