package model.twitter;

public class BabelDomainModel extends BabelInfo {
    BabelDomainModel(int seqId, String idString) {
        super(seqId, idString);
    }

    @Override
    public String toString() {
        return getIdString();
    }
}
