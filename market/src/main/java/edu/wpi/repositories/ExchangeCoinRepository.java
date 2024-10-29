package edu.wpi.repositories;

import edu.wpi.entities.CoinThumb;
import edu.wpi.entities.ExchangeCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeCoinRepository extends JpaRepository<ExchangeCoin, String> {

    // Fetch all visible trading pairs
    @Query("SELECT e FROM ExchangeCoin e WHERE e.visible = 1")
    List<ExchangeCoin> findAllVisible();

    // Fetch coin summary data (thumbnail information)
    @Query("SELECT new edu.wpi.entities.CoinThumb(e.symbol, k.openPrice, k.highestPrice, k.lowestPrice, k.closePrice, k.volume, k.turnover) " +
            "FROM ExchangeCoin e JOIN KLine k ON e.symbol = k.symbol WHERE e.visible = 1")
    List<CoinThumb> findAllThumbs();
}
