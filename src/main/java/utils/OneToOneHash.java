package utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of a 1:1 hashmap, where both the keys and the values can be retrieved in O(1)
 *
 * @param <K> left key "a"
 * @param <V> right key "b"
 */
public class OneToOneHash<K, V>  implements IndexedSerializable {
    private HashMap<K, V> aToB = new HashMap<>();
    private HashMap<V, K> bToA = new HashMap<>();

    /**
     * Puts the pair (a,b) into the map
     * @param a the left key
     * @param b the right key
     */
    private void add(K a, V b) {
        if (!aToB.containsKey(a) && !bToA.containsKey(b)) {
            aToB.put(a, b);
            bToA.put(b, a);
        } else {
            throw new RuntimeException("Enforcing 1:1 mapping");
        }
    }

    /**
     * Get the values associated to the left key a
     *
     * @param a the left key
     * @return the right key associated to a
     */
    public V getA(K a) {
        return aToB.get(a);
    }

    /**
     * Get the values associated to the right key b
     *
     * @param b the right key
     * @return the left key associated to b
     */
    public K getB(V b) {
        return bToA.get(b);
    }

    /**
     * Checks if the left key a is present in the map
     *
     * @param a the left key
     * @return true if it is present
     */
    public boolean containsA(K a) {
        return aToB.containsKey(a);
    }

    /**
     * Checks if the right key a is present in the map
     *
     * @param b the right ey
     * @return true if it is present
     */
    public boolean containsB(V b) {
        return bToA.containsKey(b);
    }

    /**
     * Removes the couple (a,b) from the map
     * @param a the left key
     * @param b the right key
     */
    public void delete(K a, V b) {
        aToB.remove(a);
        bToA.remove(b);
    }

    @Override
    public String toString() {
        return this.aToB + "\n" + this.bToA;
    }

    public int size() {
        return aToB.size();
    }

    public boolean isEmpty() {
        return aToB.isEmpty();
    }

    public V put(K keyA, V keyB) {
        this.add(keyA, keyB);
        return null;
    }
}
