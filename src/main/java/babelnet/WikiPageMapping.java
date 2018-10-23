package babelnet;

import com.google.common.net.InternetDomainName;
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
import twitter4j.DirectMessage;
import twittermodel.WikiPageModel;
import utils.IndexedSerializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WikiPageMapping implements IndexedSerializable {

    public static void main(String[] args) {
        WikiPageMapping mapping = new WikiPageMapping();
        Utils.save(mapping, PathConstants.WIKIPAGE_TO_CATDOM.getPath());
    }

    private BabelNet babelnet;

    private HashMap<Integer, BabelSynset> wikiToSynset = new HashMap<>();
    private HashMap<BabelSynset, List<BabelCategory>> synsetToCategories = new HashMap<>();
    private HashMap<BabelSynset, Set<Domain>> synsetToDomain = new HashMap<>();

    public WikiPageMapping() {
        this.babelnet = BabelNet.getInstance();

        for (DatasetName name : DatasetName.values()) {
            Dataset d = Cache.readFromCache(name, Dimension.SMALL);

            for (Map.Entry<Integer, WikiPageModel> pageEntry : d.getPages().entrySet()) {
                WikiPageModel page = pageEntry.getValue();

                BabelSynset syn = getSynset(page);
                System.out.println(syn);

                if (syn != null) {

                    List<BabelCategory> cat = getCategories(syn);
                    System.out.println("CATEGORIES: " + cat);
                    synsetToCategories.put(syn, cat);

                    Set<Domain> doms = getDomains(syn);
                    System.out.println("DOMAINS: " + doms);
                    synsetToDomain.put(syn, doms);
                }
                System.out.println();
            }
        }
    }

    public BabelSynset getSynset(WikiPageModel wikiPage) {
        assert (wikiPage != null);

        BabelSynset synset = this.babelnet.getSynset(new WikipediaID(wikiPage.getSimpleName(), Language.EN));
        if (synset == null) {
            System.out.println(String.format("Synset not found: %s", wikiPage.getSimpleName()));
            return null;
        }

        assert synset != null;
        return synset;
    }

    public List<BabelCategory> getCategories(BabelSynset synset) {
        assert (synset != null);

        List<BabelCategory> l = synset.getCategories();

        assert l != null;
//        assert !l.isEmpty();
        return l;
    }

    public Set<Domain> getDomains(BabelSynset synset) {
        assert synset != null;

        HashMap<Domain, Double> l = synset.getDomains();

        assert l != null;
//        assert !l.isEmpty();
        return l.keySet();
    }


}
//System.out.println(BabelNet.getInstance().getSynset(new WikipediaID("Maxene_Magalona", Language.EN)).getCategories());