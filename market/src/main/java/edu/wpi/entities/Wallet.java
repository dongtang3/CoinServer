package edu.wpi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

// Wallet.java
@Entity
@Table(name = "wallet")
@Data
public class Wallet {
    @Id
    private String userId;
    
    private BigDecimal usdtBalance;
    
    @ElementCollection
    @CollectionTable(name = "coin_balances")
    private Map<String, BigDecimal> coinBalances = new HashMap<>();
    
    public void addCoinBalance(String symbol, BigDecimal amount) {
        BigDecimal current = coinBalances.getOrDefault(symbol, BigDecimal.ZERO);
        coinBalances.put(symbol, current.add(amount));
    }
    
    public void subtractCoinBalance(String symbol, BigDecimal amount) {
        BigDecimal current = coinBalances.getOrDefault(symbol, BigDecimal.ZERO);
        coinBalances.put(symbol, current.subtract(amount));
    }
    public BigDecimal getCoinBalance(String symbol) {
        return coinBalances.getOrDefault(symbol, BigDecimal.ZERO);
    }

    public void setUsdtBalance(BigDecimal balance) {
        this.usdtBalance = balance;
    }

}