package space.ralphsplace;

import java.util.Comparator;

public record Correlation(String ticker1, String ticker2, double correlation) {

    public static Comparator<Correlation> comparator() {
        return new Comparator<Correlation>() {
            @Override
            public int compare(Correlation o1, Correlation o2) {
                return Double.compare(o1.correlation, o2.correlation);
            }
        };
    }
}
