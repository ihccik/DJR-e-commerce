package com.company.balance;

import com.company.BalanceActions;
import java.util.UUID;

public class CustomerBalance extends Balance implements BalanceActions {

    public CustomerBalance(UUID customerId, Double balance) {
        super(customerId, balance);
    }

    @Override
    public Double addBalance(Double additionalBalance) {
        setBalance(getBalance() + additionalBalance);
        return getBalance();
    }

    @Override
    public boolean transferAccountBalanceToGiftCardBalance(double amount, GiftCardBalance giftCardBalance) {
        if (amount > getBalance()){
            throw new RuntimeException("Amount is bigger than your balance");
        }
        else {
            giftCardBalance.addBalance(amount);
            setBalance(getBalance() - amount);
        }
        return true;
    }
}
