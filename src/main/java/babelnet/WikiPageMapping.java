package babelnet;

import constants.DatasetName;
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
import utils.Statistics;

import java.util.*;
import java.util.stream.Collectors;

public class WikiPageMapping implements IndexedSerializable {

    public static WikiPageMapping getInstance() throws Utils.CacheNotPresent {
        try {
            return Cache.WikiMappingCache.read();
        } catch (Utils.CacheNotPresent cacheNotPresent) {
            WikiPageMapping mapping = new WikiPageMapping();
            mapping.compute();
            Cache.WikiMappingCache.write(mapping);
            return mapping;
        }
    }

    private Map<String, String> wikiToSynset = new HashMap<>();
    private Map<String, Set<String>> synsetToCategories = new HashMap<>();
    private Map<String, Set<String>> synsetToDomain = new HashMap<>();

    public WikiPageMapping() {
    }

    public void compute() throws Utils.CacheNotPresent
    {
        BabelNet.getInstance();

        for (DatasetName name : DatasetName.values()) {
            Dataset d = Cache.DatasetCache.read(name);
            Chrono c = new Chrono(String.format("Generating wikipage mapping for %s...", name));
            final int[] notFound = {0};

            d.getPages().forEach((key, value) -> {
                boolean synFound = addSynsetToMap(value);
                notFound[0] = synFound ? notFound[0] : notFound[0] + 1;
            });

            String counts = String.format("[synsets not found: %s/%s]", notFound[0], d.getPages().size());
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
        return getSet(pageModel, synsetToCategories);
    }

    public Set<String> getDomains(WikiPageModel pageModel) {
        return getSet(pageModel, synsetToDomain);
    }

    public Set<String> getSet(WikiPageModel pageModel, Map<String, Set<String>> idToString) {
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


    @Override
    public String toString() {
        return String.format("(wikipages: %s {categories: %s, domains: %s})", wikiToSynset.size(), getNumberOfCategories(), getNumberOfDomains());
    }

    public String stats() {
        return stats(50);
    }

    public String stats(int k) {
        return String.format("\n[WIKIPAGES MAPPING STATS]\n" +
                "\tsynsets found: %s\n", wikiToSynset.size()) +
                _stats(synsetToDomain, "domains", k).append(_stats(synsetToCategories, "categories", k));
    }

    private StringBuilder _stats(Map<String, Set<String>> map, String name, int k) {
        StringBuilder s = new StringBuilder(String.format("\n[OCCURRENCES OF THE ~ %s ~ ACROSS SYNSETS]\n", name.toUpperCase()));

        Counter<String> elements = Counter.fromMultiMap(map);
        Statistics stat = new Statistics(elements);

        s.append(stat.report());

        s.append(String.format("\n[%s DISTRIBUTION]\n%s\n", name.toUpperCase(), elements.getDistribution(k)));
        return s;
    }
}
