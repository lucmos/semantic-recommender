package twittermodel;

import utils.OneToOneHash;

/**
 * Model a wikipedia page
 */
public class WikiPageModel extends TwitterObjectModel {
    private static final int NAME_COMPONENTS_NUMBER = 3;

    /**
     * A mapping between an integer identifier and a string one
     */
    private static OneToOneHash<Integer, String> idMap = new OneToOneHash<>();

    public WikiPageModel(String name) {
        super(name);
    }

    /**
     * Rebuilds the url from the name
     * @return the unique url of this page
     */
    public String getURL() {
        String[] parts = this.getIdString().split(":");
        assert parts.length == WikiPageModel.NAME_COMPONENTS_NUMBER;
        return String.format("https://%s.wikipedia.org/wiki/%s", parts[1], parts[2]);
    }

    @Override
    public OneToOneHash<Integer, String> getIdMapping() {
        assert WikiPageModel.idMap != null;

        return WikiPageModel.idMap;
    }

    @Override
    public String toString(){

//        return "Wikipedia page "+ idMap +" with URL: "+ getURL();
        return String.format("(wikiPage: %s)", getId());
    }
}
