package utils;

import com.google.common.collect.Lists;
import io.IndexedSerializable;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import java.util.*;
import java.util.stream.Collectors;

/**
 * It's a map where any keys is associated with the number of time that appears
 * @param <T> the type to count
 */
public class Counter<T> implements IndexedSerializable {
    private final Object2DoubleOpenHashMap<T> counts = new Object2DoubleOpenHashMap<>();

    /**
     * The constructor
     */
    public Counter() {
    }

    /**
     * Builds a counter from a collection
     * @param collection the original collection whose elements must be count
     * @param <T> the type of the collection
     * @return the corresponding Counter
     */
    public static <T> Counter<T> fromCollection(Collection<T> collection) {
        Counter<T> c = new Counter<>();
        return c.increment(collection);
    }

//    public static <E, N, V extends Collection<N>> Counter<N> fromMultiMap(Map<E, V> map) {
//        Counter<N> c = new Counter<>();
//        for (V value : map.values()) {
//            c.increment(value);
//        }
//        return c;
//    }
//
//    public static <E, N> Counter<N> fromMap(Map<E, N> map) {
//        Counter<N> c = new Counter<>();
//        for (N value : map.values()) {
//            c.increment(value);
//        }
//        return c;
//    }

    /**
     * Add a value to count
     * @param t the object to count
     * @param value the amount of time to sum in the counter for the specified object
     * @return the updated Counter
     */
    public Counter<T> add(T t, double value) {
        counts.mergeDouble(t, value, (x, y) -> x + y);
        return this;

    }

    /**
     * Sum the count of another Counter
     * @param counter the other Counter
     * @return the updated Counter
     */
    public Counter<T> add(Counter<T> counter) {
        for (Object2DoubleMap.Entry<T> entry : counter.getMap().object2DoubleEntrySet()) {
            add(entry.getKey(), entry.getDoubleValue());
        }
        return this;
    }

    /**
     * Increments of one the count for one object of the Counter
     * @param t the object to increment the count
     * @return the updated count
     */
    public Counter<T> increment(T t) {
        return add(t, 1);
    }

    /**
     * Increments the counts according to a new collection
     * @param coll the new collection
     * @return the updated Counter
     */
    public Counter<T> increment(Collection<T> coll) {
        for (T e : coll) {
            this.increment(e);
        }
        return this;
    }

    /**
     * TODO: commenta
     */
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

    /**
     * Returns the distribution of the values
     * @return the distribution
     */
    public String getDistribution() {
        return getDistribution(-1);
    }
//TODO: commenta
    public int count(T t) {
        return (int) get(t);
    }
//    public double countDouble(T t) {
//        return get(t);
//    }

    /**
     *Counts the amount of values of the Counter
     * @return the total of the values
     */
    public int total() {
        return counts.values().stream().mapToInt(Double::intValue).sum();
    }

//    public double totalDouble() {
//        return counts.values().stream().mapToDouble(Double::doubleValue).sum();
//    }

    /**
     * Returns the count for the specified object
     * @param t the object to discover the occurrences
     * @return the count of the object
     */
    public double get(T t) {
        return counts.getOrDefault(t, 0d);
    }

    /**
     * Set the count of an object of the Counter to a certain value
     * @param t the object
     * @param v the value
     */
    public void set(T t, double v) {
        counts.put(t, v);
    }

    /**
     * The dimension of the Counter
     * @return the result
     */
    public int size() {
        return counts.size();
    }

    /**
     * Return the list of the most common object for the Counter
     * @param k the minimum count value that an object should have to be inserted in the result
     * @return the list of the most common items
     */
    public List<T> mostCommon(int k) {
        return counts.object2DoubleEntrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Return the list of the less common object for the Counter
     * @param k the maximum count value that an object should have to be inserted in the result
     * @return the list of the less common items
     */
    public List<T> leastCommon(int k) {
        return counts.object2DoubleEntrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
//
//    public T mostCommon() {
//        return mostCommon(1).get(0);
//    }
//
//    public T leastCommon(){
//        return leastCommon(1).get(0);}

    /**
     * Returns a String that represents the object
     * @return a textual rapresentation of the Counter
     */
    @Override
    public String toString() {
        return counts.toString();
    }

    /**
     * TODO commenta
     */
    public Object2DoubleOpenHashMap<T> getMap() {
        return counts;
    }
}
