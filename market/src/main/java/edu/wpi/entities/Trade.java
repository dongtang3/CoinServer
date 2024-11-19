package edu.wpi.entities;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Data
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String symbol;
    private BigDecimal amount; // Quantity of the asset
    private BigDecimal price;  // Price per unit
    private BigDecimal total;  // Total value (amount * price)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private ExchangeOrderDirection type;
}