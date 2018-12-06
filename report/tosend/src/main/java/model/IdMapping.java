package model;

import io.IndexedSerializable;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.HashMap;

/**
 * An implementation of a 1:1 hashmap, where both the keys and the values can be retrieved in O(1)
 *
 */
public class IdMapping implements IndexedSerializable {
//
//    private Object2IntOpenHashMap<String> string2int = new Object2IntOpenHashMap<>();
//    private Int2ObjectOpenHashMap<String> int2string = new Int2ObjectOpenHashMap<>();

    private HashMap<String, Integer> string2int = new HashMap<>();
    private HashMap<Integer, String> int2string = new HashMap<>();

    public void put(int a, String b) {
        if (!int2string.containsKey(a) && !string2int.containsKey(b)) {
            int2string.put(a, b);
            string2int.put(b, a);
        } else {
            throw new RuntimeException("Enforcing 1:1 mapping");
        }
    }

    public int getIntId(String a) {
        return string2int.get(a);
    }

    public String getStringId(int b) {
        return int2string.get(b);
    }

    public boolean containsIntId(int a) {
        return int2string.containsKey(a);
    }

    public boolean containsStringId(String b) {
        return string2int.containsKey(b);
    }

    public int size() {
        return int2string.size();
    }

    public boolean isEmpty() {
        return int2string.isEmpty();
    }
}
