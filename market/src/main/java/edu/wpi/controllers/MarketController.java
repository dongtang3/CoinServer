package edu.wpi.controllers;

import edu.wpi.entities.*;
import edu.wpi.exceptions.PriceNotFoundException;
import edu.wpi.services.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/market")
public class MarketController {

    @Autowired
    private MarketService marketService;

    // Get all visible trading pairs
    @GetMapping("/symbol")
    public List<ExchangeCoin> findAllSymbol() {
        return marketService.findAllVisibleSymbols();
    }

    // Get coin summary (thumbnail) data
    @GetMapping("/symbol-thumb")
    public List<CoinThumb> findSymbolThumb() {
        return marketService.findAllSymbolThumb();
    }

    // Get K-line history data
    @GetMapping("/history")
    public List<KLine> findKHistory(@RequestParam String symbol,
                                    @RequestParam long from,
                                    @RequestParam long to,
                                    @RequestParam String resolution) {
        return marketService.findKLineHistory(symbol, from, to, resolution);
    }

    // Get recent trade records
    @GetMapping("/latest-trade")
    public List<ExchangeTrade> latestTrade(@RequestParam String symbol, @RequestParam int size) {
        return marketService.findLatestTrade(symbol, size);
    }

    // Get trading volume within a specific time range
    @GetMapping("/trade-volume")
    public BigDecimal getTradeVolume(@RequestParam String symbol, @RequestParam long timeStart, @RequestParam long timeEnd) {
        return marketService.findTradeVolume(symbol, timeStart, timeEnd);
    }

    @GetMapping("/price")
    public ResponseEntity<PriceResponse> getPrice(@RequestParam String symbol) {
        try {
            BigDecimal price = marketService.getCurrentPrice(symbol);
            return ResponseEntity.ok(new PriceResponse(symbol, price));
        } catch (PriceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
