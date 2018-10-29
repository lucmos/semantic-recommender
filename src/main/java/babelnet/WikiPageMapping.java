package babelnet;

import constants.DatasetName;
import constants.Dimension;
import datasetsreader.Dataset;
import io.Cache;
import io.Utils;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.data.BabelCategory;
import it.uniroma1.lcl.babelnet.resources.WikipediaID;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma1.lcl.kb.Domain;
import twittermodel.WikiPageModel;
import utils.Chrono;
import utils.Counter;
import utils.IndexedSerializable;

import java.util.*;
import java.util.stream.Collectors;

public class WikiPageMapping implements IndexedSerializable {

    private final Dimension dimension;

    public static void main(String[] args) throws Utils.CacheNotPresent {
        WikiPageMapping p = WikiPageMapping.getInstance(Dimension.COMPLETE);
        System.out.println(p);

        p = WikiPageMapping.getInstance(Dimension.SMALL);
        System.out.println(p);
    }

    private static WikiPageMapping getInstance(Dimension dimension) throws Utils.CacheNotPresent {
        try {
            return Cache.WikiMappingCache.readFromCache(dimension);
        } catch (Utils.CacheNotPresent cacheNotPresent) {
            WikiPageMapping mapping = new WikiPageMapping(dimension);
            Cache.WikiMappingCache.writeToCache(mapping);
            return mapping;
        }
    }

    private Map<String, String> wikiToSynset = new HashMap<>();
    private Map<String, Set<String>> synsetToCategories = new HashMap<>();
    private Map<String, Set<String>> synsetToDomain = new HashMap<>();

    public WikiPageMapping(Dimension dim) throws Utils.CacheNotPresent {
        BabelNet.getInstance();

        this.dimension = dim;

        for (DatasetName name : DatasetName.values()) {
            Dataset d = Cache.DatasetCache.readFromCache(name, dim);
            Chrono c = new Chrono(String.format("Generating wikipage mapping for %s %s...", name, dim));
            int notFound = 0;
            for (Map.Entry<Integer, WikiPageModel> pageEntry : d.getPages().entrySet()) {
                WikiPageModel page = pageEntry.getValue();
                boolean synFound = addSynsetToMap(page);
                notFound = synFound ? notFound : notFound + 1;
            }

            String counts = String.format("[synsets not found: %s/%s]", notFound, d.getPages().size());
            c.millis(String.format("%s - %s", "done (in %s %s)", counts));
        }
    }

    public Map<String, String> getWikiToSynset() {
        return wikiToSynset;
    }
    public Map<String, Set<String>> getSynsetToCategories() {
        return synsetToCategories;
    }

    public Map<String, Set<String>> getSynsetToDomain() {
        return synsetToDomain;
    }

    public Set<String> getCategories(WikiPageModel pageModel) {
        return getStrings(pageModel, synsetToCategories);
    }

    public Set<String> getDomains(WikiPageModel pageModel) {
        return getStrings(pageModel, synsetToDomain);
    }

    public Set<String> getStrings(WikiPageModel pageModel,
                                   Map<String, Set<String>> idToString) {
        String synsetId = getSynsetID(pageModel);

        if (!idToString.containsKey(synsetId)) return null;
        Set<String> strings = idToString.get(synsetId);
        assert strings != null;
        assert !strings.isEmpty();

        return strings;
    }

    public String getSynsetID(WikiPageModel pageModel) {
        String key = pageModel.getIdString();

        if (!wikiToSynset.containsKey(key)) return null;

        String synset = wikiToSynset.get(key);
        assert synset != null;

        return synset;
    }

    private boolean addSynsetToMap(WikiPageModel page) {
        if (wikiToSynset.containsKey(page.getIdString())) {
            return true;
        }

        BabelSynset syn = getSynsetFromBabelnet(page);

        if (syn != null) {
            String synID = syn.getID().getID();
            wikiToSynset.put(page.getIdString(), synID);
//            System.out.println(synID);
            List<BabelCategory> cat = getCategoriesFromBabelnet(syn);
            if (!cat.isEmpty()) {

                HashSet<String> catSet = new HashSet<>();
                for (BabelCategory b : cat) {
                    catSet.add(b.toString());
                }
//                System.out.println(catSet);
                synsetToCategories.put(synID, catSet);
            }

            Set<Domain> doms = getDomainsFromBabelnet(syn);
            if (!doms.isEmpty()) {

                HashSet<String> domSet = new HashSet<>();
                for (Domain d : doms) {
                    domSet.add(d.toString());
                }
//                System.out.println(domSet);
                synsetToDomain.put(synID, domSet);
            }
            return true;
        }
//        System.out.println();
        return false;
    }


    private BabelSynset getSynsetFromBabelnet(WikiPageModel wikiPage) {
        assert (wikiPage != null);

        BabelSynset synset = BabelNet.getInstance().getSynset(new WikipediaID(wikiPage.getSimpleName(), Language.EN));
        if (synset == null) {
//            System.out.println(String.format("Synset not found: %s", wikiPage.getSimpleName()));
            return null;
        }

//        assert synset != null;
        return synset;
    }

    private List<BabelCategory> getCategoriesFromBabelnet(BabelSynset synset) {
        assert (synset != null);

        List<BabelCategory> l = synset.getCategories().stream().filter(x -> x.getLanguage().equals(Language.EN)).collect(Collectors.toList());

        assert l != null;
//        assert !l.isEmpty();
        return l;
    }

    private Set<Domain> getDomainsFromBabelnet(BabelSynset synset) {
        assert synset != null;

        HashMap<Domain, Double> l = synset.getDomains();

        assert l != null;
//        assert !l.isEmpty();
        return l.keySet();
    }

    public Dimension getDimension() {
        return dimension;
    }

    public int getNumberOfCategories() {
        return getNumberOf(synsetToCategories);
    }

    public int getNumberOfDomains() {
        return getNumberOf(synsetToDomain);
    }

    public int getNumberOf(Map<String, Set<String>> map) {
        Set<String> elements = new HashSet<>();

        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            elements.addAll(entry.getValue());
        }
        return elements.size();
    }

    public Counter<String> getDistributionOf(Map<String, Set<String>> map) {
        Counter<String> elements = new Counter<>();

        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            elements.increment(entry.getValue());
        }
        return elements;
    }

    @Override
    public String toString() {
        return String.format("(wikipages: %s {categories: %s, domains: %s})", wikiToSynset.size(), getNumberOfCategories(), getNumberOfDomains());
    }

    public String stats(int k) {
        int numberSyn = wikiToSynset.size();
        int domainsNum = getNumberOfDomains();
        String domaindistr = getDistributionOf(synsetToDomain).getDistribution();
        int catNum = getNumberOfCategories();
        String catdistr = getDistributionOf(synsetToCategories).getDistribution(k);

        return String.format("Stats of the wikipages mapping %s\n" +
                "Number of synsets found: %s\n" +
                "Number of domains found: %s\n" +
                "Number of cateries found: %s\n" +
                "Distribution of domains: \n%s\n" +
                "Distribution of categories: \n%s\n", dimension, numberSyn, domainsNum, catNum, domaindistr, catdistr);
    }

    public String stats() {
        return stats(50);
    }

}
