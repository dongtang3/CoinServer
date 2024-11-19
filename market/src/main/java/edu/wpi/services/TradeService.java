package edu.wpi.services;

import edu.wpi.entities.ExchangeOrderDirection;
import edu.wpi.entities.Trade;
import edu.wpi.entities.Wallet;
import edu.wpi.exceptions.InsufficientBalanceException;
import edu.wpi.repositories.TradeRepository;
import edu.wpi.repositories.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

// TradeService.java
@Service
@Transactional
public class TradeService {
    private static final BigDecimal INITIAL_USDT_BALANCE = new BigDecimal("1000000");
    private final WalletRepository walletRepository;
    private final TradeRepository tradeRepository;

    public TradeService(WalletRepository walletRepository, TradeRepository tradeRepository) {
        this.walletRepository = walletRepository;
        this.tradeRepository = tradeRepository;
    }

    public Trade executeTrade(String userId, String symbol, BigDecimal amount, BigDecimal price, ExchangeOrderDirection type) {
        Wallet userWallet = walletRepository.findByUserId(userId)
            .orElseGet(() -> createWallet(userId));

        if (type == ExchangeOrderDirection.BUY) {
            return executeBuy(userWallet, symbol, amount, price);
        } else {
            return executeSell(userWallet, symbol, amount, price);
        }
    }

    private Trade executeBuy(Wallet wallet, String symbol, BigDecimal amount, BigDecimal price) {
        BigDecimal totalCost = amount.multiply(price);
        if (wallet.getUsdtBalance().compareTo(totalCost) < 0) {
            throw new InsufficientBalanceException("Insufficient USDT balance");
        }

        wallet.setUsdtBalance(wallet.getUsdtBalance().subtract(totalCost));
        wallet.addCoinBalance(symbol, amount);
        walletRepository.save(wallet);

        return createTrade(wallet.getUserId(), symbol, amount, price, ExchangeOrderDirection.BUY);
    }

    private Trade executeSell(Wallet wallet, String symbol, BigDecimal amount, BigDecimal price) {
        BigDecimal coinBalance = wallet.getCoinBalance(symbol);
        if (coinBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient " + symbol + " balance");
        }

        BigDecimal totalEarned = amount.multiply(price);
        wallet.setUsdtBalance(wallet.getUsdtBalance().add(totalEarned));
        wallet.subtractCoinBalance(symbol, amount);
        walletRepository.save(wallet);

        return createTrade(wallet.getUserId(), symbol, amount, price, ExchangeOrderDirection.SELL);
    }

    private Wallet createWallet(String userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setUsdtBalance(new BigDecimal("1000000")); // Initial 1M USDT
        wallet.setCoinBalances(new HashMap<>());
        return walletRepository.save(wallet);
    }

    private Trade createTrade(String userId, String symbol, BigDecimal amount,
                              BigDecimal price, ExchangeOrderDirection direction) {
        Trade trade = new Trade();
        trade.setUserId(userId);
        trade.setSymbol(symbol);
        trade.setAmount(amount); // Quantity of the asset
        trade.setPrice(price);   // Price per unit
        trade.setTotal(amount.multiply(price)); // Total value
        trade.setType(direction);
        trade.setTimestamp(LocalDateTime.now());

        return tradeRepository.save(trade);
    }
}


