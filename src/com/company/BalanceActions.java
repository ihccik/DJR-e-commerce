package com.company;

import com.company.balance.GiftCardBalance;

public interface BalanceActions {
  boolean transferAccountBalanceToGiftCardBalance(double amount, GiftCardBalance giftCardBalance);
}
