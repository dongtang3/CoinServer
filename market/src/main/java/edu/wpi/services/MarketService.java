package edu.wpi.services;

import edu.wpi.entities.CoinThumb;
import edu.wpi.entities.ExchangeCoin;
import edu.wpi.entities.ExchangeTrade;
import edu.wpi.entities.KLine;
import edu.wpi.repositories.ExchangeCoinRepository;
import edu.wpi.repositories.ExchangeTradeRepository;
import edu.wpi.repositories.KLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MarketService {

    @Autowired
    private ExchangeCoinRepository exchangeCoinRepository;

    @Autowired
    private KLineRepository kLineRepository;

    @Autowired
    private ExchangeTradeRepository exchangeTradeRepository;

    // Get all visible trading pairs
    public List<ExchangeCoin> findAllVisibleSymbols() {
        return exchangeCoinRepository.findAllVisible();
    }

    // Get coin summary data (thumbnail information)
    public List<CoinThumb> findAllSymbolThumb() {
        return exchangeCoinRepository.findAllThumbs();
    }

    // Get K-line historical data for a specific symbol within a time range and resolution
    public List<KLine> findKLineHistory(String symbol, long fromTime, long toTime, String period) {
        return kLineRepository.findBySymbolAndTimeBetweenAndPeriod(symbol, fromTime, toTime, period);
    }

    // Get the latest trade records for a specific symbol up to a certain size
    public List<ExchangeTrade> findLatestTrade(String symbol, int size) {
        Pageable pageable = PageRequest.of(0, size);
        return exchangeTradeRepository.findBySymbolOrderByTimeDesc(symbol, pageable);
    }

    // Calculate the total trading volume for a specific symbol within a time range
    public BigDecimal findTradeVolume(String symbol, long timeStart, long timeEnd) {
        return kLineRepository.calculateTotalVolume(symbol, timeStart, timeEnd);
    }
}
