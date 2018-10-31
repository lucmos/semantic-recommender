package twittermodel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model a wikipedia page
 */
public class WikiPageModel extends ObjectModel {
    private static final int NAME_COMPONENTS_NUMBER = 3;

    WikiPageModel(int seqId, String name) {
        super(seqId, name);
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
    private static final Pattern PATTERN = Pattern.compile("^\\w+:\\w+:((.*))$");

    public String getSimpleName() {
        Matcher matcher = PATTERN.matcher(getIdString());
        if (matcher.find()) {

            String simpleName = matcher.group(1);
            assert simpleName != null;
            assert simpleName.length() > 0;
            return simpleName;
        }
        throw new RuntimeException();
    }
}
