package edu.wpi.repositories;

import edu.wpi.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByUserId(String userId);
    List<Trade> findBySymbol(String symbol);
    List<Trade> findByUserIdAndSymbol(String userId, String symbol);
    List<Trade> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT t FROM Trade t WHERE t.userId = :userId AND t.timestamp >= :startTime")
    List<Trade> findRecentTradesByUser(@Param("userId") String userId,
                                     @Param("startTime") LocalDateTime startTime);
}