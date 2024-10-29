package edu.wpi.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "coin_thumb")
@Data
public class CoinThumb {

    @Id
    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal open = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal high = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal low = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal close = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal chg = BigDecimal.ZERO.setScale(2);

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal change = BigDecimal.ZERO.setScale(2);

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal volume = BigDecimal.ZERO.setScale(2);

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal turnover = BigDecimal.ZERO;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal lastDayClose = BigDecimal.ZERO;

    @Column(precision = 18, scale = 8)
    private BigDecimal usdRate;

    @Column(precision = 18, scale = 8)
    private BigDecimal baseUsdRate;

    @Column(nullable = false)
    private int zone = 0;

    // Constructors
    public CoinThumb() {
    }

    public CoinThumb(String symbol, BigDecimal open, BigDecimal high, BigDecimal low,
                     BigDecimal close, BigDecimal volume, BigDecimal turnover) {
        this.symbol = symbol;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.turnover = turnover;
    }
}
