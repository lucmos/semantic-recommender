package utils;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Counter<T> {
    private final Object2DoubleOpenHashMap<T> counts = new Object2DoubleOpenHashMap<>();

    public Counter() {
    }

    public static <T> Counter<T> fromCollection(Collection<T> collection) {
        Counter<T> c = new Counter<>();
        return c.increment(collection);
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

    public Counter<T> add(T t, double value) {
        counts.mergeDouble(t, value, (x, y) -> x + y);
        return this;

    }

    public Counter<T> add(Counter<T> counter) {
        for (Object2DoubleMap.Entry<T> entry : counter.getMap().object2DoubleEntrySet()) {
            add(entry.getKey(), entry.getDoubleValue());
        }
        return this;
    }

    public Counter<T> increment(T t) {
        return add(t, 1);
    }

    public Counter<T> increment(Collection<T> coll) {
        for (T e : coll) {
            this.increment(e);
        }
        return this;
    }

    public String getDistribution(int k) {
        double middle = counts.size() / 2.0;
        int middle_floor = (int) Math.floor(middle);
        int middle_ceil = (int) Math.ceil(middle);

        List<T> most = k < 0 || k > middle ? mostCommon(middle_ceil) : mostCommon(k);
        List<T> least = k < 0 || k > middle ? leastCommon(middle_floor) : leastCommon(k);

        StringBuilder s = new StringBuilder();
        for (T e : most) {
            s.append(String.format("\t%s\t\t%s\n", e, count(e)));
        }

        if (k > 0 && k < middle) {
            s.append("\t...\n");
        }

        for (T e : Lists.reverse(least)) {
            s.append(String.format("\t%s\t\t%s\n", e, count(e)));
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
        return counts.object2DoubleEntrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<T> leastCommon(int k) {
        return counts.object2DoubleEntrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public T mostCommon() {
        return mostCommon(1).get(0);
    }

    public T leastCommon(){
        return leastCommon(1).get(0);}

    @Override
    public String toString() {
        return counts.toString();
    }

    public Object2DoubleOpenHashMap<T> getMap() {
        return counts;
    }
}
