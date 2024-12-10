package edu.wpi.entities;

import java.math.BigDecimal;

public class WalletBalanceResponse {
    private BigDecimal balance;

    public WalletBalanceResponse(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}