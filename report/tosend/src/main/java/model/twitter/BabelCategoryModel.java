package model.twitter;

public class BabelCategoryModel extends BabelInfo {
    BabelCategoryModel(int seqId) {
        super(seqId);
    }

    @Override
    public String toString() {
        return String.format("(category: #%s)", getId());
    }
}
