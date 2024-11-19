

import edu.wpi.entities.CoinThumb;
import edu.wpi.entities.ExchangeCoin;
import edu.wpi.entities.ExchangeTrade;
import edu.wpi.entities.KLine;
import edu.wpi.repositories.ExchangeCoinRepository;
import edu.wpi.repositories.ExchangeTradeRepository;
import edu.wpi.repositories.KLineRepository;
import edu.wpi.services.MarketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MarketServiceTest {

    @Mock
    private KLineRepository kLineRepository;

    @Mock
    private ExchangeCoinRepository exchangeCoinRepository;

    @Mock
    private ExchangeTradeRepository exchangeTradeRepository;

    @InjectMocks
    private MarketService marketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllVisibleSymbols() {
        ExchangeCoin coin = new ExchangeCoin();
        coin.setSymbol("BTC/USD");
        when(exchangeCoinRepository.findAllVisible()).thenReturn(Collections.singletonList(coin));

        List<ExchangeCoin> result = marketService.findAllVisibleSymbols();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BTC/USD", result.get(0).getSymbol());
    }

    @Test
    void testFindAllSymbolThumb() {
        CoinThumb thumb = new CoinThumb();
        thumb.setSymbol("BTC/USD");
        when(exchangeCoinRepository.findAllThumbs()).thenReturn(Collections.singletonList(thumb));

        List<CoinThumb> result = marketService.findAllSymbolThumb();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BTC/USD", result.get(0).getSymbol());
    }

    @Test
    void testFindKLineHistory() {
        KLine kLine = new KLine();
        kLine.setSymbol("BTC/USD");
        when(kLineRepository.findBySymbolAndTimeBetweenAndPeriod("BTC/USD", 1609459200L, 1609545600L, "1m"))
                .thenReturn(Collections.singletonList(kLine));

        List<KLine> result = marketService.findKLineHistory("BTC/USD", 1609459200L, 1609545600L, "1m");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BTC/USD", result.get(0).getSymbol());
    }

    @Test
    void testFindLatestTrade() {
        ExchangeTrade trade = new ExchangeTrade();
        trade.setSymbol("BTC/USD");
        when(exchangeTradeRepository.findBySymbolOrderByTimeDesc(eq("BTC/USD"), any()))
                .thenReturn(Collections.singletonList(trade));

        List<ExchangeTrade> result = marketService.findLatestTrade("BTC/USD", 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BTC/USD", result.get(0).getSymbol());
    }

    @Test
    void testFindTradeVolume() {
        when(kLineRepository.calculateTotalVolume("BTC/USD", 1609459200L, 1609545600L))
                .thenReturn(BigDecimal.valueOf(1000));

        BigDecimal result = marketService.findTradeVolume("BTC/USD", 1609459200L, 1609545600L);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(1000), result);
    }
}