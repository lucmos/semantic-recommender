package model;

import io.IndexedSerializable;

public abstract class ObjectCollection implements IndexedSerializable {
    /**
     * A mapping between an integer identifier and a string one
     */
    private IdMapping idMap;

     public ObjectCollection() {
        idMap = new IdMapping();
    }

    /**
     * Defines the mapping between the given literal identifier and the generated integer identifier
     *
     * @return the mapping, local to each class
     */
    public IdMapping getIdMapping() {
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

        assert !idMap.containsIntId(seqId);
        assert !idMap.containsStringId(idString);

        idMap.put(seqId, idString);

        return seqId;
    }

    public boolean exixstObj(String id) {
        return idMap.containsStringId(id);
    }

    public boolean exixstObj(int id) {
        return idMap.containsIntId(id);
    }

    public String getStringId(int i) {
        return idMap.getStringId(i);
    }

    public int getIntegerId(String s) {
        return idMap.getIntId(s);
    }
}
