package babelnet;

import constants.DatasetName;
import twittermodel.Dataset;
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
    private WikiPageMapping() {
    }

//    private static Map<Integer, BabelSynset> wikiToSynsetObjCache = new HashMap<>();
//    private static Map<BabelSynset, Set<BabelCategory>> synsetToCategoriesObjCache = new HashMap<>();
//    private static Map<BabelSynset, Set<Domain>> synsetToDomainObjCache = new HashMap<>();

    private static BabelSynset getSynsetObj(WikiPageModel wikiPage) {
        assert (wikiPage != null);

//        if (wikiToSynsetObjCache.containsKey(wikiPage.getId())) {
//            return wikiToSynsetObjCache.get(wikiPage.getId());
//        }
//
        BabelSynset synset = BabelNet.getInstance().getSynset(new WikipediaID(wikiPage.getSimpleName(), Language.EN));
//        wikiToSynsetObjCache.put(wikiPage.getId(), synset);
//
        return synset;
    }

    private static Set<BabelCategory> getCategoriesObj(BabelSynset synset) {
        assert synset != null;

//        if (synsetToCategoriesObjCache.containsKey(synset)) {
//            return synsetToCategoriesObjCache.get(synset);
//        }

        Set<BabelCategory> l = synset.getCategories().stream().filter(x -> x.getLanguage().equals(Language.EN)).collect(Collectors.toSet());
//        synsetToCategoriesObjCache.put(synset, l);
        return l;
    }

    private static Set<Domain> getDomainsObj(BabelSynset synset) {
        assert synset != null;
//
//        if (synsetToDomainObjCache.containsKey(synset)) {
//            return synsetToDomainObjCache.get(synset);
//        }

        Set<Domain> l = synset.getDomains().keySet();
//        synsetToDomainObjCache.put(synset, l);
        return l;
    }

//    private static Map<Integer, String> wikiToSynsetCache = new HashMap<>();
//    private static Map<Integer, Set<String>> wikiToCategoriesCache = new HashMap<>();
//    private static Map<Integer, Set<String>> wikiToDomainCache = new HashMap<>();
//
    public static String getSynset(WikiPageModel pageModel) {
        assert pageModel != null;

//        if (wikiToSynsetCache.containsKey(pageModel.getId())) {
//            return wikiToSynsetCache.get(pageModel.getId());
//        }

        BabelSynset synId = getSynsetObj(pageModel);
        String out = synId == null ? null : synId.getID().getID();

//        wikiToSynsetCache.put(pageModel.getId(), out);

        return out;
    }

    public static Set<String> getCategories(WikiPageModel pageModel) {
        assert pageModel != null;

//        if (wikiToCategoriesCache.containsKey(pageModel.getId())) {
//            return wikiToCategoriesCache.get(pageModel.getId());
//        }

        BabelSynset synId = getSynsetObj(pageModel);

        HashSet<String> catSet = new HashSet<>();
        if (synId != null) {
            Set<BabelCategory> cats = getCategoriesObj(synId);

            for (BabelCategory b : cats) {
                catSet.add(b.toString());
            }

//            wikiToCategoriesCache.put(pageModel.getId(), catSet);
//            return catSet;
        }

//        wikiToCategoriesCache.put(pageModel.getId(), catSet);
        return catSet;
    }

    public static Set<String> getDomains(WikiPageModel pageModel) {
        assert pageModel != null;

//        if (wikiToDomainCache.containsKey(pageModel.getId())) {
//            return wikiToDomainCache.get(pageModel.getId());
//        }

        BabelSynset synId = getSynsetObj(pageModel);

        HashSet<String> domSet = new HashSet<>();
        if (synId != null) {
            Set<Domain> doms = getDomainsObj(synId);

            for (Domain b : doms) {
                domSet.add(b.toString());
            }

//            wikiToDomainCache.put(pageModel.getId(), domSet);
//            return domSet;
        }
//        wikiToDomainCache.put(pageModel.getId(), domSet);
        return domSet;
    }
}
