package edu.wpi.repositories;

import edu.wpi.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    
    Optional<Wallet> findByUserId(String userId);
    
    @Query("SELECT w FROM Wallet w WHERE w.usdtBalance > :minBalance")
    List<Wallet> findWalletsWithMinimumBalance(@Param("minBalance") BigDecimal minBalance);
    
    @Query("SELECT w FROM Wallet w WHERE w.userId = :userId AND w.coinBalances[:symbol] > :amount")
    Optional<Wallet> findWalletWithSufficientCoinBalance(
        @Param("userId") String userId, 
        @Param("symbol") String symbol, 
        @Param("amount") BigDecimal amount
    );
    
    @Modifying
    @Query("UPDATE Wallet w SET w.usdtBalance = w.usdtBalance + :amount WHERE w.userId = :userId")
    int updateUsdtBalance(@Param("userId") String userId, @Param("amount") BigDecimal amount);
    
    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Wallet w " +
           "WHERE w.userId = :userId AND w.usdtBalance >= :requiredAmount")
    boolean hasEnoughUsdtBalance(
        @Param("userId") String userId, 
        @Param("requiredAmount") BigDecimal requiredAmount
    );
    
    List<Wallet> findByUsdtBalanceGreaterThan(BigDecimal amount);
    
    @Query("SELECT w FROM Wallet w WHERE w.userId = :userId")
    Optional<BigDecimal> getCoinBalance(
        @Param("userId") String userId

    );
}