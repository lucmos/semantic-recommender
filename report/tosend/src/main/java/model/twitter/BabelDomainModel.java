package model.twitter;

public class BabelDomainModel extends BabelInfo {
    BabelDomainModel(int seqId) {
        super(seqId);
    }

    @Override
    public String toString() {
        return String.format("(domain: #%s)", getId());
    }
}
