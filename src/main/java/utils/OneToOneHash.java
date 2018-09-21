package utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OneToOneHash<K, V> implements Map<K,V>, Serializable {
    private HashMap<K, V> aToB = new HashMap<>();
    private HashMap<V, K> bToA = new HashMap<>();

    private void add(K a, V b) {
        if (!aToB.containsKey(a) && !bToA.containsKey(b)) {
            aToB.put(a, b);
            bToA.put(b, a);
        } else {
            throw new RuntimeException("Enforcing 1:1 mapping");
        }
    }

    public V getA(K a) {
        return aToB.get(a);
    }

    public K getB(V b) {
        return bToA.get(b);
    }

    public boolean containsA(K a) {
        return aToB.containsKey(a);
    }

    public boolean containsB(V b) {
        return bToA.containsKey(b);
    }

    public void delete(K a, V b) {
        aToB.remove(a);
        bToA.remove(b);
    }

    @Override
    public String toString() {
        return this.aToB + "\n" + this.bToA;
    }

    @Override
    public int size() {
        return aToB.size();
    }

    @Override
    public boolean isEmpty() {
        return aToB.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return aToB.containsKey(key) || bToA.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return aToB.containsValue(value) || bToA.containsValue(value);
    }

    @Override
    public V get(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V put(K keyA, V keyB) {
        this.add(keyA, keyB);
        return null;
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();

    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();

    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
