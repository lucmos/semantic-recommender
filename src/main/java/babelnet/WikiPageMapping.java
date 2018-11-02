package babelnet;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.data.BabelCategory;
import it.uniroma1.lcl.babelnet.resources.WikipediaID;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma1.lcl.kb.Domain;
import model.twitter.WikiPageModel;
import utils.IndexedSerializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WikiPageMapping implements IndexedSerializable {
    private WikiPageMapping() {
    }
    public static String getSynset(WikiPageModel pageModel) {
        assert pageModel != null;

        BabelSynset synId = getSynsetObj(pageModel);

        return synId == null ? null : synId.getID().getID();
    }

    public static Set<String> getCategories(WikiPageModel pageModel) {
        assert pageModel != null;
        return getCategories(getSynsetObj(pageModel));
    }

    public static Set<String> getDomains(WikiPageModel pageModel) {
        assert pageModel != null;
        return getDomains(getSynsetObj(pageModel));
    }

//  caching to improve performances.

    private static Map<Integer, BabelSynset> wikiToSynsetObjCache = new HashMap<>();
    private static Map<BabelSynset, Set<String>> synsetToCategoriesObjCache = new HashMap<>();
    private static Map<BabelSynset, Set<String>> synsetToDomainObjCache = new HashMap<>();

    private static BabelSynset getSynsetObj(WikiPageModel wikiPage) {
        assert (wikiPage != null);

        if (wikiToSynsetObjCache.containsKey(wikiPage.getId())) {
            return wikiToSynsetObjCache.get(wikiPage.getId());
        }

        BabelSynset synset = BabelNet.getInstance().getSynset(new WikipediaID(wikiPage.getSimpleName(), Language.EN));
        wikiToSynsetObjCache.put(wikiPage.getId(), synset);

        return synset;
    }

    private static Set<String> getCategories(BabelSynset synset) {
        if (synsetToCategoriesObjCache.containsKey(synset)) {
            return synsetToCategoriesObjCache.get(synset);
        }

        HashSet<String> catSet = new HashSet<>();

        if (synset != null) {
            Set<BabelCategory> cats = synset.getCategories().stream().filter(x -> x.getLanguage().equals(Language.EN)).collect(Collectors.toSet());
            for (BabelCategory b : cats) {
                catSet.add(b.toString());
            }
        }

        synsetToCategoriesObjCache.put(synset, catSet);
        return catSet;
    }

    private static Set<String> getDomains(BabelSynset synset) {
        if (synsetToDomainObjCache.containsKey(synset)) {
            return synsetToDomainObjCache.get(synset);
        }

        HashSet<String> domSet = new HashSet<>();

        if (synset != null) {
            Set<Domain> doms = synset.getDomains().keySet();
            for (Domain b : doms) {
                domSet.add(b.toString());
            }
        }

        synsetToDomainObjCache.put(synset, domSet);
        return domSet;
    }
}
