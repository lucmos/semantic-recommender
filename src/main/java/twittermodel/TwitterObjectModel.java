package twittermodel;

import utils.OneToOneHash;

import java.io.Serializable;

public abstract class TwitterObjectModel implements Serializable {
    private int id;
    private String idString;

    TwitterObjectModel(int id) {
        this.setId(id);
    }

    TwitterObjectModel(String idString) {
        this.setId(getNextId(idString));
        this.setIdString(idString);
    }

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

    private int getNextId(String idString) {
        OneToOneHash<Integer, String> mapping = getIdMapping();
        int index = mapping.size();
        mapping.put(index, idString);
        return index;
    }
}
