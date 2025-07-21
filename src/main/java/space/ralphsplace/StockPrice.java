package space.ralphsplace;

import java.time.LocalDate;

    // Record representing a row of stock data
public record StockPrice(
    String ticker,
    LocalDate date,
    double close,
    double high,
    double low,
    double open,
    long volume
) {
    // Implement percentage change calculation
    public double percentageChange() {
        return ((close - open) / open) * 100;
    }
}
