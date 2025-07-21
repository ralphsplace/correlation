package space.ralphsplace;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.SequencedCollection;
import static org.junit.jupiter.api.Assertions.*;

class CorrelationCalculatorTest {

    @Test
    void testCalculateCorrelationWithValidData() {
        SequencedCollection<StockPrice> prices1 = new ArrayList<>();
        SequencedCollection<StockPrice> prices2 = new ArrayList<>();

        prices1.add(new StockPrice("AAPL", LocalDate.of(2024, 7, 20), 110.0, 115.0, 105.0, 100.0, 1_000_000));
        prices1.add(new StockPrice("AAPL", LocalDate.of(2024, 7, 21), 120.0, 125.0, 115.0, 110.0, 2_000_000));

        prices2.add(new StockPrice("GOOG", LocalDate.of(2024, 7, 20), 100.0, 105.0, 95.0, 90.0, 900_000));
        prices2.add(new StockPrice("GOOG", LocalDate.of(2024, 7, 21), 130.0, 135.0, 125.0, 120.0, 2_500_000));

        double result = CorrelationCalculator.calculate(prices1, prices2);

        // Calculate expected manually:
        // 2024-07-20: |(110-100)/100*100 - (100-90)/90*100| = |10.0 - 11.1111| = 1.1111
        // 2024-07-21: |(120-110)/110*100 - (130-120)/120*100| = |9.0909 - 8.3333| = 0.7576
        // Average: (1.1111 + 0.7576) / 2 = 0.93435
        assertEquals(0.93435, result, 0.0001);
    }

    @Test
    void testCalculateCorrelationWithNullOrEmpty() {
        SequencedCollection<StockPrice> prices1 = null;
        SequencedCollection<StockPrice> prices2 = new ArrayList<>();

        assertTrue(Double.isNaN(CorrelationCalculator.calculate(prices1, prices2)));
    }

    @Test
    void testCalculateCorrelationWithDifferentSizes() {
        SequencedCollection<StockPrice> prices1 = new ArrayList<>();
        SequencedCollection<StockPrice> prices2 = new ArrayList<>();

        prices1.add(new StockPrice("AAPL", LocalDate.of(2024, 7, 20), 110.0, 115.0, 105.0, 100.0, 1_000_000));
        prices1.add(new StockPrice("AAPL", LocalDate.of(2024, 7, 21), 120.0, 125.0, 115.0, 110.0, 2_000_000));

        prices2.add(new StockPrice("GOOG", LocalDate.of(2024, 7, 19), 140.0, 145.0, 135.0, 130.0, 3_000_000));
        prices2.add(new StockPrice("GOOG", LocalDate.of(2024, 7, 20), 100.0, 105.0, 95.0, 90.0, 900_000));
        prices2.add(new StockPrice("GOOG", LocalDate.of(2024, 7, 21), 130.0, 135.0, 125.0, 120.0, 2_500_000));
       
        double result = CorrelationCalculator.calculate(prices1, prices2);

        // Calculate expected manually:
        // 2024-07-20: |(110-100)/100*100 - (100-90)/90*100| = |10.0 - 11.1111| = 1.1111
        // 2024-07-21: |(120-110)/110*100 - (130-120)/120*100| = |9.0909 - 8.3333| = 0.7576
        // Average: (1.1111 + 0.7576) / 2 = 0.93435
        assertEquals(0.93435, result, 0.0001);
    }
}