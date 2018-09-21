package twittermodel;

import utils.OneToOneHash;

import java.io.Serializable;

/**
 * A generic twitter object.
 * It always has an integer identifier
 * It may have a literal identifier
 */
public abstract class TwitterObjectModel implements Serializable {
    /**
     * The integer identifier. It is unique inside the same class
     */
    private int id;

    /**
     * The literal identifier. If present, it is unique.
     */
    private String idString;

    TwitterObjectModel(int id) {
        this.setId(id);
    }

    TwitterObjectModel(String idString) {
        this.setId(getNextId(idString));
        this.setIdString(idString);
    }

    /**
     * Defines the mapping between the given literal identifier and the generated integer identifier
     * @return the mapping, local to each class
     */
    abstract OneToOneHash<Integer, String> getIdMapping();

    private void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getIdString() {
        return idString;
    }

    private void setIdString(String idString) {
        this.idString = idString;
    }

    /**
     * Finds the next available integer identifier in the current class, and associates it to the literal id
     * @param idString the literal identifier
     * @return the integer identifier
     */
    private int getNextId(String idString) {
        OneToOneHash<Integer, String> mapping = getIdMapping();
        int index = mapping.size();
        mapping.put(index, idString);
        return index;
    }
}
