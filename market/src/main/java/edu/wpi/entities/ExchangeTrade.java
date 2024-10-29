package edu.wpi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Trade matching information
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exchange_trade")
@Data
public class ExchangeTrade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Trading pair symbol
    @Column(nullable = false)
    private String symbol;

    // Trade price
    @Column(columnDefinition = "decimal(18,8) default 0")
    private BigDecimal price;

    // Trade amount
    @Column(columnDefinition = "decimal(18,8) default 0")
    private BigDecimal amount;

    // Buy turnover
    @Column(columnDefinition = "decimal(18,8) default 0")
    private BigDecimal buyTurnover;

    // Sell turnover
    @Column(columnDefinition = "decimal(18,8) default 0")
    private BigDecimal sellTurnover;

    // Trade direction (buy/sell)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExchangeOrderDirection direction;

    // Buy order ID
    @Column(name = "buy_order_id", nullable = false)
    private String buyOrderId;

    // Sell order ID
    @Column(name = "sell_order_id", nullable = false)
    private String sellOrderId;

    // Timestamp of the trade
    @Column(nullable = false)
    private Long time;
}
