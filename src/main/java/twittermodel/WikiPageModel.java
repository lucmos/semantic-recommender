package twittermodel;

/**
 * Model a wikipedia page
 */
public class WikiPageModel extends ObjectModel {
    private static final int NAME_COMPONENTS_NUMBER = 3;

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
    public String toString(){
        return String.format("(wikiPage: %s)", getIdString());
    }

    //    Example of wikiname: WIKI:EN:Teru
    public String getSimpleName() {
        String[] nameComp = getIdString().split(":");
        assert (nameComp.length == 3);
        return nameComp[2];
    }
}
