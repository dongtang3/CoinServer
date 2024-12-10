package edu.wpi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PriceResponse {
    private String symbol;
    private BigDecimal price;
}