package edu.wpi.repositories;

import edu.wpi.entities.KLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface KLineRepository extends JpaRepository<KLine, Long> {

    // Fetch K-line historical data for a specific symbol within a time range and period
    List<KLine> findBySymbolAndTimeBetweenAndPeriod(String symbol, long fromTime, long toTime, String period);

    // Calculate total trading volume for a specific symbol within a time range
    @Query("SELECT SUM(k.volume) FROM KLine k WHERE k.symbol = :symbol AND k.time BETWEEN :timeStart AND :timeEnd")
    BigDecimal calculateTotalVolume(String symbol, long timeStart, long timeEnd);

    Optional<KLine> findFirstBySymbolOrderByTimeDesc(String symbol);
}
