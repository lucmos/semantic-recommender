package babelnet;

import com.sun.org.apache.xpath.internal.operations.Bool;
import constants.DatasetName;
import constants.Dimension;
import constants.PathConstants;
import datasetsreader.Dataset;
import io.Cache;
import io.Utils;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.data.BabelCategory;
import it.uniroma1.lcl.babelnet.data.BabelDomain;
import it.uniroma1.lcl.babelnet.resources.WikipediaID;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma1.lcl.kb.Domain;
import twittermodel.WikiPageModel;
import utils.Chrono;
import utils.IndexedSerializable;

import javax.rmi.CORBA.Util;
import java.util.*;

public class WikiPageMapping implements IndexedSerializable {

    private final Dimension dimension;

    public static void main(String[] args) throws Utils.CacheNotPresent {
        WikiPageMapping p = WikiPageMapping.getInstance(Dimension.COMPLETE);
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

    private HashMap<String, String> wikiToSynset = new HashMap<>();
    private HashMap<String, Set<String>> synsetToCategories = new HashMap<>();
    private HashMap<String, Set<String>> synsetToDomain = new HashMap<>();

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

    public Set<String> getCategories(WikiPageModel pageModel) {
        return getStrings(pageModel, synsetToCategories);
    }

    public Set<String> getDomains(WikiPageModel pageModel) {
        return getStrings(pageModel, synsetToDomain);
    }
    private Set<String> getStrings(WikiPageModel pageModel,
                                   HashMap<String, Set<String>> idToString) {
        String synsetId = getSynsetID(pageModel);

        if (!idToString.containsKey(synsetId)) return null;
        Set<String> babelDomains = idToString.get(synsetId);
        assert babelDomains != null;
        assert !babelDomains.isEmpty();

        return babelDomains;
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

        List<BabelCategory> l = synset.getCategories();

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
}
