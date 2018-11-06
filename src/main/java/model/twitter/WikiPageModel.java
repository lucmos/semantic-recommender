package model.twitter;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import model.ObjectModel;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Model a wikipedia page
 */
public class WikiPageModel extends ObjectModel {
    private static final int NAME_COMPONENTS_NUMBER = 3;

    private IntOpenHashSet babelCategories;
    private IntOpenHashSet babelDomains;
    private String simpleName;


    WikiPageModel(int seqId, String name) {
        super(seqId);
        this.babelCategories = new IntOpenHashSet();
        this.babelDomains = new IntOpenHashSet();
        this.simpleName = getSimpleName(name);
    }

    public BabelCategoryModel getCategoryModel(Int2ObjectOpenHashMap<BabelCategoryModel> categories, int catID) {
        assert categories.containsKey(catID);

        BabelCategoryModel cat = categories.get(catID);
        assert cat.getId() == catID;

        return cat;
    }

    public BabelDomainModel getDomainModel(Int2ObjectOpenHashMap<BabelDomainModel> domains, int domId) {
        assert domains.containsKey(domId);

        BabelDomainModel dom = domains.get(domId);
        assert dom.getId() == domId;

        return dom;
    }

    public Set<BabelCategoryModel> getCategoriesModel(Int2ObjectOpenHashMap<BabelCategoryModel> categories) {
        return babelCategories.stream().map(x -> getCategoryModel(categories, x)).collect(Collectors.toSet());
    }

    public Set<BabelDomainModel> getDomainsModel(Int2ObjectOpenHashMap<BabelDomainModel> domains) {
        return babelDomains.stream().map(x -> getDomainModel(domains, x)).collect(Collectors.toSet());
    }

    public IntOpenHashSet getBabelCategories() {
        return babelCategories;
    }

    public IntOpenHashSet getBabelDomains() {
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

    @Override
    public String toString(){
        return String.format("(wikiPage: %s {categories: %s, domains: %s})", simpleName, babelCategories.size(), babelDomains.size());
    }

    //    Example of wikiname: WIKI:EN:Teru

    /**
     * Rebuilds the url from the name
     * @return the unique url of this page
     */
    public String getURL(String name) {
        String[] parts = name.split(":");
        assert parts.length == WikiPageModel.NAME_COMPONENTS_NUMBER;
        return String.format("https://%s.wikipedia.org/wiki/%s", parts[1], parts[2]);
    }

    private static final Pattern PATTERN = Pattern.compile("^\\w+:\\w+:((.*))$");
    private String getSimpleName(String name) {
        Matcher matcher = PATTERN.matcher(name);
        if (matcher.find()) {

            String simpleName = matcher.group(1);
            assert simpleName != null;
            assert simpleName.length() > 0;
            return simpleName;
        }
        throw new RuntimeException();
    }

    public String getSimpleName() {
        return simpleName;
    }
}
