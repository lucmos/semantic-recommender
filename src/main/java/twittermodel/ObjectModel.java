package twittermodel;

import utils.IndexedSerializable;
import utils.OneToOneHash;

import java.io.Serializable;

/**
 * A generic twitter object.
 * It always has an integer identifier
 * It may have a literal identifier
 */
public abstract class ObjectModel implements IndexedSerializable {
    /**
     * The integer identifier. It is globally unique inside the same class
     * and automatically generated.
     */
    private int seqId;

    /**
     * The literal identifier present in the dataset. If present, it is unique.
     */
    private String idString;

    /**
     * A mapping between an integer identifier and a string one
     */
    private static OneToOneHash<Integer, String> idMap = new OneToOneHash<>();

    ObjectModel(String idString) {
        this.setId(getNextId(idString));
        this.setIdString(idString);
    }

    /**
     * Defines the mapping between the given literal identifier and the generated integer identifier
     *
     * @return the mapping, local to each class
     */
    private OneToOneHash<Integer, String> getIdMapping() {
        assert idMap != null;

        return idMap;
    }

    private void setId(int seqId) {
        assert seqId >= 0;

        this.seqId = seqId;
    }

    public int getId() {
        assert seqId >= 0;

        return seqId;
    }

    public String getIdString() {
        assert idString != null && !idString.equals("");

        return idString;
    }

    private void setIdString(String idString) {
        assert idString != null && !idString.equals("");

        this.idString = idString;
    }

    /**
     * Finds the next available integer identifier in the current class, and associates it to the literal id
     * @param idString the literal identifier
     * @return the integer identifier
     */
    private int getNextId(String idString) {
        assert idString != null && !idString.equals("");

        OneToOneHash<Integer, String> mapping = getIdMapping();
        int index = mapping.size();
        mapping.put(index, idString);
        return index;
    }
}
