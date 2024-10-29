package edu.wpi.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "exchange_coin")
@Data
public class ExchangeCoin {

    @Id
    @Column(nullable = false)
    private String symbol;

    private String coinSymbol;

    private String baseSymbol;

    @Column(nullable = false)
    private int enable = 1;

    @Column(precision = 8, scale = 4)
    private BigDecimal fee;

    @Column(nullable = false)
    private int sort = 0;

    @Column(nullable = false)
    private int coinScale = 8;

    @Column(nullable = false)
    private int baseCoinScale = 8;

    private BigDecimal minSellPrice = BigDecimal.ZERO;

    private BigDecimal maxBuyPrice = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BooleanEnum enableMarketSell = BooleanEnum.IS_TRUE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BooleanEnum enableMarketBuy = BooleanEnum.IS_TRUE;

    private int maxTradingTime = 0;

    private int maxTradingOrder = 0;

    private int robotType = 0;

    private int flag = 0;

    private BigDecimal minTurnover = BigDecimal.ZERO;

    @Column(nullable = false)
    private int zone = 0;

    private BigDecimal minVolume = BigDecimal.ZERO;

    private BigDecimal maxVolume = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExchangeCoinPublishType publishType = ExchangeCoinPublishType.NONE;

    private String startTime = "2000-01-01 01:00:00";

    private String endTime = "2000-01-01 01:00:00";

    private String clearTime = "2000-01-01 01:00:00";

    private BigDecimal publishPrice = BigDecimal.ZERO;

    private BigDecimal publishAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private int visible = 1;

    @Column(nullable = false)
    private int exchangeable = 1;

    @Transient
    private Long currentTime;

    @Transient
    private int engineStatus = 0;

    @Transient
    private int marketEngineStatus = 0;

    @Transient
    private int exEngineStatus = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BooleanEnum enableSell = BooleanEnum.IS_TRUE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BooleanEnum enableBuy = BooleanEnum.IS_TRUE;
}
