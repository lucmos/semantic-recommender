package model;

import utils.IndexedSerializable;
import utils.OneToOneHash;

public abstract class ObjectCollection implements IndexedSerializable {
    /**
     * A mapping between an integer identifier and a string one
     */
    private OneToOneHash<Integer, String> idMap;

     public ObjectCollection() {
        idMap = new OneToOneHash<>();
    }

    /**
     * Defines the mapping between the given literal identifier and the generated integer identifier
     *
     * @return the mapping, local to each class
     */
    public OneToOneHash<Integer, String> getIdMapping() {
        assert idMap != null;

        return idMap;
    }

    /**
     * Finds the next available integer identifier in the current class, and associates it to the literal id
     * @param idString the literal identifier
     * @return the integer identifier
     */
    public int getNextId(String idString) {
        assert idString != null && !idString.equals("");

        int seqId = idMap.size();

        assert !idMap.containsA(seqId);
        assert !idMap.containsB(idString);

        idMap.put(seqId, idString);

        return seqId;
    }

    public boolean exixstObj(String id) {
        return idMap.containsB(id);
    }

    public String getStringId(int i) {
        return idMap.getA(i);
    }

    public int getIntegerId(String s) {
        return idMap.getB(s);
    }
}
