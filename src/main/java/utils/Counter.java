package utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Counter<T> {
    private final Map<T, Integer> counts = new HashMap<>();

    public Counter<T> increment(T t) {
        counts.merge(t, 1, Integer::sum);
        return this;
    }

    public Counter<T> increment(Iterable<T> iterable) {
        for (T e : iterable) {
            this.increment(e);
        }
        return this;
    }

    public int count(T t) {
        return counts.getOrDefault(t, 0);
    }

    public List<T> mostCommon(int k) {
        return counts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public T mostCommon() {
        return mostCommon(1).get(0);
    }

    @Override
    public String toString() {
        return counts.toString();
    }

    public String getDistribution(int k) {
        List<T> list = k < 0 ? mostCommon(counts.size()) : mostCommon(k);

        StringBuilder s = new StringBuilder();
        for (T e : list) {
            s.append(String.format("\t%s\t\t%s\n", e, count(e)));
        }
        return s.toString();
    }

    public String getDistribution() {
        return getDistribution(-1);
    }
}
