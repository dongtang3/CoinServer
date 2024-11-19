package edu.wpi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "k_line")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private long time;

    @Column(nullable = false)
    private String period;

    @Column(name = "open_price", precision = 18, scale = 8, nullable = false)
    private BigDecimal openPrice;

    @Column(name = "highest_price", precision = 18, scale = 8, nullable = false)
    private BigDecimal highestPrice;

    @Column(name = "lowest_price", precision = 18, scale = 8, nullable = false)
    private BigDecimal lowestPrice;

    @Column(name = "close_price", precision = 18, scale = 8, nullable = false)
    private BigDecimal closePrice;

    @Column(nullable = false)
    private BigDecimal volume;

    @Column(nullable = false)
    private BigDecimal turnover;

    @Column(nullable = false)
    private Integer count;
}