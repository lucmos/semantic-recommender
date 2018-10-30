package utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Counter<T> {
    private final Map<T, Double> counts = new HashMap<>();

    public Counter() {
    }

    public static <E, N, V extends Collection<N>> Counter<N> fromMultiMap(Map<E, V> map) {
        Counter<N> c = new Counter<>();
        for (V value : map.values()) {
            c.increment(value);
        }
        return c;
    }

    public static <E, N> Counter<N> fromMap(Map<E, N> map) {
        Counter<N> c = new Counter<>();
        for (N value : map.values()) {
            c.increment(value);
        }
        return c;
    }

    public Counter<T> increment(T t) {
        counts.merge(t, 1d, Double::sum);
        return this;
    }

    public Counter<T> increment(Collection<T> coll) {
        for (T e : coll) {
            this.increment(e);
        }
        return this;
    }

    public String getDistribution(int k) {
        List<T> list = k < 0 ? mostCommon(counts.size()) : mostCommon(k);

        StringBuilder s = new StringBuilder();
        for (T e : list) {
            s.append(String.format("\t%s\t\t%s\n", e, count(e)));
        }

        if (k > 0 && k < counts.size() - 1) {
            s.append("\t...\n");
        }
        return s.toString();
    }

    public String getDistribution() {
        return getDistribution(-1);
    }

    public int count(T t) {
        return (int) get(t);
    }
    public double countDouble(T t) {
        return get(t);
    }

    public int total() {
        return counts.values().stream().mapToInt(Double::intValue).sum();
    }

    public double totalDouble() {
        return counts.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double get(T t) {
        return counts.getOrDefault(t, 0d);
    }

    public void set(T t, double v) {
        counts.put(t, v);
    }

    public int size() {
        return counts.size();
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

    public Map<T, Double> getMap() {
        return counts;
    }
}
