import edu.wpi.MarketApplication; // Import your main application class
import edu.wpi.controllers.MarketController;
import edu.wpi.entities.CoinThumb;
import edu.wpi.entities.ExchangeCoin;
import edu.wpi.entities.ExchangeTrade;
import edu.wpi.entities.KLine;
import edu.wpi.services.MarketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = MarketApplication.class) // Specify the main application class
public class MarketControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MarketService marketService;

    @InjectMocks
    private MarketController marketController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(marketController).build();
    }

    @Test
    public void testFindAllSymbol() throws Exception {
        ExchangeCoin coin = new ExchangeCoin();
        coin.setSymbol("BTC/USD");
        Mockito.when(marketService.findAllVisibleSymbols()).thenReturn(Collections.singletonList(coin));

        mockMvc.perform(MockMvcRequestBuilders.get("/market/symbol"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").value("BTC/USD"));
    }

    @Test
    public void testFindSymbolThumb() throws Exception {
        CoinThumb thumb = new CoinThumb();
        thumb.setSymbol("BTC/USD");
        Mockito.when(marketService.findAllSymbolThumb()).thenReturn(Collections.singletonList(thumb));

        mockMvc.perform(MockMvcRequestBuilders.get("/market/symbol-thumb"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").value("BTC/USD"));
    }

    @Test
    public void testFindKHistory() throws Exception {
        KLine kLine = new KLine();
        kLine.setSymbol("BTC/USD");
        Mockito.when(marketService.findKLineHistory("BTC/USD", 1609459200L, 1609545600L, "1m"))
                .thenReturn(Collections.singletonList(kLine));

        mockMvc.perform(MockMvcRequestBuilders.get("/market/history")
                        .param("symbol", "BTC/USD")
                        .param("from", "1609459200")
                        .param("to", "1609545600")
                        .param("resolution", "1m"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").value("BTC/USD"));
    }

    @Test
    public void testLatestTrade() throws Exception {
        ExchangeTrade trade = new ExchangeTrade();
        trade.setSymbol("BTC/USD");
        Mockito.when(marketService.findLatestTrade("BTC/USD", 10)).thenReturn(Collections.singletonList(trade));

        mockMvc.perform(MockMvcRequestBuilders.get("/market/latest-trade")
                        .param("symbol", "BTC/USD")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").value("BTC/USD"));
    }

    @Test
    public void testGetTradeVolume() throws Exception {
        Mockito.when(marketService.findTradeVolume("BTC/USD", 1609459200L, 1609545600L))
                .thenReturn(BigDecimal.valueOf(1000));

        mockMvc.perform(MockMvcRequestBuilders.get("/market/trade-volume")
                        .param("symbol", "BTC/USD")
                        .param("timeStart", "1609459200")
                        .param("timeEnd", "1609545600"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(1000));
    }
}