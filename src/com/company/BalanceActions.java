package com.company;

import com.company.balance.GiftCardBalance;

@FunctionalInterface
public interface BalanceActions {
  boolean transferAccountBalanceToGiftCardBalance(double amount, GiftCardBalance giftCardBalance);
}
