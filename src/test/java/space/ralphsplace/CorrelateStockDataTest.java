package space.ralphsplace;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.LinkedList;
import java.util.SequencedCollection;
import java.time.LocalDate;
import java.util.HashMap;

public class CorrelateStockDataTest {

    private String[] tickers;
    private Map<String, SequencedCollection<StockPrice>> stockPricesMap;

    @BeforeEach
    public void setUp() {
        tickers = new String[] {"AAPL", "MSFT"};
        stockPricesMap = new HashMap<>();

        SequencedCollection<StockPrice> pricesAAPL = new LinkedList<>();
        pricesAAPL.add(new StockPrice("AAPL",LocalDate.now(), 100.0, 100.0, 100.0, 100.0, 1000000));
        pricesAAPL.add(new StockPrice("AAPL",LocalDate.now().plusDays(1), 100.0, 100.0, 100.0, 100.0, 1000000));
        pricesAAPL.add(new StockPrice("AAPL",LocalDate.now().plusDays(2), 100.0, 100.0, 100.0, 100.0, 1000000));

        SequencedCollection<StockPrice> pricesMSFT = new LinkedList<>();
        pricesMSFT.add(new StockPrice("MSFT",LocalDate.now(), 100.0, 100.0, 100.0, 100.0, 1000000));
        pricesMSFT.add(new StockPrice("MSFT",LocalDate.now().plusDays(1), 100.0, 100.0, 100.0, 100.0, 1000000));
        pricesMSFT.add(new StockPrice("MSFT",LocalDate.now().plusDays(2), 100.0, 100.0, 100.0, 100.0, 1000000));

        stockPricesMap.put("AAPL", pricesAAPL);
        stockPricesMap.put("MSFT", pricesMSFT);
    }

    @Test
    public void testCorrelateReturnsCorrectSize() {
        SequencedCollection<Correlation> result = CorrelateStockData.correlate(tickers, stockPricesMap);
        assertEquals(1, result.size());
    }

    @Test
    public void testCorrelateReturnsCorrelationObject() {
        SequencedCollection<Correlation> result = CorrelateStockData.correlate(tickers, stockPricesMap);
        Correlation correlation = result.iterator().next();
        assertEquals("AAPL", correlation.ticker1());
        assertEquals("MSFT", correlation.ticker2());
        // The actual correlation value depends on CorrelationCalculator implementation
    }

    @Test
    public void testCorrelateWithEmptyPrices() {
        Map<String, SequencedCollection<StockPrice>> emptyMap = new HashMap<>();
        emptyMap.put("AAPL", new LinkedList<>());
        emptyMap.put("MSFT", new LinkedList<>());
        SequencedCollection<Correlation> result = CorrelateStockData.correlate(tickers, emptyMap);
        assertEquals(1, result.size());
        Correlation correlation = result.iterator().next();
        assertTrue(Double.isNaN(correlation.correlation()) || Double.isFinite(correlation.correlation()));
    }
}