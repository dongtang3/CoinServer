package edu.wpi.repositories;

import edu.wpi.entities.ExchangeTrade;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExchangeTradeRepository extends JpaRepository<ExchangeTrade, Long> {

    // Fetch the latest trade records for a specific symbol
    List<ExchangeTrade> findBySymbolOrderByTimeDesc(String symbol, Pageable pageable);
}
