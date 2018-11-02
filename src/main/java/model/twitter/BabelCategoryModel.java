package model.twitter;

public class BabelCategoryModel extends BabelInfo {
    BabelCategoryModel(int seqId, String idString) {
        super(seqId, idString);
    }

    @Override
    public String toString() {
        return getIdString();
    }
}
