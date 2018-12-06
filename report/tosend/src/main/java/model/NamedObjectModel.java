package model;

public class NamedObjectModel extends ObjectModel {

    private String name;

    public NamedObjectModel(int seqId, String name) {
        super(seqId);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
