package space.ralphsplace;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StockPriceTest {

    @Test
    void testPercentageChangePositive() {
        StockPrice price = new StockPrice(
            "AAPL",
            LocalDate.of(2024, 7, 20),
            110.0, // close
            115.0, // high
            105.0, // low
            100.0, // open
            1_000_000 // volume
        );
        // ((110 - 100) / 100) * 100 = 10%
        assertEquals(10.0, price.percentageChange(), 0.0001);
    }

    @Test
    void testPercentageChangeNegative() {
        StockPrice price = new StockPrice(
            "GOOG",
            LocalDate.of(2024, 7, 20),
            90.0,   // close
            95.0,   // high
            85.0,   // low
            100.0,  // open
            500_000 // volume
        );
        // ((90 - 100) / 100) * 100 = -10%
        assertEquals(-10.0, price.percentageChange(), 0.0001);
    }

    @Test
    void testPercentageChangeZero() {
        StockPrice price = new StockPrice(
            "MSFT",
            LocalDate.of(2024, 7, 20),
            100.0,  // close
            105.0,  // high
            95.0,   // low
            100.0,  // open
            750_000 // volume
        );
        // ((100 - 100) / 100) * 100 = 0%
        assertEquals(0.0, price.percentageChange(), 0.0001);
    }
}