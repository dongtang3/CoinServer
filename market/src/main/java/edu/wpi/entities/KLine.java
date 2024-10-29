package edu.wpi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "k_line")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KLine {


    public KLine(String period) {
        this.period = period;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Open price
    @Column(columnDefinition = "decimal(18,8) default 0")
    private BigDecimal openPrice = BigDecimal.ZERO;

    // Highest price
    @Column(columnDefinition = "decimal(18,8) default 0")
    private BigDecimal highestPrice = BigDecimal.ZERO;

    // Lowest price
    @Column(columnDefinition = "decimal(18,8) default 0")
    private BigDecimal lowestPrice = BigDecimal.ZERO;

    // Close price
    @Column(columnDefinition = "decimal(18,8) default 0")
    private BigDecimal closePrice = BigDecimal.ZERO;

    // Timestamp of the K-line
    @Column(nullable = false)
    private long time;

    // Period (e.g., "1m", "5m", "1h")
    @Column(nullable = false)
    private String period;

    // Number of transactions
    @Column(columnDefinition = "int default 0")
    private int count;

    // Volume of the K-line
    @Column(columnDefinition = "decimal(18,8) default 0")
    private BigDecimal volume = BigDecimal.ZERO;

    // Turnover of the K-line
    @Column(columnDefinition = "decimal(18,8) default 0")
    private BigDecimal turnover = BigDecimal.ZERO;

    @Column(nullable = false)
    private String symbol;
}
