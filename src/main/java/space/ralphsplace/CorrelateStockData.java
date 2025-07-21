package space.ralphsplace;

import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SequencedCollection;
import java.util.concurrent.ExecutionException;

public class CorrelateStockData {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] tickers = {"AAPL","AMZN","COIN","ENB","JPM","MSFT","NVDA","ORCL","QQQ","SPY","TSLA"};

        Map<String, SequencedCollection<StockPrice>> stockPricesMap = StockDataLoader.readAllCsvFiles(tickers);

        stockPricesMap.forEach((ticker, prices) -> {
            System.out.println("Ticker: " + ticker);
            // Print the first 5 prices for brevity
            prices.stream().limit(5).forEach(price -> System.out.println("  " + price));
            System.out.println("Total prices: " + prices.size());
        });

        SequencedCollection<Correlation> correlations = correlate(tickers, stockPricesMap);
        System.out.println("Correlations calculated: " + correlations.size());
        correlations.forEach(correlation -> {
            System.out.println("Correlation between " + correlation.ticker1() + " and " + correlation.ticker2() + ": " + correlation.correlation());
        });
    }

    public static SequencedCollection<Correlation> correlate(String[] tickers, Map<String, SequencedCollection<StockPrice>> stockPricesMap) {
        List<Correlation> correlations = new ArrayList<>();

        for (int i = 0; i < tickers.length; i++) {
            for (int j = i + 1; j < tickers.length; j++) {
                String ticker1 = tickers[i];
                String ticker2 = tickers[j];

                SequencedCollection<StockPrice> prices1 = stockPricesMap.get(ticker1);
                SequencedCollection<StockPrice> prices2 = stockPricesMap.get(ticker2);

                double correlation = CorrelationCalculator.calculate(prices1, prices2);

                correlations.add(new Correlation(ticker1, ticker2, correlation));
            }
        }
        correlations.sort(Correlation.comparator());

        return new LinkedList<>(correlations);
    }
}
