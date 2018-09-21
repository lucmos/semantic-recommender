package twittermodel;

import utils.OneToOneHash;

public class WikiPage extends TwitterObjectModel {
    private static final int NAME_COMPONENTS_NUMBER = 3;

    private static OneToOneHash<Integer, String> idMap = new OneToOneHash<>();

    public WikiPage(String name) {
        super(name);
    }

    public String getURL() {
        String[] parts = this.getIdString().split(":");
        assert parts.length == WikiPage.NAME_COMPONENTS_NUMBER;
        return String.format("https://%s.wikipedia.org/wiki/%s", parts[1], parts[2]);
    }

    @Override
    public OneToOneHash<Integer, String> getIdMapping() {
        return WikiPage.idMap;
    }
}
