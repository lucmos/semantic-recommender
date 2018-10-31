package twittermodel;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model a wikipedia page
 */
public class WikiPageModel extends ObjectModel {
    private static final int NAME_COMPONENTS_NUMBER = 3;

    private Set<Integer> babelCategories;
    private Set<Integer> babelDomains;


    WikiPageModel(int seqId, String name) {
        super(seqId, name);
        this.babelCategories = new HashSet<>();
        this.babelDomains = new HashSet<>();
    }

    public Set<Integer> getBabelCategories() {
        return babelCategories;
    }

    public Set<Integer> getBabelDomains() {
        return babelDomains;
    }

    public void addCategory(BabelCategoryModel categoryModel) {
        assert categoryModel != null;

        babelCategories.add(categoryModel.getId());
    }

    public void addBabelDomain(BabelDomainModel domainModel) {
        assert domainModel != null;

        babelDomains.add(domainModel.getId());
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
