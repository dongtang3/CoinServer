import edu.wpi.entities.*;
import edu.wpi.exceptions.InsufficientBalanceException;
import edu.wpi.repositories.TradeRepository;
import edu.wpi.repositories.WalletRepository;
import edu.wpi.services.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {

    @InjectMocks
    private TradeService tradeService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TradeRepository tradeRepository;

    private Wallet wallet;

    @BeforeEach
    public void setUp() {
        wallet = new Wallet();
        wallet.setUserId("user123");
        wallet.setUsdtBalance(new BigDecimal("1000000"));
        wallet.setCoinBalances(new HashMap<>());

        when(walletRepository.findByUserId("user123"))
                .thenReturn(Optional.of(wallet));
        tradeService = new TradeService(walletRepository, tradeRepository);

    }
    @Test
    public void testExecuteBuy_Success() {
        // Arrange
        String symbol = "BTC/USD";
        BigDecimal amount = new BigDecimal("1");
        BigDecimal price = new BigDecimal("50000");

        // Mock save methods
        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(tradeRepository.save(any(Trade.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Trade trade = tradeService.executeTrade("user123", symbol, amount, price, ExchangeOrderDirection.BUY);

        // Assert
        assertNotNull(trade);
        assertEquals("user123", trade.getUserId());
        assertEquals(symbol, trade.getSymbol());
        assertEquals(amount, trade.getAmount());
        assertEquals(price, trade.getPrice());
        assertEquals(amount.multiply(price), trade.getTotal());
        assertEquals(ExchangeOrderDirection.BUY, trade.getType());

        // Verify wallet updates
        verify(walletRepository).save(wallet);
        assertEquals(new BigDecimal("950000"), wallet.getUsdtBalance());
        assertEquals(amount, wallet.getCoinBalance(symbol));
    }

    @Test
    public void testExecuteBuy_InsufficientBalance() {
        // Arrange
        String symbol = "BTC/USD";
        BigDecimal amount = new BigDecimal("50");
        BigDecimal price = new BigDecimal("50000"); // Total cost: 2,500,000

        // Act & Assert
        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> tradeService.executeTrade("user123", symbol, amount, price, ExchangeOrderDirection.BUY)
        );

        assertEquals("Insufficient USDT balance", exception.getMessage());

        // Verify wallet not updated
        verify(walletRepository, never()).save(any(Wallet.class));
        assertEquals(new BigDecimal("1000000"), wallet.getUsdtBalance());
    }

    @Test
    public void testExecuteSell_Success() {
        // Arrange
        String symbol = "BTC/USD";
        BigDecimal amount = new BigDecimal("1");
        BigDecimal price = new BigDecimal("50000");

        wallet.addCoinBalance(symbol, new BigDecimal("2"));

        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(tradeRepository.save(any(Trade.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Trade trade = tradeService.executeTrade("user123", symbol, amount, price, ExchangeOrderDirection.SELL);

        // Assert
        assertNotNull(trade);
        assertEquals(ExchangeOrderDirection.SELL, trade.getType());

        verify(walletRepository).save(wallet);
        assertEquals(new BigDecimal("1050000"), wallet.getUsdtBalance());
        assertEquals(new BigDecimal("1"), wallet.getCoinBalance(symbol));
    }

    @Test
    public void testExecuteSell_InsufficientCoinBalance() {
        // Arrange
        String symbol = "BTC/USD";
        BigDecimal amount = new BigDecimal("1");
        BigDecimal price = new BigDecimal("50000");

        // No coin balance

        // Act & Assert
        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> tradeService.executeTrade("user123", symbol, amount, price, ExchangeOrderDirection.SELL)
        );

        assertEquals("Insufficient BTC/USD balance", exception.getMessage());

        // Verify wallet not updated
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    public void testCreateWallet_NewUser() {
        // Arrange
        when(walletRepository.findByUserId("newUser"))
                .thenReturn(Optional.empty());
        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Trade trade = tradeService.executeTrade("newUser", "BTC/USD", new BigDecimal("1"),
                new BigDecimal("50000"), ExchangeOrderDirection.BUY);

        // Assert
        assertNotNull(trade);
        verify(walletRepository, times(2)).save(any(Wallet.class)); // Wallet creation & update
    }

}