package space.ralphsplace;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.SequencedCollection;


import static org.junit.jupiter.api.Assertions.*;

public class StockDataLoaderTest {

    @Test
    void testReadCsvParsesValidLines() {

        String[] tickers = {"AAPL","AMZN","COIN","ENB","JPM","MSFT","NVDA","ORCL","QQQ","SPY","TSLA"};
        Map<String, SequencedCollection<StockPrice>> stockPricesMap = StockDataLoader.readAllCsvFiles(tickers);

        assertEquals(tickers.length, stockPricesMap.size());

        StockPrice first = stockPricesMap.get("AAPL").getFirst();
        assertEquals("AAPL", first.ticker());
        assertEquals(LocalDate.of(2019,07,22), first.date());
        assertEquals(49.764122009277344, first.close());
        assertEquals(49.76652220160761, first.high());
        assertEquals(48.89717620400364, first.low());
        assertEquals(48.90678063774045, first.open());
        assertEquals(89111600, first.volume());

        StockPrice second =stockPricesMap.get("AAPL").getLast();
        assertEquals("AAPL", second.ticker());
        assertEquals(LocalDate.of(2025,07,18), second.date());
        assertEquals(211.17999267578125, second.close());
        assertEquals(211.7899932861328, second.high());
        assertEquals(209.6999969482422,second.low());
        assertEquals(210.8699951171875, second.open());
        assertEquals(48939500, second.volume());
    }

    @Test
    void testReadCsvSkipsInvalidLines() {

        String[] tickers = {"AAPL"};
        Map<String, SequencedCollection<StockPrice>> stockPricesMap = StockDataLoader.readAllCsvFiles(tickers);

        assertEquals(1, stockPricesMap.size());
        assertEquals(LocalDate.of(2019,07,22), stockPricesMap.get("AAPL").getFirst().date());
    }
}