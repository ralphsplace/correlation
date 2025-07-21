package space.ralphsplace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Optional;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.SequencedCollection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;


public class StockDataLoader {

    /**
     * Reads all CSV files for the given tickers from the specified directory.
     *
     * @param tickers Array of stock tickers to read.
     * @return A map where the key is the ticker and the value is a SequencedCollection of StockPrice.
     */
    public static Map<String, SequencedCollection<StockPrice>> readAllCsvFiles(String[] tickers) {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        var futures = new LinkedList<Future<SequencedCollection<StockPrice>>>();

        for (String ticker : tickers) {
            try {

                InputStream InputStream = StockDataLoader.class.getResourceAsStream("/"+ ticker + "_2019-07-21_2025-07-19.csv");
                InputStreamReader reaeder = new InputStreamReader(InputStream);
                futures.add(executor.submit(() -> readCsv(reaeder, ticker)));
            } catch (Exception e) {
                System.err.println("Error processing ticker " + ticker + ": " + e.getMessage());
            }
        }

        var futurePriceMap = new HashMap<String,SequencedCollection<StockPrice>>();
        futures.forEach(future -> {
            try {
                SequencedCollection<StockPrice> prices = future.get();
                var ticker = prices.getFirst().ticker();
                futurePriceMap.put(ticker, prices);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error processing file: " + e.getMessage());
            }
        });

        executor.shutdown();

        return futurePriceMap;
    }

    public static SequencedCollection<StockPrice> readCsv(Reader inputStreamReader, String ticker) {
        SequencedCollection<StockPrice> stockPrices = new LinkedList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            stockPrices = reader.lines()
                .skip(1) // skip header
                .map(line -> parseLine(line, ticker, formatter).get())
                .toList(); // SequencedCollection

        } catch (IOException e) {
            System.err.println("Error reading file for " + ticker + ": " + e.getMessage());
        }

        return stockPrices;
    }

    private static Optional<StockPrice> parseLine(String line, String ticker, DateTimeFormatter formatter) {
        try {
            String[] parts = line.split(",");

            LocalDate date = LocalDate.parse(parts[0], formatter);
            double close = Double.parseDouble(parts[1]);
            double high = Double.parseDouble(parts[2]);
            double low = Double.parseDouble(parts[3]);
            double open = Double.parseDouble(parts[4]);
            long volume = Long.parseLong(parts[5]);

            return Optional.of(new StockPrice(ticker, date, close, high, low, open, volume));
        } catch (Exception e) {
            System.err.println("Invalid line for " + ticker + ": " + line);
            return Optional.empty();
        }
    }
}
