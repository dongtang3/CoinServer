// TradeRequest.java
package edu.wpi.entities;

import java.math.BigDecimal;

public class TradeRequest {
    private String symbol;
    private BigDecimal amount;

    // Getters and setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}