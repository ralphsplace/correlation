package space.ralphsplace;

import java.util.SequencedCollection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CorrelationCalculator {

    public static final Predicate<SequencedCollection<StockPrice>> IS_NULL_OR_EMPTY = prices -> prices == null || prices.isEmpty();

    public static double calculate(SequencedCollection<StockPrice> prices1, SequencedCollection<StockPrice> prices2) {
        // Check for null or empty collections
        if (IS_NULL_OR_EMPTY.test(prices1) || IS_NULL_OR_EMPTY.test(prices2)) {
            return Double.NaN; // Return NaN if either collection is null or empty
        }
        
        Stream<StockPrice> mergedPrices = Stream.empty();
        if (prices1.size() == prices2.size()) {
            mergedPrices = Stream.concat(prices1.stream(), prices2.stream());

        } else if (prices1.size() < prices2.size()) {
            int diff = prices2.size() - prices1.size();
            var prices2b = prices2.stream()
                .skip(diff)
                .collect(Collectors.toList());
             mergedPrices = Stream.concat(prices1.stream(), prices2b.stream());    
        } else if (prices1.size() > prices2.size()) {
            int diff = prices1.size() - prices2.size();
            var prices1b = prices1.stream()
                .skip(diff)
                .collect(Collectors.toList());
            mergedPrices = Stream.concat(prices1b.stream(), prices2.stream());
        }

        return mergedPrices.collect(Collectors.groupingBy(StockPrice::date)).entrySet().stream()
                    .filter(e -> e.getValue().size() == 2) // Ensure both prices exist for the date
                    .mapToDouble(e -> Math.abs(e.getValue().get(0).percentageChange() - e.getValue().get(1).percentageChange()))
                    .average().orElse(Double.NaN);
    }
}
